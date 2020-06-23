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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Network Check Util
 *
 * @since 2019-12-13
 */
public class NetCheckUtil {
    private static final String TAG = "NetCheckUtil";

    private NetCheckUtil() {}

    /**
     * Check Is Network Available
     *
     * @param context Context
     * @return true:Network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean mobileConnection = isMobileConnection(context);
        boolean wifiConnection = isWifiConnection(context);
        if (!mobileConnection && !wifiConnection) {
            Log.i(TAG, "No network available");
            return false;
        }
        return true;
    }

    /**
     * Is Mobile Connection
     *
     * @param context Context
     * @return true:Mobile is connection
     */
    public static boolean isMobileConnection(Context context) {
        Object object = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(object instanceof ConnectivityManager)) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) object;
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Is WIFI Connection
     *
     * @param context Context
     * @return true:wifi is connection
     */
    public static boolean isWifiConnection(Context context) {
        Object object = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(object instanceof ConnectivityManager)) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) object;
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
