/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.caaskitdemo.caaskit.CaasKitHelper;

public class CaasKitDemoActivity extends AppCompatActivity {
    private static final String TAG = "CaaSKitDemoActivity";

    private Button mCallShow;

    private Button mCallHide;

    private Button mSharePause;

    private Button mShareResume;

    private TextView mPasswordView;

    private ImageView mBackgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_caaskitdemo);
        CaasKitHelper.getInstance().caasKitInit();
        mCallShow = findViewById(R.id.call_show);
        mCallHide = findViewById(R.id.call_hide);
        mCallShow.setOnClickListener(v ->{
            CaasKitHelper.getInstance().sendShow();
        });
        mCallHide.setOnClickListener(v ->{
            CaasKitHelper.getInstance().sendHide();
        });
        mSharePause = findViewById(R.id.share_pause);
        mShareResume = findViewById(R.id.share_resume);
        mSharePause.setOnClickListener(v ->{
            CaasKitHelper.getInstance().pauseShare();
        });
        mShareResume.setOnClickListener(v ->{
            CaasKitHelper.getInstance().resumeShare();
        });
        mPasswordView = findViewById(R.id.paasword_view);
        mPasswordView.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange:" + hasFocus);
                if (hasFocus) {
                    CaasKitHelper.getInstance().pauseShare();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    CaasKitHelper.getInstance().resumeShare();
                }
            }
        });
        mBackgroundView = findViewById(R.id.back_ground);
        mBackgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View var1, MotionEvent var2) {
                if (mPasswordView != null) {
                    mPasswordView.clearFocus();
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause.");
        CaasKitHelper.getInstance().pauseShare();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume.");
        CaasKitHelper.getInstance().resumeShare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy.");
        CaasKitHelper.getInstance().caasKitRelease();
    }
}
