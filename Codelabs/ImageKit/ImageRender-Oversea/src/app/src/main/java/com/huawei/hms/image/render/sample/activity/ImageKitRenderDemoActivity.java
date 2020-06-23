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
package com.huawei.hms.image.render.sample.activity;

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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.huawei.hms.image.render.*;
import com.huawei.hms.image.render.sample.R;
import com.huawei.hms.image.render.sample.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
        setContentView(R.layout.activity_image_kit_demo);
        initView();
        int permissionCheck = ContextCompat.checkSelfPermission(ImageKitRenderDemoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
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
    }

    /**
     * Create default resources.
     * You can compile the manifest.xml file and image resource file. The code is for reference only.
     */
    private void initData() {
        // Absolute path of the resource files.
        String absolutelyPath = SOURCE_PATH;

        FileOutputStream fops = null;
        OutputStreamWriter writer = null;

        try {
            if(!Utils.createResourceDirs(absolutelyPath)){
                Log.e(TAG, "Create dirs fail, please check permission");
            }
            File xmlFile = new File(absolutelyPath + File.separator + "manifest.xml");
            if(!xmlFile.createNewFile()){
                Log.e(TAG, "Create file fail, please check permission");
            }
            fops = new FileOutputStream(xmlFile);
            try {
                writer = new OutputStreamWriter(fops, "utf-8");
                fops.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // Add the BOM format.
                writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                writer.write("<Root screenWidth=\"1080\">");
                writer.write("<Image src=\"default_background.jpg\" w=\"#screen_width\" h=\"#screen_height\"/>");
                writer.write(
                        "<Image  x=\"380\" y=\"390\" centerX=\"139\" centerY=\"151\"  src=\"ty.png\">");
                writer.write(
                        "<SizeAnimation>");
                writer.write("<Size w=\"114\" h=\"120\" time=\"0\"/>");
                writer.write("<Size w=\"278\" h=\"301\" time=\"600\"/>");
                writer.write("<Size w=\"114\" h=\"120\" time=\"1200\"/>");
                writer.write("<Size w=\"278\" h=\"301\" time=\"1800\"/>");
                writer.write("</SizeAnimation>");
                writer.write("</Image>");
                writer.write("</Root>");
                writer.flush();
                writer.close();
                fops.flush();
                fops.close();

                if(!Utils.copyAssetsFileToDirs(this, "default_background.jpg", absolutelyPath + File.separator + "default_background.jpg")){
                    Log.e(TAG, "Copy resource file fail, please check permission");
                }
                if(!Utils.copyAssetsFileToDirs(this, "ty.png", absolutelyPath + File.separator + "ty.png")){
                    Log.e(TAG, "Copy resource file fail, please check permission");
                }
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Throwable e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "InitData fail, please check source path. error == " + e.getMessage());
        } finally {
            if (fops != null) {
                try {
                    fops.close();
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
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
    private void useImageRender(){
        // Initialize the ImageRender object.
        if(imageRenderAPI == null){
            Log.e(TAG, "initRemote fail, please check kit version");
            return;
        }
        int initResult = imageRenderAPI.doInit(SOURCE_PATH, getAuthJson());
        Log.i(TAG, "DoInit result == " + initResult);
        if (initResult == 0) {
            /// Obtain the rendered view.
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
            } else if(renderView.getResultCode() == ResultCode.ERROR_REMOTE){
                Log.w(TAG, "GetRenderView fail, remote call failed, please check HMS service");
            } else if(renderView.getResultCode() == ResultCode.ERROR_DOINIT){
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
        if(requestCode == PERMISSION_REQUEST_CODE){
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
