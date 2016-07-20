package com.axolotl.dota2traker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.retrofit.model.Match;
import com.axolotl.dota2traker.retrofit.model.PlayerSummaries;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by axolotl on 16/7/10.
 */
public class MatchPlayerAdapter extends RecyclerView.Adapter<MatchPlayerAdapter.ViewHolder> {

    private Context mContext;
    private List<PlayerInMatch> mData;

    public MatchPlayerAdapter(Context mContext, List<PlayerInMatch> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<PlayerInMatch> players){
        this.mData = players;
        this.notifyDataSetChanged();
    }

    public void refreshData(List<PlayerSummaries> playerSummaries){
        if(mData != null){
            for(PlayerSummaries p : playerSummaries){
                for(PlayerInMatch tp : mData){
                    if(p.getSteamid().equals(tp.getSteamID64())){
                        tp.setPlayerName(p.getPersonaname());
                        tp.setPlayerAvatar(p.getAvatarfull());
                        break;
                    }
                }
            }
            this.notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_player_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayerInMatch p = mData.get(position);
        holder.ivHeroAvatar.setImageResource(p.getHeroAvatar());
        holder.ivItem0.setImageResource(p.getItem0());
        holder.ivItem1.setImageResource(p.getItem1());
        holder.ivItem2.setImageResource(p.getItem2());
        holder.ivItem3.setImageResource(p.getItem3());
        holder.ivItem4.setImageResource(p.getItem4());
        holder.ivItem5.setImageResource(p.getItem5());
        holder.tvDamage.setText(p.getDamage());
        holder.tvInBattle.setText(p.getInBattle());
        holder.tvKda.setText(p.getKda());
        if(!TextUtils.isEmpty(p.getPlayerAvatar())){
            Glide.with(mContext).load(p.getPlayerAvatar()).crossFade().into(holder.ivPlayerAvatar);
        }
        if(!TextUtils.isEmpty(p.getPlayerName())){
            holder.tvPlayerName.setText(p.getPlayerName());
        }else{
            holder.tvPlayerName.setText(R.string.anony_player_name);
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_hero_avatar)
        ImageView ivHeroAvatar;
        @Bind(R.id.tv_player_name)
        TextView tvPlayerName;
        @Bind(R.id.tv_in_battle)
        TextView tvInBattle;
        @Bind(R.id.tv_damage)
        TextView tvDamage;
        @Bind(R.id.tv_kda)
        TextView tvKda;
        @Bind(R.id.iv_item_0)
        ImageView ivItem0;
        @Bind(R.id.iv_item_1)
        ImageView ivItem1;
        @Bind(R.id.iv_item_2)
        ImageView ivItem2;
        @Bind(R.id.iv_item_3)
        ImageView ivItem3;
        @Bind(R.id.iv_item_4)
        ImageView ivItem4;
        @Bind(R.id.iv_item_5)
        ImageView ivItem5;
        @Bind(R.id.iv_player_avatar)
        CircleImageView ivPlayerAvatar;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
