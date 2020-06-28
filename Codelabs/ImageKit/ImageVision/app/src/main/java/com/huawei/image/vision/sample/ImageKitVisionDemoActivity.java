/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.image.vision.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hms.image.vision.*;
import com.huawei.hms.image.vision.bean.ImageVisionResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Filter activity.
 *
 * @author huawei
 * @since 1.0.2.301
 */
public class ImageKitVisionDemoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "FilterActivity";
    String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int mRequestCode = 100;
    List<String> mPermissionList = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Button btn_submit;
    private Button btn_init;
    private Button btn_picture;
    private Button btn_stop;
    private EditText btn_filter;
    private EditText btn_compress;
    private EditText btn_intensity;
    private ImageView iv;
    private TextView tv;
    private TextView tv2;
    String string = "{\"projectId\":\"projectIdTest\",\"appId\":\"appIdTest\",\"authApiKey\":\"authApiKeyTest\",\"clientSecret\":\"clientSecretTest\",\"clientId\":\"clientIdTest\",\"token\":\"tokenTest\"}";
    private JSONObject authJson;

    {
        try {
            authJson = new JSONObject(string);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
    private Context context;
    private Bitmap bitmap;
    private int initCodeState = -2;
    private int stopCodeState = -2;

    /**
     * The Image vision api.
     */
    ImageVisionImpl imageVisionAPI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        }
        iv = findViewById(R.id.iv);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);

        btn_filter = findViewById(R.id.btn_filter);
        btn_init = findViewById(R.id.btn_init);
        btn_picture = findViewById(R.id.btn_picture);
        btn_intensity = findViewById(R.id.btn_intensity);
        btn_compress = findViewById(R.id.btn_compress);
        btn_submit = findViewById(R.id.btn_submit);
        btn_stop = findViewById(R.id.btn_stop);

        btn_submit.setOnClickListener(this);
        btn_init.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_picture.setOnClickListener(this);

    }

   //Verify and apply for permissions.
    private void initPermission() {
       // Clear the permissions that fail the verification.
        mPermissionList.clear();
       //Check whether the required permissions are granted.
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
               // Add permissions that have not been granted.
                mPermissionList.add(permissions[i]);
            }
        }
        //Apply for permissions.
        if (mPermissionList.size() > 0) {//The permission has not been granted. Please apply for the permission.
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //The permission has been granted and you can continue with subsequent operations.
        }
    }

    /**
     * @param requestCode Permission request code.
     * @param permissions  An array of requested permission names.
     * @param grantResults An array of granted permission names. The length is equal to length of the corresponding permission names. Value 0 indicates that the permission is granted, and value -1 indicates that the permission is disabled.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//Certain permissions have not been granted.
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
        }
    }

    /**
     * Process the obtained image.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (null != data) {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 801:
                        try {
                            Uri uri = data.getData();
                            iv.setImageURI(uri);
                            bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                            break;
                        } catch (Exception e) {
                            Log.e(TAG, "Exception: " + e.getMessage());
                        }

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String filterType = btn_filter.getText().toString();
                String intensity = btn_intensity.getText().toString();
                String compress = btn_compress.getText().toString();
                if (initCodeState != 0 | stopCodeState == 0) {
                    tv2.setText("The service has not been initialized. Please initialize the service before calling it.");
                    break;
                }
                startFilter(filterType, intensity, compress,authJson);
                break;
            case R.id.btn_init:
                initFilter(context);
                break;
            case R.id.btn_picture:
                getByAlbum(this);
                break;
            case R.id.btn_stop:
                stopFilter();
                break;
        }

    }

    /**
     * Obtain images from the album.
     */
    public static void getByAlbum(Activity act) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpeg", "image/png", "image/webp", "image/gif"};
        getAlbum.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        getAlbum.setType("image/*");
        getAlbum.addCategory(Intent.CATEGORY_OPENABLE);
        act.startActivityForResult(getAlbum, 801);
    }

    private void stopFilter() {
        if (null != imageVisionAPI) {
            int stopCode = imageVisionAPI.stop();
            tv2.setText("stopCode:" + stopCode);
            iv.setImageBitmap(null);
            bitmap = null;
            stopCodeState = stopCode;
        } else {
            tv2.setText("The service has not been enabled.");
            stopCodeState = 0;
        }
    }

    private Context mContext;
    private void initFilter(final Context context) {
        imageVisionAPI = ImageVision.getInstance(this);
        imageVisionAPI.setVisionCallBack(new ImageVision.VisionCallBack() {
            @Override
            public void onSuccess(int successCode) {
                int initCode = imageVisionAPI.init(context, authJson);
                tv2.setText("initCode:" + initCode);
                long start2 = System.currentTimeMillis();
                initCodeState = initCode;
                stopCodeState = -2;
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "getImageVisionAPI failure, errorCode = " + errorCode);
                tv2.setText("initFailed");
            }
        });

    }

    private void startFilter(final String filterType, final String intensity,
                             final String compress, final JSONObject authJson) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long start3 = System.currentTimeMillis();
                JSONObject jsonObject = new JSONObject();
                JSONObject taskJson = new JSONObject();
                try {
                    taskJson.put("intensity", intensity);
                    taskJson.put("filterType", filterType);
                    taskJson.put("compressRate", compress);
                    jsonObject.put("requestId", "1");
                    jsonObject.put("taskJson", taskJson);
                    jsonObject.put("authJson", authJson);
                    long start4 = System.currentTimeMillis();
                    Log.e(TAG, "prepare request parameters cost" + (start4 - start3));
                    final ImageVisionResult visionResult = imageVisionAPI.getColorFilter(jsonObject,
                            bitmap);
                    long start5 = System.currentTimeMillis();
                    Log.e(TAG, "interface response cost" + (start5 - start4));
                    iv.post(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap image = visionResult.getImage();
                            iv.setImageBitmap(image);
                            tv.setText(
                                    visionResult.getResponse().toString() + "resultCode:" + visionResult
                                            .getResultCode());
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                }
            }
        };
        executorService.execute(runnable);
    }
}
