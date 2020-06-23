package com.huawei.sinktester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PinActivity extends AppCompatActivity {
    private static final String TAG = "SinkTesterPinActivity";
    private String mPinCode;
    private String mDeviceName;
    private TextView[] mPinTextViewArray = new TextView[6];

    private TextView mDeviceNameTextView;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Broadcast received, action: " + action);
            if (SinkTesterService.BROADCAST_ACTION_FINISH_PIN_ACTIVITY.equals(action)) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pin);


        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(SinkTesterService.BROADCAST_ACTION_FINISH_PIN_ACTIVITY);
        registerReceiver(mBroadcastReceiver, broadcastFilter);

        mDeviceNameTextView = findViewById(R.id.device_name_textview);

        Intent intent = getIntent();
        if(intent != null){
            mPinCode = intent.getStringExtra("pincode");
            mDeviceName = intent.getStringExtra("devicename");
            if(mPinCode == null){
                mPinCode = "666666";
                mDeviceName = "test";
            }
            Log.d(TAG, "mPinCode: " + mPinCode + " mDeviceName: " + mDeviceName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPinTextViewArray[0] = findViewById(R.id.pin_1_textview);
        mPinTextViewArray[1] = findViewById(R.id.pin_2_textview);
        mPinTextViewArray[2] = findViewById(R.id.pin_3_textview);
        mPinTextViewArray[3] = findViewById(R.id.pin_4_textview);
        mPinTextViewArray[4] = findViewById(R.id.pin_5_textview);
        mPinTextViewArray[5] = findViewById(R.id.pin_6_textview);

        char[] pinArray = mPinCode.toCharArray();
        for(int i = 0;i < 6;i++){
            mPinTextViewArray[i].setText("" + pinArray[i]);
        }

        mDeviceNameTextView.setText(mDeviceName);
        mDeviceNameTextView.setTextColor(Color.argb(60, 255, 255, 255));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
