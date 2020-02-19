/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
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
        // 创建InstallReferrerClient实例 | Create InstallReferrerClient
        mReferrerClient = InstallReferrerClient.newBuilder(mContext).setTest(true).build();
        // 连接转化跟踪参数服务 | Start connecting service
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
                //获取转化跟踪参数，不要在主线程中调用该方法。 | install referrer id information. Do not call this method in the main thread.
                ReferrerDetails referrerDetails = mReferrerClient.getInstallReferrer();
                if (null != referrerDetails && null != mCallback) {
                    // 展示转化跟踪参数 | Update install referer details.
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
                    //服务不支持,下载安装最新华为移动服务（APK）| Service not supported. Please download and install the latest version of Huawei Mobile Services(APK).
                    Log.i(TAG, "FEATURE_NOT_SUPPORTED");
                    break;
                case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                    //服务不存在, 升级华为移动服务（APK）版本2.6.5及以上 | Service unavailable. Please update the version of Huawei Mobile Services(APK) to 2.6.5 or later.
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
