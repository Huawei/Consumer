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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.safetydetect.SysIntegrityResp;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example of how to use SysIntegrity Service API.
 * Note that you have to configure an AppId for SafetyDetect Service first.
 */
public class SafetyDetectSysIntegrityAPIFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SafetyDetectSysIntegrityAPIFragment.class.getSimpleName();

    private static final String APP_ID = "101324691";

    private Button mButton1;

    private TextView basicIntegrityTextView;

    private TextView adviceTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_sysintegrity, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButton1 = getActivity().findViewById(R.id.fg_button_sys_integrity_go);
        mButton1.setOnClickListener(this);

        basicIntegrityTextView = getActivity().findViewById(R.id.fg_payloadBasicIntegrity);
        adviceTextView = getActivity().findViewById(R.id.fg_payloadAdvice);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fg_button_sys_integrity_go) {
            processView();
            invokeSysIntegrity();
        }
    }

    private void invokeSysIntegrity() {
        // TODO(developer): Change the nonce generation to include your own value.
        byte[] nonce = ("Sample" + System.currentTimeMillis()).getBytes();
        SafetyDetect.getClient(getActivity())
                .sysIntegrity(nonce, APP_ID)
                .addOnSuccessListener(new OnSuccessListener<SysIntegrityResp>() {
                    @Override
                    public void onSuccess(SysIntegrityResp response) {
                        // Indicates communication with the service was successful.
                        // Use response.getResult() to get the result data.
                        String jwsStr = response.getResult();
                        // Process the result data here
                        String[] jwsSplit = jwsStr.split("\\.");
                        String jwsPayloadStr = jwsSplit[1];
                        String payloadDetail = new String(Base64.decode(jwsPayloadStr.getBytes(), Base64.URL_SAFE));
                        try {
                            final JSONObject jsonObject = new JSONObject(payloadDetail);
                            final boolean basicIntegrity = jsonObject.getBoolean("basicIntegrity");
                            mButton1.setBackgroundResource(basicIntegrity ? R.drawable.btn_round_green : R.drawable.btn_round_red);
                            mButton1.setText(R.string.rerun);
                            String isBasicIntegrity = String.valueOf(basicIntegrity);
                            String basicIntegrityResult = "Basic Integrity: " + isBasicIntegrity;
                            basicIntegrityTextView.setText(basicIntegrityResult);
                            if (!basicIntegrity) {
                                String advice = "Advice: " + jsonObject.getString("advice");
                                adviceTextView.setText(advice);
                            }
                        } catch (JSONException e) {
                            String errorMsg = e.getMessage();
                            Log.e(TAG, errorMsg != null ? errorMsg : "unknown error");
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
                            // unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        Log.e(TAG, errorMsg);
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        mButton1.setBackgroundResource(R.drawable.btn_round_yellow);
                        mButton1.setText(R.string.rerun);
                    }
                });
    }

    private void processView() {
        basicIntegrityTextView.setText("");
        adviceTextView.setText("");
        ((TextView) getActivity().findViewById(R.id.fg_textView_title)).setText("");
        mButton1.setText(R.string.processing);
        mButton1.setBackgroundResource(R.drawable.btn_round_processing);
    }
}
