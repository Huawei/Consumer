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

import android.util.SparseArray;

import com.huawei.hms.kit.awareness.barrier.BehaviorBarrier;
import com.huawei.hms.kit.awareness.barrier.TimeBarrier;
import com.huawei.hms.kit.awareness.status.CapabilityStatus;

public class Constant {
    public static final SparseArray<String> TIME_DESCRIPTION_MAP = new SparseArray<>();

    static {
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKDAY, "Today is weekday.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKEND, "Today is weekend.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_HOLIDAY, "Today is holiday.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NOT_HOLIDAY, "Today is not holiday.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_MORNING, "Good morning.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_AFTERNOON, "Good afternoon.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_EVENING, "Good evening.");
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NIGHT, "Good night.");
    }

    public static final SparseArray<String> BEHAVIOR_DESCRIPTION_MAP = new SparseArray<>();

    static {
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_IN_VEHICLE, "in vehicle");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_ON_BICYCLE, "on bicycle");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_ON_FOOT, "on foot");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_STILL, "still");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_UNKNOWN, "unknown");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_WALKING, "walking");
        BEHAVIOR_DESCRIPTION_MAP.put(BehaviorBarrier.BEHAVIOR_RUNNING, "running");
    }

    static final SparseArray<String> CAPABILITIES_DESCRIPTION_MAP = new SparseArray<>();

    static {
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_HEADSET, "headset");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_LOCATION_CAPTURE, "location capture");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_LOCATION_NORMAL_BARRIER, "location barrier");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_LOCATION_LOW_POWER_BARRIER, "low power location barrier");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_BEHAVIOR, "behavior");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_TIME, "time");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_AMBIENT_LIGHT, "ambient light");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_WEATHER, "weather");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_BEACON, "beacon");
        CAPABILITIES_DESCRIPTION_MAP.put(CapabilityStatus.AWA_CAP_CODE_INCAR_BLUETOOTH, "bluetooth");
    }
}
