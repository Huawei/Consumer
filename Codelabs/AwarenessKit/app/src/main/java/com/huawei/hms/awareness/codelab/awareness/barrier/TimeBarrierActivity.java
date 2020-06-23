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
import com.huawei.hms.kit.awareness.barrier.TimeBarrier;

import java.util.TimeZone;

public class TimeBarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IN_SUNRISE_OR_SUNSET_PERIOD_BARRIER_LABEL = "sunset barrier label";
    private static final String DURING_PERIOD_OF_DAT_BARRIER_LABEL = "period of day barrier label";
    private static final String DURING_TIME_PERIOD_BARRIER_LABEL = "time period barrier label";
    private static final String DURING_PERIOD_OF_WEEK_BARRIER_LABEL = "period of week barrier label";
    private static final String IN_TIME_CATEGORY_LABEL = "in time category label";

    private LogView mLogView;
    private ScrollView mScrollView;
    private PendingIntent mPendingIntent;
    private TimeBarrierReceiver mBarrierReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_barrier);
        initView();

        final String barrierReceiverAction = getApplication().getPackageName() + "TIME_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new TimeBarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));
    }

    private void initView() {
        findViewById(R.id.add_timeBarrier_inSunriseOrSunsetPeriod).setOnClickListener(this);
        findViewById(R.id.add_timeBarrier_duringPeriodOfDay).setOnClickListener(this);
        findViewById(R.id.add_timeBarrier_duringTimePeriod).setOnClickListener(this);
        findViewById(R.id.add_timeBarrier_duringPeriodOfWeek).setOnClickListener(this);
        findViewById(R.id.add_timeBarrier_inTimeCategory).setOnClickListener(this);
        findViewById(R.id.delete_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        mLogView = findViewById(R.id.logView);
        mScrollView = findViewById(R.id.log_scroll);
    }

    @Override
    public void onClick(View v) {
        long oneHourMilliSecond = 60 * 60 * 1000L;
        switch (v.getId()) {
            case R.id.add_timeBarrier_inSunriseOrSunsetPeriod:
                AwarenessBarrier sunsetBarrier = TimeBarrier.inSunriseOrSunsetPeriod(TimeBarrier.SUNSET_CODE,
                        -oneHourMilliSecond, oneHourMilliSecond);
                Utils.addBarrier(this, IN_SUNRISE_OR_SUNSET_PERIOD_BARRIER_LABEL,
                        sunsetBarrier, mPendingIntent);
                break;

            case R.id.add_timeBarrier_duringPeriodOfDay:
                AwarenessBarrier periodOfDayBarrier = TimeBarrier.duringPeriodOfDay(TimeZone.getDefault(),
                        11 * oneHourMilliSecond, 12 * oneHourMilliSecond);
                Utils.addBarrier(this, DURING_PERIOD_OF_DAT_BARRIER_LABEL, periodOfDayBarrier,
                        mPendingIntent);
                break;

            case R.id.add_timeBarrier_duringTimePeriod:
                long currentTimeStamp = System.currentTimeMillis();
                long tenSecondsMillis = 10 * 1000L;
                AwarenessBarrier timePeriodBarrier = TimeBarrier.duringTimePeriod(currentTimeStamp,
                        currentTimeStamp + tenSecondsMillis);
                Utils.addBarrier(this, DURING_TIME_PERIOD_BARRIER_LABEL,
                        timePeriodBarrier, mPendingIntent);
                break;

            case R.id.add_timeBarrier_duringPeriodOfWeek:
                AwarenessBarrier periodOfWeekBarrier = TimeBarrier.duringPeriodOfWeek(TimeBarrier.MONDAY_CODE,
                        TimeZone.getDefault(), 9 * oneHourMilliSecond, 10 * oneHourMilliSecond);
                Utils.addBarrier(this, DURING_PERIOD_OF_WEEK_BARRIER_LABEL,
                        periodOfWeekBarrier, mPendingIntent);
                break;

            case R.id.add_timeBarrier_inTimeCategory:
                AwarenessBarrier inTimeCategoryBarrier = TimeBarrier.inTimeCategory(TimeBarrier.TIME_CATEGORY_WEEKEND);
                Utils.addBarrier(this, IN_TIME_CATEGORY_LABEL, inTimeCategoryBarrier, mPendingIntent);
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

    final class TimeBarrierReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {
                case IN_SUNRISE_OR_SUNSET_PERIOD_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("It's around sunset time.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("It's not around sunset time.");
                    } else {
                        mLogView.printLog("The time status is unknown.");
                    }
                    break;

                case DURING_PERIOD_OF_DAT_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("It's between 11 am and 12 am.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("It's not between 11 am and 12 am.");
                    } else {
                        mLogView.printLog("The time status is unknown.");
                    }
                    break;

                case DURING_TIME_PERIOD_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("The barrier was added no more than 10 seconds ago.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("10 seconds have passed after adding the duringTimePeriod barrier.");
                    } else {
                        mLogView.printLog("The time status is unknown.");
                    }
                    break;

                case DURING_PERIOD_OF_WEEK_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("It's between 9 am and 10 am on Monday.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("It's not between 9 am and 10 am on Monday.");
                    } else {
                        mLogView.printLog("The time status is unknown.");
                    }
                    break;

                case IN_TIME_CATEGORY_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        mLogView.printLog("Today is a weekend.");
                    } else if (barrierPresentStatus == BarrierStatus.FALSE) {
                        mLogView.printLog("Today is not a weekend.");
                    } else {
                        mLogView.printLog("The time status is unknown.");
                    }
                    break;

                default:
                    break;
            }
            mScrollView.postDelayed(()-> mScrollView.smoothScrollTo(0,mScrollView.getBottom()),200);
        }
    }
}
