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
 *
 */

package com.huawei.hmsawarenesssample.awareness;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.barrier.TimeBarrier;
import com.huawei.hms.kit.awareness.capture.HeadsetStatusResponse;
import com.huawei.hms.kit.awareness.capture.TimeCategoriesResponse;
import com.huawei.hms.kit.awareness.status.HeadsetStatus;
import com.huawei.hms.kit.awareness.status.TimeCategories;
import com.huawei.hmsawarenesssample.R;
import com.huawei.hmsawarenesssample.logger.LogView;

import java.util.HashMap;
import java.util.Map;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private LogView logView;
    private Map<Integer,String> mTimeInfoMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        logView = findViewById(R.id.logView);
        findViewById(R.id.capture_getTimeCategories).setOnClickListener(this);
        findViewById(R.id.capture_getHeadsetStatus).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        initTimeInfoMap();
    }

    private void getHeadsetStatus() {
        // Use the getHeadsetStatus API to get headset connection status.
        Awareness.getCaptureClient(this)
                .getHeadsetStatus()
                .addOnSuccessListener(
                        new OnSuccessListener<HeadsetStatusResponse>() {
                            @Override
                            public void onSuccess(HeadsetStatusResponse headsetStatusResponse) {
                                HeadsetStatus headsetStatus = headsetStatusResponse.getHeadsetStatus();
                                int status = headsetStatus.getStatus();
                                String stateStr = "Headsets are " +
                                        (status == HeadsetStatus.CONNECTED ? "connected" : "disconnected");
                                logView.printLog(stateStr);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "get Headsets Capture failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "get Headsets Capture failed", e);
                            }
                        });
    }

    private void getTimeCategories() {
        try {
            // Use getTimeCategories() to get the information about the current time of the user location.
            // Time information includes whether the current day is a workday or a holiday, and whether the current day is in the morning, afternoon, or evening, or at the night.
            Task<TimeCategoriesResponse> task = Awareness.getCaptureClient(this).getTimeCategories();
            task.addOnSuccessListener(new OnSuccessListener<TimeCategoriesResponse>() {
                @Override
                public void onSuccess(TimeCategoriesResponse timeCategoriesResponse) {
                    TimeCategories timeCategories = timeCategoriesResponse.getTimeCategories();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int timeCategoriesCode : mTimeInfoMap.keySet()) {
                        if (timeCategories.isTimeCategory(timeCategoriesCode)) {
                            stringBuilder.append(mTimeInfoMap.get(timeCategoriesCode));
                        }
                    }
                    logView.printLog(stringBuilder.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "get Time Categories failed",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "get Time Categories failed", e);
                }
            });
        }
        catch (Exception e) {
            logView.printLog("get Time Categories failed.Exception:" + e.getMessage());
        }
    }


    private void initTimeInfoMap() {
        final String weekday = "Today is weekday.";
        final String weekend = "Today is weekend.";
        final String holiday = "Today is holiday.";
        final String morning = "Good morning.";
        final String afternoon = "Good afternoon.";
        final String evening = "Good evening.";
        final String night = "Good night.";
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_WEEKDAY,weekday);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_WEEKEND,weekend);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_HOLIDAY,holiday);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_MORNING,morning);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_AFTERNOON,afternoon);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_EVENING,evening);
        mTimeInfoMap.put(TimeBarrier.TIME_CATEGORY_NIGHT,night);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_getTimeCategories:
                getTimeCategories();
                break;
            case R.id.capture_getHeadsetStatus:
                getHeadsetStatus();
                break;
            case R.id.clear_log:
                logView.setText("");
                break;
            default:
                break;
        }
    }
}
