package com.huawei.caaskitdemo.caaskit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.caaskitdemo.CaasKitApplication;
import com.huawei.dmsdpsdk.localapp.HwDmsdpService;

public class DeviceDiscoverReceiver extends BroadcastReceiver {
    private static final String TAG = "DmsdpStartDiscoverReceiver";
    private Context mApplicationContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive.");
        mApplicationContext = CaasKitApplication.getContext();
        HwDmsdpService.init(mApplicationContext, new VirtualCameraListener());
    }
}
