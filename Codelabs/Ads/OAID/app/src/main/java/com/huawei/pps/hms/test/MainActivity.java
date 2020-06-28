/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
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
