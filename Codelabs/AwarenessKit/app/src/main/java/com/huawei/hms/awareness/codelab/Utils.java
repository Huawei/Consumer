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

package com.huawei.hms.awareness.codelab;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierQueryRequest;
import com.huawei.hms.kit.awareness.barrier.BarrierQueryResponse;
import com.huawei.hms.kit.awareness.barrier.BarrierStatus;
import com.huawei.hms.kit.awareness.barrier.BarrierStatusMap;
import com.huawei.hms.kit.awareness.barrier.BarrierUpdateRequest;

import java.util.List;

public class Utils {
    private static final String TAG = "Utils";

    public static void addBarrier(Context context, final String label, AwarenessBarrier barrier, PendingIntent pendingIntent) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        // When the status of the registered barrier changes, pendingIntent is triggered.
        // label is used to uniquely identify the barrier. You can query a barrier by label and delete it.
        BarrierUpdateRequest request = builder.addBarrier(label, barrier, pendingIntent).build();
        Awareness.getBarrierClient(context).updateBarriers(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, "add barrier success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        showToast(context, "add barrier failed");
                        Log.e(TAG, "add barrier failed", e);
                    }
                });
    }

    public static void addBatchBarrier(Context context, List<BarrierEntity> barrierList) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        for (BarrierEntity entity : barrierList) {
            builder.addBarrier(entity.getBarrierLabel(), entity.getBarrier(), entity.getPendingIntent());
        }
        Awareness.getBarrierClient(context).updateBarriers(builder.build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, "add barrier success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        showToast(context, "add barrier failed");
                        Log.e(TAG, "add barrier failed", e);
                    }
                });
    }

    public static void deleteBarrier(Context context, PendingIntent... pendingIntents) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        for (PendingIntent pendingIntent : pendingIntents) {
            builder.deleteBarrier(pendingIntent);
        }
        Awareness.getBarrierClient(context).updateBarriers(builder.build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, "delete Barrier success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        showToast(context, "delete barrier failed");
                        Log.e(TAG, "remove Barrier failed", e);
                    }
                });
    }

    public static void deleteBarrier(Context context, String... labels) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        for (String label : labels) {
            builder.deleteBarrier(label);
        }
        Awareness.getBarrierClient(context).updateBarriers(builder.build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, "delete Barrier success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        showToast(context, "delete barrier failed");
                        Log.e(TAG, "remove Barrier failed", e);
                    }
                });
    }

    public static void queryBarrier(Context context, String... labels) {
        BarrierQueryRequest request = BarrierQueryRequest.forBarriers(labels);
        Awareness.getBarrierClient(context).queryBarriers(request)
                .addOnSuccessListener(new OnSuccessListener<BarrierQueryResponse>() {
                    @Override
                    public void onSuccess(BarrierQueryResponse barrierQueryResponse) {
                        BarrierStatusMap statusMap = barrierQueryResponse.getBarrierStatusMap();
                        String barrierLabel = "target barrier label";
                        BarrierStatus barrierStatus = statusMap.getBarrierStatus(barrierLabel);
                        if (barrierStatus != null) {
                            String str = "target barrier status is " + barrierStatus.getPresentStatus();
                            Log.i(TAG, str);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "query barrier failed.", e);
                    }
                });
    }

    private static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
