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

package com.huawei.pps.hms.test.installreferrer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.pps.hms.test.Constants;

import org.json.JSONObject;

/**
 * get install referrer by AIDL mode
 */
public class InstallReferrerAidlUtil {
    private static final String TAG = "InstallReferrerAidlUtil";
    private Context mContext;
    private ServiceConnection mServiceConnection;
    private InstallReferrerCallback mCallback;

    public InstallReferrerAidlUtil(Context context) {
        mContext = context;
    }

    private boolean bindService() {
        Log.i(TAG, "bindService");
        if (null == mContext) {
            Log.e(TAG, "context is null");
            return false;
        }

        boolean result =false;
        // TODO

        return result;
    }

    private void unbindService() {
        Log.i(TAG, "unbindService");
        if (null == mContext) {
            Log.e(TAG, "context is null");
            return;
        }
        if (null != mServiceConnection) {
            // TODO

            mContext = null;
            mCallback = null;
        }
    }

    public void getInstallReferrer(InstallReferrerCallback callback) {
        mCallback = callback;
        bindService();
    }
}
