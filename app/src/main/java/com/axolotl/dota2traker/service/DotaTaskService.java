package com.axolotl.dota2traker.service;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.text.TextUtils;

import com.axolotl.dota2traker.DotaApp;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.event.LoadErrorEvent;
import com.axolotl.dota2traker.event.LoadedEvent;
import com.axolotl.dota2traker.retrofit.DotaApi;
import com.axolotl.dota2traker.retrofit.model.Match;
import com.axolotl.dota2traker.retrofit.model.MatchDetailRes;
import com.axolotl.dota2traker.retrofit.model.MatchHistoryResults;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by axolotl on 16/6/30.
 */
public class DotaTaskService extends GcmTaskService {

    @Inject
    DotaApi mDotaApi;

    private String mPerId;

    private Context mContext;

    public DotaTaskService() {
    }

    public DotaTaskService(Context mContext) {
        this.mContext = mContext;
        LogUtils.log("taskservice new");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DotaApp.getNetComponent(this).inject(this);
        LogUtils.log("taskservice create");
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        LogUtils.log("task call " + taskParams.getTag());
        if(mDotaApi == null){
            LogUtils.log("null api");
        }
        int result = GcmNetworkManager.RESULT_FAILURE;
        List<String> ids = new ArrayList<>();
        Cursor c = getContentResolver().query(DotaProvider.Players.CONTENT_URI,
                new String[]{PlayerColumns.STEAM64},
                null,
                null,
                null);
        if(c != null && c.getCount() > 0){
            LogUtils.log("perform periodic update");
            while(c.moveToNext()){
                String id64 = CursorHelper.getString(c, PlayerColumns.STEAM64);
                ids.add(DotaUtil.get32Id(Long.parseLong(id64)));
            }
        }else{
            LogUtils.log("null crusor");
            return result;
        }
        final ArrayList<ContentProviderOperation> insertOperations = new ArrayList<>();
        Observable.from(ids)
                .flatMap(new Func1<String, Observable<MatchHistoryResults>>() {
                    @Override
                    public Observable<MatchHistoryResults> call(String id32) {
                        mPerId = id32;
                        return mDotaApi.getMatchHistory(id32, 3, null);
                    }
                })
                .flatMap(new Func1<MatchHistoryResults, Observable<Match>>() {
                    @Override
                    public Observable<Match> call(MatchHistoryResults matchHistoryResults) {
                        List<Match> matches = matchHistoryResults.getResult().getMatches();
                        return Observable.from(matches);
                    }
                })
                .filter(new Func1<Match, Boolean>() {
                    @Override
                    public Boolean call(Match match) {
                        int lobbyType = match.getLobbyType();
                        return lobbyType == 0 || lobbyType == 2 || lobbyType == 5 || lobbyType == 7;
                    }
                })
                .flatMap(new Func1<Match, Observable<MatchDetailRes>>() {
                    @Override
                    public Observable<MatchDetailRes> call(Match match) {
                        return mDotaApi.getMatchDetails(match.getMatchId());
                    }
                })
                .subscribe(new Subscriber<MatchDetailRes>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.log("task omcompleted");
                        try {
                            getContentResolver().applyBatch(DotaProvider.AUTHORITY, insertOperations);
                            LogUtils.log("task insert successs ");
                        } catch (RemoteException | OperationApplicationException e) {
                            e.printStackTrace();
                            LogUtils.log("task insert fail " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.log(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(MatchDetailRes matchDetailRes) {
                        insertOperations.add(DotaIntentService.buildBatchInert(matchDetailRes, DotaUtil.get64Id(Long.parseLong(mPerId))));
                    }
                });
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private void handleActionUpdate(String id32) {
        final ArrayList<ContentProviderOperation> insertOperations = new ArrayList<>();
        final String steams64 = DotaUtil.get64Id(Long.parseLong(id32));
        mDotaApi.getMatchHistory(id32, 3, null)
                .flatMap(new Func1<MatchHistoryResults, Observable<Match>>() {
                    @Override
                    public Observable<Match> call(MatchHistoryResults matchHistoryResults) {
                        List<Match> matches = matchHistoryResults.getResult().getMatches();
                        return Observable.from(matches);
                    }
                })
                .filter(new Func1<Match, Boolean>() {
                    @Override
                    public Boolean call(Match match) {
                        int lobbyType = match.getLobbyType();
                        return lobbyType == 0 || lobbyType == 2 || lobbyType == 5 || lobbyType == 7;
                    }
                })
                .flatMap(new Func1<Match, Observable<MatchDetailRes>>() {
                    @Override
                    public Observable<MatchDetailRes> call(Match match) {
                        return mDotaApi.getMatchDetails(match.getMatchId());
                    }
                })
                .subscribe(new Subscriber<MatchDetailRes>() {
                    @Override
                    public void onCompleted() {
                        try {
                            getContentResolver().applyBatch(DotaProvider.AUTHORITY, insertOperations);
                        } catch (RemoteException | OperationApplicationException e) {
                            LogUtils.log("error while update insert batch " + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new LoadErrorEvent());
                        LogUtils.log(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(MatchDetailRes matchDetailRes) {
                        LogUtils.log(matchDetailRes.getResult().matchId + " -- " + matchDetailRes.getResult().startTime + "");
                        insertOperations.add(DotaIntentService.buildBatchInert(matchDetailRes, steams64));
                    }
                });

    }
}
