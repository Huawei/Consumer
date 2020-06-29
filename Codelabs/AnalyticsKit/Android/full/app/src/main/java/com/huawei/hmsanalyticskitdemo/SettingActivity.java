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

package com.huawei.hmsanalyticskitdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

// import classes from Analytics Kit
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;

public class SettingActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText editFavorSport;
    private String strFavorSport;

    //Define a var for Analytics Instance
    HiAnalyticsInstance instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Genarate Analytics Kit Instance
        instance = HiAnalytics.getInstance(this);

        btnSave = (Button) findViewById(R.id.save_setting_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFavorSport = (EditText) findViewById(R.id.edit_favoraite_sport);
                strFavorSport = editFavorSport.getText().toString().trim();
                // save favorite sport by user setUserProperty
                instance.setUserProfile("favor_sport", strFavorSport);
            }
        });
    }
}
