package com.axolotl.dota2traker.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.dota2traker.DotaApp;
import com.axolotl.dota2traker.MatchDetailActivity;
import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.adapter.HistoryAdapter;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.MatchDetailColumns;
import com.axolotl.dota2traker.retrofit.model.MatchDetailRes;
import com.axolotl.dota2traker.service.DotaIntentService;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by axolotl on 16/7/3.
 */
public class MatchHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        HistoryAdapter.OnItemClickListener {

    public static final String ARG_ID64 = "arg_steamID64";
    public static final String ARG_IMG = "arg_imgurl";
    public static final String ARG_USER_NAME = "arg_namep";
    public static final String ARG_TWO_PANE = "arg_two_pane";
    private static final int CURSOR_LOADER_ID = 1;


    private static String steamID64;
    private static String imgUrl;
    private static String name;

    @Bind(R.id.rcv_history)
    RecyclerView rcvHistory;
    @Bind(R.id.iv_player_avatar)
    CircleImageView ivPlayerAvatar;
    @Bind(R.id.tv_player_name)
    TextView tvPlayerName;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.app_bar)
    Toolbar appBar;
    private HistoryAdapter mAdapter;
    private int mMutedColor = 0xFF333333;
    private boolean mTwoPane;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DotaApp.getNetComponent(getActivity()).inject(this);
        Bundle argument = getArguments();
        if (argument != null) {
            LogUtils.log("has argu");
            steamID64 = argument.getString(ARG_ID64);
            imgUrl = argument.getString(ARG_IMG);
            name = argument.getString(ARG_USER_NAME);
            mTwoPane = argument.getBoolean(ARG_TWO_PANE);
            LogUtils.log("his " + mTwoPane);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_match_history, container, false);
        ButterKnife.bind(this, v);
        if(!mTwoPane) {
            appBar.setNavigationIcon(R.drawable.arrow_left);
            appBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().supportFinishAfterTransition();
                }
            });
        }
        Glide.with(getContext()).load(imgUrl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Palette p = Palette.from(resource).generate();
//                            mMutedColor = p.getDarkMutedColor(0xFF333333);
                        mMutedColor = p.getDarkMutedColor(0xFF333333);
                        appBarLayout.setBackgroundColor(mMutedColor);
                        ImageViewTarget imTarget = (ImageViewTarget) target;
                        return new DrawableCrossFadeFactory<>()
                                .build(isFromMemoryCache, isFirstResource)
                                .animate(new BitmapDrawable(imTarget.getView().getResources(), resource), imTarget);
                    }
                }).into(ivPlayerAvatar);
        if(!TextUtils.isEmpty(steamID64)) {
            tvAccount.setText(DotaUtil.get32Id(Long.parseLong(steamID64)));
        }
        tvPlayerName.setText(name);

//        DotaIntentService.startActionUpdateMatches(getContext(), DotaUtil.get32Id(Long.parseLong(steamID64)));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        mAdapter = new HistoryAdapter(null, getContext(), this);
        rcvHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvHistory.setAdapter(mAdapter);
        rcvHistory.setNestedScrollingEnabled(false);

        return v;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        LogUtils.log("history on create " + steamID64);
        if(TextUtils.isEmpty(steamID64)){
            return null;
        }
        return new CursorLoader(getContext(), DotaProvider.Matches.CONTENT_URI,
                null,
                "steamid=?",
                new String[]{steamID64},
                MatchDetailColumns.START_TIME + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        LogUtils.log("history load finish");
        List<HistoryAdapter.HistoryItem> dataItems = new ArrayList<>();
        Gson gson = new Gson();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                HistoryAdapter.HistoryItem item = new HistoryAdapter.HistoryItem();
                //match id
                String matchId = CursorHelper.getString(data, MatchDetailColumns.MATCH_ID);
                item.setMatchId(matchId);
                //time
                item.setTime((long) (CursorHelper.getDouble(data, MatchDetailColumns.START_TIME) * 1000));
                //game mode
                item.setGameMode(DotaUtil.getLobby(CursorHelper.getInt(data, MatchDetailColumns.LOBBY_TYPE), getContext()));
                String id32 = DotaUtil.get32Id(Long.valueOf(steamID64));
                boolean radiantWin = CursorHelper.getInt(data, MatchDetailColumns.RADIANT_WIN) == 0;
                String playerMsg = CursorHelper.getString(data, MatchDetailColumns.PLAYERS_MSG);
                List<MatchDetailRes.PlayerMatchDetail> players = gson.fromJson(playerMsg,
                        new TypeToken<List<MatchDetailRes.PlayerMatchDetail>>() {
                        }.getType());
                if (players != null) {
                    for (MatchDetailRes.PlayerMatchDetail p : players) {
                        if (id32.equals(p.accountId)) {
                            //win
                            boolean win = (radiantWin && DotaUtil.isRadiant(p.playerSlot))
                                    || (!radiantWin && !DotaUtil.isRadiant(p.playerSlot));
                            item.setWin(win);
                            //kda
                            item.setKda(p.kills + "/" + p.deaths + "/" + p.assists);
                            //hero
                            Integer heroD = DotaUtil.HERO.get(p.heroId);
                            if (null == heroD) {
                                heroD = R.drawable.def;
                            }
                            item.setHeroDrawable(heroD);
                            break;
                        }
                    }
                }
                dataItems.add(item);
            } while (data.moveToNext());
            //set data
            mAdapter.setData(dataItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        LogUtils.log("history loader reset");
        mAdapter.setData(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(String matchId) {
//        Toast.makeText(getContext(), matchId, Toast.LENGTH_SHORT).show();
        MatchDetailActivity.startDetailActivity(getContext(), matchId);
    }
}
