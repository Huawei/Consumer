package com.huawei.dvkitdemo.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.huawei.dvkitdemo.AutoFitTextureView;
import com.huawei.dvkitdemoone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RegitsterCameraAPI2 extends Activity {
    private static final String TAG = "RegitsterCameraAPI2";

    private String currentCamId = "0";

    private Size mPreviewSize;

    private HandlerThread mBackgroundThread;

    private Handler mBackgroundHandler;

    private AutoFitTextureView mTextureView;

    private CameraDevice mCameraDevice;

    private CameraCaptureSession mPreviewSession;

    private CaptureRequest.Builder mPreviewBuilder;

    Range<Integer>[] fps;

    private Spinner m_fpsList;

    private ArrayAdapter<Range<Integer>> fpsAdapter = null;

    private List<Range<Integer>> fpsList = new ArrayList<>();

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                Log.i(TAG, "onOpened, width x height = " + mTextureView.getWidth() + " x " + mTextureView.getHeight());
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            finish();
        }
    };

    private CameraCaptureSession.StateCallback previewCaptureCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mPreviewSession = session;
            updatePreview();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.i(TAG, "Failed");
        }
    };

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.i(TAG, "onSurfaceTextureAvailable: width: " + width + ", height: " + height);
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            Log.i(TAG, "onSurfaceTextureSizeChanged: width: " + width + ", height: " + height);
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_camera_api2);
        initFpsAndResolution();
        mTextureView = findViewById(R.id.texture);
    }

    private void initFpsAndResolution() {
        m_fpsList = (Spinner) findViewById(R.id.fps);
        fpsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fpsList);
        m_fpsList.setAdapter(fpsAdapter);
        m_fpsList.setVisibility(View.VISIBLE);
        m_fpsList.setSelection(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            Log.i(TAG, "onResume: mTextureView width: " + mTextureView.getWidth() + ",mTextureView height: "
                + mTextureView.getHeight());
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
        stopBackgroundThread();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (mBackgroundThread == null) {
            return;
        }
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height) {

        closeCamera();
        stopBackgroundThread();

        if (isFinishing()) {
            return;
        }

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId;
            cameraId = chooseCameraId();

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            fps = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            fpsList.clear();
            for (int i = fps.length - 1; i >= 0; i--) {
                Log.i(TAG, "index: " + i + ", [" + fps[i].getLower() + ", " + fps[i].getUpper() + "]");
                fpsList.add(fps[i]);
            }
            fpsAdapter.notifyDataSetChanged();

            mPreviewSize = new Size(1920, 1080);

            Log.i(TAG, "openCamera, mPreviewSize width x mPreviewSize height = " + mPreviewSize.getWidth() + " x "
                + mPreviewSize.getHeight());
            int orientation = getResources().getConfiguration().orientation;
            Log.i(TAG, "orientation is : " + orientation);
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            Log.i(TAG, "openCamera, mTextureView width x mTextureView height = " + mTextureView.getWidth() + " x "
                + mTextureView.getHeight());
            Log.i(TAG, "openCamera, width x height = " + width + " x " + height);
            configureTransform(width, height);
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (Exception ex) {
            this.finish();
            Toast.makeText(this, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private String chooseCameraId() {
        return getIntent().getStringExtra("cameraId");
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }

        try {
            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Log.i(TAG, "startPreview, mPreviewSize width x mPreviewSize height = " + mPreviewSize.getWidth() + " x "
                + mPreviewSize.getHeight());

            Range<Integer> fpsSel = (Range<Integer>) m_fpsList.getSelectedItem();
            Log.i(TAG, String.format("startPreview by fps: left: %d, right: %d", fpsSel.getLower(), fpsSel.getUpper()));
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsSel);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface), previewCaptureCallback,
                mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }

        Matrix matrix = new Matrix();
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.i(TAG, "rotation is: " + rotation);
        if (Integer.parseInt(currentCamId) < 1000) {
            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
                RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
                float centerX = viewRect.centerX();
                float centerY = viewRect.centerY();
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
                matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
                float scale =
                    ((float) viewHeight / mPreviewSize.getHeight()) >= ((float) viewWidth / mPreviewSize.getWidth()) ?
                    ((float) viewHeight / mPreviewSize.getHeight()) : ((float) viewWidth / mPreviewSize.getWidth());
                matrix.postScale(scale, scale, centerX, centerY);
                matrix.postRotate(90 * (rotation - 2), centerX, centerY);
            }
        } else {
            int prewWidth = mPreviewSize.getWidth();
            int prewHeight = mPreviewSize.getHeight();
            Log.i(TAG, "prewWidth x prewHeight = " + prewWidth + " x " + prewHeight);
            viewHeight = prewWidth * viewWidth / prewHeight;
            Log.i(TAG, "viewWidth x viewHeight = " + viewWidth + " x " + viewHeight);
            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
            float centerX = viewRect.centerX();
            float centerY = viewRect.centerY();
            RectF bufferRect = new RectF(0, 0, prewWidth, prewHeight);
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = ((float) viewHeight / prewWidth) >= ((float) viewWidth / prewHeight) ?
                ((float) viewHeight / prewWidth) : ((float) viewWidth / prewHeight);
            Log.i(TAG, "scale is: " + scale);
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
        Log.i(TAG,
            "configureTransform end, width x height = " + mTextureView.getWidth() + " x " + mTextureView.getHeight());
    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }
}