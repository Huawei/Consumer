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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.awareness.codelab.R;
import com.huawei.hms.awareness.codelab.awareness.barrier.AmbientLightBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.BarrierCombinationActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.BeaconBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.BehaviorBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.BluetoothBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.HeadsetBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.LocationBarrierActivity;
import com.huawei.hms.awareness.codelab.awareness.barrier.TimeBarrierActivity;

public class BarrierActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrier);
        findViewById(R.id.headset_barrier).setOnClickListener(this);
        findViewById(R.id.location_barrier).setOnClickListener(this);
        findViewById(R.id.bluetooth_barrier).setOnClickListener(this);
        findViewById(R.id.behavior_barrier).setOnClickListener(this);
        findViewById(R.id.time_barrier).setOnClickListener(this);
        findViewById(R.id.ambientLight_barrier).setOnClickListener(this);
        findViewById(R.id.beacon_barrier).setOnClickListener(this);
        findViewById(R.id.barrier_combination).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.headset_barrier:
                intent = new Intent(this, HeadsetBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.location_barrier:
                intent = new Intent(this, LocationBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.bluetooth_barrier:
                intent = new Intent(this, BluetoothBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.behavior_barrier:
                intent = new Intent(this, BehaviorBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.time_barrier:
                intent = new Intent(this, TimeBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.ambientLight_barrier:
                intent = new Intent(this, AmbientLightBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.beacon_barrier:
                intent = new Intent(this, BeaconBarrierActivity.class);
                startActivity(intent);
                break;
            case R.id.barrier_combination:
                intent = new Intent(this, BarrierCombinationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
