/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.demo.identitydemo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.identity.Address;
import com.huawei.hms.identity.entity.GetUserAddressResult;
import com.huawei.hms.identity.entity.UserAddress;
import com.huawei.hms.identity.entity.UserAddressRequest;
import com.huawei.hms.support.api.client.Status;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "identitycodelab";

    private static final int GET_ADDRESS = 1000;

    private TextView textView;


    private Button queryAddrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.user_address);
        queryAddrButton = findViewById(R.id.query_user_address);
        queryAddrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserAddress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        switch (requestCode) {
            case GET_ADDRESS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        UserAddress userAddress = UserAddress.parseIntent(data);
                        if (userAddress != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("name:" + userAddress.getName() + ",");
                            sb.append("city:" + userAddress.getAdministrativeArea() + ",");
                            sb.append("area:" + userAddress.getLocality() + ",");
                            sb.append("address:" + userAddress.getAddressLine1() + userAddress.getAddressLine2() + ",");
                            sb.append("phone:" + userAddress.getPhoneNumber());
                            Log.i(TAG, "user address is " + sb.toString());
                            textView.setText(sb.toString());
                        } else {
                            textView.setText("Failed to get user address.");
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        Log.i(TAG, "result is wrong, result code is " + resultCode);
                        break;
                }
            default:
                break;
        }
    }

    private void getUserAddress() {
        UserAddressRequest req = new UserAddressRequest();
        Task<GetUserAddressResult> task = Address.getAddressClient(this).getUserAddress(req);
        task.addOnSuccessListener(new OnSuccessListener<GetUserAddressResult>() {
            @Override
            public void onSuccess(GetUserAddressResult result) {
                Log.i(TAG, "onSuccess result code:" + result.getReturnCode());
                try {
                    startActivityForResult(result);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "on Failed result code:" + e.getMessage());
            }
        });
    }

    private void startActivityForResult(GetUserAddressResult result) throws IntentSender.SendIntentException {
        Status status = result.getStatus();
        if (result.getReturnCode() == 0 && status.hasResolution()) {
            Log.i(TAG, "the result had resolution.");
            status.startResolutionForResult(this, GET_ADDRESS);
        } else {
            Log.i(TAG, "the response is wrong, the return code is " + result.getReturnCode());
        }
    }
}
