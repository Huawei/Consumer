package com.huawei.sinktester;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.castpluskit.Constant;
import com.huawei.castpluskit.DisplayInfo;
import com.huawei.castpluskit.Event;
import com.huawei.castpluskit.IEventListener;
import com.huawei.castpluskit.PlayerClient;




public class AlertActivity extends Activity implements View.OnFocusChangeListener {
    private static final String TAG = "AlertActivity";
    private TextView mAlertTextView;
    private Button mRejectButton;
    private Button mConfirmAlwaysButton;
    private Button mConfirmOnceButton;
    private String mPinCode;
    private String mDeviceName;
    private Drawable mBackgroundDrawable;
    private Drawable mButtonFocusDrawable;
    private Drawable mButtonUnfocusDrawable;
    private CallbackHandler mCallbackHandler;
    private PlayerClient mPlayerClient;

    private IEventListener mCallback = new IEventListener.Stub() {
        public boolean onEvent(Event event) {
            int eventId = event.getEventId();
            Log.e(TAG, "eventId: " + eventId);
            Message msg = mCallbackHandler.obtainMessage();
            msg.what = eventId;
            msg.obj = event;
            msg.sendToTarget();
            return true;
        }

        public boolean onDisplayEvent(int eventId, DisplayInfo info) {
            Message msg = mCallbackHandler.obtainMessage();
            msg.what = eventId;
            Bundle bundle = new Bundle();
            bundle.putString("pincode", info.getPinCode());
            msg.setData(bundle);
            msg.sendToTarget();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //去掉信息栏，
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_alert);

        mAlertTextView = findViewById(R.id.alert_textview);
        mRejectButton = findViewById(R.id.reject_button);
        mConfirmAlwaysButton = findViewById(R.id.confirm_button_always);
        mConfirmOnceButton = findViewById(R.id.confirm_button_once);

        mBackgroundDrawable = getDrawable(R.drawable.svgdialog);
        mButtonFocusDrawable = getDrawable(R.drawable.svgbutton_selected);
        mButtonUnfocusDrawable = getDrawable(R.drawable.svgbutton_unselected);

        getWindow().setBackgroundDrawable(mBackgroundDrawable);
        getWindow().setLayout(Utils.dp2px(this, 620), Utils.dp2px(this, 162)); //420 152  -> 620 162
        getWindow().setGravity(Gravity.CENTER);
        Intent intent = getIntent();
        if (intent != null) {
            mPinCode = intent.getStringExtra("pincode");
            mDeviceName = intent.getStringExtra("devicename");
        }

        mAlertTextView.setText(mDeviceName + "想要连接您的电视");

        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent disconnectIntent = new Intent();
                disconnectIntent.setAction(SinkTesterService.BROADCAST_ACTION_REJECT_CONNECTION);
                sendBroadcast(disconnectIntent);
                finish();
                Log.d(TAG, "onClick: reject");
            }
        });

        mConfirmAlwaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AlertActivity.this, PinActivity.class);
                intent2.putExtra("pincode", mPinCode);
                intent2.putExtra("devicename", mDeviceName);
                startActivity(intent2);
                Intent connectIntent = new Intent();
                connectIntent.setAction(SinkTesterService.BROADCAST_ACTION_ALLOW_CONNECTION_ALWAYS);
                sendBroadcast(connectIntent);
                finish();
                Log.d(TAG, "onClick: always");
            }
        });

        mConfirmOnceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AlertActivity.this, PinActivity.class);
                intent3.putExtra("pincode", mPinCode);
                intent3.putExtra("devicename", mDeviceName);
                startActivity(intent3);
                Intent connectIntent = new Intent();
                connectIntent.setAction(SinkTesterService.BROADCAST_ACTION_ALLOW_CONNECTION_ONCE);
                sendBroadcast(connectIntent);
                finish();
                Log.d(TAG, "onClick: once");
            }
        });

        mRejectButton.setOnFocusChangeListener(this);
        mConfirmAlwaysButton.setOnFocusChangeListener(this);
        mConfirmOnceButton.setOnFocusChangeListener(this);


        mCallbackHandler = new CallbackHandler(getMainLooper());
        mPlayerClient = PlayerClient.getInstance();
        mPlayerClient.registerCallback(mCallback);
    }

    @Override
    public void onBackPressed() {
        Intent discconnectIntent = new Intent();
        discconnectIntent.setAction(SinkTesterService.BROADCAST_ACTION_REJECT_CONNECTION);
        sendBroadcast(discconnectIntent);
        super.onBackPressed();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange() called, view: " + v.getId() + "hasFocus: " + hasFocus);
        if ((v.getId() != R.id.confirm_button_always) && (v.getId() != R.id.reject_button)&& (v.getId() != R.id.confirm_button_once)) {
            return;
        }
        if (hasFocus) {
            v.setBackground(mButtonFocusDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Button) v).setTextColor(getColor(R.color.colorTextColorSelected));
            } else {
                ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColorSelected));
            }
        } else {
            v.setBackground(mButtonUnfocusDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Button) v).setTextColor(getColor(R.color.colorTextColor));
            } else {
                ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColor));
            }
        }
    }

    private class CallbackHandler extends Handler {
        public CallbackHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) return;
            Log.d(TAG, "msg " + msg.what);
            switch (msg.what) {
                case Constant.EVENT_ID_DEVICE_DISCONNECTED:
                case Constant.EVENT_ID_PIN_CODE_SHOW_FINISH:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
