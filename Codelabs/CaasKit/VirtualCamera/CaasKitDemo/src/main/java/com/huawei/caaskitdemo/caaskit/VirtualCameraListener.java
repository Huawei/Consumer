package com.huawei.caaskitdemo.caaskit;

import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huawei.dmsdpsdk.localapp.CameraInfo;
import com.huawei.dmsdpsdk.localapp.CameraListener;
import com.huawei.dmsdpsdk.localapp.CameraParameters;
import com.huawei.dmsdpsdk.localapp.CommonUtils;
import com.huawei.dmsdpsdk.localapp.DeviceInfo;
import com.huawei.dmsdpsdk.localapp.HwDmsdpService;

public class VirtualCameraListener extends CameraListener {
    // Use java.util.UUID.randomUUID().toString() to generate, please do not directly use the ID provided in the demo
    public static final String DEVICE_ID = "ff66bf58-6413-48e7-8dcd-c084254b199e";

    // this ID is customized by the developer.
    public static final String VIRCAMERA_ID = "virtualcamera0";

    private static final String TAG = "VirtualCameraListener";

    private static final int VIDEO_BUFFER_MODE_YUV = 1;

    private static final int DEVICE_CAMERA = 5;

    private static final String DEVICE_NAME = "CaasKitCamera";

    public VirtualCameraListener() {
    }

    @Override
    public List<DeviceInfo> getDeviceInfo() {
        Log.d(TAG, "getDeviceInfo.");
        List<DeviceInfo> listDeviceInfo = new ArrayList<>();

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(DEVICE_ID);
        deviceInfo.setDeviceName(DEVICE_NAME);
        deviceInfo.setDeviceType(DEVICE_CAMERA);
        deviceInfo.properties.put(CommonUtils.DEVICE_SUPPORTCAMERA_BOOLEAN, "true");

        listDeviceInfo.add(deviceInfo);

        return listDeviceInfo;
    }

    @Override
    public HashMap<String, CameraInfo> getCameraInfo(String deviceId) {
            Log.d(TAG, "getCameraInfo.");
            HashMap<String, CameraInfo> mapCameraInfo = new HashMap<>();

            CameraInfo cameraInfo = new CameraInfo();
            cameraInfo.setCameraId(VIRCAMERA_ID);
            cameraInfo.setVideoType(VIDEO_BUFFER_MODE_YUV);
            cameraInfo.setSupportedFpsRange("25000,25000");
            cameraInfo.setSupportedResolutionRange("1080,1920");

            mapCameraInfo.put(VIRCAMERA_ID, cameraInfo);

            return mapCameraInfo;
    }

    @Override
    public CameraParameters getCameraParameters(String cameraId) {
        Log.d(TAG, "getCameraParameters.");
        CameraParameters cameraParams = new CameraParameters();

        cameraParams.cameraId = VIRCAMERA_ID;
        cameraParams.properties.put(CommonUtils.CURRENT_RESOLUTION, "1080,1920");
        cameraParams.properties.put(CommonUtils.CURRENT_FRAMERATES, "25000,30000");
        cameraParams.properties.put(CommonUtils.CURRENT_IMAGEFORMAT, CommonUtils.IMAGE_FORMAT_NV21);
        cameraParams.properties.put(CommonUtils.CURRENT_DECODEFORMAT, CommonUtils.DECODE_FORMAT_RGBA);

        return cameraParams;
    }

    @Override
    public int startCaptureVideo(String cameraId, Surface surface) {
        Log.d(TAG, "startCaptureVideo." + cameraId);
        if (ExtSurfaceRender.getInstance().isEGLContextReady()) {
            Log.d(TAG, "startCaptureVideo.");
            ExtSurfaceRender.getInstance().startRendering(surface);
        }
        return 0;
    }

    @Override
    public int stopCaptureVideo(String cameraId) {
        Log.d(TAG, "stopCaptureVideo.");
        ExtSurfaceRender.getInstance().stopRendering();
        return 0;
    }

    @Override
    public int notifyUnbindMSDPService() {
       Log.d(TAG, "notifyUnbindMSDPService.");
        HwDmsdpService.release();
        return 0;
    }

    @Override
    public void checkPermissionOnCallback(int result) {
        Log.d(TAG, "checkPermissionOnCallback.result: " + result);
    }
}
