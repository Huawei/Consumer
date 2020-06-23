package com.huawei.sinktester;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import com.huawei.castpluskit.Constant;
import com.huawei.castpluskit.HiSightSurfaceView;

public class PlayActivity extends Activity {
    private static final String TAG = "SinkTesterPlayActivity";
    private static final int INVALID_NETWORK_QUALITY = -1000;


    private Drawable mDrawableNetworkWorse;
    private Drawable mDrawableNetworkBad;
    private Drawable mDrawableNetworkGeneral;
    private Drawable mVectorAnimDrawableNetworkWorse;
    private Drawable mVectorAnimDrawableNetworkGeneral;
    private Drawable mVectorAnimDrawableNetworkBad;


    private boolean mIsFinishSelfBehavior = true;
    private ImageView mWlanImageView;

    private boolean needVectorAnimShow = true;
    private AnimatedVectorDrawable animatedImageDrawable;

    public static HiSightSurfaceView mHiView;
    public static volatile boolean isSurfaceReady = false;


    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated() called.");
            isSurfaceReady = true;
            sendBroadcast(SinkTesterService.BROADCAST_ACTION_PLAY);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged() called.");
            isSurfaceReady = true;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed() called.");
            isSurfaceReady = false;
            sendBroadcast(SinkTesterService.BROADCAST_ACTION_PAUSE);
        }
    };


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            Log.d(TAG, "Broadcast received, action: " + action);
            Log.e(TAG, "onReceive:------------- " + action);
            if (SinkTesterService.BROADCAST_ACTION_FINISH_PLAY_ACTIVITY.equals(action)) {
                mIsFinishSelfBehavior = false;
                finish();
            } else if (SinkTesterService.BROADCAST_ACTION_NETWORK_QUALITY.equals(action)) {
                int networkQuality = intent.getIntExtra("networkquality", INVALID_NETWORK_QUALITY);
                Log.e(TAG, "onReceive: " + "              ----------------" + networkQuality);
                switch (networkQuality) {
                    case INVALID_NETWORK_QUALITY:
                        Log.e(TAG, "invalid network quality.");
                        break;
                    case Constant.NETWORK_QUALITY_EXCEPTION:
                        Log.d(TAG, "network exception.");
                        mWlanImageView.setVisibility(View.GONE);
                        networkBreakToast();
                        break;
                    case Constant.NETWORK_QUALITY_WORST:
                    case Constant.NETWORK_QUALITY_WORSE:
                    case Constant.NETWORK_QUALITY_BAD:
                    case Constant.NETWORK_QUALITY_GENERAL:
                    case Constant.NETWORK_QUALITY_GOOD:
                        Log.e(TAG, "networkQuality:---->" + networkQuality);
                        dispatchDrawableSet(networkQuality);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called.");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);

        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(SinkTesterService.BROADCAST_ACTION_FINISH_PLAY_ACTIVITY);
        broadcastFilter.addAction(SinkTesterService.BROADCAST_ACTION_NETWORK_QUALITY);

        registerReceiver(mBroadcastReceiver, broadcastFilter);

        mWlanImageView = findViewById(R.id.wlan_imageview);

        mDrawableNetworkWorse = getDrawable(R.drawable.ic_network_worse_icon);
        mDrawableNetworkBad = getDrawable(R.drawable.ic_network_bad_icon);
        mDrawableNetworkGeneral = getDrawable(R.drawable.ic_network_general_icon);

        mVectorAnimDrawableNetworkWorse = getDrawable(R.drawable.vector_anim_worse_show);
        mVectorAnimDrawableNetworkGeneral = getDrawable(R.drawable.vector_anim_general_show);
        mVectorAnimDrawableNetworkBad = getDrawable(R.drawable.vector_anim_bad_show);


        mHiView = (HiSightSurfaceView) findViewById(R.id.HiSightSurfaceView);
        if (mHiView != null) {
            mHiView.setSecure(true);
            SurfaceHolder surfaceHolder = mHiView.getHolder();
            if (surfaceHolder != null) {
                surfaceHolder.addCallback(mSurfaceHolderCallback);
            } else {
                Log.e(TAG, "surfaceHolder is null.");
            }
        } else {
            Log.e(TAG, "mHiView is null.");
        }
        Log.d(TAG, "onCreate() end.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called.");

        if (animatedImageDrawable != null) {
            if (animatedImageDrawable.isRunning()) {
                animatedImageDrawable.stop();
                animatedImageDrawable = null;
            }
        }

        if (mIsFinishSelfBehavior) {
            Log.d(TAG, "Finish is self behavior, send broadcast to service.");
            Intent disconnectIntent = new Intent();
            disconnectIntent.setAction(SinkTesterService.BROADCAST_ACTION_DISCONNECT);
            sendBroadcast(disconnectIntent);
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called.");
    }

    private void sendBroadcast(String broadcastAction) {
        Intent intent = new Intent();
        intent.setAction(broadcastAction);
        sendBroadcast(intent);
    }



    private void dispatchDrawableSet(int networkQuality ){
        if(networkQuality == Constant.NETWORK_QUALITY_GOOD){
            mWlanImageView.clearAnimation();
            mWlanImageView.setVisibility(View.GONE);
            needVectorAnimShow = true;
        }else {
            mWlanImageView.setVisibility(View.VISIBLE);
            if (needVectorAnimShow) {
                setVectorAnimDrawable(networkQuality);
                needVectorAnimShow = false;
            }else {
                setVectorDrawable(networkQuality);
            }
        }
    }

    private void setVectorAnimDrawable(int networkQuality) {
        switch (networkQuality) {
            case Constant.NETWORK_QUALITY_WORST:
            case Constant.NETWORK_QUALITY_WORSE:
                mWlanImageView.setImageDrawable(mVectorAnimDrawableNetworkWorse);
                startVectorAnimator();
                break;
            case Constant.NETWORK_QUALITY_BAD:
                mWlanImageView.setImageDrawable(mVectorAnimDrawableNetworkBad);
                startVectorAnimator();
                break;
            case Constant.NETWORK_QUALITY_GENERAL:
                mWlanImageView.setImageDrawable(mVectorAnimDrawableNetworkGeneral);
                startVectorAnimator();
            default:
                break;
        }
    }

    private void startVectorAnimator(){
        animatedImageDrawable = (AnimatedVectorDrawable) mWlanImageView.getDrawable();
        animatedImageDrawable.start();
    }

    private void setVectorDrawable(int networkQuality){
        switch (networkQuality) {
            case Constant.NETWORK_QUALITY_WORST:
            case Constant.NETWORK_QUALITY_WORSE:
                mWlanImageView.setImageDrawable(mDrawableNetworkWorse);
                break;

            case Constant.NETWORK_QUALITY_BAD:
                mWlanImageView.setImageDrawable(mDrawableNetworkBad);
                break;

            case Constant.NETWORK_QUALITY_GENERAL:
                mWlanImageView.setImageDrawable(mDrawableNetworkGeneral);
                break;

            default:
                break;
        }
    }

    private void networkBreakToast(){
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageDrawable(getDrawable(R.drawable.ic_toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(imageView);
        toast.show();
    }

}
