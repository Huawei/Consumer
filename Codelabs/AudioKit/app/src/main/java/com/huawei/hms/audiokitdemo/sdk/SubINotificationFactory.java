/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.hms.audiokitdemo.sdk;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.api.config.NotificationConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.INotificationFactory;
import com.huawei.hms.audiokitdemo.MainActivity;
import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.constant.PlayActions;
import com.huawei.hms.audiokitdemo.utils.NotificationUtils;

import androidx.core.app.NotificationCompat;

/**
 * SubINotificationFactory
 *
 * @since 2020-06-01
 */
public class SubINotificationFactory implements INotificationFactory {
    private Application application;

    private HwAudioManager hwAudioManager;

    private static final String TAG = "SubINotificationFactory";

    /**
     * construct if notification factory
     * @param application application
     * @param hwAudioManager hwAudioManager
     */
    public SubINotificationFactory(Application application, HwAudioManager hwAudioManager) {
        this.application = application;
        this.hwAudioManager = hwAudioManager;
    }

    @Override
    public Notification createNotification(NotificationConfig notificationConfig) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(application, null);
            RemoteViews remoteViews = new RemoteViews(application.getPackageName(), R.layout.notification_player);
            builder.setContent(remoteViews);
            builder.setSmallIcon(R.drawable.icon_notifaction_music);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setCustomBigContentView(remoteViews);
            NotificationUtils.addChannel(application, NotificationUtils.NOTIFY_CHANNEL_ID_PLAY, builder);
            boolean isQueueEmpty = hwAudioManager.getQueueManager().isQueueEmpty();
            Bitmap bitmap;
            bitmap = notificationConfig.getBitmap();
            setBitmap(remoteViews, bitmap);
            boolean isPlaying = hwAudioManager.getPlayerManager().isPlaying() && !isQueueEmpty;
            remoteViews.setImageViewResource(R.id.image_toggle,
                    isPlaying ? R.drawable.ic_notification_stop : R.drawable.ic_notification_play);
            HwAudioPlayItem playItem = hwAudioManager.getQueueManager().getCurrentPlayItem();
            remoteViews.setTextViewText(R.id.text_song, playItem.getAudioTitle());
            remoteViews.setTextViewText(R.id.text_artist, playItem.getSinger());
            remoteViews.setImageViewResource(R.id.image_last, R.drawable.ic_notification_before);
            remoteViews.setImageViewResource(R.id.image_next, R.drawable.ic_notification_next);
            remoteViews.setOnClickPendingIntent(R.id.image_last, notificationConfig.getPrePendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.image_toggle, notificationConfig.getPlayPendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.image_next, notificationConfig.getNextPendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.image_close, getCancelPendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.layout_content, getMainIntent());
            return builder.build();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(application, null);
        RemoteViews remoteViews = new RemoteViews(application.getPackageName(), R.layout.statusbar);
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.drawable.icon_notifaction_music);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setCustomBigContentView(remoteViews);
        NotificationUtils.addChannel(application, NotificationUtils.NOTIFY_CHANNEL_ID_PLAY, builder);
        boolean isQueueEmpty = hwAudioManager.getQueueManager().isQueueEmpty();
        Bitmap bitmap;
        bitmap = notificationConfig.getBitmap();
        setBitmap(remoteViews, bitmap);
        boolean isPlaying = hwAudioManager.getPlayerManager().isPlaying() && !isQueueEmpty;
        remoteViews.setImageViewResource(R.id.widget_id_control_play,
                isPlaying ? R.drawable.notify_btn_pause_selector : R.drawable.notify_btn_play_selector);
        HwAudioPlayItem playItem = hwAudioManager.getQueueManager().getCurrentPlayItem();
        remoteViews.setTextViewText(R.id.trackname, playItem.getAudioTitle());
        remoteViews.setTextViewText(R.id.artistalbum, playItem.getSinger());
        remoteViews.setImageViewResource(R.id.widget_id_control_prev, R.drawable.notify_btn_close_selector);
        remoteViews.setImageViewResource(R.id.widget_id_control_next, R.drawable.notify_btn_next_selector);
        remoteViews.setOnClickPendingIntent(R.id.widget_id_control_prev, getCancelPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.widget_id_control_play, notificationConfig.getPlayPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.widget_id_control_next, notificationConfig.getNextPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.statusbar_layout, getMainIntent());
        return builder.build();
    }

    private void setBitmap(RemoteViews remoteViews, Bitmap bitmap) {
        if (bitmap != null) {
            Log.i(TAG, "Notification bitmap not empty");
            remoteViews.setImageViewBitmap(R.id.image_cover, bitmap);
        } else {
            Log.w(TAG, "Notification bitmap is null");
            remoteViews.setImageViewResource(R.id.image_cover, R.drawable.icon_notifaction_default);
        }
    }

    /**
     * cancel Notification
     *
     * @return android.app.PendingIntent
     */
    private PendingIntent getCancelPendingIntent() {
        Log.i(TAG, "getCancelPendingIntent");
        Intent closeIntent = new Intent(PlayActions.CANCEL_NOTIFICATION);
        closeIntent.setPackage(application.getPackageName());
        return PendingIntent.getBroadcast(application, 2, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * [Main entry PendingIntent]<BR>
     *
     * @return android.app.PendingIntent
     */
    private PendingIntent getMainIntent() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setClass(application.getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return PendingIntent.getActivity(application, 0, intent, 0);
    }
}
