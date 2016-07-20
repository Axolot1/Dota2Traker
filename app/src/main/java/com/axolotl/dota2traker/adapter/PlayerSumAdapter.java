package com.axolotl.dota2traker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.axolotl.dota2traker.view.BezelImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by axolotl on 16/6/30.
 */
public class PlayerSumAdapter extends CursorRecyclerViewAdapter<PlayerSumAdapter.ViewHolder> {

    private Context mContext;
    private final OnCardClickListener mListener;
    private int mMutedColor = 0xFF333333;

    public PlayerSumAdapter(Context context, Cursor cursor, OnCardClickListener listener) {
        super(context, cursor);
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        String avatarUrl = cursor.getString(cursor.getColumnIndex(PlayerColumns.AVATAR_FULL));
        if (avatarUrl != null) {
//            Glide.with(mContext)
//                    .load(avatarUrl).crossFade()
//                    .into(holder.ivPlayerAvatar);
            Glide.with(mContext).load(avatarUrl)
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
                            mMutedColor = p.getLightMutedColor(0xFF333333);
                            holder.cardView.setCardBackgroundColor(mMutedColor);
                            ImageViewTarget imTarget = (ImageViewTarget) target;
                            return new DrawableCrossFadeFactory<>()
                                    .build(isFromMemoryCache, isFirstResource)
                                    .animate(new BitmapDrawable(imTarget.getView().getResources(), resource), imTarget);
                        }
                    }).into(holder.ivPlayerAvatar);
        }
        holder.tvDotaId.setText(String.format(mContext.getString(R.string.dotaId),
                DotaUtil.get32Id(Long.parseLong(cursor.getString(cursor.getColumnIndex(PlayerColumns.STEAM64))))));
        holder.tvPlayerName.setText(cursor.getString(cursor.getColumnIndex(PlayerColumns.NAME)));
        holder.tvStatus.setText(String.format(mContext.getString(R.string.status),
                DotaUtil.getStatus(cursor.getInt(cursor.getColumnIndex(PlayerColumns.PERSONA_STATE)), mContext)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_card_layout, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_player_avatar)
        public CircleImageView ivPlayerAvatar;
        @Bind(R.id.tv_player_name)
        TextView tvPlayerName;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_dota_id)
        TextView tvDotaId;
        @Bind(R.id.player_card)
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            getCursor().moveToPosition(adapterPosition);
//            String steam = DotaUtil.get32Id(Long.parseLong());
            mListener.onClick(CursorHelper.getString(getCursor(), PlayerColumns.STEAM64),
                    CursorHelper.getString(getCursor(), PlayerColumns.AVATAR_FULL), this,
                    CursorHelper.getString(getCursor(), PlayerColumns.NAME));
        }
    }

    public interface OnCardClickListener {
        void onClick(String steam64, String imgUrl, ViewHolder viewHolder, String name);
    }
}
