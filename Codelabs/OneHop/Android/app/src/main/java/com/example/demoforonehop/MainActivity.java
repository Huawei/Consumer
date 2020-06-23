/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */
package com.example.demoforonehop;

import android.emcom.IOneHopAppCallback;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.onehop.appsdk.HwOneHopSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String MSG_DATA = "data_received";
    private static final String PKG_NAME = "com.example.demoforonehop";
    private static final String DATA_STR = "Test";
    private static final int MSG_RECEIVE = 0;
    private static int count = 0;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHandlder();
        register();
    }

    @Override
    protected void onDestroy() {
        unregister();
        super.onDestroy();
    }

    private void unregister() {
        int retCode = HwOneHopSdk.getInstance().unregisterOneHop(getPackageName(),
                HwOneHopSdk.ONEHOP_DATA_TYPE_BUSINESS_CONTINUITY);
        if (retCode == HwOneHopSdk.ONEHOP_ERR) {
            Log.d(TAG, "unregister failed");
        }
    }

    private void initHandlder() {
        mHandler = new MyHandler(this);
    }

    private void register() {
        new RegisterThread().start();
    }

    private void makeToast(Message message) {
        if (message == null) {
            Log.e(TAG, "the message is null");
            return;
        }
        Bundle data = message.getData();
        if (data == null) {
            Log.e(TAG, "data is null");
            return;
        }
        String text = data.getString(MSG_DATA, "default");
        Log.d(TAG, "get para: " + text);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        TextView para = findViewById(R.id.text);
        para.setText(text);
        para.setHighlightColor(Color.BLUE);

    }

    /**
     * Thread to register onehop
     *
     * @since 2019-11-11
     */
    private class RegisterThread extends Thread {
        @Override
        public void run() {
            int retCode = HwOneHopSdk.getInstance().registerOneHop(PKG_NAME,
                    HwOneHopSdk.ONEHOP_DATA_TYPE_BUSINESS_CONTINUITY, new IOneHopAppCallback.Stub() {
                        @Override
                        public void onOneHopReceived(String s) throws RemoteException {
                            // 调用OneHop同步接口OneHopData，通知当前触碰事件。
                            if (TextUtils.isEmpty(s)) {
                                Log.e(TAG, "onOneHopReceived got wrong para");
                                return;
                            }
                            Log.d(TAG, "param is " + s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean isEvent;
                                if (jsonObject.has(HwOneHopSdk.ONEHOP_RECEIVE_TYPE)) {
                                    int type = jsonObject.getInt(HwOneHopSdk.ONEHOP_RECEIVE_TYPE);
                                    isEvent = type == HwOneHopSdk.ONEHOP_RECEIVE_TYPE_EVENT ? true : false;
                                } else {
                                    Log.e(TAG, "the para is false " + s);
                                    return;
                                }
                                if (!isEvent) {
                                    // pad: receive parameters
                                    handleReceive(s);
                                } else {
                                    // phone: send parameters
                                    handleSend();
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON parsed failed " + e.getMessage());
                            }
                        }
                    });
            if (retCode == HwOneHopSdk.ONEHOP_ERR) {
                Log.e(TAG, "register OneHop error");
            }
        }
    }

    private void handleSend() {
        Map<String, Object> map = new HashMap<>();
        map.put(DATA_STR, "Hello World! " + (count++));
        int retCode = HwOneHopSdk.getInstance().oneHopSend(getPackageName(),
                new JSONObject(map));
        if (retCode == HwOneHopSdk.ONEHOP_ERR) {
            Log.e(TAG, "send failed");
        }
    }

    private void handleReceive(String param) {
        Message message = new Message();
        message.what = MSG_RECEIVE;
        Bundle bundle = new Bundle();
        bundle.putString(MSG_DATA, param);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mActivity.get() == null) {
                Log.e(TAG, "current activity is null");
                return;
            }
            MainActivity activity = mActivity.get();
            switch (msg.what) {
                case MSG_RECEIVE:
                    Log.d(TAG, "receive");
                    activity.makeToast(msg);
                    break;
                default:
                    Log.e(TAG, "get unknown message " + msg.what);
                    break;
            }
        }
    }
}