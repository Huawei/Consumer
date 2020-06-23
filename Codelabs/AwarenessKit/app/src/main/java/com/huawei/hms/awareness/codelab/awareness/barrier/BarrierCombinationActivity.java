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
import com.huawei.hms.kit.awareness.barrier.BehaviorBarrier;
import com.huawei.hms.kit.awareness.barrier.BluetoothBarrier;
import com.huawei.hms.kit.awareness.barrier.HeadsetBarrier;
import com.huawei.hms.kit.awareness.barrier.TimeBarrier;
import com.huawei.hms.kit.awareness.status.BluetoothStatus;
import com.huawei.hms.kit.awareness.status.HeadsetStatus;

public class BarrierCombinationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String COMBINED_BEHAVIOR_HEADSET_BARRIER_LABEL = "behavior headset barrier label";
    private static final String COMBINED_TIME_BLUETOOTH_BARRIER_LABEL = "time bluetooth barrier label";
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private CombinedBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "COMBINED_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new CombinedBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_combinedBarrier_behavior_headset).setOnClickListener(this);
        findViewById(R.id.add_combinedBarrier_time_bluetooth).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_combinedBarrier_behavior_headset:
                // When a user is taking exercise (running or riding a bike) with the headset connected, the barrier status is true.
                AwarenessBarrier combinedBehaviorHeadsetBarrier = AwarenessBarrier.and(
                        HeadsetBarrier.keeping(HeadsetStatus.CONNECTED),
                        AwarenessBarrier.or(
                                BehaviorBarrier.keeping(BehaviorBarrier.BEHAVIOR_RUNNING),
                                BehaviorBarrier.keeping(BehaviorBarrier.BEHAVIOR_ON_BICYCLE)));
                Utils.addBarrier(this, COMBINED_BEHAVIOR_HEADSET_BARRIER_LABEL,
                        combinedBehaviorHeadsetBarrier, mPendingIntent);
                break;

            case R.id.add_combinedBarrier_time_bluetooth:
                // When the Bluetooth car stereo is connected on a weekend, the barrier status is true.
                int deviceType = 0;  // Value 0 indicates a Bluetooth car stereo.
                AwarenessBarrier combinedTimeBluetoothBarrier = AwarenessBarrier.and(
                        TimeBarrier.inTimeCategory(TimeBarrier.TIME_CATEGORY_WEEKEND),
                        BluetoothBarrier.keep(deviceType, BluetoothStatus.CONNECTED));
                Utils.addBarrier(this, COMBINED_TIME_BLUETOOTH_BARRIER_LABEL,
                        combinedTimeBluetoothBarrier, mPendingIntent);
                break;

            case R.id.delete_barrier:
                Utils.deleteBarrier(this, mPendingIntent);
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

    final class CombinedBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case COMBINED_BEHAVIOR_HEADSET_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The user is taking exercise and the headset is connected.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The user is not taking exercise or the headset is not connected.");
                    } else {
                        mLogView.printLog("The combined barrier status is unknown.");
                    }
                    break;

                case COMBINED_TIME_BLUETOOTH_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("Bluetooth car stereo is connected on a weekend.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("Bluetooth car stereo is not connected or the current day is not a weekend.");
                    } else {
                        mLogView.printLog("The combined barrier status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
