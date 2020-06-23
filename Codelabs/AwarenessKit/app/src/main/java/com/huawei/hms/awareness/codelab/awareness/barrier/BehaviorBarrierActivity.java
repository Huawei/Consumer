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

public class BehaviorBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEEPING_BARRIER_LABEL = "behavior keeping barrier label";
    private static final String BEGINNING_BARRIER_LABEL = "behavior beginning barrier label";
    private static final String ENDING_BARRIER_LABEL = "behavior ending barrier label";
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private BehaviorBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "BEHAVIOR_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new BehaviorBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_behaviorBarrier_keeping).setOnClickListener(this);
        findViewById(R.id.add_behaviorBarrier_beginning).setOnClickListener(this);
        findViewById(R.id.add_behaviorBarrier_ending).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_behaviorBarrier_keeping:
                AwarenessBarrier keepStillBarrier = BehaviorBarrier.keeping(BehaviorBarrier.BEHAVIOR_STILL);
                Utils.addBarrier(this, KEEPING_BARRIER_LABEL, keepStillBarrier, mPendingIntent);
                break;
            case R.id.add_behaviorBarrier_beginning:
                AwarenessBarrier beginWalkingBarrier = BehaviorBarrier.beginning(BehaviorBarrier.BEHAVIOR_WALKING);
                Utils.addBarrier(this, BEGINNING_BARRIER_LABEL, beginWalkingBarrier, mPendingIntent);
                break;
            case R.id.add_behaviorBarrier_ending:
                AwarenessBarrier endCyclingBarrier = BehaviorBarrier.ending(BehaviorBarrier.BEHAVIOR_ON_BICYCLE);
                Utils.addBarrier(this, ENDING_BARRIER_LABEL, endCyclingBarrier, mPendingIntent);
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

    final class BehaviorBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case KEEPING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The user is still.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The user is not still.");
                    } else {
                        mLogView.printLog("The user behavior status is unknown.");
                    }
                    break;

                case BEGINNING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The user begins to walk.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The beginning barrier status is false.");
                    } else {
                        mLogView.printLog("The user behavior status is unknown.");
                    }
                    break;

                case ENDING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The user stops cycling.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("The ending barrier status is false.");
                    } else {
                        mLogView.printLog("The user behavior status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
