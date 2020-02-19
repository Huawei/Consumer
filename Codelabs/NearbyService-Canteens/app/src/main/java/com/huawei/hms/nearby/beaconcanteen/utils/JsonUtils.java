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
 */

package com.huawei.hms.nearby.beaconcanteen.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * JsonUtils
 *
 * @since 2019-12-27
 */
public class JsonUtils {
    private static final String TAG = "JsonUtils";

    private static Gson sGsonInstance;

    private static Gson getGsonInstance() {
        if (sGsonInstance == null) {
            synchronized (JsonUtils.class) {
                if (sGsonInstance == null) {
                    sGsonInstance = getGsonBuilder().create();
                }
            }
        }
        return sGsonInstance;
    }

    private static GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeSpecialFloatingPointValues()
                .disableHtmlEscaping()
                .setLenient();
    }

    /**
     * JSON to Object
     *
     * @param json        json String
     * @param objectClass objectClass
     * @return Object
     */
    public static Object json2Object(String json, Class objectClass) {
        if (TextUtils.isEmpty(json) || objectClass == null) {
            return null;
        }
        try {
            return getGsonInstance().fromJson(json, objectClass);
        } catch (JsonParseException e) {
            return null;
        }
    }

    /**
     * Object to Json
     *
     * @param object object
     * @return Json String
     */
    public static String object2Json(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return getGsonInstance().toJson(object);
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.e(TAG, "JsonParseException:" + e.getMessage());
            return null;
        }
    }
}
