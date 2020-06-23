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

package com.huawei.hms.awareness.codelab.awareness.barrier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.huawei.hms.awareness.codelab.R;
import com.huawei.hms.awareness.codelab.Utils;
import com.huawei.hms.awareness.codelab.logger.LogView;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierStatus;
import com.huawei.hms.kit.awareness.barrier.BluetoothBarrier;
import com.huawei.hms.kit.awareness.status.BluetoothStatus;

public class BluetoothBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEEP_BARRIER_LABEL = "bluetooth keep barrier label";
    private static final String CONNECTING_BARRIER_LABEL = "bluetooth connecting barrier label";
    private static final String DISCONNECTING_BARRIER_LABEL = "bluetooth disconnecting barrier label";
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private BluetoothBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "BLUETOOTH_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new BluetoothBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_bluetoothBarrier_keeping).setOnClickListener(this);
        findViewById(R.id.add_bluetoothBarrier_connecting).setOnClickListener(this);
        findViewById(R.id.add_bluetoothBarrier_disconnecting).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        final int deviceType = 0; // Value 0 indicates a Bluetooth car stereo.
        switch (v.getId()) {
            case R.id.add_bluetoothBarrier_keeping:
                AwarenessBarrier keepingBarrier = BluetoothBarrier.keep(deviceType, BluetoothStatus.CONNECTED);
                Utils.addBarrier(this, KEEP_BARRIER_LABEL, keepingBarrier, mPendingIntent);
                break;
            case R.id.add_bluetoothBarrier_connecting:
                AwarenessBarrier connectingBarrier = BluetoothBarrier.connecting(deviceType);
                Utils.addBarrier(this, CONNECTING_BARRIER_LABEL, connectingBarrier, mPendingIntent);
                break;
            case R.id.add_bluetoothBarrier_disconnecting:
                AwarenessBarrier disconnectingBarrier = BluetoothBarrier.disconnecting(deviceType);
                Utils.addBarrier(this, DISCONNECTING_BARRIER_LABEL, disconnectingBarrier, mPendingIntent);
                break;
            case R.id.delete_barrier:
                Utils.deleteBarrier(this, KEEP_BARRIER_LABEL, CONNECTING_BARRIER_LABEL, DISCONNECTING_BARRIER_LABEL);
                break;
            case R.id.clear_log:
                mLogView.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBarrierReceiver != null) {
            unregisterReceiver(mBarrierReceiver);
        }
    }

    final class BluetoothBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case KEEP_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The Bluetooth car stereo is connected.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The Bluetooth car stereo is disconnected.");
                    } else {
                        mLogView.printLog("The Bluetooth car stereo status is unknown.");
                    }
                    break;

                case CONNECTING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The Bluetooth car stereo is connecting.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The Bluetooth car stereo is not connecting.");
                    } else {
                        mLogView.printLog("The Bluetooth car stereo status is unknown.");
                    }
                    break;

                case DISCONNECTING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The Bluetooth car stereo is disconnecting.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The Bluetooth car stereo is not disconnecting.");
                    } else {
                        mLogView.printLog("The Bluetooth car stereo status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
