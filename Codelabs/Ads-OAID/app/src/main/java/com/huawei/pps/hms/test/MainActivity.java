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

package com.huawei.pps.hms.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.huawei.pps.hms.test.oaid.OaidActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RelativeLayout mAdIdRl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //Create the "ad_id" view, which tries to enter "OAID" show page.
        mAdIdRl = findViewById(R.id.enter_ad_id_rl);
        mAdIdRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enter_ad_id_rl) {
            startActivity(OaidActivity.class);
        }
    }

    private void startActivity(Class activity) {
        try {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "startActivity Exception: " + e.toString());
        }
    }
}
