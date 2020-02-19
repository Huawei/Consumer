/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.huawei.hms.safetydetect.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    // UI Object
    private TextView txt_topbar;
    private TextView txt_sysintegrity;

    // Fragment Object
    private Fragment fg;
    private FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fManager = getFragmentManager();
        bindViews();
        txt_sysintegrity.performClick();
    }

    private void bindViews() {
        txt_topbar = findViewById(R.id.txt_topbar);
        txt_sysintegrity = findViewById(R.id.txt_sysintegrity);

        txt_sysintegrity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        android.app.FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        txt_topbar.setText(R.string.title_activity_sys_integrity);
        if (fg == null) {
            fg = new SafetyDetectSysIntegrityAPIFragment();
            fTransaction.add(R.id.ly_content, fg);
        } else {
            fTransaction.show(fg);
        }

        fTransaction.commit();
    }

    private void hideAllFragment(android.app.FragmentTransaction fragmentTransaction) {
        if (fg != null) {
            fragmentTransaction.hide(fg);
        }
    }
}
