/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.demo.log;

import android.util.Log;

public class LogUtil {
    private static final String LOG_TAG = "identity_demo";

    public static void i(String tag, String msg) {
        Log.i(LOG_TAG, tag + " : " + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(LOG_TAG, tag + " : " + msg);
    }
}
