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

package com.huawei.pps.hms.test.installreferrer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huawei.pps.hms.test.BaseActivity;
import com.huawei.pps.hms.test.Constants;
import com.huawei.pps.hms.test.R;

import org.json.JSONException;
import org.json.JSONObject;

public class InstallReferrerWriteActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "InstallReferrerWrite";
    private EditText mPackageNameEt;
    private EditText mInstallReferrerEt;
    private Button mDeleteBtn;
    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_referrer_write);
        init();
    }

    protected void init() {
        super.init();
        // Create the "package_name" EditText. User can write service package name.
        mPackageNameEt = findViewById(R.id.package_name_et);
        // Create the "install_referrer" EditText. User can write install referrer info.
        mInstallReferrerEt = findViewById(R.id.install_referrer_et);
        // Create the "delete" button, which tries to delete existed install referrer according package name.
        mDeleteBtn = findViewById(R.id.delete_btn);
        mDeleteBtn.setOnClickListener(this);
        // Create the "save" button, which tries to save what just typed.
        mSaveBtn = findViewById(R.id.save_btn);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_btn) {
            deleteInstallReferrer();
        } else if (v.getId() == R.id.save_btn) {
            saveInstallReferrer();
        }
    }

    private void saveInstallReferrer() {
        if (isInvalid(mPackageNameEt)) {
            Log.e(TAG, "invalid package name");
            Toast.makeText(this, R.string.invalid_package_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isInvalid(mInstallReferrerEt)) {
            Log.e(TAG, "invalid install referrer");
            Toast.makeText(this, R.string.invalid_install_referrer, Toast.LENGTH_SHORT).show();
            return;
        }
        String pkgNamme = mPackageNameEt.getText().toString();
        String installReferrer = mInstallReferrerEt.getText().toString();
        saveOrDelete(pkgNamme, installReferrer, false);
    }

    private void deleteInstallReferrer() {
        if (isInvalid(mPackageNameEt)) {
            Log.e(TAG, "invalid package name");
            Toast.makeText(this, R.string.invalid_package_name, Toast.LENGTH_SHORT).show();
            return;
        }
        String pkgNamme = mPackageNameEt.getText().toString();
        saveOrDelete(pkgNamme, "", true);
    }

    private boolean isInvalid(EditText editText) {
        if (null == editText.getText() || TextUtils.isEmpty(editText.getText().toString())) {
            return true;
        }
        return false;
    }

    private void saveOrDelete(String pkgName, String installReferrer, boolean isDelete) {
        Log.i(TAG, "saveOrDelete isDelete=" + isDelete);
        SharedPreferences sp = getSharedPreferences(Constants.INSTALL_REFERRER_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            if (isDelete) {
                // Delete existed install referrer according package name
                editor.remove(pkgName);
                editor.commit();
                Toast.makeText(this, R.string.delete_install_referrer_success, Toast.LENGTH_SHORT).show();
            } else {
                // Save the typed install referrer.
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("channelInfo", installReferrer);
                    jsonObject.put("clickTimestamp", System.currentTimeMillis() - 123456L);
                    jsonObject.put("installTimestamp", System.currentTimeMillis());
                    editor.putString(pkgName, jsonObject.toString());
                    editor.commit();
                    Toast.makeText(this, R.string.save_install_referrer_success, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG, "saveOrDelete JSONException");
                    Toast.makeText(this, R.string.save_install_referrer_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


