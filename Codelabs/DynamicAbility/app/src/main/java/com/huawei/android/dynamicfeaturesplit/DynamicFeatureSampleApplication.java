/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.android.dynamicfeaturesplit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.huawei.hms.feature.dynamicinstall.FeatureCompat;

/**
 * Dynamic feature sample application.
 *
 */
public class DynamicFeatureSampleApplication extends Application {
    /**
     * The constant TAG.
     */
    public static final String TAG = DynamicFeatureSampleApplication.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            FeatureCompat.install(base);
        } catch (Exception e) {
            Log.w(TAG, "", e);
        }
    }
}
