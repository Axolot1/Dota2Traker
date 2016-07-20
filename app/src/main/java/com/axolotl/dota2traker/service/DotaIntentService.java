package com.axolotl.dota2traker.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.axolotl.dota2traker.DotaApp;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.MatchDetailColumns;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.event.LoadErrorEvent;
import com.axolotl.dota2traker.event.LoadedEvent;
import com.axolotl.dota2traker.event.RefreshEvent;
import com.axolotl.dota2traker.retrofit.DotaApi;
import com.axolotl.dota2traker.retrofit.model.Match;
import com.axolotl.dota2traker.retrofit.model.MatchDetailRes;
import com.axolotl.dota2traker.retrofit.model.MatchHistoryResults;
import com.axolotl.dota2traker.retrofit.model.PlayerSumRes;
import com.axolotl.dota2traker.retrofit.model.PlayerSummaries;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DotaIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ADD = "com.axolotl.dota2traker.service.action.FOO";
    private static final String ACTION_BAZ = "com.axolotl.dota2traker.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.axolotl.dota2traker.service.extra.PARAM1";
    private static final String EXTRA_PLAYER_IDS = "com.axolotl.dota2traker.service.extra.PLAYER_IDS";

    @Inject
    DotaApi mDotaApi;

    private long mPerId;


    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionAdd(Context context, String steamId32) {
        Intent intent = new Intent(context, DotaIntentService.class);
        intent.setAction(ACTION_ADD);
        intent.putExtra(EXTRA_PARAM1, steamId32);
        context.startService(intent);
    }



    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startRefreshPlayerInfo(Context context, String steamId64s) {
        Intent intent = new Intent(context, DotaIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PLAYER_IDS, steamId64s);
        context.startService(intent);
    }

    public DotaIntentService() {
        super("DotaIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DotaApp.getNetComponent(this).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionAdd(param1);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PLAYER_IDS);
                handleActionRefreshPlayers(param1);
            }
        }
    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAdd(String steamId) {
        // TODO: Handle action Foo
        Log.i("dota", "perform action add");
        final String steams64 = DotaUtil.get64Id(Long.parseLong(steamId));
        final ArrayList<ContentProviderOperation> insertOperations = new ArrayList<>();
        final String[] imgUrl = new String[1];
        final String[] name = new String[1];
        mDotaApi.getPlayerSummaries(steams64)
                .doOnNext(new Action1<PlayerSumRes>() {
                    @Override
                    public void call(PlayerSumRes playerSumRes) {
                        List<PlayerSummaries> players = playerSumRes.getResponse().getPlayers();
                        imgUrl[0] = players.get(0).getAvatarfull();
                        name[0] = players.get(0).getPersonaname();
                        savePlayerSum(players);
                    }
                })
                .flatMap(new Func1<PlayerSumRes, Observable<MatchHistoryResults>>() {
                    @Override
                    public Observable<MatchHistoryResults> call(PlayerSumRes playerSumRes) {
                        PlayerSummaries p = playerSumRes.getResponse().getPlayers().get(0);
                        mPerId = Long.parseLong(p.getSteamid());
                        String steamID32 = DotaUtil.get32Id(mPerId);
                        return mDotaApi.getMatchHistory(steamID32, 10, null);
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
                        try {
                            getContentResolver().applyBatch(DotaProvider.AUTHORITY, insertOperations);
                            LoadedEvent event = new LoadedEvent();
                            event.setSteamId64(steams64);
                            event.setImgUrl(imgUrl[0]);
                            event.setName(name[0]);
                            EventBus.getDefault().post(event);
                        } catch (RemoteException | OperationApplicationException e) {
                            EventBus.getDefault().post(new LoadErrorEvent());
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
                        insertOperations.add(buildBatchInert(matchDetailRes, mPerId+""));
                    }
                });

    }

    private void savePlayerSum(List<PlayerSummaries> players) {
        if (players != null && players.size() > 0) {
            PlayerSummaries player = players.get(0);
            ContentValues values = new ContentValues();
            values.put(PlayerColumns.AVATAR_MEDIUM, player.getAvatarmedium());
            values.put(PlayerColumns.AVATAR_FULL, player.getAvatarfull());
            values.put(PlayerColumns.LAST_LOGOFF, player.getLastlogoff());
            values.put(PlayerColumns.NAME, player.getPersonaname());
            values.put(PlayerColumns.PERSONA_STATE, player.getPersonastate());
            values.put(PlayerColumns.STEAM64, player.getSteamid());
            values.put(PlayerColumns.PROFILE_URL, player.getProfileurl());
            values.put(PlayerColumns.TIME_CREATED, player.getTimecreated());
            getContentResolver().insert(DotaProvider.Players.CONTENT_URI, values);
            Log.i("dota", "insert success");
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRefreshPlayers(String steam64s) {
        mDotaApi.getPlayerSummaries(steam64s).subscribe(new Subscriber<PlayerSumRes>() {
            @Override
            public void onCompleted() {
                EventBus.getDefault().post(new RefreshEvent());
            }

            @Override
            public void onError(Throwable e) {
                EventBus.getDefault().post(new LoadErrorEvent());
            }

            @Override
            public void onNext(PlayerSumRes playerSumRes) {
                List<PlayerSummaries> players = playerSumRes.getResponse().getPlayers();
                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                for (PlayerSummaries p : players) {
                    operations.add(buildBatchOperation(p));
                }
                try {
                    getContentResolver().applyBatch(DotaProvider.AUTHORITY, operations);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                    LogUtils.log("error when bulk update players");
                }


            }
        });
    }

    public static ContentProviderOperation buildBatchOperation(PlayerSummaries player) {
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newUpdate(DotaProvider.Players.CONTENT_URI)
                .withSelection(PlayerColumns.STEAM64 + "=?", new String[]{player.getSteamid()})
                .withValue(PlayerColumns.NAME, player.getPersonaname())
                .withValue(PlayerColumns.AVATAR_FULL, player.getAvatarfull())
                .withValue(PlayerColumns.AVATAR_MEDIUM, player.getAvatarmedium())
                .withValue(PlayerColumns.PERSONA_STATE, player.getPersonastate());
        return builder.build();
    }

    public static ContentProviderOperation buildBatchInert(MatchDetailRes matchDetailRes, String steams64) {
        MatchDetailRes.DetailResult result = matchDetailRes.getResult();
        Gson gson = new Gson();
        String players = gson.toJson(result.players);
        int radiantWin = result.radiantWin ? 0 : 1;
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(DotaProvider.Matches.CONTENT_URI)
                .withValue(MatchDetailColumns.MATCH_ID, result.matchId)
                .withValue(MatchDetailColumns.STEAM_ID, steams64)
                .withValue(MatchDetailColumns.DIRE_SCORE, result.direScore)
                .withValue(MatchDetailColumns.LOBBY_TYPE, result.lobbyType)
                .withValue(MatchDetailColumns.DIRE_SCORE, result.direScore)
                .withValue(MatchDetailColumns.RADIANT_SCORE, result.radiantScore)
                .withValue(MatchDetailColumns.GAME_MODE, result.gameMode)
                .withValue(MatchDetailColumns.DURATION, result.duration)
                .withValue(MatchDetailColumns.RADIANT_WIN, radiantWin)
                .withValue(MatchDetailColumns.START_TIME, result.startTime)
                .withValue(MatchDetailColumns.MATCH_SEQ_NUMBER, result.matchSeqNum)
                .withValue(MatchDetailColumns.PLAYERS_MSG, players);
        return builder.build();
    }

}