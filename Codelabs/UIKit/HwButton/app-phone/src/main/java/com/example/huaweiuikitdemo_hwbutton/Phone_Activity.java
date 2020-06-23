/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.example.huaweiuikitdemo_hwbutton;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.huawei.uikit.hwbutton.widget.HwButton;


public class Phone_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        LinearLayout parent = findViewById(R.id.parent_phone);
        HwButton hwButton = HwButton.instantiate(this);
        if (hwButton != null) {
            hwButton.setText("Code instantiate HwButton");
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT);
            parent.addView(hwButton, params);
        }
    }
}
