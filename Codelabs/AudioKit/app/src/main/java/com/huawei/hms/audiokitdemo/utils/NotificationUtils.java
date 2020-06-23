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

package com.huawei.hms.audiokitdemo.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * Notification utils
 *
 * @since 2020-06-01
 */
public class NotificationUtils {

    /**
     * The id of the channel.
     */
    public static final String NOTIFY_CHANNEL_ID_PLAY = "music_notify_channel_id_play";

    /**
     * add channel
     *
     * @param channelId channelId
     * @param builder Notification builder
     */
    @TargetApi(android.os.Build.VERSION_CODES.O)
    public static void addChannel(Application application, String channelId, NotificationCompat.Builder builder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        createNotificationChannel(application, channelId, builder);
    }

    @TargetApi(android.os.Build.VERSION_CODES.O)
    private static void createNotificationChannel(Application application, String channelId,
        NotificationCompat.Builder builder) {

        NotificationChannel notificationChannel =
            new NotificationChannel(channelId, "Play", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableVibration(false);
        notificationChannel.setSound(null, null);
        NotificationManager notificationManager =
            (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setGroup(channelId);
        builder.setChannelId(channelId);
    }
}
