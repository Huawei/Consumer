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
import com.huawei.hms.kit.awareness.barrier.AmbientLightBarrier;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierStatus;

public class AmbientLightBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String RANGE_BARRIER_LABEL = "range barrier label";
    private static final String ABOVE_BARRIER_LABEL = "above barrier label";
    private static final String BELOW_BARRIER_LABEL = "below barrier label";
    private static final float LOW_LUX_VALUE = 500.0f;
    private static final float HIGH_LUX_VALUE = 2500.0f;
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private AmbientLightBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient_light_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "LIGHT_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new AmbientLightBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_ambientLightBarrier_range).setOnClickListener(this);
        findViewById(R.id.add_ambientLightBarrier_above).setOnClickListener(this);
        findViewById(R.id.add_ambientLightBarrier_below).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_ambientLightBarrier_range:
                AwarenessBarrier lightRangeBarrier = AmbientLightBarrier.range(LOW_LUX_VALUE, HIGH_LUX_VALUE);
                Utils.addBarrier(this, RANGE_BARRIER_LABEL, lightRangeBarrier, mPendingIntent);
                break;
            case R.id.add_ambientLightBarrier_above:
                AwarenessBarrier lightAboveBarrier = AmbientLightBarrier.above(HIGH_LUX_VALUE);
                Utils.addBarrier(this, ABOVE_BARRIER_LABEL, lightAboveBarrier, mPendingIntent);
                break;
            case R.id.add_ambientLightBarrier_below:
                AwarenessBarrier lightBelowBarrier = AmbientLightBarrier.below(LOW_LUX_VALUE);
                Utils.addBarrier(this, BELOW_BARRIER_LABEL, lightBelowBarrier, mPendingIntent);
                break;
            case R.id.delete_barrier:
                Utils.deleteBarrier(this, RANGE_BARRIER_LABEL, ABOVE_BARRIER_LABEL, BELOW_BARRIER_LABEL);
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

    final class AmbientLightBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case RANGE_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("Ambient light intensity is in the range of "
                                + LOW_LUX_VALUE + " to " + HIGH_LUX_VALUE + " lux");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("Ambient light intensity is not in the range of "
                                + LOW_LUX_VALUE + " to " + HIGH_LUX_VALUE + " lux");
                    } else {
                        mLogView.printLog("Ambient light status is unknown.");
                    }
                    break;

                case ABOVE_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("Ambient light intensity is above " + HIGH_LUX_VALUE
                                + " lux");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("Ambient light intensity is not above " + HIGH_LUX_VALUE
                                + " lux");
                    } else {
                        mLogView.printLog("Ambient light status is unknown.");
                    }
                    break;

                case BELOW_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("Ambient light intensity is below " + LOW_LUX_VALUE
                                + " lux");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("Ambient light intensity is not below " + LOW_LUX_VALUE
                                + " lux");
                    } else {
                        mLogView.printLog("Ambient light status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
