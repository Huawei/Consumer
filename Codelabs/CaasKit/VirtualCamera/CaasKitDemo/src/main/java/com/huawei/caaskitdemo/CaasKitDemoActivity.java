/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.caaskitdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huawei.caaskitdemo.caaskit.CaasKitHelper;

import java.io.File;

public class CaasKitDemoActivity extends AppCompatActivity {
    private static final String TAG = "CaaSKitDemoActivity";
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private final int REQUEST_MEDIA_DATA = 1;
    public CaasKitDemoView mVideoView;
    private RelativeLayout mRootView;
    private Button mVideoControl;
    private Button mVideoPick;
    private Button mCallShow;
    private Button mCallHide;
    private View mCallView;
    private File mVideoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_caaskitdemo);
        int permission = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            setButtonClickEvent();
        }
    }

    private void setButtonClickEvent() {
        Log.d(TAG, "setButtonClickEvent.");
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mVideoControl = findViewById(R.id.video_control);
        mVideoControl.setOnClickListener(v -> {
            if (mVideoView != null) {
                startPlaying();
            }
        });
        mVideoPick = findViewById(R.id.video_pick);
        mVideoPick.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_MEDIA_DATA);
        });
        mCallView = findViewById(R.id.call_view);
        mCallShow = findViewById(R.id.call_show);
        mCallHide = findViewById(R.id.call_hide);
        mCallShow.setOnClickListener(v ->{
            CaasKitHelper.getInstance().sendShow();
        });
        mCallHide.setOnClickListener(v ->{
            CaasKitHelper.getInstance().sendHide();
        });
    }

    private void startPlaying() {
        if (mVideoFilePath != null) {
            boolean isVideoPlaying = mVideoView.startPlaying(mVideoFilePath);
            if (isVideoPlaying) {
                mVideoPick.setVisibility(View.GONE);
                mVideoControl.setVisibility(View.GONE);
                mCallView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause.");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop.");
        if (mVideoView != null) {
            mVideoView.onPause();
        }
        CaasKitHelper.getInstance().releaseVirtualCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume.");
        if (mVideoView != null) {
            mVideoView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy.");
        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }
        CaasKitHelper.getInstance().caasKitRelease();
    }

    private void addVideoView() {
        if (mVideoView == null) {
            Log.d(TAG, "addVideoView.");
            mVideoView = new CaasKitDemoView(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            mRootView.addView(mVideoView, params);
            mVideoControl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == 0) {
                    setButtonClickEvent();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_DATA) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setVideoFilePath(data);
                addVideoView();
                CaasKitHelper.getInstance().caasKitInit();
            }
        }
    }

    private void setVideoFilePath(Intent intent) {
        Uri uri = intent.getData();
        Cursor cursor = null;
        try {
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            cursor = getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            if (cursor == null) {
                return;
            }
            cursor.moveToFirst();
            String filePath = cursor.getString(0);
            Toast.makeText(this, "FILE PATH: " + filePath, Toast.LENGTH_SHORT).show();
            mVideoFilePath = new File(filePath);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
