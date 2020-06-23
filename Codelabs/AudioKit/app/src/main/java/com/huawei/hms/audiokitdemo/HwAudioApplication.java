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

package com.huawei.hms.audiokitdemo;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.huawei.hms.audiokitdemo.constant.PlayActions;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;

/**
 * Application
 *
 * @since 2020-06-01
 */
public class HwAudioApplication extends Application {
    private static final String TAG = "HwAudioApplication";

    private static class ClickEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (PlayActions.CANCEL_NOTIFICATION.equals(action)) {
                Log.i(TAG, "onReceive----->cancelNotification");
                PlayHelper.getInstance().stop();
            }
        }
    }

    private BroadcastReceiver mClickEventReceiver = new ClickEventReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intent = new IntentFilter();
        intent.addAction(PlayActions.CANCEL_NOTIFICATION);
        registerReceiver(mClickEventReceiver, intent);
        PlayHelper.getInstance().init(this);
    }
}
