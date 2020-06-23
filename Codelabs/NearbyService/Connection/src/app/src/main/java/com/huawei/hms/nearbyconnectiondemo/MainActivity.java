/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.huawei.hms.nearbyconnectiondemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.huawei.hms.nearby.Nearby;
import com.huawei.hms.nearby.StatusCode;
import com.huawei.hms.nearby.discovery.BroadcastOption;
import com.huawei.hms.nearby.discovery.ConnectCallback;
import com.huawei.hms.nearby.discovery.ConnectInfo;
import com.huawei.hms.nearby.discovery.ConnectResult;
import com.huawei.hms.nearby.discovery.DiscoveryEngine;
import com.huawei.hms.nearby.discovery.Policy;
import com.huawei.hms.nearby.discovery.ScanEndpointCallback;
import com.huawei.hms.nearby.discovery.ScanEndpointInfo;
import com.huawei.hms.nearby.discovery.ScanOption;
import com.huawei.hms.nearby.transfer.Data;
import com.huawei.hms.nearby.transfer.DataCallback;
import com.huawei.hms.nearby.transfer.TransferEngine;
import com.huawei.hms.nearby.transfer.TransferStateUpdate;
import com.huawei.hms.nearbyconnectiondemo.utils.ToastUtil;
import com.huawei.hms.nearbyconnectiondemo.utils.permission.PermissionHelper;
import com.huawei.hms.nearbyconnectiondemo.utils.permission.PermissionInterface;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class
 *
 * @since 2020-01-13
 */
public class MainActivity extends AppCompatActivity implements PermissionInterface, View.OnClickListener {
    private static final int TIMEOUT_MILLISECONDS = 10000;
    private static final String TAG = "Nearby Connection Demo";

    private TransferEngine mTransferEngine = null;
    private DiscoveryEngine mDiscoveryEngine = null;

    private PermissionHelper mPermissionHelper;

    private EditText myNameEt;
    private EditText friendNameEt;
    private EditText msgEt;

    private ListView messageListView;

    private List<MessageBean> msgList;

    private ChatAdapter adapter;

    private Button sendBtn;
    private Button connectBtn;

    private int connectTaskResult;

    private String myNameStr;
    private String friendNameStr;
    private String myServiceId;
    private String mEndpointId;
    private String msgStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        initView();
        sendBtn.setEnabled(false);
        msgEt.setEnabled(false);
    }

    private void initView() {
        myNameEt = findViewById(R.id.et_my_name);
        friendNameEt = findViewById(R.id.et_friend_name);
        msgEt = findViewById(R.id.et_msg);
        connectBtn = findViewById(R.id.btn_connect);
        sendBtn = findViewById(R.id.btn_send);
        connectBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);

        messageListView = findViewById(R.id.lv_chat);
        msgList = new ArrayList<>();
        adapter = new ChatAdapter(this, msgList);
        messageListView.setAdapter(adapter);
        connectTaskResult = StatusCode.STATUS_ENDPOINT_UNKNOWN;
    }

    /**
     * Handle timeout function
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            handler.removeMessages(0);
            if (connectTaskResult != StatusCode.STATUS_SUCCESS) {
                ToastUtil.showShortToastTop("Connection timeout, make sure your friend is ready and try again.");
                if (myNameStr.compareTo(friendNameStr) > 0) {
                    mDiscoveryEngine.stopScan();
                } else {
                    mDiscoveryEngine.stopBroadcasting();
                }
                myNameEt.setEnabled(true);
                friendNameEt.setEnabled(true);
                connectBtn.setEnabled(true);
            }
        }
    };

    private void requestPermissions() {
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();
    }

    @Override
    public int getPermissionsRequestCode() {
        return 10086;
    }

    /**
     * Permission for this app
     */
    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
    }

    @Override
    public void requestPermissionsSuccess() {
    }

    @Override
    public void requestPermissionsFail() {
        Toast.makeText(this, R.string.error_missing_permissions, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                if (checkName()) {
                    return;
                }
                connect(view);
                handler.sendEmptyMessageDelayed(0, TIMEOUT_MILLISECONDS);
                break;
            case R.id.btn_send:
                if (checkMessage()) {
                    return;
                }
                sendMessage();
                break;
            default:
                break;
        }
    }

    /**
     * Check input message
     */
    private boolean checkMessage() {
        if (TextUtils.isEmpty(msgEt.getText())) {
            ToastUtil.showShortToastTop("Please input data you want to send.");
            return true;
        }
        return false;
    }

    /**
     * Check input name
     */
    private boolean checkName() {
        if (TextUtils.isEmpty(myNameEt.getText())) {
            ToastUtil.showShortToastTop("Please input your name.");
            return true;
        }
        if (TextUtils.isEmpty(friendNameEt.getText())) {
            ToastUtil.showShortToastTop("Please input your friend's name.");
            return true;
        }
        if (TextUtils.equals(myNameEt.getText().toString(), friendNameEt.getText().toString())) {
            ToastUtil.showShortToastTop("Please input two different names.");
            return true;
        }
        friendNameStr = friendNameEt.getText().toString();
        myNameStr = myNameEt.getText().toString();
        getServiceId();
        return false;
    }

    /**
     * Send message function
     */
    private void sendMessage() {
        msgStr = msgEt.getText().toString();
        Data data = Data.fromBytes(msgStr.getBytes(Charset.defaultCharset()));
        Log.d(TAG, "myEndpointId " + mEndpointId);
        mTransferEngine.sendData(mEndpointId, data);
        MessageBean item = new MessageBean();
        item.setMyName(myNameStr);
        item.setFriendName(friendNameStr);
        item.setMsg(msgStr);
        item.setSend(true);
        msgList.add(item);
        adapter.notifyDataSetChanged();
        msgEt.setText("");
        messageListView.setSelection(messageListView.getBottom());
    }

    /**
     * Receive message function
     */
    private void receiveMessage(Data data) {
        msgStr = new String(data.asBytes());
        MessageBean item = new MessageBean();
        item.setMyName(myNameStr);
        item.setFriendName(friendNameStr);
        item.setMsg(msgStr);
        item.setSend(false);
        msgList.add(item);
        adapter.notifyDataSetChanged();
        messageListView.setSelection(messageListView.getBottom());
    }

    private void connect(View view) {
        ToastUtil.showShortToastTop("Connecting to your friend.");
        connectBtn.setEnabled(false);
        myNameEt.setEnabled(false);
        friendNameEt.setEnabled(false);
        Context context = getApplicationContext();
        mDiscoveryEngine = Nearby.getDiscoveryEngine(context);
        try {
            if (myNameStr.compareTo(friendNameStr) > 0) {
                doStartScan(view);
            } else {
                doStartBroadcast(view);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "remote exception." + e.getMessage());
        }
    }

    public void doStartBroadcast(View view) throws RemoteException {
        BroadcastOption.Builder advBuilder = new BroadcastOption.Builder();
        advBuilder.setPolicy(Policy.POLICY_STAR);
        mDiscoveryEngine.startBroadcasting(myNameStr, myServiceId, mConnCb, advBuilder.build());
    }

    private void getServiceId() {
        if (myNameStr.compareTo(friendNameStr) > 0) {
            myServiceId = myNameStr + friendNameStr;
        } else {
            myServiceId = friendNameStr + myNameStr;
        }
    }

    public void doStartScan(View view) throws RemoteException {
        ScanOption.Builder discBuilder = new ScanOption.Builder();
        discBuilder.setPolicy(Policy.POLICY_STAR);
        mDiscoveryEngine.startScan(myServiceId, mDiscCb, discBuilder.build());
    }

    private ConnectCallback mConnCb =
            new ConnectCallback() {
                @Override
                public void onEstablish(String endpointId, ConnectInfo connectionInfo) {
                    mTransferEngine = Nearby.getTransferEngine(getApplicationContext());
                    mEndpointId = endpointId;
                    mDiscoveryEngine.acceptConnect(endpointId, mDataCb);
                    ToastUtil.showShortToastTop("Let's chat!");
                    sendBtn.setEnabled(true);
                    msgEt.setEnabled(true);
                    connectBtn.setEnabled(false);
                    connectTaskResult = StatusCode.STATUS_SUCCESS;
                    if (myNameStr.compareTo(friendNameStr) > 0) {
                        mDiscoveryEngine.stopScan();
                    } else {
                        mDiscoveryEngine.stopBroadcasting();
                    }
                }

                @Override
                public void onResult(String endpointId, ConnectResult resolution) {
                    mEndpointId = endpointId;
                }

                @Override
                public void onDisconnected(String endpointId) {
                    ToastUtil.showShortToastTop("Disconnect.");
                    connectTaskResult = StatusCode.STATUS_NOT_CONNECTED;
                    sendBtn.setEnabled(false);
                    connectBtn.setEnabled(true);
                    msgEt.setEnabled(false);
                    myNameEt.setEnabled(true);
                    friendNameEt.setEnabled(true);
                }
            };

    private ScanEndpointCallback mDiscCb =
            new ScanEndpointCallback() {
                @Override
                public void onFound(String endpointId, ScanEndpointInfo discoveryEndpointInfo) {
                    mEndpointId = endpointId;
                    mDiscoveryEngine.requestConnect(myNameStr, mEndpointId, mConnCb);
                }

                @Override
                public void onLost(String endpointId) {
                    Log.d(TAG, "Nearby Connection Demo app: Lost endpoint: " + endpointId);
                }
            };

    private DataCallback mDataCb =
            new DataCallback() {
                @Override
                public void onReceived(String string, Data data) {
                    receiveMessage(data);
                }

                @Override
                public void onTransferUpdate(String string, TransferStateUpdate update) {
                }
            };
}
