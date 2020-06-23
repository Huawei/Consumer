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

package com.huawei.pps.hms.test.oaid;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.huawei.pps.hms.test.BaseActivity;
import com.huawei.pps.hms.test.R;

public class OaidActivity extends BaseActivity implements OaidCallback {
    private static final String TAG = "OaidActivity";
    private TextView mAdIdTv;
    private TextView mDisableAdIdTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_id);
        init();
    }

    protected void init() {
        super.init();
        // Create the "ad_id" TextView, which tries to show the obtained "OAID".
        mAdIdTv = findViewById(R.id.ad_id_tv);
        // Create the "disable_ad_id" TextView, which tries to show the obtained "Disable Personalized Ads" Switch
        mDisableAdIdTv = findViewById(R.id.disable_ad_id_tv);
        getIdentifierThread.start();
    }

    /**
     * Obtains device ID information from a non-UI thread.
     */
    private Thread getIdentifierThread = new Thread() {

        @Override
        public void run() {
            getOaid();
        }
    };

    private void getOaid() {
        //  Get OAID by sdk mode.
        OaidSdkUtil.getOaid(this, this);
    }

    /**
     * Update the device ID information from a UI thread.
     */
    private void updateAdIdInfo(final String oaid, final boolean isLimitAdTrackingEnabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(oaid)) {
                    mAdIdTv.setText(oaid);
                }
                mDisableAdIdTv.setText(String.valueOf(isLimitAdTrackingEnabled));
            }
        });
    }

    @Override
    public void onSuccuss(String oaid, boolean isOaidTrackLimited) {
        Log.i(TAG, "oiad=" + oaid + ", isLimitAdTrackingEnabled=" + isOaidTrackLimited);
        updateAdIdInfo(oaid, isOaidTrackLimited);
    }

    @Override
    public void onFail(String errMsg) {
        Log.e(TAG, "getOaid Fail: " + errMsg);
    }

}


