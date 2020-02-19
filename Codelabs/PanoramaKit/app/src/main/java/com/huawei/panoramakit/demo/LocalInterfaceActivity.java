/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.panoramakit.demo;

import com.huawei.hms.panorama.Panorama;
import com.huawei.hms.panorama.PanoramaInterface;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LocalInterfaceActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "LocalInterfaceActivity";

    private PanoramaInterface.PanoramaLocalInterface mLocalInterface;

    private ImageButton mImageButton;

    private boolean mChangeButtonCompass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_interface);
        Log.d(TAG, "onCreate");

        Intent intent = getIntent();
        Uri uri = intent.getData();
        int type = intent.getIntExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL);

        callLocalApi(uri, type);
    }

    private void callLocalApi(Uri uri, int type) {
        mLocalInterface = Panorama.getInstance().getLocalInstance(this);

        if (uri == null || mLocalInterface == null) {
            Log.e(TAG, "uri or local api is null");
            finish();
			return;
        }

        if (mLocalInterface.init() == 0 && mLocalInterface.setImage(uri, type) == 0) {
            // getview and add to layout
            View view = mLocalInterface.getView();
            RelativeLayout layout = findViewById(R.id.relativeLayout);
            layout.addView(view);

            // update MotionEvent to sdk
            view.setOnTouchListener(this);

            // change control mode
            mImageButton = findViewById(R.id.changeButton);
            mImageButton.bringToFront();
            mImageButton.setOnClickListener(this);
        } else {
            Log.e(TAG, "local api error");
            Toast.makeText(this, "Local init error!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.changeButton) {
            if (mChangeButtonCompass) {
                mImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_touch));
                mChangeButtonCompass = false;
                mLocalInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_TOUCH);
                // dynamic change image
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
                // mLocalInterface.setImage(uri, PanoramaApiExt.IMAGE_TYPE_SPHERICAL);
            } else {
                mImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_compass));
                mChangeButtonCompass = true;
                mLocalInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_POSE);
                // dynamic change image
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano2);
                // mLocalInterface.setImage(uri, PanoramaApiExt.IMAGE_TYPE_SPHERICAL);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mLocalInterface != null) {
            mLocalInterface.updateTouchEvent(event);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mLocalInterface != null) {
            mLocalInterface.deInit();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
