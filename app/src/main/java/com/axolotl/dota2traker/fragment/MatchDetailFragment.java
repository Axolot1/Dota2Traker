package com.axolotl.dota2traker.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.axolotl.dota2traker.DotaApp;
import com.axolotl.dota2traker.MatchDetailActivity;
import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.adapter.MatchPlayerAdapter;
import com.axolotl.dota2traker.adapter.PlayerInMatch;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.MatchDetailColumns;
import com.axolotl.dota2traker.retrofit.DotaApi;
import com.axolotl.dota2traker.retrofit.model.MatchDetailRes;
import com.axolotl.dota2traker.retrofit.model.PlayerSumRes;
import com.axolotl.dota2traker.retrofit.model.PlayerSummaries;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by axolotl on 16/7/11.
 */
public class MatchDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Toolbar.OnMenuItemClickListener {

    private static final int CURSOR_LOADER_ID = 3;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 5;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    HashMap<Integer, String> mItemMap;

    @Inject
    DotaApi mDotaApi;

    @Bind(R.id.tv_time_ago)
    TextView tvTimeAgo;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.tv_lobby_type)
    TextView tvLobbyType;
    @Bind(R.id.tv_game_mode)
    TextView tvGameMode;
    @Bind(R.id.team_radiant)
    View viewRadiant;
    @Bind(R.id.team_dire)
    View viewDire;

    TextView tvRadiant;
    TextView tvDire;
    RecyclerView rcvRadiant;
    RecyclerView rcvDire;
    MatchPlayerAdapter mRadiantAdapter;
    MatchPlayerAdapter mDireAdapter;
    @Bind(R.id.match_content)
    NestedScrollView matchContent;


    private String mMatchId;
    private HashMap<String, Boolean> mTeamMap;

    protected Subscription subscription;
    Observable<PlayerSumRes> mObservable;
    Observer<PlayerSumRes> mObserver = new Observer<PlayerSumRes>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(PlayerSumRes playerSumRes) {
            if (playerSumRes == null) {
                return;
            }
            List<PlayerSummaries> players = playerSumRes.getResponse().getPlayers();
            List<PlayerSummaries> radiant = new ArrayList<>();
            List<PlayerSummaries> dire = new ArrayList<>();
            for (PlayerSummaries p : players) {
                if (mTeamMap.get(p.getSteamid())) {
                    radiant.add(p);
                } else {
                    dire.add(p);
                }
            }
            if (mRadiantAdapter != null) {
                mRadiantAdapter.refreshData(radiant);
            }
            if (mDireAdapter != null) {
                mDireAdapter.refreshData(dire);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        DotaApp.getNetComponent(getContext()).inject(this);
        mMatchId = getActivity().getIntent().getStringExtra(MatchDetailActivity.EXTRA_MATCH_ID);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.act_match_detail, container, false);
        ButterKnife.bind(this, v);
        mTeamMap = new HashMap<>();

        tvRadiant = (TextView) viewRadiant.findViewById(R.id.tv_team);
        rcvRadiant = (RecyclerView) viewRadiant.findViewById(R.id.rcv_match_player);
        tvDire = (TextView) viewDire.findViewById(R.id.tv_team);
        rcvDire = (RecyclerView) viewDire.findViewById(R.id.rcv_match_player);

        toolbar.setTitle(getString(R.string.match_detail_title) + mMatchId);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mRadiantAdapter = new MatchPlayerAdapter(getContext(), null);
        mDireAdapter = new MatchPlayerAdapter(getContext(), null);
        rcvRadiant.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvDire.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvRadiant.setAdapter(mRadiantAdapter);
        rcvDire.setAdapter(mDireAdapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), DotaProvider.Matches.CONTENT_URI,
                null,
                "match_id=?",
                new String[]{mMatchId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Gson gson = new Gson();
        PrettyTime time = new PrettyTime(Locale.getDefault());
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            String playerMsg = CursorHelper.getString(data, MatchDetailColumns.PLAYERS_MSG);

            long duration = (Long.parseLong(CursorHelper.getString(data, MatchDetailColumns.DURATION)) / 60);
            //set up title
            tvDuration.setText(String.format(Locale.getDefault(), "%d min", duration));
            tvTimeAgo.setText(time.format(new Date((long) (CursorHelper.getDouble(data,
                    MatchDetailColumns.START_TIME) * 1000))));
            tvLobbyType.setText(DotaUtil.getLobby(CursorHelper.getInt(data, MatchDetailColumns.LOBBY_TYPE), getContext()));
            tvGameMode.setText(DotaUtil.getGameMode(CursorHelper.getInt(data, MatchDetailColumns.GAME_MODE), getContext()));

            tvDire.setBackgroundResource(R.drawable.rouned_border_dire);

            //win
            boolean radiantWin = CursorHelper.getInt(data, MatchDetailColumns.RADIANT_WIN) == 0;
            if (radiantWin) {
                tvRadiant.setText(R.string.radiant_win);
                tvDire.setText(R.string.dire_lost);
            } else {
                tvRadiant.setText(R.string.radiant_lost);
                tvDire.setText(R.string.dire_win);
            }
            //players
            List<MatchDetailRes.PlayerMatchDetail> players = gson.fromJson(playerMsg,
                    new TypeToken<List<MatchDetailRes.PlayerMatchDetail>>() {
                    }.getType());
            List<PlayerInMatch> teamRadiant = new ArrayList<>();
            List<PlayerInMatch> teamDire = new ArrayList<>();
            long radiantDamage = 0;
            long direDamage = 0;
            int radiantScore = 0;
            int direScore = 0;
            if (players != null) {

                for (MatchDetailRes.PlayerMatchDetail p : players) {

                    if (p.playerSlot < 100) {
                        radiantDamage += p.heroDamage;
                        radiantScore += p.kills;
                    } else {
                        direDamage += p.heroDamage;
                        direScore += p.kills;
                    }
                }


                List<String> steamId64s = new ArrayList<>();
                for (MatchDetailRes.PlayerMatchDetail p : players) {
                    //id64
                    String id64 = DotaUtil.get64Id(Long.parseLong(p.accountId));
                    steamId64s.add(id64);

                    mTeamMap.put(id64, p.playerSlot < 100);

                    PlayerInMatch playerInMatch = new PlayerInMatch();
                    //id64
                    playerInMatch.setSteamID64(id64);
                    //hero
                    setHeroAvatar(p, playerInMatch);
                    //items
                    playerInMatch.setItem(
                            DotaUtil.getResId(mItemMap.get(p.item0), R.drawable.class),
                            DotaUtil.getResId(mItemMap.get(p.item1), R.drawable.class),
                            DotaUtil.getResId(mItemMap.get(p.item2), R.drawable.class),
                            DotaUtil.getResId(mItemMap.get(p.item3), R.drawable.class),
                            DotaUtil.getResId(mItemMap.get(p.item4), R.drawable.class),
                            DotaUtil.getResId(mItemMap.get(p.item5), R.drawable.class));
                    //kda
                    playerInMatch.setKda(p.kills + "/" + p.deaths + "/" + p.assists);
                    if (p.playerSlot < 100) {
                        playerInMatch.setDamage(String.format(Locale.getDefault(),
                                getString(R.string.damage_rate), p.heroDamage * 100 / radiantDamage));
                        playerInMatch.setInBattle(String.format(Locale.getDefault(),
                                getString(R.string.in_battle), (p.kills + p.assists) * 100 / radiantScore));
                        teamRadiant.add(playerInMatch);
                    } else {
                        playerInMatch.setDamage(String.format(Locale.getDefault(),
                                getString(R.string.damage_rate), p.heroDamage * 100 / direDamage));
                        playerInMatch.setInBattle(String.format(Locale.getDefault(),
                                getString(R.string.in_battle), (p.kills + p.assists) * 100 / direScore));
                        teamDire.add(playerInMatch);
                    }

                }
                //request player information
                String ids = TextUtils.join(",", steamId64s);
                if (mObservable == null) {
                    mObservable = mDotaApi.getPlayerSummaries(ids).cache();
                }
                subscription = mObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mObserver);
            }
            mRadiantAdapter.setData(teamRadiant);
            mDireAdapter.setData(teamDire);

        }
    }

    private void setHeroAvatar(MatchDetailRes.PlayerMatchDetail p, PlayerInMatch playerInMatch) {
        Integer heroD = DotaUtil.HERO.get(p.heroId);
        if (null == heroD) {
            heroD = R.drawable.def;
        }
        playerInMatch.setHeroAvatar(heroD);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            shareMatch();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareMatch();
            }
        }
    }

    private void shareMatch() {
        Bitmap bitmap = DotaUtil.getScreenShot(matchContent);
        File file = DotaUtil.store(bitmap, mMatchId);
        if (file != null) {
            shareImage(file);
        }
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.match_detail_title) + mMatchId);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getContext().getString(R.string.share_title)));
    }


}
