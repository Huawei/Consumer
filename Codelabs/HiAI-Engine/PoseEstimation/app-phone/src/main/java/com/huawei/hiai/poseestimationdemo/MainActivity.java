package com.huawei.hiai.poseestimationdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;
import com.huawei.hiai.pdk.pluginservice.ILoadPluginCallback;
import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.common.VisionImage;
import com.huawei.hiai.vision.image.detector.PoseEstimationDetector;
import com.huawei.hiai.vision.visionkit.image.detector.BodySkeletons;
import com.huawei.hiai.vision.visionkit.image.detector.PeConfiguration;
import com.huawei.hiai.vision.visionkit.text.config.VisionTextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 110;
    private int availability;
    private Context mContext;
    private Object mObject = new Object();
    private ImageView mOrigin;
    private ImageView mResult;
    private Bitmap copyBitmap;
    private Bitmap OriginBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initEngine();
        initView();
        mThread.start();
    }

    private PoseEstimationDetector mPoseDetector;

    private void initEngine() {
        VisionBase.init(mContext, new ConnectionCallback() {
            @Override
            public void onServiceConnect() {
                mPoseDetector = new PoseEstimationDetector(getApplicationContext());
                synchronized (mObject) {
                    mObject.notifyAll();
                }
            }

            @Override
            public void onServiceDisconnect() {

            }
        });
    }

    private void detect(Bitmap bitmap) {
        PeConfiguration config = new PeConfiguration.Builder()
                .setProcessMode(VisionTextConfiguration.MODE_OUT)
                .build();
        mPoseDetector.setConfiguration(config);
        VisionImage image = VisionImage.fromBitmap(bitmap);
        List<BodySkeletons> result = new ArrayList<>();
        int resultCode = mPoseDetector.detect(image, result, null);
        drawPoint(result);
    }

    private void loadPlugin() {
        availability = mPoseDetector.getAvailability();
        Log.e(TAG, "code:" + availability);
        if (availability == 0) {

        } else if (availability == -6) {//loadPlugin
            mPoseDetector.loadPlugin(new LoadPluginCallback());
        } else {

        }
    }

    private void initView() {
        mOrigin = (ImageView) findViewById(R.id.origin);
        mResult = (ImageView) findViewById(R.id.result);
        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(availability == 0){
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE);
                }else if(availability == -6){
                    Toast.makeText(MainActivity.this,getString(R.string.load_plugin),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,getString(R.string.not_support),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Thread mThread = new Thread() {
        @Override
        public void run() {
            super.run();
            synchronized (mObject) {
                try {
                    mObject.wait(2000);
                    loadPlugin();
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    OriginBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    mOrigin.setImageBitmap(OriginBitmap);
                    detect(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawPoint(List<BodySkeletons> result) {
        if(result.size() > 0){
            Canvas canvas = new Canvas(copyBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            for (int i = 0; i < result.size(); i++) {
                BodySkeletons bodySkeletons = result.get(i);
                for (int j = 0; j < bodySkeletons.getPosition().size(); j++) {
                    canvas.drawCircle(bodySkeletons.getPosition().get(j).x, bodySkeletons.getPosition().get(j).y, 10, paint);
                }
            }
            mResult.setImageBitmap(copyBitmap);
        }else {
            Toast.makeText(MainActivity.this,getString(R.string.no_point),Toast.LENGTH_SHORT).show();
        }

    }

    private class LoadPluginCallback extends ILoadPluginCallback.Stub {

        @Override
        public void onResult(int resultCode) throws RemoteException {
            Log.d(TAG, "LoadPluginCode: " + resultCode);
            if(resultCode == 0){
                availability = 0;
            }else {
                Toast.makeText(MainActivity.this,getString(R.string.plugin_install_failed),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProgress(int i) throws RemoteException {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPoseDetector != null){
            mPoseDetector.release();
        }
    }

}