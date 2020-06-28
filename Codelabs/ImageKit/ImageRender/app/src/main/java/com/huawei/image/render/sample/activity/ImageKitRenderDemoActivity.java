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
package com.huawei.image.render.sample.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.huawei.hms.image.render.ImageRender;
import com.huawei.hms.image.render.ImageRenderImpl;
import com.huawei.hms.image.render.RenderView;
import com.huawei.hms.image.render.ResultCode;
import com.huawei.image.render.sample.R;
import com.huawei.image.render.sample.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * The ImageRenderSample code provides examples of initializing the service, obtaining views, playing animation, pausing animation, and destroying resources.
 *
 * @author huawei
 * @since 5.0.0
 */
public class ImageKitRenderDemoActivity extends Activity {

    /**
     * TAG
     */
    public static final String TAG = "ImageKitRenderDemo";

    /**
     * Layout container
     */
    private FrameLayout contentView;

    // imageRender object
    private ImageRenderImpl imageRenderAPI;

    // Resource folder, which can be set as you want.
    private static final String SOURCE_PATH = Environment.getExternalStorageDirectory() + File.separator + "lockscreen" + File.separator + "fastDir";

    /**
     * requestCode for applying for permissions.
     */
    public static final int PERMISSION_REQUEST_CODE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getActionBar().hide();
        setContentView(R.layout.activity_image_kit_demo);
        initView();
        int permissionCheck = ContextCompat.checkSelfPermission(ImageKitRenderDemoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            initData();
            initImageRender();
        } else {
            ActivityCompat.requestPermissions(ImageKitRenderDemoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Initialize the view.
     */
    private void initView() {
        contentView = findViewById(R.id.content);
        final Spinner spinner = findViewById(R.id.spinner_animations);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAnimation(spinner.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * change the animation which is choose in spinner
     * @param animationName animationName
     */
    private void changeAnimation(String animationName) {
        if(!Utils.copyAssetsFilesToDirs(this, animationName, SOURCE_PATH)){
            Log.e(TAG, "copy files failure, please check permissions");
            return;
        }
        if (imageRenderAPI == null) {
            Log.e(TAG, "initRemote fail, please check kit version");
            return;
        }
        if(contentView.getChildCount() > 0) {
            contentView.removeAllViews();
            imageRenderAPI.removeRenderView();
            int initResult = imageRenderAPI.doInit(SOURCE_PATH, getAuthJson());
            Log.i(TAG, "DoInit result == " + initResult);
            if (initResult == 0) {
                // Obtain the rendered view.
                RenderView renderView = imageRenderAPI.getRenderView();
                if (renderView.getResultCode() == ResultCode.SUCCEED) {
                    View view = renderView.getView();
                    if (null != view) {
                        // Add the rendered view to the layout.
                        contentView.addView(view);
                    } else {
                        Log.w(TAG, "GetRenderView fail, view is null");
                    }
                } else if (renderView.getResultCode() == ResultCode.ERROR_GET_RENDER_VIEW_FAILURE) {
                    Log.w(TAG, "GetRenderView fail");
                } else if (renderView.getResultCode() == ResultCode.ERROR_XSD_CHECK_FAILURE) {
                    Log.w(TAG, "GetRenderView fail, resource file parameter error, please check resource file.");
                } else if (renderView.getResultCode() == ResultCode.ERROR_VIEW_PARSE_FAILURE) {
                    Log.w(TAG, "GetRenderView fail, resource file parsing failed, please check resource file.");
                } else if (renderView.getResultCode() == ResultCode.ERROR_REMOTE) {
                    Log.w(TAG, "GetRenderView fail, remote call failed, please check HMS service");
                } else if (renderView.getResultCode() == ResultCode.ERROR_DOINIT) {
                    Log.w(TAG, "GetRenderView fail, init failed, please init again");
                }
            } else {
                Log.w(TAG, "Do init fail, errorCode == " + initResult);
            }
        }
    }

    /**
     * Create default resources.
     * You can compile the manifest.xml file and image resource file. The code is for reference only.
     */
    private void initData() {
        // Absolute path of the resource files.

        if (!Utils.createResourceDirs(SOURCE_PATH)) {
            Log.e(TAG, "Create dirs fail, please check permission");
        }

        if (!Utils.copyAssetsFileToDirs(this, "AlphaAnimation" + File.separator + "aixin7.png", SOURCE_PATH + File.separator + "aixin7.png")) {
            Log.e(TAG, "Copy resource file fail, please check permission");
        }
        if (!Utils.copyAssetsFileToDirs(this, "AlphaAnimation" + File.separator + "bj.jpg", SOURCE_PATH + File.separator + "bj.jpg")) {
            Log.e(TAG, "Copy resource file fail, please check permission");
        }
        if (!Utils.copyAssetsFileToDirs(this, "AlphaAnimation" + File.separator + "manifest.xml", SOURCE_PATH + File.separator + "manifest.xml")) {
            Log.e(TAG, "Copy resource file fail, please check permission");
        }
    }

    /**
     * Use the ImageRender API.
     */
    private void initImageRender() {
        // Obtain an ImageRender object.
        ImageRender.getInstance(this, new ImageRender.RenderCallBack() {
            @Override
            public void onSuccess(ImageRenderImpl imageRender) {
                Log.i(TAG, "getImageRenderAPI success");
                imageRenderAPI = imageRender;
                useImageRender();
            }

            @Override
            public void onFailure(int i) {
                Log.e(TAG, "getImageRenderAPI failure, errorCode = " + i);
            }
        });
    }

    /**
     * The Image Render service is required.
     */
    private void useImageRender() {
        // Initialize the ImageRender object.
        if (imageRenderAPI == null) {
            Log.e(TAG, "initRemote fail, please check kit version");
            return;
        }
        int initResult = imageRenderAPI.doInit(SOURCE_PATH, getAuthJson());
        Log.i(TAG, "DoInit result == " + initResult);
        if (initResult == 0) {
            // Obtain the rendered view.
            RenderView renderView = imageRenderAPI.getRenderView();
            if (renderView.getResultCode() == ResultCode.SUCCEED) {
                View view = renderView.getView();
                if (null != view) {
                    // Add the rendered view to the layout.
                    contentView.addView(view);
                } else {
                    Log.w(TAG, "GetRenderView fail, view is null");
                }
            } else if (renderView.getResultCode() == ResultCode.ERROR_GET_RENDER_VIEW_FAILURE) {
                Log.w(TAG, "GetRenderView fail");
            } else if (renderView.getResultCode() == ResultCode.ERROR_XSD_CHECK_FAILURE) {
                Log.w(TAG, "GetRenderView fail, resource file parameter error, please check resource file.");
            } else if (renderView.getResultCode() == ResultCode.ERROR_VIEW_PARSE_FAILURE) {
                Log.w(TAG, "GetRenderView fail, resource file parsing failed, please check resource file.");
            } else if (renderView.getResultCode() == ResultCode.ERROR_REMOTE) {
                Log.w(TAG, "GetRenderView fail, remote call failed, please check HMS service");
            } else if (renderView.getResultCode() == ResultCode.ERROR_DOINIT) {
                Log.w(TAG, "GetRenderView fail, init failed, please init again");
            }
        } else {
            Log.w(TAG, "Do init fail, errorCode == " + initResult);
        }
    }


    @Override
    protected void onDestroy() {
        // Destroy the view.
        if (null != imageRenderAPI) {
            imageRenderAPI.removeRenderView();
        }
        super.onDestroy();
    }

    /**
     * Add authentication parameters.
     *
     * @return JsonObject of Authentication parameters.
     */
    private JSONObject getAuthJson() {
        JSONObject authJson = new JSONObject();
        try {
            authJson.put("projectId", "projectId-test");
            authJson.put("appId", "appId-test");
            authJson.put("authApiKey", "authApiKey-test");
            authJson.put("clientSecret", "clientSecret-test");
            authJson.put("clientId", "clientId-test");
            authJson.put("token", "token-test");
        } catch (JSONException e) {
            Log.w(TAG, "Get authJson fail, please check auth info");
        }
        return authJson;
    }

    /**
     * Play the animation.
     *
     * @param view button
     */
    public void startAnimation(View view) {
        // Play the rendered view.
        Log.i(TAG, "Start animation");
        if (null != imageRenderAPI) {
            int playResult = imageRenderAPI.playAnimation();
            if (playResult == ResultCode.SUCCEED) {
                Log.i(TAG, "Start animation success");
            } else {
                Log.i(TAG, "Start animation failure");
            }
        } else {
            Log.w(TAG, "Start animation fail, please init first.");
        }
    }

    /**
     * Stop the animation.
     *
     * @param view button
     */
    public void stopAnimation(View view) {
        // Stop the renderView animation.
        Log.i(TAG, "Stop animation");
        if (null != imageRenderAPI) {
            int playResult = imageRenderAPI.stopAnimation();
            if (playResult == ResultCode.SUCCEED) {
                Log.i(TAG, "Stop animation success");
            } else {
                Log.i(TAG, "Stop animation failure");
            }
        } else {
            Log.w(TAG, "Stop animation fail, please init first.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The permission is granted.
                initData();
                initImageRender();
            } else {
                // The permission is rejected.
                Log.w(TAG, "permission denied");
                Toast.makeText(ImageKitRenderDemoActivity.this, "Please grant the app the permission to read the SD card", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
