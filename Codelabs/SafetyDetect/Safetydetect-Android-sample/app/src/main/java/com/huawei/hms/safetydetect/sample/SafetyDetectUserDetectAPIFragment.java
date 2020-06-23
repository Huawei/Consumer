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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.safetydetect.UserDetectResponse;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectClient;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * An example of how to use the UserDetect Service API.
 *
 */
public class SafetyDetectUserDetectAPIFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SafetyDetectUserDetectAPIFragment.class.getSimpleName();

    private static final int APP_ID = 101324691;

    private SafetyDetectClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = SafetyDetect.getClient(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        client.initUserDetect();
    }

    @Override
    public void onPause() {
        super.onPause();
        client.shutdownUserDetect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_userdetect, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.fg_login_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fg_login_btn) {
            detect();
        }
    }

    private void detect() {
        Log.i(TAG, "User detection start.");
        client.userDetection(String.valueOf(APP_ID))
                .addOnSuccessListener(new OnSuccessListener<UserDetectResponse>() {
                    /**
                     * Called after successfully communicating with the SafetyDetect API.
                     * The #onSuccess callback receives an
                     * {@link UserDetectResponse} that contains a
                     * responseToken that can be used to get user detect result.
                     */
                    @Override
                    public void onSuccess(UserDetectResponse userDetectResponse) {
                        // Indicates communication with the service was successful.
                        Log.i(TAG, "User detection succeed, response = " + userDetectResponse);
                        boolean verifySucceed = verify(userDetectResponse.getResponseToken());
                        if (verifySucceed) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "User detection succeed and verify succeed",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "User detection succeed but verify fail, please replace verify url with your's server address",
                                    Toast.LENGTH_SHORT)
                                    .show();
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
                        Log.i(TAG, "User detection fail. Error info: " + errorMsg);
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Send responseToken to your server to get the result of user detect.
     */
    private static boolean verify(final String responseToken) {
        try {
            return new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... strings) {
                    String input = strings[0];
                    JSONObject jsonObject = new JSONObject();
                    try {
                        // TODO(developer): Replace the baseUrl with your own server address,better not hard code.
                        String baseUrl = "https://www.example.com/userdetect/verify";
                        jsonObject.put("response", input);
                        String result = sendPost(baseUrl, jsonObject);
                        JSONObject resultJson = new JSONObject(result);
                        boolean success = resultJson.getBoolean("success");
                        // if success is true that means the user is real human instead of a robot.
                        Log.i(TAG, "verify: result = " + success);
                        return success;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }.execute(responseToken).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * post the response token to yur own server.
     */
    private static String sendPost(String baseUrl, JSONObject postDataParams) throws Exception {
        URL url = new URL(baseUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(20000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = conn.getOutputStream(); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            writer.write(postDataParams.toString());
            writer.flush();
        }

        int responseCode = conn.getResponseCode(); // To Check for 200
        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;

    }
}
