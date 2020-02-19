/**
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.huawei.www.driveapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.cloud.base.auth.DriveCredential;
import com.huawei.cloud.base.http.FileContent;
import com.huawei.cloud.base.media.MediaHttpDownloader;
import com.huawei.cloud.base.util.StringUtils;
import com.huawei.cloud.client.exception.DriveCode;
import com.huawei.cloud.services.drive.Drive;
import com.huawei.cloud.services.drive.DriveScopes;
import com.huawei.cloud.services.drive.model.File;
import com.huawei.cloud.services.drive.model.FileList;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import static com.huawei.hms.support.hwid.request.HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";

    private static int REQUEST_SIGN_IN_LOGIN = 1002;

    private DriveCredential mCredential;

    private String accessToken;

    private String unionId;

    private File directoryCreated;
    private File fileSearched;
    private EditText uploadFileName;
    private EditText searchFileName;
    private TextView queryResult;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<String, String>();

    static {
        MIME_TYPE_MAP.put(".doc", "application/msword");
        MIME_TYPE_MAP.put(".jpg", "image/jpeg");
        MIME_TYPE_MAP.put(".mp3", "audio/x-mpeg");
        MIME_TYPE_MAP.put(".mp4", "video/mp4");
        MIME_TYPE_MAP.put(".pdf", "application/pdf");
        MIME_TYPE_MAP.put(".png", "image/png");
        MIME_TYPE_MAP.put(".txt", "text/plain");
    }

    private DriveCredential.AccessMethod refreshAT = new DriveCredential.AccessMethod() {
        @Override
        public String refreshToken() {
            return accessToken;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("DriveApplication");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS_STORAGE, 1);
        }

        uploadFileName = findViewById(R.id.uploadFileName);
        searchFileName = findViewById(R.id.searchFileName);
        queryResult = findViewById(R.id.queryResult);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.buttonUploadFiles).setOnClickListener(this);
        findViewById(R.id.buttonQueryFiles).setOnClickListener(this);
        findViewById(R.id.buttonDownloadFiles).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == REQUEST_SIGN_IN_LOGIN) {
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                accessToken = huaweiAccount.getAccessToken();
                unionId = huaweiAccount.getUnionId();
                int returnCode = init(unionId, accessToken, refreshAT);
                if (DriveCode.SUCCESS == returnCode) {
                    showTips("login ok");
                } else if (DriveCode.SERVICE_URL_NOT_ENABLED == returnCode) {
                    showTips("drive is not enabled");
                } else {
                    showTips("login error");
                }
            } else {
                Log.d(TAG, "onActivityResult, signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
                Toast.makeText(getApplicationContext(), "onActivityResult, signIn failed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showTips(final String toastText) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                TextView textView = findViewById(R.id.textView);
                textView.setText(toastText);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                driveLogin();
                break;
            case R.id.buttonUploadFiles:
                uploadFiles();
                break;
            case R.id.buttonQueryFiles:
                queryFiles();
                break;
            case R.id.buttonDownloadFiles:
                downloadFiles();
                break;
            default:
                break;
        }
    }

    private void driveLogin() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        List<Scope> scopeList = new ArrayList<>();
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE));
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE_READONLY));
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE_FILE));
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE_METADATA));
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE_METADATA_READONLY));
        scopeList.add(new Scope(DriveScopes.SCOPE_DRIVE_APPDATA));
        scopeList.add(HuaweiIdAuthAPIManager.HUAWEIID_BASE_SCOPE);

        HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(DEFAULT_AUTH_REQUEST_PARAM)
                .setAccessToken()
                .setIdToken()
                .setScopeList(scopeList)
                .createParams();
        HuaweiIdAuthService client = HuaweiIdAuthManager.getService(this, authParams);
        startActivityForResult(client.getSignInIntent(), REQUEST_SIGN_IN_LOGIN);
    }

    private void uploadFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (accessToken == null) {
                        showTips("please click 'Login'.");
                        return;
                    }
                    if (StringUtils.isNullOrEmpty(uploadFileName.getText().toString())) {
                        showTips("please input upload file name above.");
                        return;
                    }
                    java.io.File fileObject = new java.io.File("/sdcard/" + uploadFileName.getText());
                    if (!fileObject.exists()) {
                        showTips("the input file does not exit.");
                        return;
                    }
                    Drive drive = buildDrive();
                    Map<String, String> appProperties = new HashMap<>();
                    appProperties.put("appProperties", "property");
                    // create somepath directory
                    File file = new File();
                    file.setFileName("somepath" + System.currentTimeMillis())
                            .setMimeType("application/vnd.huawei-apps.folder")
                            .setAppSettings(appProperties);
                    directoryCreated = drive.files().create(file).execute();
                    // create test.jpg on cloud
                    File content = new File()
                            .setFileName(fileObject.getName())
                            .setMimeType(mimeType(fileObject))
                            .setParentFolder(Collections.singletonList(directoryCreated.getId()));
                    drive.files()
                            .create(content, new FileContent("image/jpeg", fileObject))
                            .setFields("*")
                            .execute();
                    showTips("upload success");
                } catch (Exception ex) {
                    Log.d(TAG, "upload", ex);
                    showTips("upload error " + ex.toString());
                }
            }
        }).start();
    }

    private void queryFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (accessToken == null) {
                        showTips("please click 'Login'.");
                        return;
                    }
                    if (StringUtils.isNullOrEmpty(searchFileName.getText().toString())) {
                        showTips("please input file name above.");
                        return;
                    }
                    String queryFile = "fileName = '" + searchFileName.getText() + "' and mimeType != 'application/vnd.huawei-apps.folder'";
                    Drive drive = buildDrive();
                    Drive.Files.List request = drive.files().list();
                    FileList files;
                    while (true) {
                        files = request
                                .setQueryParam(queryFile)
                                .setPageSize(10)
                                .setOrderBy("fileName")
                                .setFields("category,nextCursor,files/id,files/fileName,files/size")
                                .execute();
                        if (files == null || files.getFiles().size() > 0) {
                            break;
                        }
                        if (!StringUtils.isNullOrEmpty(files.getNextCursor())) {
                            request.setCursor(files.getNextCursor());
                        } else {
                            break;
                        }
                    }
                    String text = "";
                    if (files != null && files.getFiles().size() > 0) {
                        fileSearched = files.getFiles().get(0);
                        text = fileSearched.toString();
                    } else {
                        text = "empty";
                    }
                    final String finalText = text;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryResult.setText(finalText);
                        }
                    });
                    showTips("query ok");
                } catch (Exception ex) {
                    Log.d(TAG, "query", ex);
                    showTips("query error " + ex.toString());
                }
            }
        }).start();
    }

    private void downloadFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (accessToken == null) {
                        showTips("please click 'Login'.");
                        return;
                    }
                    if (fileSearched == null) {
                        showTips("please click 'QUERY FILE'.");
                        return;
                    }
                    Drive drive = buildDrive();
                    File content = new File();
                    Drive.Files.Get request = drive.files().get(fileSearched.getId());
                    content.setFileName(fileSearched.getFileName()).setId(fileSearched.getId());
                    MediaHttpDownloader downloader = request.getMediaHttpDownloader();
                    downloader.setContentRange(0, fileSearched.getSize() - 1);
                    String filePath = "/storage/emulated/0/Huawei/Drive/DownLoad/Demo_" + fileSearched.getFileName();
                    request.executeContentAndDownloadTo(new FileOutputStream(new java.io.File(filePath)));
                    showTips("download to " + filePath);
                } catch (Exception ex) {
                    Log.d(TAG, "download", ex);
                    showTips("download error " + ex.toString());
                }
            }
        }).start();
    }

    private Drive buildDrive() {
        Drive service = new Drive.Builder(mCredential, this).build();
        return service;
    }

    private String mimeType(java.io.File file) {
        if (file != null && file.exists() && file.getName().contains(".")) {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            if (MIME_TYPE_MAP.keySet().contains(suffix)) {
                return MIME_TYPE_MAP.get(suffix);
            }
        }
        return "*/*";
    }

    public int init(String unionID, String at, DriveCredential.AccessMethod refreshAT) {
        if (StringUtils.isNullOrEmpty(unionID) || StringUtils.isNullOrEmpty(at)) {
            return DriveCode.ERROR;
        }
        DriveCredential.Builder builder = new DriveCredential.Builder(unionID, refreshAT);
        mCredential = builder.build().setAccessToken(at);
        return DriveCode.SUCCESS;
    }

}
