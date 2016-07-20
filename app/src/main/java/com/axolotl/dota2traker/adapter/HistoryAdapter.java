package com.axolotl.dota2traker.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.utils.LogUtils;
import com.axolotl.dota2traker.view.BezelImageView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by axolotl on 16/7/5.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryItem> mData;
    private Context mCtx;
    private OnItemClickListener mListener;
    private PrettyTime mPrettyTime;

    public HistoryAdapter(List<HistoryItem> mData, Context ctx, OnItemClickListener listener) {
        this.mData = mData;
        this.mCtx = ctx;
        this.mListener = listener;
        mPrettyTime = new PrettyTime(Locale.getDefault());
    }

    public List<HistoryItem> getData() {
        return mData;
    }

    public void setData(List<HistoryItem> mData) {
        if (mData != null) {
            LogUtils.log(mData.size() + "--");
            this.mData = mData;
            this.notifyDataSetChanged();
        } else {
            LogUtils.log("adapter data null ");
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryItem item = mData.get(position);
        holder.ivHeroAvatar.setImageResource(item.heroDrawable);
        holder.tvKda.setText(item.kda);
        holder.tvLobbyType.setText(item.gameMode);
        holder.tvResult.setText(item.win ? mCtx.getString(R.string.win) : mCtx.getString(R.string.lost));
        holder.tvResult.setTextColor(mCtx.getResources()
                .getColor(item.win ? R.color.radiant_green : R.color.dire_red));
//        holder.tvTime.setText(DateUtils.getRelativeTimeSpanString(item.time,
//                System.currentTimeMillis(),
//                DateUtils.FORMAT_ABBREV_ALL)
//                .toString().toLowerCase());
        holder.tvTime.setText(mPrettyTime.format(new Date(item.time)));
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_hero_avatar)
        BezelImageView ivHeroAvatar;
        @Bind(R.id.tv_result)
        TextView tvResult;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_lobby_type)
        TextView tvLobbyType;
        @Bind(R.id.tv_kda)
        TextView tvKda;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HistoryItem item = mData.get(getAdapterPosition());
            mListener.onClick(item.matchId);
        }
    }

    public interface OnItemClickListener {
        void onClick(String matchId);
    }

    public static class HistoryItem {
        public String matchId;
        public int heroDrawable;
        public boolean win;
        public long time;
        public String gameMode;
        public String kda;

        public HistoryItem(String matchId, int heroDrawable, boolean win, long time, String gameMode, String kda) {
            this.matchId = matchId;
            this.heroDrawable = heroDrawable;
            this.win = win;
            this.time = time;
            this.gameMode = gameMode;
            this.kda = kda;
        }

        public HistoryItem() {
        }


        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public int getHeroDrawable() {
            return heroDrawable;
        }

        public void setHeroDrawable(int heroDrawable) {
            this.heroDrawable = heroDrawable;
        }

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getGameMode() {
            return gameMode;
        }

        public void setGameMode(String gameMode) {
            this.gameMode = gameMode;
        }

        public String getKda() {
            return kda;
        }

        public void setKda(String kda) {
            this.kda = kda;
        }
    }
}
