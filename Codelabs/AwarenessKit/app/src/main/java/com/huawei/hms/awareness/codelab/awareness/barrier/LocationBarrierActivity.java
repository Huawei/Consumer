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
import com.huawei.hms.kit.awareness.barrier.LocationBarrier;

public class LocationBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ENTER_BARRIER_LABEL = "enter barrier label";
    private static final String STAY_BARRIER_LABEL = "stay barrier label";
    private static final String EXIT_BARRIER_LABEL = "exit barrier label";
    private final long mTimeOfDuration = 1000L;
    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private LocationBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "LOCATION_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new LocationBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_locationBarrier_enter).setOnClickListener(this);
        findViewById(R.id.add_locationBarrier_stay).setOnClickListener(this);
        findViewById(R.id.add_locationBarrier_exit).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        double latitude = 22.4943;
        double longitude = 113.7436;
        double radius = 200;

        switch (v.getId()) {
            case R.id.add_locationBarrier_enter:
                AwarenessBarrier enterBarrier = LocationBarrier.enter(latitude, longitude, radius);
                Utils.addBarrier(this, ENTER_BARRIER_LABEL, enterBarrier, mPendingIntent);
                break;
            case R.id.add_locationBarrier_stay:
                AwarenessBarrier stayBarrier = LocationBarrier.stay(latitude, longitude, radius, mTimeOfDuration);
                Utils.addBarrier(this, STAY_BARRIER_LABEL, stayBarrier, mPendingIntent);
                break;
            case R.id.add_locationBarrier_exit:
                AwarenessBarrier exitBarrier = LocationBarrier.exit(latitude, longitude, radius);
                Utils.addBarrier(this, EXIT_BARRIER_LABEL, exitBarrier, mPendingIntent);
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
        if (mBarrierReceiver != null) {
            unregisterReceiver(mBarrierReceiver);
        }
        super.onDestroy();
    }

    final class LocationBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case ENTER_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("You are entering the area set by locationBarrier.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("You are not entering the area set by locationBarrier.");
                    } else {
                        mLogView.printLog("The location status is unknown.");
                    }
                    break;
                case STAY_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("You have stayed in the area set by locationBarrier for "
                                + mTimeOfDuration + "ms");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("You are not staying in the area set by locationBarrier" +
                                " or the time of duration is not enough.");
                    } else {
                        mLogView.printLog("The location status is unknown.");
                    }
                    break;
                case EXIT_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("You are exiting the area set by locationBarrier.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("You are not exiting the area set by locationBarrier.");
                    } else {
                        mLogView.printLog("The location status is unknown.");
                    }
                    break;
                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
