/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.huawei.pps.hms.test.oaid;

import android.content.Context;
import android.util.Log;

import com.huawei.hms.ads.identifier.AdvertisingIdClient;

import java.io.IOException;

public class OaidSdkUtil {
    private static final String TAG = "OaidSdkUtil";

    public static void getOaid(Context context, OaidCallback callback) {
        if (null == context || null == callback) {
            Log.e(TAG, "invalid input param");
            return;
        }
        try {
            //Get advertising id information. Do not call this method in the main thread.
            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (null != info) {
                callback.onSuccuss(info.getId(), info.isLimitAdTrackingEnabled());
            } else {
                callback.onFail("oaid is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "getAdvertisingIdInfo IOException");
            callback.onFail("getAdvertisingIdInfo IOException");
        }
    }

}
