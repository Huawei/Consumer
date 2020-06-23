/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Main application.
 *
 * @since 2019-10-21
 */
public class CaasKitApplication extends Application {
    private static final String TAG = "CaasKitApplication";

    private static CaasKitApplication sApplication;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate.");
        super.onCreate();
        setApp(this);
    }

    public static CaasKitApplication getInstance() {
        return sApplication;
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

    private static synchronized void setApp(CaasKitApplication app) {
        sApplication = app;
    }
}
