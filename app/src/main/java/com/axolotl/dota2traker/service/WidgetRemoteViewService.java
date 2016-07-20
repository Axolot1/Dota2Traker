package com.axolotl.dota2traker.service;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.axolotl.dota2traker.MatchHistoryActivity;
import com.axolotl.dota2traker.R;
import com.axolotl.dota2traker.data.DotaProvider;
import com.axolotl.dota2traker.data.PlayerColumns;
import com.axolotl.dota2traker.utils.CursorHelper;
import com.axolotl.dota2traker.utils.DotaUtil;
import com.axolotl.dota2traker.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

/**
 * Created by axolotl on 16/7/18.
 */
public class WidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                LogUtils.log("RemoteViewsFactory onCreate");
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(DotaProvider.Players.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                LogUtils.log("onDataSetChanged data size: " + data.getCount());
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                LogUtils.log("getViewAt " + position);
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                LogUtils.log("getViewAt to bind");
                String name = CursorHelper.getString(data, PlayerColumns.NAME);
                String steamId64 = CursorHelper.getString(data, PlayerColumns.STEAM64);
                String avatarUrl = CursorHelper.getString(data, PlayerColumns.AVATAR_FULL);
                LogUtils.log("name " + name);
                LogUtils.log("id " + steamId64);
                LogUtils.log("url " + avatarUrl);
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_list_item);
                remoteViews.setTextViewText(R.id.tv_player_name, name);
                remoteViews.setTextViewText(R.id.tv_account_id, DotaUtil.get32Id(Long.parseLong(steamId64)));
                Bitmap avatar = null;
                try {
                    avatar = Glide.with(WidgetRemoteViewService.this)
                            .load(avatarUrl)
                            .asBitmap()
                            .error(R.drawable.def)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if(avatar != null){
                    remoteViews.setImageViewBitmap(R.id.iv_player_avatar, avatar);
                }else{
                    remoteViews.setImageViewResource(R.id.iv_player_avatar, R.drawable.user_def);
                }
                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(MatchHistoryActivity.EXTRA_NAME, name);
                fillInIntent.putExtra(MatchHistoryActivity.EXTRA_IMGURL, avatarUrl);
                fillInIntent.putExtra(MatchHistoryActivity.EXTRA_ID64, steamId64);
                remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
