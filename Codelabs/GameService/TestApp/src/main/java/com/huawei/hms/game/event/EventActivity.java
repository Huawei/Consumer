
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

package com.huawei.hms.game.event;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.huawei.hms.R;
import com.huawei.hms.game.common.BaseActivity;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends BaseActivity {

    @BindView(R.id.et_load_some_event)
    public EditText etEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_load_event)
    public void loadEvent() {
        loadEvent(true, "");
    }

    @OnClick(R.id.btn_load_event_off)
    public void loadEventOff() {
        loadEvent(false, "");
    }

    private void loadEvent(boolean forceReload, String idsString) {
        if (getAuthHuaweiId() == null) {
            showLog("signIn first");
            return;
        }
        String jString = "";
        try {
            jString = getAuthHuaweiId().toJson();
        } catch (JSONException e) {
            showLog("signIn first");
        }
        Intent intent = new Intent(EventActivity.this, EventListActivity.class);
        intent.putExtra("forceReload", forceReload);
        intent.putExtra("mSign", jString);
        intent.putExtra("idsString", idsString);
        startActivity(intent);
    }

    @OnClick(R.id.btn_load_some_event)
    public void loadSomeEvent() {
        String idsString = etEventId.getText().toString().trim();
        if (TextUtils.isEmpty(idsString)) {
            showLog("event id can not be null");
            return;
        }
        loadEvent(true, idsString);
    }

    @OnClick(R.id.btn_load_some_event_off)
    public void loadSomeEventOff() {
        String idsString = etEventId.getText().toString().trim();
        if (TextUtils.isEmpty(idsString)) {
            showLog("event id can not be null");
            return;
        }
        loadEvent(false, idsString);
    }
}
