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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.huawei.pps.hms.test.installreferrer.InstallReferrerActivity;
import com.huawei.pps.hms.test.installreferrer.InstallReferrerWriteActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RelativeLayout mInstallReferrerRl;
    private RelativeLayout mWriteInstallReferrerRl;
    private RadioGroup mModeGroup;
    private int mCallMode = CallMode.SDK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        // 进入转化跟踪参数示例。| Create the "install_referrer" TextView, which tries to show the obtained install referrer.
        mInstallReferrerRl = findViewById(R.id.enter_install_referrer_rl);
        mInstallReferrerRl.setOnClickListener(this);
        // 进入写入转化跟踪参数示例。| // Create the "write_install_referrer" view, which tries to enter the page where user can set service package name and install referer.
        mWriteInstallReferrerRl = findViewById(R.id.write_install_referrer_rl);
        mWriteInstallReferrerRl.setOnClickListener(this);

        mModeGroup = findViewById(R.id.call_mode_rg);
        mModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getCallMode(checkedId);
            }
        });
    }

    private void getCallMode(int buttonId) {
        if (R.id.mode_sdk_rb == buttonId) {
            mCallMode = CallMode.SDK;
        } else if (R.id.mode_aidl_rb == buttonId) {
            mCallMode = CallMode.AIDL;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enter_install_referrer_rl) {
            startActivity(InstallReferrerActivity.class);
        } else if (id == R.id.write_install_referrer_rl) {
            startActivity(InstallReferrerWriteActivity.class);
        }
    }

    private void startActivity(Class activity) {
        try {
            Intent intent = new Intent(this, activity);
            intent.putExtra("mode", mCallMode);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "startActivity Exception: " + e.toString());
        }
    }
}
