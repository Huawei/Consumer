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

import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;

public class BarrierEntity {
    private String barrierLabel;
    private AwarenessBarrier barrier;
    private PendingIntent pendingIntent;

    public void setBarrierLabel(String barrierLabel) {
        this.barrierLabel = barrierLabel;
    }

    public void setBarrier(AwarenessBarrier barrier) {
        this.barrier = barrier;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public String getBarrierLabel() {
        return barrierLabel;
    }

    public AwarenessBarrier getBarrier() {
        return barrier;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }
}
