package com.huawei.hiaicodedemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.huawei.hiai.pdk.pluginservice.ILoadPluginCallback;
import com.huawei.hiai.pdk.resultcode.HwHiAIResultCode;
import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.common.VisionImage;
import com.huawei.hiai.vision.common.VisionImageMetadata;
import com.huawei.hiai.vision.image.segmentation.ImageSegmentation;
import com.huawei.hiai.vision.image.segmentation.SegConfiguration;
import com.huawei.hiai.vision.visionkit.image.ImageResult;
import com.huawei.hiai.vision.visionkit.text.config.VisionTextConfiguration;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.PreviewRender;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VideoSegmentActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = VideoSegmentActivity.class.getSimpleName();
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.AutoFocusCallback mAutoFocusCallback;
    private SurfaceView mSurfaceView;
    private ImageSegmentation imageSegmentation;
    private VisionImage image;

    public static final int CAPTURE_DATA_WIDTH = 1280;

    public static final int CAPTURE_DATA_HEIGHT = 720;

    private PreviewRender mPreviewRender = null;

    private SurfaceTexture mCameraTexture = null;

    private Button mSwitchButton;

    private boolean mSegmentEnable = true;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private byte[] resultBytes;

    @Override
    protected void init() {
        initView();
        initHiAI();
        initData();
    }

    private void initHiAI() {



    }

    private void initImageSegmentation(byte[] data) {



        this.image = image;
        this.imageSegmentation = imageSegmentation;
    }

    public static void loadPlugin(VisionBase imageSegmentation) {



    }

    private byte[] doPortraitSegmentation() {



        return resultBytes;
    }


    @Override
    protected int layout() {
        return R.layout.activity_video_segment;
    }

    private void initData() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.autoFocus(mAutoFocusCallback);
                }
            }
        });
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.surface);
        mSwitchButton = findViewById(R.id.switchFeature);
        mSwitchButton.setText("OFF");
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSegmentEnable = !mSegmentEnable;
                if (mSegmentEnable) {
                    mSwitchButton.setText("OFF");
                } else {
                    mSwitchButton.setText("ON");
                }
            }
        });
        Log.i(TAG, "initView");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        mCamera = Camera.open(mCameraId);
        mCamera.getParameters().setPreviewSize(CAPTURE_DATA_HEIGHT, CAPTURE_DATA_WIDTH);
        mCamera.setPreviewCallback(this);

        mPreviewRender = new PreviewRender(holder.getSurface());
        mPreviewRender.setRotation(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT ? 270 : 90);
        mPreviewRender.setBackGroundPicture(getBackGroundPic());

        mCameraTexture = mPreviewRender.getEglSurfaceTexture();
        mCameraTexture.setOnFrameAvailableListener(this);
        try {
            mCamera.setPreviewTexture(mCameraTexture);
        } catch (IOException e) {
            releaseCamera();
            e.printStackTrace();
            toast("Failed to start the camera. Please enable the camera permission!");
        }

        openCamera();
    }

    private Bitmap getBackGroundPic() {
        InputStream is = mContext.getResources().openRawResource(R.raw.a0);
        return BitmapFactory.decodeStream(is);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //Log.d(TAG, "onFrameAvailable");
        if (!mSegmentEnable) {
            if (mPreviewRender != null) {
                mPreviewRender.drawNormalFrame();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    private void openCamera() {
        setCameraDisplayOrientation(this, mCameraId, mCamera);
        mCamera.startPreview();
    }

    public void setCameraDisplayOrientation(AppCompatActivity activity,
                                            int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        Log.i(TAG, "setCameraDisplayOrientation rotation: " + result);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        releaseCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (!mSegmentEnable) {
            return;
        }
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        int width = previewSize.height;
        int height = previewSize.width;
        Log.d(TAG, "onPreviewFrame" + width + ";" + height + "date:" + data.length);

        initImageSegmentation(data);
        byte[] resultBytes = doPortraitSegmentation();

        if (resultBytes != null) {
            if (mPreviewRender != null) {
                mPreviewRender.drawSegmentFrame(data, resultBytes, previewSize.width, previewSize.height);
            }

        } else {

        }

    }

    private void releaseCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        if (imageSegmentation != null) {
            imageSegmentation.release();
        }
        if (mPreviewRender != null) {
            mPreviewRender.release();
        }
    }

}
