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
import com.huawei.hms.support.api.client.ResultCallback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mButtonSample1;

    private Button mButtonSample2;

    private Button mButtonSample3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        mButtonSample1 = findViewById(R.id.button1);
        mButtonSample1.setOnClickListener(this);

        mButtonSample2 = findViewById(R.id.button2);
        mButtonSample2.setOnClickListener(this);

        mButtonSample3 = findViewById(R.id.button3);
        mButtonSample3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // if read picture form sdcard,should requestPermission
        // requestPermission();
        switch (v.getId()) {
            case R.id.button1:
                PanoramaInterface_loadImageInfo();
                break;
            case R.id.button2:
                PanoramaInterface_loadImageInfoWithType();
                break;
            case R.id.button3:
                PanoramaInterface_localInterface();
            default:
                break;
        }
    }

    private class ResultCallbackImpl implements ResultCallback<PanoramaInterface.ImageInfoResult> {

        @Override
        public void onResult(PanoramaInterface.ImageInfoResult result) {
            if (result == null) {
                Log.e(TAG, "ImageInfoResult is null");
                return;
            }
            // if result is ok,start Activity
            if (result.getStatus().isSuccess()) {
                Intent intent = result.getImageDisplayIntent();
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Log.e(TAG, "unknown error, intent is null");
                }
            } else {
                Log.e(TAG, "error status : " + result.getStatus());
            }
        }
    }

    // picture size <= 20MB, resolution <= 16384x8192
    // sample 1： loadImageInfo without panorama type,and will check if the picture have the XMP information about GPano
    private void PanoramaInterface_loadImageInfo() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
        Panorama.getInstance().loadImageInfoWithPermission(this, uri).setResultCallback(new ResultCallbackImpl());
    }

    // sample 2： loadImageInfo with panorama type
    private void PanoramaInterface_loadImageInfoWithType() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
        Panorama.getInstance()
            .loadImageInfoWithPermission(this, uri, PanoramaInterface.IMAGE_TYPE_SPHERICAL)
            .setResultCallback(new ResultCallbackImpl());
    }

    // sample 3：activity with local interface
    private void PanoramaInterface_localInterface() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
        Intent intent = new Intent(MainActivity.this, LocalInterfaceActivity.class);
        intent.setData(uri);
        intent.putExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL);

        startActivity(intent);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Log.i(TAG, "permission ok");
        }
    }
}
