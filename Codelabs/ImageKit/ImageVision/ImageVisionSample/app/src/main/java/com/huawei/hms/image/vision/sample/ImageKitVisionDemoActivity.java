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

package com.huawei.hms.image.vision.sample;

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

import com.huawei.hms.image.vision.ImageVisionImpl;
import com.huawei.hms.image.vision.ImageVision;
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
 * @since 1.0.2.300
 */
public class ImageKitVisionDemoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "FilterActivity";
    String[] permissions = new String[] {Manifest.permission.READ_PHONE_STATE,
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
    private Context context;
    String string
        = "{\"projectId\":\"projectIdTest\",\"appId\":\"appIdTest\",\"authApiKey\":\"authApiKeyTest\",\"clientSecret\":\"clientSecretTest\",\"clientId\":\"clientIdTest\",\"token\":\"tokenTest\"}";
    private JSONObject authJson;

    {
        try {
            authJson = new JSONObject(string);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

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

    //权限判断和申请
    private void initPermission() {
        //清空没有通过的权限
        mPermissionList.clear();
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                != PackageManager.PERMISSION_GRANTED) {
                //添加还未授予的权限
                mPermissionList.add(permissions[i]);
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }
    }

    /**
     * @param requestCode 是我们自己定义的权限请求码
     * @param permissions 是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
        }
    }

    /**
     * 处理获取的图片
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
                    tv2.setText("服务未初始化，请初始化后调用服务");
                    break;
                }
                startFilter(filterType, intensity, compress, authJson);
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
     * 从相册获取图片
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
            tv2.setText("服务未开启");
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

    private void startFilter(final String filterType, final String intensity, final String compress,
        final JSONObject authJson) {
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
