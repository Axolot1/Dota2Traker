package com.axolotl.dota2traker.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.axolotl.dota2traker.DotaApp;
import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.adapter.PlayerSumAdapter;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.event.LoadErrorEvent;
import com.axolotl.dota2traker.event.LoadedEvent;
import com.axolotl.dota2traker.event.RefreshEvent;
import com.axolotl.dota2traker.service.DotaIntentService;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.axolotl.dota2traker.view.InputDialog;
import com.axolotl.dota2traker.view.RecyclerViewEmptySupport;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by axolotl on 16/6/28.
 */
public class PlayerCardFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, PlayerSumAdapter.OnCardClickListener {

    private static final int CURSOR_LOADER_ID = 0;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rcv_player_card)
    RecyclerViewEmptySupport rcvPlayerCard;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;


    //    PlayerCardAdapter mAdapter;
//    PlayerCardPresenter mPresenter;
    PlayerSumAdapter mPlayerCursorAdapter;
    ProgressDialog mProgressDialog;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    private boolean mTwoPaneLayout;

    public interface CallBack {
        void onPlayerCardClick(String steamID64, String imgUrl, PlayerSumAdapter.ViewHolder viewHolder,
                               String name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DotaApp.getNetComponent(getActivity()).inject(this);
    }

    public void setTwoPaneLayout(boolean twoPaneLayout) {
        this.mTwoPaneLayout = twoPaneLayout;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        rcvPlayerCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvPlayerCard.setEmptyView(tvEmpty);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        mPlayerCursorAdapter = new PlayerSumAdapter(getContext(), null, this);
        rcvPlayerCard.setAdapter(mPlayerCursorAdapter);
        mProgressDialog = new ProgressDialog(getContext());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Cursor c = getContext().getContentResolver().query(DotaProvider.Players.CONTENT_URI, null, null, null, null);
                if (c == null || c.getCount() < 1) {
                    Toast.makeText(getContext(), R.string.no_player_hint, Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    List<String> ids = new ArrayList<>();
                    while (c.moveToNext()) {
                        ids.add(CursorHelper.getString(c, PlayerColumns.STEAM64));
                    }
                    DotaIntentService.startRefreshPlayerInfo(getContext(), TextUtils.join(",", ids));
                }
            }
        });

        //admob
        AdView mAdView = (AdView) v.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return v;
    }

    @OnClick(R.id.fab)
    public void onClick(final View v) {
        if (!DotaUtil.isConnected(getContext())) {
            Toast.makeText(getContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        new InputDialog.Builder(getContext())
                .setTitle(getContext().getString(R.string.search_title))
                .setInputMaxWords(10)
                .setInputHint(getContext().getString(R.string.search_hint))
                .setPositiveButton(getContext().getString(R.string.search),
                        new InputDialog.ButtonActionListener() {
                            @Override
                            public void onClick(CharSequence inputText) {

                                String s = inputText.toString().trim();
                                if (TextUtils.isEmpty(s) || s.length() < 8) {
                                    Snackbar.make(v, R.string.wrong_id, Snackbar.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                                long id32 = 0;
                                try {
                                    id32 = Long.parseLong(s);
                                } catch (NumberFormatException e) {
                                    Snackbar.make(v, R.string.wrong_id, Snackbar.LENGTH_SHORT)
                                            .show();
                                    return;
                                }

                                Cursor c = getContext().getContentResolver()
                                        .query(DotaProvider.Players.withSteamId64(DotaUtil.get64Id(id32)),
                                                null, null, null, null);
                                if (c == null || c.getCount() == 0) {
                                    showLoadingProgress();
                                    DotaIntentService.startActionAdd(getContext(), s);
                                } else {
                                    c.close();
                                    LogUtils.log("cursor null");
                                    Toast.makeText(getContext(), "Player Added", Toast.LENGTH_SHORT).show();
                                }
                            }

                        })
                .setNegativeButton(getContext().getString(R.string.cancel),
                        new InputDialog.ButtonActionListener() {
                            @Override
                            public void onClick(CharSequence inputText) {
                            }
                        })
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoPlayerEvent(LoadErrorEvent event) {
        hideLoadingProgress();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadedEvent(LoadedEvent event) {
        hideLoadingProgress();
        Toast.makeText(getContext(), R.string.load_suc, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event) {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void showLoadingProgress() {
        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        mProgressDialog.setMessage("loading data");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hideLoadingProgress() {
        mProgressDialog.dismiss();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("dota", "create loader");
        return new CursorLoader(getContext(), DotaProvider.Players.CONTENT_URI,
                new String[]{PlayerColumns._ID, PlayerColumns.NAME, PlayerColumns.STEAM64,
                        PlayerColumns.AVATAR_FULL, PlayerColumns.PERSONA_STATE},
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlayerCursorAdapter.swapCursor(data);
//        mCursor = data;
//        if (mCursor == null || !mCursor.moveToNext()) {
//            mTvEmpty.setVisibility(View.VISIBLE);
//        } else {
//            mTvEmpty.setVisibility(View.GONE);
//        }

//        if(mSwipeRefreshLayout.isRefreshing()){
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("dota", "load reset");
        mPlayerCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String steam64, String imgUrl, PlayerSumAdapter.ViewHolder viewHolder, String name) {
        ((CallBack) getActivity()).onPlayerCardClick(steam64, imgUrl, viewHolder, name);
    }
}
