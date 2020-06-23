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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.hms.support.api.entity.safetydetect.MaliciousAppsData;
import com.huawei.hms.support.api.safetydetect.AppsCheckConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple BaseAdapter which is used to list malicious apps data through the ListView widget.
 */
public class MaliciousAppsDataListAdapter extends BaseAdapter {
    private final List<MaliciousAppsData> maliciousAppsData = new ArrayList<>();
    private final Context context;

    public MaliciousAppsDataListAdapter(List<MaliciousAppsData> data, Context context) {
        maliciousAppsData.addAll(data);
        this.context = context;
    }

    @Override
    public int getCount() {
        return maliciousAppsData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_app, parent, false);
        TextView txtAppPackageName = convertView.findViewById(R.id.txt_aName);
        TextView txtAppCategory = convertView.findViewById(R.id.txt_aCategory);
        final MaliciousAppsData oneMaliciousAppsData = this.maliciousAppsData.get(position);
        txtAppPackageName.setText(oneMaliciousAppsData.getApkPackageName());
        txtAppCategory.setText(getCategory(oneMaliciousAppsData.getApkCategory()));
        return convertView;
    }

    private String getCategory(int apkCategory) {
        if (apkCategory == AppsCheckConstants.VIRUS_LEVEL_RISK) {
            return context.getString(R.string.app_type_risk);
        } else if (apkCategory == AppsCheckConstants.VIRUS_LEVEL_VIRUS) {
            return context.getString(R.string.app_type_virus);
        }
        return context.getString(R.string.app_type_unknown);
    }
}
