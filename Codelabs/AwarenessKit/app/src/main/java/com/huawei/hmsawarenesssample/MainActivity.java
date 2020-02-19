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

package com.huawei.hmsawarenesssample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmsawarenesssample.awareness.BarrierActivity;
import com.huawei.hmsawarenesssample.awareness.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 940;
    private final String[] permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACTIVITY_RECOGNITION};
    private List<String> mPermissionsDoNotGrant = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.awareness_capture).setOnClickListener(this);
        findViewById(R.id.awareness_barrier).setOnClickListener(this);
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        mPermissionsDoNotGrant.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionsDoNotGrant.add(permission);
                }
            }
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, permissions[0])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionsDoNotGrant.add(permissions[0]);
            }
        }

        if (mPermissionsDoNotGrant.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    mPermissionsDoNotGrant.toArray(new String[0]), PERMISSION_REQUEST_CODE);
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
                Intent fenceIntent = new Intent(this, BarrierActivity.class);
                startActivity(fenceIntent);
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
