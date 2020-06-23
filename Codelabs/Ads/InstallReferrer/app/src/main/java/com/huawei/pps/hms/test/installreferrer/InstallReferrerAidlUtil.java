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

import com.huawei.android.hms.ppskit.IPPSChannelInfoService;
import com.huawei.pps.hms.test.Constants;

import org.json.JSONObject;

/**
 * get install referrer by AIDL mode
 */
public class InstallReferrerAidlUtil {
    private static final String TAG = "InstallReferrerAidlUtil";
    private Context mContext;
    private ServiceConnection mServiceConnection;
    private IPPSChannelInfoService mService;
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
        mServiceConnection = new InstallReferrerServiceConnection();
        Intent intent = new Intent(Constants.SERVICE_ACTION);
        intent.setPackage(Constants.TEST_SERVICE_PACKAGE_NAME);
        // Bind HUAWEI Ads kit
        boolean result = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "bindService result: " + result);
        return result;
    }

    private void unbindService() {
        Log.i(TAG, "unbindService");
        if (null == mContext) {
            Log.e(TAG, "context is null");
            return;
        }
        if (null != mServiceConnection) {
            // Unbind HUAWEI Ads kit
            mContext.unbindService(mServiceConnection);
            mService = null;
            mContext = null;
            mCallback = null;
        }
    }

    public void getInstallReferrer(InstallReferrerCallback callback) {
        mCallback = callback;
        bindService();
    }

    private final class InstallReferrerServiceConnection implements ServiceConnection {

        private InstallReferrerServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected");
            mService = IPPSChannelInfoService.Stub.asInterface(iBinder);
            if (null != mService) {
                try {
                    // Get channel info（Json format）
                    String channelJson = mService.getChannelInfo();
                    Log.i(TAG, "channelJson: " + channelJson);
                    // Parser
                    JSONObject jsonObject = new JSONObject(channelJson);
                    // Get install referrer.
                    String installReferrer = jsonObject.optString("channelInfo");
                    long clickTimestamp = jsonObject.optLong("clickTimestamp", 0);
                    long installTimestamp = jsonObject.optLong("installTimestamp", 0);
                    if (null != mCallback) {
                        // Update install referer details.
                        mCallback.onSuccuss(installReferrer, clickTimestamp, installTimestamp);
                    } else {
                        mCallback.onFail("install referrer is empty");
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "getChannelInfo RemoteException");
                    mCallback.onFail(e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "getChannelInfo Excepition");
                    mCallback.onFail(e.getMessage());
                } finally {
                    unbindService();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected");
            mService = null;
        }
    }

}
