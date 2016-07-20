package com.axolotl.dota2traker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.axolotl.dota2traker.adapter.PlayerSumAdapter;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.event.LoadedEvent;
import com.axolotl.dota2traker.fragment.MatchHistoryFragment;
import com.axolotl.dota2traker.fragment.PlayerCardFragment;
import com.axolotl.dota2traker.service.DotaTaskService;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.LogUtils;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PlayerCardFragment.CallBack {

    @Nullable
    @Bind(R.id.match_history_container)
    FrameLayout mHistoryLayout;
    private boolean mTwoPane;
    private PlayerCardFragment mPlayerCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LogUtils.log("act create");
//        IntentTaskService.startTaskService(this);
        if(mHistoryLayout != null){
            LogUtils.log("two pane");
            mTwoPane = true;
            if (savedInstanceState == null) {
                LogUtils.log("set his two");

                Cursor c = getContentResolver().query(DotaProvider.Players.CONTENT_URI, null, null, null, null, null);
                String steam64 = "";
                String avatar = "";
                String name = "";
                if(c != null && c.moveToFirst()){
                    steam64 = CursorHelper.getString(c, PlayerColumns.STEAM64);
                    avatar = CursorHelper.getString(c, PlayerColumns.AVATAR_FULL);
                    name = CursorHelper.getString(c, PlayerColumns.NAME);
                    c.close();
                }
                refreshHistoryFragment(steam64, avatar, name);
//                MatchHistoryFragment fragment = new MatchHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(MatchHistoryFragment.ARG_TWO_PANE, true);
//                fragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.match_history_container, fragment).commit();
            }
        }else {
            LogUtils.log("one pane");
            mTwoPane = false;
        }
        mPlayerCardFragment = (PlayerCardFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_player_card);
        mPlayerCardFragment.setTwoPaneLayout(mTwoPane);
        startPeriodicTask();

        //analytics
        ((DotaApp)getApplication()).startTracking();
    }

    @Override
    public void onPlayerCardClick(String steamID64, String imgUrl,
                                  PlayerSumAdapter.ViewHolder viewHolder, String name) {
        if(mTwoPane) {
            refreshHistoryFragment(steamID64, imgUrl, name);
        }else {
            Intent i = new Intent(this, MatchHistoryActivity.class);
            i.putExtra(MatchHistoryActivity.EXTRA_ID64, steamID64);
            i.putExtra(MatchHistoryActivity.EXTRA_IMGURL, imgUrl);
            i.putExtra(MatchHistoryActivity.EXTRA_NAME, name);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, viewHolder.ivPlayerAvatar, getString(R.string.transition_avatar));
            startActivity(i, options.toBundle());
        }
    }

    private void refreshHistoryFragment(String steamID64, String imgUrl, String name) {
        MatchHistoryFragment detailFragment = new MatchHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MatchHistoryFragment.ARG_ID64, steamID64);
        bundle.putString(MatchHistoryFragment.ARG_IMG, imgUrl);
        bundle.putString(MatchHistoryFragment.ARG_USER_NAME, name);
        bundle.putBoolean(MatchHistoryFragment.ARG_TWO_PANE, true);
        detailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.match_history_container, detailFragment).commit();
    }


    private void startPeriodicTask() {
        long period = 3 * 3600L;
        long flex = 10L;
        String periodicTag = "periodic";

        // create a periodic task to pull stocks once every hour after the app has been opened. This
        // is so Widget data stays up to date.
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(DotaTaskService.class)
                .setPeriod(period)
                .setFlex(flex)
                .setTag(periodicTag)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .build();
        // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
        // are updated.
        GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadedEvent(LoadedEvent event) {
        LogUtils.log("loaded in main to start refresh");
        LogUtils.log("event " + event.getName() +"==="+ event.getSteamId64() +"==="+ event.getImgUrl());
        if(mTwoPane) {
            refreshHistoryFragment(event.getSteamId64(), event.getImgUrl(), event.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
