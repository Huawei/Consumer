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

package com.huawei.hms.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.wallet.apptest.R;

public class MainIndexActivity extends FragmentActivity implements HuaweiApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_main);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed: " + connectionResult);
    }

    public void saveLoyaltyCard(View view) {
        Intent intent = new Intent(this, PassDataObjectActivity.class);
        startActivity(intent);
    }

    public void saveGiftCard(View view) {
        Intent intent = new Intent(this, GiftCardActivity.class);
        startActivity(intent);
    }

    public void saveCouponCard(View view) {
        Intent intent = new Intent(this, CouponCardActivity.class);
        startActivity(intent);
    }

}
