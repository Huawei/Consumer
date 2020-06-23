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

package com.huawei.hms.awareness.codelab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.awareness.codelab.awareness.BarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.CaptureActivity;
import com.huawei.hms.awareness.codelab.logger.LogView;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.capture.CapabilityResponse;
import com.huawei.hms.kit.awareness.status.CapabilityStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 940;
    private final String[] mPermissionsOnHigherVersion = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION};
    private final String[] mPermissionsOnLowerVersion = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            "com.huawei.hms.permission.ACTIVITY_RECOGNITION"};

    private final String TAG = getClass().getSimpleName();
    private LogView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.awareness_capture).setOnClickListener(this);
        findViewById(R.id.awareness_barrier).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        queryDeviceSupportingCapabilities();
        checkAndRequestPermissions();
    }

    private void queryDeviceSupportingCapabilities() {
        // Use querySupportingCapabilities to query awareness capabilities supported by the current device.
        Awareness.getCaptureClient(this).querySupportingCapabilities()
                .addOnSuccessListener(new OnSuccessListener<CapabilityResponse>() {
                    @Override
                    public void onSuccess(CapabilityResponse capabilityResponse) {
                        CapabilityStatus status = capabilityResponse.getCapabilityStatus();
                        int[] capabilities = status.getCapabilities();
                        Log.i(TAG, "capabilities code :" + Arrays.toString(capabilities));
                        StringBuilder deviceSupportingStr = new StringBuilder();
                        deviceSupportingStr.append("This device supports the following awareness capabilities:\n");
                        for (int capability : capabilities) {
                            deviceSupportingStr.append(Constant.CAPABILITIES_DESCRIPTION_MAP.get(capability));
                            deviceSupportingStr.append("\n");
                        }
                        mLogView.printLog(deviceSupportingStr.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Failed to get supported capabilities.", e);
                        mLogView.printLog("Failed to get supported capabilities.");
                    }
                });
    }

    private void checkAndRequestPermissions() {
        List<String> permissionsDoNotGrant = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (String permission : mPermissionsOnHigherVersion) {
                if (ActivityCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission);
                }
            }
        } else {
            for (String permission : mPermissionsOnLowerVersion) {
                if (ActivityCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission);
                }
            }
        }

        if (permissionsDoNotGrant.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    permissionsDoNotGrant.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awareness_capture:
                Intent snapshotIntent = new Intent(this, CaptureActivity.class);
                startActivity(snapshotIntent);
                break;
            case R.id.awareness_barrier:
                Intent barrierIntent = new Intent(this, BarrierActivity.class);
                startActivity(barrierIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean hasPermissionDenied = false;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    hasPermissionDenied = true;
                }
            }
            if (hasPermissionDenied) {
                Toast.makeText(this, "grant permission failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "grant permission success", Toast.LENGTH_LONG).show();
            }
        }
    }
}
