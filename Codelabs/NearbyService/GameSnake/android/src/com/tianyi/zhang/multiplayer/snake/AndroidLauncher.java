/*
Copyright 2018 Tianyi Zhang

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


    2020.3.15 - Replaced Kryonet by HMS Nearby Service.
    2020.3.15 - Add Chinese version.
    2020.3.15 - Add Game speed selection.
                Huawei Technologies Co.,Ltd. <nearbyservice@huawei.com>
 */


package com.tianyi.zhang.multiplayer.snake;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tianyi.zhang.multiplayer.snake.App;
//import com.huawei.hms.nearby.Nearby;
//import com.huawei.hms.nearby.StatusCode;
//import com.huawei.hms.nearby.discovery.BroadcastOption;
//import com.huawei.hms.nearby.discovery.ConnectCallback;
//import com.huawei.hms.nearby.discovery.ConnectInfo;
//import com.huawei.hms.nearby.discovery.ConnectResult;
//import com.huawei.hms.nearby.discovery.DiscoveryEngine;
//import com.huawei.hms.nearby.discovery.Policy;
//import com.huawei.hms.nearby.discovery.ScanEndpointCallback;
//import com.huawei.hms.nearby.discovery.ScanEndpointInfo;
//import com.huawei.hms.nearby.discovery.ScanOption;
//import com.huawei.hms.nearby.transfer.Data;
//import com.huawei.hms.nearby.transfer.DataCallback;
//import com.huawei.hms.nearby.transfer.TransferEngine;
//import com.huawei.hms.nearby.transfer.TransferStateUpdate;

public class AndroidLauncher extends AndroidApplication {
//    private TransferEngine mTransferEngine = null;
//    private DiscoveryEngine mDiscoveryEngine = null;
    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,};
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private App _app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("wmq", "onCreate");
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);

//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//        mManager.createGroup(mChannel, null);

        Context context = getApplicationContext();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        _app = new App(this);
        initialize(_app, config);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wmq", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wmq", "onResume");
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wmq", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wmq", "onStop");
    }

    @Override
    protected void onDestroy () {
        Log.d("wmq", "onDestroy");
        //mManager.removeGroup(mChannel, null);
        _app.dispose();
        super.onDestroy();
        System.exit(0);
    }


}
