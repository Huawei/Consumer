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

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.hms.ads.installreferrer.api.InstallReferrerClient;
import com.huawei.hms.ads.installreferrer.api.InstallReferrerStateListener;
import com.huawei.hms.ads.installreferrer.api.ReferrerDetails;

import java.io.IOException;

public class InstallReferrerSdkUtil {
    private static final String TAG = "InstallReferrerSdkUtil";
    private Context mContext;
    private InstallReferrerClient mReferrerClient;
    private InstallReferrerCallback mCallback;

    public InstallReferrerSdkUtil(Context context) {
        mContext = context;
    }

     /**
     * connect huawei ads service.
     */
    private boolean connect() {
        Log.i(TAG, "connect...");
        if (null == mContext) {
            Log.e(TAG, "connect context is null");
            return false;
        }
        // Create InstallReferrerClient
        mReferrerClient = InstallReferrerClient.newBuilder(mContext).setTest(true).build();
        // Start connecting service
        mReferrerClient.startConnection(installReferrerStateListener);
        return true;
    }

    /**
     * diconnect from huawei ads service.
     */
    private void disconnect() {
        Log.i(TAG, "disconnect");
        if (null != mReferrerClient) {
            mReferrerClient.endConnection();
            mReferrerClient = null;
            mContext = null;
        }
    }

    public void getInstallReferrer(InstallReferrerCallback callback) {
        if (null == callback) {
            Log.e(TAG, "getInstallReferrer callback is null");
            return;
        }
        mCallback = callback;
        connect();
    }

    /**
     * Obtain install referrer.
     */
    private void get() {
        if (null != mReferrerClient) {
            try {
                // install referrer id information. Do not call this method in the main thread.
                ReferrerDetails referrerDetails = mReferrerClient.getInstallReferrer();
                if (null != referrerDetails && null != mCallback) {
                    // Update install referer details.
                    mCallback.onSuccuss(referrerDetails.getInstallReferrer(),
                            referrerDetails.getReferrerClickTimestampMillisecond(),
                            referrerDetails.getInstallBeginTimestampMillisecond());
                }
            } catch (RemoteException e) {
                Log.i(TAG, "getInstallReferrer RemoteException: " + e.getMessage());
            } catch (IOException e) {
                Log.i(TAG, "getInstallReferrer IOException: " + e.getMessage());
            } finally {
                disconnect();
            }
        }
    }

    /**
     * create new connect listener.
     */
    private InstallReferrerStateListener installReferrerStateListener = new InstallReferrerStateListener() {
        @Override
        public void onInstallReferrerSetupFinished(int responseCode) {
            switch (responseCode) {
                case InstallReferrerClient.InstallReferrerResponse.OK:
                    Log.i(TAG, "connect ads kit ok");
                    get();
                    break;
                case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                    // Service not supported. Please download and install the latest version of Huawei Mobile Services(APK).
                    Log.i(TAG, "FEATURE_NOT_SUPPORTED");
                    break;
                case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                    // Service unavailable. Please update the version of Huawei Mobile Services(APK) to 2.6.5 or later.
                    Log.i(TAG, "SERVICE_UNAVAILABLE");
                    break;
                default:
                    Log.i(TAG, "responseCode: " + responseCode);
                    break;
            }
        }

        @Override
        public void onInstallReferrerServiceDisconnected() {
            Log.i(TAG, "onInstallReferrerServiceDisconnected");
        }
    };

}
