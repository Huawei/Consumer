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

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.entity.safetydetect.MaliciousAppsData;
import com.huawei.hms.support.api.entity.safetydetect.MaliciousAppsListResp;
import com.huawei.hms.support.api.entity.safetydetect.VerifyAppsCheckEnabledResp;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import java.util.List;

/**
 * An example of how to use AppsCheck Service API.
 * Note that you have to configure an AppId for SafetyDetect Service first.
 */
public class SafetyDetectAppsCheckAPIFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SafetyDetectAppsCheckAPIFragment.class.getSimpleName();

    private ListView maliciousAppListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_appscheck, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.fg_enable_appscheck).setOnClickListener(this);
        getActivity().findViewById(R.id.fg_verify_appscheck).setOnClickListener(this);
        getActivity().findViewById(R.id.fg_get_malicious_apps).setOnClickListener(this);

        maliciousAppListView = getActivity().findViewById(R.id.fg_list_app);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fg_enable_appscheck) {
            enableAppsCheck();
        } else if (id == R.id.fg_verify_appscheck) {
            verifyAppsCheckEnabled();
        } else if (id == R.id.fg_get_malicious_apps) {
            getMaliciousApps();
        }
    }

    /**
     * isVerifyAppsCheck() shows whether app verification is enabled
     */
    private void verifyAppsCheckEnabled() {
        SafetyDetect.getClient(getActivity().getApplicationContext())
                .isVerifyAppsCheck()
                .addOnSuccessListener(new OnSuccessListener<VerifyAppsCheckEnabledResp>() {
                    @Override
                    public void onSuccess(VerifyAppsCheckEnabledResp appsCheckResp) {
                        boolean isVerifyAppsEnabled = appsCheckResp.getResult();
                        if (isVerifyAppsEnabled) {
                            String text = "The AppsCheck feature is enabled.";
                            Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // There was an error communicating with the service.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            // An error with the HMS API contains some additional details.
                            ApiException apiException = (ApiException) e;
                            errorMsg = SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) +
                                    ": " + apiException.getMessage();
                            // You can use the apiException.getStatusCode() method to get the status code.

                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        String msg = "Verify AppsCheck Enabled failed! Message: " + errorMsg;
                        Log.e(TAG, msg);
                        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * enableAppsCheck()  enable appsCheck
     */
    private void enableAppsCheck() {
        SafetyDetect.getClient(getActivity().getApplicationContext())
                .enableAppsCheck()
                .addOnSuccessListener(new OnSuccessListener<VerifyAppsCheckEnabledResp>() {
                    @Override
                    public void onSuccess(VerifyAppsCheckEnabledResp appsCheckResp) {
                        boolean isEnableAppsCheck = appsCheckResp.getResult();
                        if (isEnableAppsCheck) {
                            String text = "The AppsCheck feature is enabled.";
                            Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // There was an error communicating with the service.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            // An error with the HMS API contains some additional details.
                            ApiException apiException = (ApiException) e;
                            errorMsg = SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) +
                                    ": " + apiException.getMessage();
                            // You can use the apiException.getStatusCode() method to get the status code.

                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        String msg = "Enable AppsCheck failed! Message: " + errorMsg;
                        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * getMaliciousAppsList()  List malicious installed apps
     */
    private void getMaliciousApps() {
        SafetyDetect.getClient(getActivity())
                .getMaliciousAppsList()
                .addOnSuccessListener(new OnSuccessListener<MaliciousAppsListResp>() {
                    @Override
                    public void onSuccess(MaliciousAppsListResp maliciousAppsListResp) {
                        List<MaliciousAppsData> appsDataList = maliciousAppsListResp.getMaliciousAppsList();
                        if (maliciousAppsListResp.getRtnCode() == CommonCode.OK) {
                            if (appsDataList.isEmpty()) {
                                final String text = "No known potentially malicious apps are installed.";
                                Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            } else {
                                for (MaliciousAppsData maliciousApp : appsDataList) {
                                    Log.e(TAG, "Information about a malicious app:");
                                    Log.e(TAG, "  APK: " + maliciousApp.getApkPackageName());
                                    Log.e(TAG, "  SHA-256: " + maliciousApp.getApkSha256());
                                    Log.e(TAG, "  Category: " + maliciousApp.getApkCategory());
                                }

                                ListAdapter maliciousAppAdapter = new MaliciousAppsDataListAdapter(appsDataList, getActivity().getApplicationContext());
                                maliciousAppListView.setAdapter(maliciousAppAdapter);
                            }
                        } else {
                            String msg = "Get malicious apps list failed! Message: " + maliciousAppsListResp.getErrorReason();
                            Log.e(TAG, msg);
                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // There was an error communicating with the service.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            // An error with the HMS API contains some additional details.
                            ApiException apiException = (ApiException) e;
                            errorMsg = SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) +
                                    ": " + apiException.getMessage();
                            // You can use the apiException.getStatusCode() method to get the status code.

                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        String msg = "Get malicious apps list failed! Message: " + errorMsg;
                        Log.e(TAG, msg);
                        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
