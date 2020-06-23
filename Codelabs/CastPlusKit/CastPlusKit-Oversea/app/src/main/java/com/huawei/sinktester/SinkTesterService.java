package com.huawei.sinktester;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import com.huawei.castpluskit.AuthInfo;
import com.huawei.castpluskit.ConnectRequestChoice;
import com.huawei.castpluskit.Constant;
import com.huawei.castpluskit.DeviceInfo;
import com.huawei.castpluskit.DisplayInfo;
import com.huawei.castpluskit.Event;
import com.huawei.castpluskit.HiSightCapability;
import com.huawei.castpluskit.IEventListener;
import com.huawei.castpluskit.PlayerClient;
import com.huawei.castpluskit.ProjectionDevice;
import com.huawei.castpluskit.TrackControl;

import static com.huawei.sinktester.PlayActivity.mHiView;

public class SinkTesterService extends Service {
    private static final String TAG = "SinkTesterService";

    public static final String BROADCAST_ACTION_DISCONNECT = "castplus.intent.action.disconnect";
    public static final String BROADCAST_ACTION_SET_DISCOVERABLE = "castplus.intent.action" +
            ".setdiscoverable";
    public static final String BROADCAST_ACTION_SET_AUTH_MODE = "castplus.intent.action" +
            ".setauthmode";
    public static final String BROADCAST_ACTION_REJECT_CONNECTION = "castplus.intent.action" +
            ".rejectconnection";
    public static final String BROADCAST_ACTION_ALLOW_CONNECTION_ALWAYS = "castplus.intent.action" +
            ".allowconnection"+".always";

    public static final String BROADCAST_ACTION_ALLOW_CONNECTION_ONCE = "castplus.intent.action" +
            ".allowconnection"+".once";

    public static final String BROADCAST_ACTION_PLAY = "castplus.intent.action" +
            ".play";
    public static final String BROADCAST_ACTION_PAUSE = "castplus.intent.action" +
            ".pause";

    public static final String BROADCAST_ACTION_FINISH_PIN_ACTIVITY = "castplus.intent.action.finishpinactivity";

    public static final String BROADCAST_ACTION_FINISH_PLAY_ACTIVITY = "castplus.intent.action.finishplayactivity";

    public static final String BROADCAST_ACTION_NETWORK_QUALITY = "castplus.intent.action.networkquality";

    private static final int OPTIMIZATION_TAG_CODEC_CONFIGURE_FLAG = 1;
    private static final int OPTIMIZATION_TAG_MEDIA_FORMAT_INTEGER = 2;
    private static final int OPTIMIZATION_TAG_MEDIA_FORMAT_FLOAT = 4;
    private static final int OPTIMIZATION_TAG_MEDIA_FORMAT_LONG = 8;
    private static final int OPTIMIZATION_TAG_MEDIA_FORMAT_STRING = 16;

    private boolean mIsPinShown = false;

    public ProjectionDevice mProjectionDevice; //....
    private boolean mIsDiscoverable;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private PlayerClient mPlayerClient;
    private CallbackHandler mCallbackHandler;
    private boolean mCastServiceReady = false;

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

        public boolean onDisplayEvent(int eventId, DisplayInfo displayInfo) {
            Log.e(TAG, "handleDisplayEvent: " + eventId);
            Message msg = mCallbackHandler.obtainMessage();
            msg.what = eventId;
            msg.obj = displayInfo;
            msg.sendToTarget();
            return true;
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Broadcast received, action: " + action);
            if (BROADCAST_ACTION_DISCONNECT.equals(action)) {
                disconnectDevice();
            } else if (BROADCAST_ACTION_SET_DISCOVERABLE.equals(action)) {
                mIsDiscoverable = intent.getBooleanExtra("discoverable", true);
                setDiscoverable(mIsDiscoverable);
            } else if (BROADCAST_ACTION_SET_AUTH_MODE.equals(action)) {
                boolean needPassword = intent.getBooleanExtra("needpassword", false);
                String password = intent.getStringExtra("password");
                boolean isNewPassword = intent.getBooleanExtra("isnewpassword", false);
                setAuthMode(needPassword, password, isNewPassword);
            } else if (BROADCAST_ACTION_ALLOW_CONNECTION_ALWAYS.equals(action)) {
                if (mPlayerClient != null) {
                    mPlayerClient.setConnectRequestChooseResult(new ConnectRequestChoice(Constant.CONNECT_REQ_CHOICE_ALWAYS, mProjectionDevice));
                } else {
                    Log.e(TAG, "mPlayerClient is null.");
                }
            }else if (BROADCAST_ACTION_ALLOW_CONNECTION_ONCE.equals(action)) {
                if (mPlayerClient != null) {
                    mPlayerClient.setConnectRequestChooseResult(new ConnectRequestChoice(Constant.CONNECT_REQ_CHOICE_ONCE, mProjectionDevice));
                } else {
                    Log.e(TAG, "mPlayerClient is null.");
                }
            }
            else if (BROADCAST_ACTION_REJECT_CONNECTION.equals(action)) {
                if (mPlayerClient != null) {
                    mPlayerClient.setConnectRequestChooseResult(new ConnectRequestChoice(Constant.CONNECT_REQ_CHOICE_REJECT, mProjectionDevice));
                } else {
                    Log.e(TAG, "mPlayerClient is null.");
                }
            } else if (BROADCAST_ACTION_PAUSE.equals(action)) {
                pause();
            } else if (BROADCAST_ACTION_PLAY.equals(action)) {
                startPlay();
            } else if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action)) {
                setDiscoverable(mIsDiscoverable);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate called().");

        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(BROADCAST_ACTION_DISCONNECT);
        broadcastFilter.addAction(BROADCAST_ACTION_SET_DISCOVERABLE);
        broadcastFilter.addAction(BROADCAST_ACTION_SET_AUTH_MODE);
        broadcastFilter.addAction(BROADCAST_ACTION_REJECT_CONNECTION);
        broadcastFilter.addAction(BROADCAST_ACTION_ALLOW_CONNECTION_ALWAYS);
        broadcastFilter.addAction(BROADCAST_ACTION_ALLOW_CONNECTION_ONCE);
        broadcastFilter.addAction(BROADCAST_ACTION_PLAY);
        broadcastFilter.addAction(BROADCAST_ACTION_PAUSE);
        broadcastFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        registerReceiver(mBroadcastReceiver, broadcastFilter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mIsDiscoverable = SharedPreferenceUtil.getDiscoverable(this);
        mContext = this;

        mCallbackHandler = new CallbackHandler(getMainLooper());
        mPlayerClient = PlayerClient.getInstance();
        mPlayerClient.registerCallback(mCallback);
        mPlayerClient.init(mContext);
    }

    /**
     * onBind is the virtual method of Service, and must be implemented.
     * If null is returned, the client cannot connect to the service
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind in");
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand in");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind in");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy in");
        unregisterReceiver(mBroadcastReceiver);
        if (mPlayerClient != null) {
            mPlayerClient.unregisterCallback(mCallback);
            mPlayerClient.deinit();
        } else {
            Log.d(TAG, "mPlayerClient is null.");
        }

        Intent intent = new Intent(mContext, SinkTesterService.class);
        startService(intent);
    }

    private void startPlay() {
        if(!mCastServiceReady || !PlayActivity.isSurfaceReady) {
            return;
        }
        if (mPlayerClient != null) {
            mPlayerClient.setHiSightSurface(mHiView.getHolder().getSurface());
            mPlayerClient.play(new TrackControl(mProjectionDevice.getDeviceId()));
            mCastServiceReady = false;
        } else {
            Log.e(TAG, "mPlayerClient is null.");
        }
    }

    private void pause() {
        if (mPlayerClient != null) {
            Log.d(TAG, "pause() called.");
            mPlayerClient.pause(new TrackControl(mProjectionDevice.getDeviceId()));
        }
    }

    private void startAlertActivity(String pinCode, String deviceName) {
        Log.d(TAG, "startAlertActivity() called.");
        Intent intent = new Intent(mContext, AlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pincode", pinCode);
        intent.putExtra("devicename", deviceName);
        startActivity(intent);
    }







    private void startPinActivity(String pinCode, String deviceName) {
        Log.d(TAG, "startPinActivity() called.");
        Intent intent = new Intent(SinkTesterService.this, PinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pincode", pinCode);
        intent.putExtra("devicename", deviceName);
        startActivity(intent);
    }

    private void startPlayActivity() {
        Log.d(TAG, "startPlayActivity() called.");
        Intent intent = new Intent(mContext, PlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }





    private void sendBroadcastToActivity(String broadcastAction) {
        Log.d(TAG, "sendBroadcastToActivity() called.");
        Intent intent = new Intent();
        intent.setAction(broadcastAction);
        sendBroadcast(intent);
    }

    private void setAuthMode(boolean needPassword, String password, boolean isNewPassword) {
        Log.d(TAG, "setAuthMode() called.");
        AuthInfo authInfo = null;
        if (needPassword) {
            //TODO check password.
            Log.d(TAG, "password: $" + password + "$");
            authInfo = new AuthInfo(AuthInfo.AUTH_MODE_PWD, password, isNewPassword);
        } else {
            authInfo = new AuthInfo(AuthInfo.AUTH_MODE_GENERIC);
        }
        if (mPlayerClient != null) {
            mPlayerClient.setAuthMode(authInfo);
        } else {
            Log.e(TAG, "mPlayerClient is null.");
        }
    }



    private DeviceInfo getDeviceInfo() {
        Log.d(TAG, "getDeviceInfo() called.");
        String deviceName = "CastPlusTestDevice";
        if (mBluetoothAdapter != null) {
            deviceName = mBluetoothAdapter.getName();
        }

        return new DeviceInfo(deviceName, DeviceInfo.TYPE_TV);
    }


    private void setDiscoverable(boolean isDiscoverable) {
        Log.d(TAG, "setDiscoverable() called.");
        DeviceInfo deviceInfo = getDeviceInfo();
        if (mPlayerClient != null) {
            mPlayerClient.setDiscoverable(isDiscoverable, deviceInfo);
        } else {
            Log.e(TAG, "mPlayerClient is null.");
        }
    }

    private void setCapability(Size screenSize, Size videoSize, int framerate, int mode) {
        Log.d(TAG, "setCapability() called.");
        int screenWidth = screenSize.getWidth();
        int screenHeight = screenSize.getHeight();
        int videoWidth = videoSize.getWidth();
        int videoHeight = videoSize.getHeight();

        HiSightCapability capability = new HiSightCapability(screenWidth, screenHeight, videoWidth, videoHeight);
        capability.setVideoFps(framerate);
        //Depending on the OS, select a method provided in HiSightCapability to optimize the decoder settings
        if ((mode & OPTIMIZATION_TAG_CODEC_CONFIGURE_FLAG) != 0) {
            capability.setMediaCodecConfigureFlag(2);
        }
        if ((mode & OPTIMIZATION_TAG_MEDIA_FORMAT_INTEGER) != 0) {
        }
        if ((mode & OPTIMIZATION_TAG_MEDIA_FORMAT_FLOAT) != 0) {
        }
        if ((mode & OPTIMIZATION_TAG_MEDIA_FORMAT_LONG) != 0) {
        }
        if ((mode & OPTIMIZATION_TAG_MEDIA_FORMAT_STRING) != 0) {
        }
        if (mPlayerClient != null) {
            mPlayerClient.setCapability(capability);
        } else {
            Log.e(TAG, "mPlayerClient is null.");
        }
    }

    private void disconnectDevice() {
        Log.d(TAG, "disconnectDevice() called.");
        if (mPlayerClient != null) {
            mPlayerClient.disconnectDevice(mProjectionDevice);
        } else {
            Log.e(TAG, "mPlayerClient is null.");
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
                case Constant.EVENT_ID_SERVICE_BIND_SUCCESS:
                    Size screenSize = new Size(1920, 1080);
                    Size videoSize = new Size(1920, 1080);
                    int framerate = 30;
                    int optimizationMode = 1;
                    setCapability(screenSize, videoSize, framerate, optimizationMode);
                    setDiscoverable(mIsDiscoverable);

                    boolean needPassword = SharedPreferenceUtil.getAuthMode(mContext);
                    String password = SharedPreferenceUtil.getPassword(mContext);
                    boolean isNewPassword = false;
                    setAuthMode(needPassword, password, isNewPassword);
                    break;

                case Constant.EVENT_ID_CONNECT_REQ: {
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_CONNECT_REQ" );
                    if (mIsPinShown) {
                        sendBroadcastToActivity(BROADCAST_ACTION_FINISH_PIN_ACTIVITY);
                        mIsPinShown = false;
                    }
                    DisplayInfo displayInfo = (DisplayInfo) msg.obj;
                    if (displayInfo != null) {
                        mProjectionDevice = displayInfo.getProjectionDevice();
                        startPlayActivity();
                    } else {
                        Log.e(TAG, "displayInfo is null.");
                    }
                }
                break;
                case Constant.EVENT_ID_PIN_CODE_SHOW: {
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_PIN_CODE_SHOW" );
                    mIsPinShown = true;
                    DisplayInfo displayInfo = (DisplayInfo) msg.obj;
                    if ((displayInfo != null) && (mProjectionDevice = displayInfo.getProjectionDevice()) != null) {
                        String pinCode = displayInfo.getPinCode();
                        String deviceName = mProjectionDevice.getDeviceName();
                        startAlertActivity(pinCode,deviceName);
                    } else {
                        Log.e(TAG, "displayInfo is null.");
                    }
                }
                break;
                case Constant.EVENT_ID_PIN_CODE_SHOW_FINISH:
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_PIN_CODE_SHOW_FINISH" );
                    if (mIsPinShown) {
                        sendBroadcastToActivity(BROADCAST_ACTION_FINISH_PIN_ACTIVITY);
                        mIsPinShown = false;
                    }
                    break;


                case Constant.EVENT_ID_DEVICE_CONNECTED:
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_DEVICE_CONNECTED" );
                    break;

                case Constant.EVENT_ID_CASTING:
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_CASTING" );
                    break;

                case Constant.EVENT_ID_PAUSED:
                    mCastServiceReady = true;
                    startPlay();
                    break;

                case Constant.EVENT_ID_DEVICE_DISCONNECTED:
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_DEVICE_DISCONNECTED" );
                    if (mIsPinShown) {
                        sendBroadcastToActivity(BROADCAST_ACTION_FINISH_PIN_ACTIVITY);
                        mIsPinShown = false;
                    }
                    sendBroadcastToActivity(BROADCAST_ACTION_FINISH_PLAY_ACTIVITY);
                    break;

                case Constant.EVENT_ID_SET_SURFACE:
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_SET_SURFACE" );
                    break;

                case Constant.EVENT_ID_NETWORK_QUALITY: {
                    Log.e(TAG, "handleMessage: "+"EVENT_ID_NETWORK_QUALITY" );
                    DisplayInfo displayInfo = (DisplayInfo) msg.obj;
                    if (displayInfo != null) {
                        int networkQuality = displayInfo.getNetworkQuality();
                        Log.d(TAG, "networkquality update: " + networkQuality);
                        Log.d(TAG, "sendBroadcastToActivity() called.");
                        Intent intent = new Intent();
                        intent.setAction(BROADCAST_ACTION_NETWORK_QUALITY);
                        intent.putExtra("networkquality", networkQuality);
                        sendBroadcast(intent);
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    class MyBinder extends Binder {
        /**
         * Methods for obtaining the service
         *
         * @return PlayerService
         */
        public SinkTesterService getService() {
            return SinkTesterService.this;
        }
    }
}