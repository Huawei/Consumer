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

package com.huawei.hms.ads.exsplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * Action of ExSplash displayed.
     */
    private static final String ACTION_EXSPLASH_DISPLAYED = "com.huawei.hms.ads.EXSPLASH_DISPLAYED";

    private static final String SP_NAME = "ExSplashSharedPreferences";

    private static final String SP_PROTOCOL_KEY = "user_consent_status";

    private ExSplashServiceManager exSplashService;

    private Button showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ACTION_EXSPLASH_DISPLAYED);
        registerReceiver(new ExSplashBroadcastReceiver(), filter);

        exSplashService = new ExSplashServiceManager(this);
        showDialog = findViewById(R.id.show_dialog);
        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProtocolDialog();
            }
        });

        // Checking user consent status.
        checkUserConsent();
    }

    /**
     * You should show the user protocol dialog and receive user's selection results.
     */
    private void checkUserConsent() {
        SharedPreferences preferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int status = preferences.getInt(SP_PROTOCOL_KEY, -1);
        if (status == -1) { // First launch App
            showProtocolDialog();
        } else if (status == 0) { // The user does not consent agreement.
            exSplashService.enableUserInfo(false);
        } else { // The user consent agreement.
            exSplashService.enableUserInfo(true);
        }
    }

    /**
     * Display a protocol dialog.
     */
    private void showProtocolDialog() {
        ProtocolDialog dialog = new ProtocolDialog(this);
        dialog.setCallback(new ProtocolDialog.ProtocolDialogCallback() {

            @Override
            public void agree() {
                exSplashService.enableUserInfo(true);
                Toast.makeText(MainActivity.this, "Try restart app and check the exsplash ad.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                exSplashService.enableUserInfo(false);
                // Exit app.
                finish();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
