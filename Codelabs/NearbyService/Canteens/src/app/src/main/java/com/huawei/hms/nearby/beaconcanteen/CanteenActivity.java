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

package com.huawei.hms.nearby.beaconcanteen;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.nearby.Nearby;
import com.huawei.hms.nearby.StatusCode;
import com.huawei.hms.nearby.beaconcanteen.constants.Constant;
import com.huawei.hms.nearby.beaconcanteen.model.CanteenAdapterInfo;
import com.huawei.hms.nearby.beaconcanteen.model.CanteenNotice;
import com.huawei.hms.nearby.beaconcanteen.permission.PermissionHelper;
import com.huawei.hms.nearby.beaconcanteen.permission.PermissionInterface;
import com.huawei.hms.nearby.beaconcanteen.utils.BluetoothCheckUtil;
import com.huawei.hms.nearby.beaconcanteen.utils.GpsCheckUtil;
import com.huawei.hms.nearby.beaconcanteen.utils.JsonUtils;
import com.huawei.hms.nearby.beaconcanteen.utils.NetCheckUtil;
import com.huawei.hms.nearby.discovery.BleSignal;
import com.huawei.hms.nearby.discovery.Distance;
import com.huawei.hms.nearby.message.GetOption;
import com.huawei.hms.nearby.message.Message;
import com.huawei.hms.nearby.message.MessageEngine;
import com.huawei.hms.nearby.message.MessageHandler;
import com.huawei.hms.nearby.message.MessagePicker;
import com.huawei.hms.nearby.message.Policy;
import com.huawei.hms.nearby.message.StatusCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * App Main Activity
 *
 * @since 2019-12-13
 */
public class CanteenActivity extends AppCompatActivity implements PermissionInterface {
    private static final String TAG = "CanteenActivity";
    private static final int REQUEST_CODE = 8488;
    private static final int THREAD_SLEEP_TIME = 500;
    private Context mContext;
    private ListView listView;
    private TextView searchTipTv;
    private LinearLayout loadingLayout;
    private CanteenAdapter canteenAdapter;
    private MessageEngine messageEngine;
    private List<String> canteenNameList;
    private List<CanteenAdapterInfo> canteenAdapterInfoList;
    private Map<String, CanteenAdapterInfo> canteenAdapterInfoMap;
    private Map<String, String> receivedNoticeMap;
    private MessageHandler mMessageHandler;
    private PermissionHelper mPermissionHelper;
    private boolean isGetPermission = false;
    private BroadcastReceiver stateChangeReceiver =
        new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Start StatusMonitoring.onReceive");
                String action = intent.getAction();
                switch (action) {
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED: {
                        showWarnDialog(Constant.BLUETOOTH_WARN);
                        break;
                    }
                    case BluetoothAdapter.ACTION_STATE_CHANGED: {
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch (blueState) {
                            case BluetoothAdapter.STATE_OFF: {
                                showWarnDialog(Constant.BLUETOOTH_WARN);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    case ConnectivityManager.CONNECTIVITY_ACTION: {
                        operateConnectivityAction(context);
                        break;
                    }
                    case LocationManager.PROVIDERS_CHANGED_ACTION: {
                        Object object = getSystemService(Context.LOCATION_SERVICE);
                        if (!(object instanceof LocationManager)) {
                            showWarnDialog(Constant.GPS_WARN);
                            return;
                        }
                        LocationManager locationManager = (LocationManager) object;
                        if (locationManager == null || !locationManager.isLocationEnabled()) {
                            showWarnDialog(Constant.GPS_WARN);
                        }
                        break;
                    }
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                    default: {
                        break;
                    }
                }
            }
        };

    private void operateConnectivityAction(Context context) {
        Object object = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(object instanceof ConnectivityManager)) {
            showWarnDialog(Constant.NETWORK_WARN);
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) object;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            showWarnDialog(Constant.NETWORK_WARN);
            return;
        }
        int networkType = activeNetworkInfo.getType();
        switch (networkType) {
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_WIFI: {
                break;
            }
            default: {
                showWarnDialog(Constant.NETWORK_WARN);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_canteen);
        boolean isSuccess = requestPermissions(this, this);
        if (!isSuccess) {
            return;
        }
        Log.i(TAG, "requestPermissions success");
        if (!NetCheckUtil.isNetworkAvailable(this)) {
            showWarnDialog(Constant.NETWORK_ERROR);
            return;
        }
        if (!BluetoothCheckUtil.isBlueEnabled()) {
            showWarnDialog(Constant.BLUETOOTH_ERROR);
            return;
        }
        if (!GpsCheckUtil.isGpsEnabled(this)) {
            showWarnDialog(Constant.GPS_ERROR);
            return;
        }
        intView();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (!isGetPermission) {
                    try {
                        Thread.sleep(THREAD_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread sleep error", e);
                    }
                }
                startScanning();
            }
        });
    }

    private boolean requestPermissions(Activity activity, PermissionInterface permissionInterface) {
        mPermissionHelper = new PermissionHelper(activity, permissionInterface);
        mPermissionHelper.requestPermissions();
        return true;
    }

    @Override
    public int getPermissionsRequestCode() {
        return REQUEST_CODE;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
    }

    @Override
    public void requestPermissionsSuccess() {
        isGetPermission = true;
        Log.i(TAG, "requestPermissionsSuccess");
    }

    @Override
    public void requestPermissionsFail() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageEngine != null && mMessageHandler != null) {
            Log.i(TAG, "unget");
            messageEngine.unget(mMessageHandler);
        }
    }

    private void intView() {
        canteenNameList = new ArrayList<>();
        canteenAdapterInfoMap = new HashMap<>();
        canteenAdapterInfoList = new ArrayList<>();
        receivedNoticeMap = new HashMap<>();
        mContext = this;
        messageEngine = Nearby.getMessageEngine(this);
        messageEngine.registerStatusCallback(
                new StatusCallback() {
                    @Override
                    public void onPermissionChanged(boolean isPermissionGranted) {
                        super.onPermissionChanged(isPermissionGranted);
                        Log.d(TAG, "onPermissionChanged:" + isPermissionGranted);
                    }
                });
        listView = findViewById(R.id.lv_canteen);
        canteenAdapter = new CanteenAdapter(mContext, canteenAdapterInfoList);
        listView.setAdapter(canteenAdapter);
        searchTipTv = findViewById(R.id.tv_search_canteen_tip);
        searchTipTv.setText(R.string.search_tip);
        loadingLayout = findViewById(R.id.ll_loading);
    }

    private void init() {
        registerStatusReceiver();
        initNotification();
        initCanteen();
    }

    private void registerStatusReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        mContext.registerReceiver(stateChangeReceiver, intentFilter);
    }

    private void initCanteen() {
        canteenNameList.add(Constant.CANTEEN_A_NAME);
        canteenNameList.add(Constant.CANTEEN_B_NAME);
        canteenNameList.add(Constant.CANTEEN_C_NAME);
    }

    private void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, importance);
        }
    }

    private void showWarnDialog(String content) {
        DialogInterface.OnClickListener onClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warn);
        builder.setIcon(R.mipmap.warn);
        builder.setMessage(content);
        builder.setNegativeButton(getText(R.string.btn_confirm), onClickListener);
        builder.show();
    }

    private void startScanning() {
        Log.i(TAG, "startScanning");
        mMessageHandler =
            new MessageHandler() {
                @Override
                public void onFound(Message message) {
                    super.onFound(message);
                    doOnFound(message);
                }

                @Override
                public void onLost(Message message) {
                    super.onLost(message);
                    doOnLost(message);
                }

                @Override
                public void onDistanceChanged(Message message, Distance distance) {
                    super.onDistanceChanged(message, distance);
                }

                @Override
                public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                    super.onBleSignalChanged(message, bleSignal);
                }
            };
        MessagePicker msgPicker = new MessagePicker.Builder().includeAllTypes().build();
        Policy policy = new Policy.Builder().setTtlSeconds(Policy.POLICY_TTL_SECONDS_INFINITE).build();
        GetOption getOption = new GetOption.Builder().setPicker(msgPicker).setPolicy(policy).build();
        Task<Void> task = messageEngine.get(mMessageHandler, getOption);
        task.addOnFailureListener(
            new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Login failed:", e);
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        int errorStatusCode = apiException.getStatusCode();
                        if (errorStatusCode == StatusCode.STATUS_MESSAGE_AUTH_FAILED) {
                            Toast.makeText(mContext, R.string.configuration_error, Toast.LENGTH_SHORT).show();
                        } else if (errorStatusCode == StatusCode.STATUS_MESSAGE_APP_UNREGISTERED) {
                            Toast.makeText(mContext, R.string.permission_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, R.string.start_get_beacon_message_failed, Toast.LENGTH_SHORT)
                                .show();
                        }
                    } else {
                            Toast.makeText(mContext, R.string.start_get_beacon_message_failed, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
    }

    private void doOnLost(Message message) {
        if (message == null) {
            return;
        }
        String type = message.getType();
        if (type == null) {
            return;
        }
        String messageContent = new String(message.getContent());
        Log.d(TAG, "onLost:" + messageContent + " type:" + type);

        if (type.equalsIgnoreCase(Constant.CANTEEN)) {
            operateOnLostCanteen(messageContent);
        } else if (type.equalsIgnoreCase(Constant.NOTICE)) {
            operateOnLostNotice(messageContent);
        } else {
            return;
        }
    }

    private void operateOnLostNotice(String messageContent) {
        CanteenNotice canteenNotice = (CanteenNotice) JsonUtils.json2Object(messageContent, CanteenNotice.class);
        if (canteenNotice == null || canteenNotice.getCanteenName() == null || canteenNotice.getNotice() == null) {
            return;
        }
        if (!canteenNameList.contains(canteenNotice.getCanteenName())) {
            return;
        }
        Log.i(TAG, "onLost CanteenName:" + canteenNotice.getCanteenName() + " notice:" + canteenNotice.getNotice());
        deleteNotice(canteenNotice.getCanteenName());
    }

    private void operateOnLostCanteen(String messageContent) {
        CanteenAdapterInfo messageCanteen =
                (CanteenAdapterInfo) JsonUtils.json2Object(messageContent, CanteenAdapterInfo.class);
        if (messageCanteen == null) {
            return;
        }
        String canteenName = messageCanteen.getCanteenName();
        if (canteenName == null) {
            return;
        }
        Log.d(TAG, "canteenName:" + canteenName);
        if (!canteenNameList.contains(canteenName) || !canteenAdapterInfoMap.containsKey(canteenName)) {
            return;
        }
        Object object = getSystemService(NOTIFICATION_SERVICE);
        if (!(object instanceof NotificationManager)) {
            return;
        }
        CanteenAdapterInfo info = canteenAdapterInfoMap.get(canteenName);
        NotificationManager manager = (NotificationManager) object;
        manager.cancel(info.getRequestCode());

        canteenAdapterInfoMap.remove(canteenName);
        Iterator<CanteenAdapterInfo> it = canteenAdapterInfoList.iterator();
        while (it.hasNext()) {
            CanteenAdapterInfo canteenAdapterInfo = it.next();
            if (canteenName.equals(canteenAdapterInfo.getCanteenName())) {
                canteenAdapterInfoList.remove(canteenAdapterInfo);
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (canteenAdapterInfoList.isEmpty()) {
                                    loadingLayout.setVisibility(View.GONE);
                                    searchTipTv.setText(R.string.search_tip);
                                }
                                canteenAdapter.setDatas(canteenAdapterInfoList);
                            }
                        });
                break;
            }
        }
    }

    private void doOnFound(Message message) {
        if (message == null) {
            return;
        }
        String type = message.getType();
        if (type == null) {
            return;
        }
        String messageContent = new String(message.getContent());
        Log.d(TAG, "New Message:" + messageContent + " type:" + type);
        if (type.equalsIgnoreCase(Constant.CANTEEN)) {
            operateOnFoundCanteen(messageContent);
        } else if (type.equalsIgnoreCase(Constant.NOTICE)) {
            operateOnFoundNotice(messageContent);
        }
    }

    private void operateOnFoundNotice(String messageContent) {
        CanteenNotice canteenNotice = (CanteenNotice) JsonUtils.json2Object(messageContent, CanteenNotice.class);
        if (canteenNotice == null || canteenNotice.getCanteenName() == null || canteenNotice.getNotice() == null) {
            return;
        }
        if (!canteenNameList.contains(canteenNotice.getCanteenName())) {
            return;
        }
        Log.i(TAG, "onFound CanteenName:" + canteenNotice.getCanteenName()
                + " notice:" + canteenNotice.getNotice());
        pushNotice(canteenNotice.getCanteenName(), canteenNotice.getNotice());
    }

    private void operateOnFoundCanteen(String messageContent) {
        CanteenAdapterInfo canteenAdapterInfo =
                (CanteenAdapterInfo) JsonUtils.json2Object(messageContent, CanteenAdapterInfo.class);
        if (canteenAdapterInfo == null) {
            return;
        }
        String canteenName = canteenAdapterInfo.getCanteenName();
        if (canteenName == null) {
            return;
        }
        Log.d(TAG, "canteenName:" + canteenName);
        if (!canteenNameList.contains(canteenName)) {
            return;
        }

        String notice = "";
        if (receivedNoticeMap.containsKey(canteenName)) {
            notice = receivedNoticeMap.get(canteenName);
        }
        int canteenImage = getCanteenImage(canteenName);
        int requestCode = getRequestCode(canteenName);
        canteenAdapterInfo.setNotice(notice);
        canteenAdapterInfo.setCanteenImage(canteenImage);
        canteenAdapterInfo.setShowNotice(true);
        canteenAdapterInfo.setRequestCode(requestCode);

        canteenAdapterInfoMap.put(canteenName, canteenAdapterInfo);
        canteenAdapterInfoList.add(canteenAdapterInfo);

        sendNotification(Constant.NOTIFICATION_TITLE, Constant.NOTIFICATION_SUBTITLE, canteenName, requestCode);
        runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    searchTipTv.setText(R.string.found_tip);
                    loadingLayout.setVisibility(View.GONE);
                    canteenAdapter.setDatas(canteenAdapterInfoList);
                }
            });
    }

    private int getRequestCode(String canteenName) {
        if (canteenName.equalsIgnoreCase(Constant.CANTEEN_A_NAME)) {
            return Constant.CANTEEN_A_REQUEST_CODE;
        } else if (canteenName.equalsIgnoreCase(Constant.CANTEEN_B_NAME)) {
            return Constant.CANTEEN_B_REQUEST_CODE;
        } else {
            return Constant.CANTEEN_C_REQUEST_CODE;
        }
    }

    private int getCanteenImage(String canteenName) {
        if (canteenName.equalsIgnoreCase(Constant.CANTEEN_A_NAME)) {
            return R.mipmap.canteen_a;
        } else if (canteenName.equalsIgnoreCase(Constant.CANTEEN_B_NAME)) {
            return R.mipmap.canteen_b;
        } else {
            return R.mipmap.canteen_c;
        }
    }

    private void pushNotice(String canteenName, String notice) {
        for (CanteenAdapterInfo mData : canteenAdapterInfoList) {
            if (canteenName.equalsIgnoreCase(mData.getCanteenName())) {
                mData.setNotice(notice);
            }
        }
        receivedNoticeMap.put(canteenName, notice);
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        searchTipTv.setText(R.string.found_tip);
                        loadingLayout.setVisibility(View.GONE);
                        canteenAdapter.setDatas(canteenAdapterInfoList);
                    }
                });
    }

    private void deleteNotice(String canteenName) {
        Log.d(TAG, "deleteNotice:" + canteenName);
        for (CanteenAdapterInfo canteenAdapterInfo : canteenAdapterInfoList) {
            if (canteenName.equalsIgnoreCase(canteenAdapterInfo.getCanteenName())) {
                canteenAdapterInfo.setNotice("");
            }
        }
        receivedNoticeMap.remove(canteenName);
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        searchTipTv.setText(R.string.found_tip);
                        loadingLayout.setVisibility(View.GONE);
                        canteenAdapter.setDatas(canteenAdapterInfoList);
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        Object object = getSystemService(NOTIFICATION_SERVICE);
        if (object instanceof NotificationManager) {
            NotificationManager notificationManager = (NotificationManager) object;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title, String subtitle, String canteenName, int requestCode) {
        Intent broadcastIntent = new Intent(this, CanteenNotificationClickReceiver.class);
        broadcastIntent.putExtra(Constant.CANTEEN_NAME, canteenName);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, requestCode, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Object object = getSystemService(NOTIFICATION_SERVICE);
        if (!(object instanceof NotificationManager)) {
            return;
        }
        NotificationManager manager = (NotificationManager) object;
        Notification notification =
                new NotificationCompat.Builder(this, "canteen")
                        .setContentTitle(title + canteenName)
                        .setContentText(subtitle)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher_android)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_android))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
        manager.notify(requestCode, notification);
    }
}
