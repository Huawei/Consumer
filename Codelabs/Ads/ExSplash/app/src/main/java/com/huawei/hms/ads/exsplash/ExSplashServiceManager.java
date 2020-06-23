/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.hms.ads.exsplash;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.hms.ads.ExSplashService;

public class ExSplashServiceManager {
    private static final String TAG = ExSplashServiceManager.class.getSimpleName();

    /**
     * Action of ExSplash Service
     */
    private static final String ACTION_EXSPLASH = "com.huawei.hms.ads.EXSPLASH_SERVICE";

    /**
     * Package name of ExSplash Service
     */
    private static final String PACKAGE_NAME = "com.huawei.hwid";

    private Context context;
    private ServiceConnection serviceConnection;
    private ExSplashService exSplashService;
    private boolean enable;

    public ExSplashServiceManager(Context context) {
        this.context = context;
    }

    /**
     * Enable user protocol
     */
    public void enableUserInfo(boolean enable) {
        this.enable = enable;
        bindService();
    }

    private boolean bindService() {
        Log.i(TAG, "bindService");
        serviceConnection = new ExSplashServiceConnection(context);
        Intent intent = new Intent(ACTION_EXSPLASH);
        intent.setPackage(PACKAGE_NAME);
        boolean result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "bindService result: " + result);
        return result;
    }

    private void unbindService() {
        Log.i(TAG, "unbindService");
        if (null == context) {
            Log.e(TAG, "context is null");
            return;
        }

        if (null != serviceConnection) {
            context.unbindService(serviceConnection);
            exSplashService = null;
            context = null;
        }
    }

    public final class ExSplashServiceConnection implements ServiceConnection {

        private static final String TAG = "ExSplashConnection";

        private Context context;

        public ExSplashServiceConnection(Context context) {
            this.context = context;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            exSplashService = ExSplashService.Stub.asInterface(service);
            if (exSplashService != null) {
                try {
                    exSplashService.enableUserInfo(enable);
                    Log.i(TAG, "enableUserInfo done");
                } catch (RemoteException e) {
                    Log.i(TAG, "enableUserInfo error");
                } finally {
                    context.unbindService(this);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    }
}