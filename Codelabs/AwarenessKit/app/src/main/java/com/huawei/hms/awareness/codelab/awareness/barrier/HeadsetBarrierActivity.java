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
import com.huawei.hms.kit.awareness.barrier.HeadsetBarrier;
import com.huawei.hms.kit.awareness.status.HeadsetStatus;

public class HeadsetBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEEPING_BARRIER_LABEL = "keeping barrier label";
    private static final String CONNECTING_BARRIER_LABEL = "connecting barrier label";
    private static final String DISCONNECTING_BARRIER_LABEL = "disconnecting barrier label";
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private HeadsetBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headset_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "HEADSET_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new HeadsetBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_headsetBarrier_keeping).setOnClickListener(this);
        findViewById(R.id.add_headsetBarrier_connecting).setOnClickListener(this);
        findViewById(R.id.add_headsetBarrier_disconnecting).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_headsetBarrier_keeping:
                AwarenessBarrier keepingConnectedBarrier = HeadsetBarrier.keeping(HeadsetStatus.CONNECTED);
                Utils.addBarrier(this, KEEPING_BARRIER_LABEL, keepingConnectedBarrier, mPendingIntent);
                break;
            case R.id.add_headsetBarrier_connecting:
                // Create a headset barrier. When the headset are connected, the barrier status changes to true temporarily for about 5 seconds.
                // After 5 seconds, the status changes to false. If headset are disconnected within 5 seconds, the status also changes to false.
                AwarenessBarrier connectingBarrier = HeadsetBarrier.connecting();
                Utils.addBarrier(this, CONNECTING_BARRIER_LABEL, connectingBarrier, mPendingIntent);
                break;
            case R.id.add_headsetBarrier_disconnecting:
                AwarenessBarrier disconnectingBarrier = HeadsetBarrier.disconnecting();
                Utils.addBarrier(this, DISCONNECTING_BARRIER_LABEL, disconnectingBarrier, mPendingIntent);
                break;
            case R.id.delete_barrier:
                Utils.deleteBarrier(this, KEEPING_BARRIER_LABEL, CONNECTING_BARRIER_LABEL,
                        DISCONNECTING_BARRIER_LABEL);
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

    final class HeadsetBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case KEEPING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The headset is connected.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The headset is disconnected.");
                    } else {
                        mLogView.printLog("The headset status is unknown.");
                    }
                    break;

                case CONNECTING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The headset is connecting.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The headset is not connecting.");
                    } else {
                        mLogView.printLog("The headset status is unknown.");
                    }
                    break;

                case DISCONNECTING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The headset is disconnecting.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The headset is not disconnecting.");
                    } else {
                        mLogView.printLog("The headset status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
