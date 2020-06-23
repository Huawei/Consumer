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

package com.huawei.hms.awareness.codelab.awareness;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.awareness.codelab.Constant;
import com.huawei.hms.awareness.codelab.R;
import com.huawei.hms.awareness.codelab.logger.LogView;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.capture.AmbientLightResponse;
import com.huawei.hms.kit.awareness.capture.BeaconStatusResponse;
import com.huawei.hms.kit.awareness.capture.BehaviorResponse;
import com.huawei.hms.kit.awareness.capture.BluetoothStatusResponse;
import com.huawei.hms.kit.awareness.capture.HeadsetStatusResponse;
import com.huawei.hms.kit.awareness.capture.LocationResponse;
import com.huawei.hms.kit.awareness.capture.TimeCategoriesResponse;
import com.huawei.hms.kit.awareness.capture.WeatherStatusResponse;
import com.huawei.hms.kit.awareness.status.AmbientLightStatus;
import com.huawei.hms.kit.awareness.status.BeaconStatus;
import com.huawei.hms.kit.awareness.status.BehaviorStatus;
import com.huawei.hms.kit.awareness.status.BluetoothStatus;
import com.huawei.hms.kit.awareness.status.DetectedBehavior;
import com.huawei.hms.kit.awareness.status.HeadsetStatus;
import com.huawei.hms.kit.awareness.status.TimeCategories;
import com.huawei.hms.kit.awareness.status.WeatherStatus;
import com.huawei.hms.kit.awareness.status.weather.Situation;
import com.huawei.hms.kit.awareness.status.weather.WeatherSituation;

import java.util.Arrays;
import java.util.List;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private ScrollView mScrollView;
    private LogView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.root_scrollView);
        findViewById(R.id.capture_getTimeCategories).setOnClickListener(this);
        findViewById(R.id.capture_getHeadsetStatus).setOnClickListener(this);
        findViewById(R.id.capture_getLocation).setOnClickListener(this);
        findViewById(R.id.capture_getBehaviorStatus).setOnClickListener(this);
        findViewById(R.id.capture_getLightIntensity).setOnClickListener(this);
        findViewById(R.id.capture_getWeatherStatus).setOnClickListener(this);
        findViewById(R.id.capture_getBluetoothStatus).setOnClickListener(this);
        findViewById(R.id.capture_getBeaconStatus).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
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
            case R.id.capture_getLocation:
                getLocation();
                break;
            case R.id.capture_getBehaviorStatus:
                getBehaviorStatus();
                break;
            case R.id.capture_getLightIntensity:
                getLightIntensity();
                break;
            case R.id.capture_getWeatherStatus:
                getWeatherStatus();
                break;
            case R.id.capture_getBluetoothStatus:
                getBluetoothStatus();
                break;
            case R.id.capture_getBeaconStatus:
                getBeaconStatus();
                break;
            case R.id.clear_log:
                mLogView.setText("");
                break;
            default:
                break;
        }
    }

    private void getTimeCategories() {
        // Use getTimeCategories() to get the information about the current time of the user location.
        // Time information includes whether the current day is a workday or a holiday, and whether the current day is in the morning, afternoon, or evening, or at the night.
        Awareness.getCaptureClient(this).getTimeCategories()
                .addOnSuccessListener(new OnSuccessListener<TimeCategoriesResponse>() {
                    @Override
                    public void onSuccess(TimeCategoriesResponse timeCategoriesResponse) {
                        TimeCategories timeCategories = timeCategoriesResponse.getTimeCategories();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int timeCode : timeCategories.getTimeCategories()) {
                            stringBuilder.append(Constant.TIME_DESCRIPTION_MAP.get(timeCode));
                        }
                        mLogView.printLog(stringBuilder.toString());
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get time categories.");
                        Log.e(TAG, "Failed to get time categories.", e);
                    }
                });
    }

    private void getHeadsetStatus() {
        // Use the getHeadsetStatus API to get headset connection status.
        Awareness.getCaptureClient(this)
                .getHeadsetStatus()
                .addOnSuccessListener(new OnSuccessListener<HeadsetStatusResponse>() {
                    @Override
                    public void onSuccess(HeadsetStatusResponse headsetStatusResponse) {
                        HeadsetStatus headsetStatus = headsetStatusResponse.getHeadsetStatus();
                        int status = headsetStatus.getStatus();
                        String stateStr = "Headsets are " +
                                (status == HeadsetStatus.CONNECTED ? "connected" : "disconnected");
                        mLogView.printLog(stateStr);
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get the headset capture.");
                        Log.e(TAG, "Failed to get the headset capture.", e);
                    }
                });
    }

    private void getLocation() {
        Awareness.getCaptureClient(this).getLocation()
                .addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
                    @Override
                    public void onSuccess(LocationResponse locationResponse) {
                        Location location = locationResponse.getLocation();
                        mLogView.printLog("Longitude:" + location.getLongitude()
                                + ",Latitude:" + location.getLatitude());
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get the location.");
                        Log.e(TAG, "Failed to get the location.", e);
                    }
                });
    }

    private void getBehaviorStatus() {
        Awareness.getCaptureClient(this).getBehavior()
                .addOnSuccessListener(new OnSuccessListener<BehaviorResponse>() {
                    @Override
                    public void onSuccess(BehaviorResponse behaviorResponse) {
                        BehaviorStatus behaviorStatus = behaviorResponse.getBehaviorStatus();
                        DetectedBehavior mostLikelyBehavior = behaviorStatus.getMostLikelyBehavior();
                        String str = "Most likely behavior is " +
                                Constant.BEHAVIOR_DESCRIPTION_MAP.get(mostLikelyBehavior.getType()) +
                                ",the confidence is " + mostLikelyBehavior.getConfidence();
                        mLogView.printLog(str);
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get the behavior.");
                        Log.e(TAG, "Failed to get the behavior.", e);
                    }
                });
    }

    private void getLightIntensity() {
        Awareness.getCaptureClient(this).getLightIntensity()
                .addOnSuccessListener(new OnSuccessListener<AmbientLightResponse>() {
                    @Override
                    public void onSuccess(AmbientLightResponse ambientLightResponse) {
                        AmbientLightStatus ambientLightStatus = ambientLightResponse.getAmbientLightStatus();
                        mLogView.printLog("Light intensity is " + ambientLightStatus.getLightIntensity() + " lux");
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get the light intensity.");
                        Log.e(TAG, "Failed to get the light intensity.", e);
                    }
                });
    }

    private void getWeatherStatus() {
        Awareness.getCaptureClient(this).getWeatherByDevice()
                .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {
                    @Override
                    public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                        WeatherStatus weatherStatus = weatherStatusResponse.getWeatherStatus();
                        WeatherSituation weatherSituation = weatherStatus.getWeatherSituation();
                        Situation situation = weatherSituation.getSituation();
                        // For more weather information, please refer to the development guide.
                        String weatherInfoStr = "City:" + weatherSituation.getCity().getName() + "\n" +
                                "Weather id is " + situation.getWeatherId() + "\n" +
                                "CN Weather id is " + situation.getCnWeatherId() + "\n" +
                                "Temperature is " + situation.getTemperatureC() + "℃" +
                                "," + situation.getTemperatureF() + "℉" + "\n" +
                                "Wind speed is " + situation.getWindSpeed() + "km/h" + "\n" +
                                "Wind direction is " + situation.getWindDir() + "\n" +
                                "Humidity is " + situation.getHumidity() + "%";
                        mLogView.printLog(weatherInfoStr);
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get weather information.");
                        Log.e(TAG, "Failed to get weather information.");
                    }
                });
    }

    private void getBluetoothStatus() {
        int deviceType = 0; // Value 0 indicates a Bluetooth car stereo.
        Awareness.getCaptureClient(this).getBluetoothStatus(deviceType)
                .addOnSuccessListener(new OnSuccessListener<BluetoothStatusResponse>() {
                    @Override
                    public void onSuccess(BluetoothStatusResponse bluetoothStatusResponse) {
                        BluetoothStatus bluetoothStatus = bluetoothStatusResponse.getBluetoothStatus();
                        int status = bluetoothStatus.getStatus();
                        String stateStr = "The Bluetooth car stereo is " +
                                (status == BluetoothStatus.CONNECTED ? "connected" : "disconnected");
                        mLogView.printLog(stateStr);
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get Bluetooth status.");
                        Log.e(TAG, "Failed to get Bluetooth status.", e);
                    }
                });
    }

    private void getBeaconStatus() {
        String namespace = "sample namespace";
        String type = "sample type";
        byte[] content = new byte[]{'s', 'a', 'm', 'p', 'l', 'e'};
        BeaconStatus.Filter filter = BeaconStatus.Filter.match(namespace, type, content);
        Awareness.getCaptureClient(this).getBeaconStatus(filter)
                .addOnSuccessListener(new OnSuccessListener<BeaconStatusResponse>() {
                    @Override
                    public void onSuccess(BeaconStatusResponse beaconStatusResponse) {
                        List<BeaconStatus.BeaconData> beaconDataList = beaconStatusResponse
                                .getBeaconStatus().getBeaconData();
                        if (beaconDataList != null && beaconDataList.size() != 0) {
                            int i = 1;
                            StringBuilder builder = new StringBuilder();
                            for (BeaconStatus.BeaconData beaconData : beaconDataList) {
                                builder.append("Beacon Data ").append(i);
                                builder.append(" namespace:").append(beaconData.getNamespace());
                                builder.append(",type:").append(beaconData.getType());
                                builder.append(",content:").append(Arrays.toString(beaconData.getContent()));
                                builder.append(". ");
                                i++;
                            }
                            mLogView.printLog(builder.toString());
                        } else {
                            mLogView.printLog("No beacon matches filters nearby.");
                        }
                        scrollToBottom();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        mLogView.printLog("Failed to get beacon status.");
                        Log.e(TAG, "Failed to get beacon status.", e);
                    }
                });
    }

    private void scrollToBottom() {
        mScrollView.postDelayed(() -> mScrollView.smoothScrollTo(0, mScrollView.getBottom()), 200);
    }
}
