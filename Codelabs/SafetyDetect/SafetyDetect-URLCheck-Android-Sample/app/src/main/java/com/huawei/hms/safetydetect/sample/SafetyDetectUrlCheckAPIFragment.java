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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.safetydetect.UrlCheckResponse;
import com.huawei.hms.support.api.entity.safetydetect.UrlCheckThreat;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectClient;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import java.util.List;

/**
 * An example of how to use UrlCheck Service API.
 *
 * Note that you should call initUrlCheck method before you call urlCheck method.
 */
public class SafetyDetectUrlCheckAPIFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = SafetyDetectUrlCheckAPIFragment.class.getSimpleName();

    private static final String APP_ID = "101324691";

    private SafetyDetectClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = SafetyDetect.getClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_urlcheck, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        client.initUrlCheck();
    }

    @Override
    public void onPause() {
        super.onPause();
        client.shutdownUrlCheck();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.fg_call_url_btn).setOnClickListener(this);
        Spinner spinner = getActivity().findViewById(R.id.fg_url_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.url_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fg_call_url_btn) {
            callUrlCheckApi();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String url = (String) adapterView.getItemAtPosition(pos);
        EditText textView = getActivity().findViewById(R.id.fg_call_urlCheck_text);
        textView.setText(url);
        final EditText testRes = getActivity().findViewById(R.id.fg_call_urlResult);
        testRes.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void callUrlCheckApi() {
        Log.i(TAG, "Start call URL check api");

        EditText editText = getActivity().findViewById(R.id.fg_call_urlCheck_text);
        String realUrl = editText.getText().toString().trim();
        final EditText testRes = getActivity().findViewById(R.id.fg_call_urlResult);
        client.urlCheck(realUrl, APP_ID,
                // Specify url threat type
                UrlCheckThreat.MALWARE,
                UrlCheckThreat.PHISHING)
                .addOnSuccessListener(new OnSuccessListener<UrlCheckResponse>() {
                    /**
                     * Called after successfully communicating with the SafetyDetect API.
                     * The #onSuccess callback receives an
                     * {@link com.huawei.hms.support.api.entity.safetydetect.UrlCheckResponse} that contains a
                     * list of UrlCheckThreat that contains the threat type of the Url.
                     */
                    @Override
                    public void onSuccess(UrlCheckResponse urlCheckResponse) {
                        // Indicates communication with the service was successful.
                        // Identify any detected threats.
                        // Call getUrlCheckResponse method of UrlCheckResponse then you can get List<UrlCheckThreat> .
                        // If List<UrlCheckThreat> is empty , that means no threats found , else that means threats found.
                        List<UrlCheckThreat> list = urlCheckResponse.getUrlCheckResponse();
                        if (list.isEmpty()) {
                            // No threats found.
                            testRes.setText("No threats found.");
                        } else {
                            // Threats found!
                            testRes.setText("Threats found!");
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Called when an error occurred when communicating with the SafetyDetect API.
                     */
                    @Override
                    public void onFailure(Exception e) {
                        // An error with the Huawei Mobile Service API contains some additional details.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            errorMsg = "Error: " +
                                    SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " +
                                    e.getMessage();

                            // You can use the apiException.getStatusCode() method to get the status code.
                            // Note: If the status code is SafetyDetectStatusCodes.CHECK_WITHOUT_INIT, you need to call initUrlCheck().
                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        Log.d(TAG, errorMsg);
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
