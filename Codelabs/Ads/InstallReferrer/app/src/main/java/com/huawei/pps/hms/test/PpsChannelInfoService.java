/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.huawei.pps.hms.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.android.hms.ppskit.IPPSChannelInfoService;

public class PpsChannelInfoService extends Service {
    private static final String TAG = "PpsChannelInfoService";

    private final IPPSChannelInfoService.Stub mBinder = new IPPSChannelInfoService.Stub() {

        @Override
        public String getChannelInfo() throws RemoteException {

            PackageManager pkgmgr = getPackageManager();
            final String callerPkg = getCallerPkgSafe(pkgmgr, Binder.getCallingUid());
            Log.i(TAG, "callerPkg=" + callerPkg);
            SharedPreferences sp = getSharedPreferences(Constants.INSTALL_REFERRER_FILE, Context.MODE_PRIVATE);
            String installReferrer = sp.getString(callerPkg, "");
            Log.i(TAG, "installReferrer=" + installReferrer);
            return installReferrer;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    public static String getCallerPkgSafe(PackageManager packageManager, int uid) {
        if (null == packageManager) {
            return "";
        }
        String pkg = "";
        try {
            pkg = packageManager.getNameForUid(uid);
        } catch (RuntimeException e) {
            Log.w(TAG, "get name for uid error");
        } catch (Exception e) {
            Log.w(TAG, "get name for uid error");
        } catch (Throwable e) {
            Log.w(TAG, "get name for uid error");
        }
        return pkg;
    }
}
