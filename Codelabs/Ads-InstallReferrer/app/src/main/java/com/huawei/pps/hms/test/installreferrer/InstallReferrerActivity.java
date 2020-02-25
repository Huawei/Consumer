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

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.huawei.pps.hms.test.BaseActivity;
import com.huawei.pps.hms.test.CallMode;
import com.huawei.pps.hms.test.R;

public class InstallReferrerActivity extends BaseActivity implements InstallReferrerCallback {
    private static final String TAG = "InstallReferrerActivity";
    private TextView mReferrerTv;
    private TextView mClickTimeTv;
    private TextView mInstallTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_referrer);
        init();
    }

    protected void init() {
        super.init();
        mReferrerTv = findViewById(R.id.install_referrer_tv);
        mClickTimeTv = findViewById(R.id.click_time_tv);
        mInstallTimeTv = findViewById(R.id.install_time_tv);
        connectThread.start();
    }

    /**
     * Get install referrer from a non-UI thread.
     */
    private Thread connectThread = new Thread() {
        @Override
        public void run() {
            getInstallReferrer();
        }
    };

    /**
     * Update install referrer from a UI thread.
     */
    private void updateReferrerDetails(final String installReferrer, final long clickTimestamp,
                                       final long installTimestamp) {
        if (TextUtils.isEmpty(installReferrer)) {
            Log.w(TAG, "installReferrer is empty");
            return;
        }
        Log.i(TAG, "installReferrer: " + installReferrer +
                ", clickTimestamp: " + clickTimestamp +
                ", installTimestamp: " + installTimestamp);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mReferrerTv.setText(installReferrer);
                mClickTimeTv.setText(String.valueOf(clickTimestamp));
                mInstallTimeTv.setText(String.valueOf(installTimestamp));
            }
        });
    }

    private void getInstallReferrer() {
        int mode = getIntExtra("mode", CallMode.SDK);
        Log.i(TAG, "getInstallReferrer mode=" + mode);
        if (CallMode.SDK == mode) {
            // Get install referrer by sdk mode.
            InstallReferrerSdkUtil sdkUtil = new InstallReferrerSdkUtil(this);
            sdkUtil.getInstallReferrer(this);
        } else if (CallMode.AIDL == mode) {
            // Get install referrer by aidl mode.
            InstallReferrerAidlUtil aidlUtil = new InstallReferrerAidlUtil(this);
            aidlUtil.getInstallReferrer(this);
        }
    }

    @Override
    public void onSuccuss(String installReferrer, long clickTimestamp, long installTimestamp) {
        Log.i(TAG, "onSuccuss");
        updateReferrerDetails(installReferrer, clickTimestamp, installTimestamp);
    }

    @Override
    public void onFail(String errMsg) {
        Log.e(TAG, "onFail:" + errMsg);
    }

}


