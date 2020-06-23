
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.hms.game.archive;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huawei.hms.R;


public class ConflictDialog extends Dialog {
    
    private TextView titleTv;
   
    private TextView recentMessageTv;

    private TextView serverMessageTv;
	
    private Button negativeBn, positiveBn;

    private String recentMessage;

    private String serverMessage;

    private String title;

    private String positive, negative;

    private String recentRadioText, serverRadioText;

    private RadioButton recentRadioButton;

    private RadioButton serverRadioButton;

    public static final int TYPE_ARCHIVE_RECENT = 1;
    public static final int TYPE_ARCHIVE_SERVER = 2;

    public ConflictDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_conflict);

        setCanceledOnTouchOutside(false);

        initView();

        refreshView();

        initEvent();
    }


    private void initEvent() {

        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    if (recentRadioButton.isChecked()) {
                        onClickBottomListener.onPositiveClick(1);
                    } else if (serverRadioButton.isChecked()) {
                        onClickBottomListener.onPositiveClick(2);
                    } else {
                        onClickBottomListener.onPositiveClick(0);
                    }

                }
            }
        });

        negativeBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegativeClick();
                }
            }
        });
    }


    private void refreshView() {

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(recentMessage)) {
            recentMessageTv.setText(recentMessage);
        }
        if (!TextUtils.isEmpty(serverMessage)) {
            serverMessageTv.setText(serverMessage);
        }

        if (!TextUtils.isEmpty(positive)) {
            positiveBn.setText(positive);
        } else {
            positiveBn.setText("OK");
        }
        if (!TextUtils.isEmpty(negative)) {
            negativeBn.setText(negative);
        } else {
            negativeBn.setText("Cancel");
        }


        if (!TextUtils.isEmpty(recentRadioText)) {
            recentRadioButton.setText(recentRadioText);
        } else {
            recentRadioButton.setText("recent");
        }
        if (!TextUtils.isEmpty(serverRadioText)) {
            serverRadioButton.setText(serverRadioText);
        } else {
            serverRadioButton.setText("server");
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }


    private void initView() {
        negativeBn = findViewById(R.id.negtive);
        positiveBn = findViewById(R.id.positive);
        titleTv = findViewById(R.id.title);
        recentMessageTv = findViewById(R.id.tv_archive_recent);
        serverMessageTv = findViewById(R.id.tv_archive_server);
        recentRadioButton = findViewById(R.id.rb_recent);
        serverRadioButton = findViewById(R.id.rb_server);
    }


    public OnClickBottomListener onClickBottomListener;

    public ConflictDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {

        public void onPositiveClick(int checkType);


        public void onNegativeClick();
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public ConflictDialog setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
        return this;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public ConflictDialog setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ConflictDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public ConflictDialog setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public ConflictDialog setNegative(String negative) {
        this.negative = negative;
        return this;
    }

    public String getRecentRadioText() {
        return recentRadioText;
    }

    public ConflictDialog setRecentRadioText(String recentRadioText) {
        this.recentRadioText = recentRadioText;
        return this;
    }

    public String getServerRadioText() {
        return serverRadioText;
    }

    public ConflictDialog setServerRadioText(String serverRadioText) {
        this.serverRadioText = serverRadioText;
        return this;
    }
}
