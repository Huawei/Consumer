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

package com.huawei.hmsawarenesssample.awareness;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierStatus;
import com.huawei.hms.kit.awareness.barrier.BarrierUpdateRequest;
import com.huawei.hms.kit.awareness.barrier.BehaviorBarrier;
import com.huawei.hms.kit.awareness.barrier.HeadsetBarrier;
import com.huawei.hms.kit.awareness.status.HeadsetStatus;
import com.huawei.hmsawarenesssample.R;
import com.huawei.hmsawarenesssample.logger.LogView;

import java.util.HashSet;
import java.util.Set;

public class BarrierActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private PendingIntent mPendingIntent;
    private BarrierReceiver mBarrierReceiver;
    private Set<String> mAddedLabels = new HashSet<>();
    private LogView logView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrier);
        findViewById(R.id.add_barrier).setOnClickListener(this);
        findViewById(R.id.add_combination_barrier).setOnClickListener(this);
        findViewById(R.id.clear_log).setOnClickListener(this);
        logView = findViewById(R.id.logView);
        final String BARRIER_RECEIVER_ACTION = getApplication().getPackageName() + "BARRIER_RECEIVER_ACTION";

        Intent intent = new Intent(BARRIER_RECEIVER_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by the system when the barrier status changes.
        mBarrierReceiver = new BarrierReceiver();
        registerReceiver(mBarrierReceiver, new IntentFilter(BARRIER_RECEIVER_ACTION));
    }

    @Override
    protected void onDestroy() {
        if (mBarrierReceiver != null) {
            unregisterReceiver(mBarrierReceiver);
            mBarrierReceiver = null;
        }
        deleteBarrier(mAddedLabels);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_barrier:
                try {
                    // Create a headset barrier. When the headset are connected, the barrier status changes to true temporarily for about 5 seconds.
                    // After 5 seconds, the status changes to false. If headset are disconnected within 5 seconds, the status also changes to false.
                    AwarenessBarrier barrier = HeadsetBarrier.connecting();
                    String headsetBarrierLabel = "headset_connecting";
                    addBarrier(headsetBarrierLabel, barrier);
                }
                catch (Exception e) {
                    logView.printLog("add barrier failed.Exception:" + e.getMessage());
                }
                break;
            case R.id.add_combination_barrier:
                try {
                    // Use the AND logic to combine two barriers. That is, when the status of both barriers is true, the status of the combined barrier changes to true. The rule is similar for other states.
                    // In the following example, the status of the combined barrier changes to true only when the user is still and the headset are connected.
                    AwarenessBarrier stillBarrier = BehaviorBarrier.keeping(BehaviorBarrier.BEHAVIOR_STILL);
                    AwarenessBarrier headsetBarrier = HeadsetBarrier.keeping(HeadsetStatus.CONNECTED);
                    String combinationBarrierLabel = "still_AND_headsetConnected";
                    AwarenessBarrier combinationBarrier = AwarenessBarrier.and(stillBarrier, headsetBarrier);
                    addBarrier(combinationBarrierLabel, combinationBarrier);
                    // In addition to the AND logic, the OR and NOT logic can be used to combine barrier.
                }
                catch (Exception e) {
                    logView.printLog("add combination barrier failed.Exception:" + e.getMessage());
                }
                break;
            case R.id.clear_log:
                logView.setText("");
                break;
            default:
                break;
        }
    }

    private void addBarrier(final String label, AwarenessBarrier barrier) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        // When the status of the registered barrier changes, pendingIntent is triggered. In this example, pendingIntent sends a broadcast.
        // label is used to uniquely identify the barrier. You can query a barrier by label and delete it.
        BarrierUpdateRequest request = builder.addBarrier(label, barrier, mPendingIntent).build();
        Task<Void> task = Awareness.getBarrierClient(this).updateBarriers(request);
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "add barrier success", Toast.LENGTH_SHORT).show();
                        mAddedLabels.add(label);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "add barrier failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "add barrier failed", e);
                            }
                        });
    }

    private void deleteBarrier(final Set<String> labels) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        for (String label : labels) {
            builder.deleteBarrier(label);
        }
        Awareness.getBarrierClient(this)
                .updateBarriers(builder.build())
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "remove Barrier success");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Log.e(TAG, "remove Barrier failed", e);
                            }
                        });
    }

    public class BarrierReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            switch (barrierStatus.getPresentStatus()) {
                case BarrierStatus.TRUE:
                    logView.printLog(label + " status:true");
                    break;
                case BarrierStatus.FALSE:
                    logView.printLog(label + " status:false");
                    break;
                case BarrierStatus.UNKNOWN:
                    logView.printLog(label + " status:unknown");
                    break;
                default:
                    break;
            }
        }
    }
}
