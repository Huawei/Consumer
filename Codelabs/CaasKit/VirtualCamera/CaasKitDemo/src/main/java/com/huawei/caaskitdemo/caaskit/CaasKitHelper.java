/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.caaskit;

import android.content.Context;
import android.content.IntentFilter;
import android.opengl.EGLContext;
import android.util.Log;

import com.huawei.caas.caasservice.HwCaasHandler;
import com.huawei.caas.caasservice.HwCaasServiceCallBack;
import com.huawei.caas.caasservice.HwCaasServiceManager;
import com.huawei.caas.caasservice.HwCaasUtils;
import com.huawei.caaskitdemo.CaasKitApplication;

/**
 * CaasKit Helper.
 *
 * @since 2019-10-21
 */
public class CaasKitHelper {
    private static final String TAG = "CaasKitHelper";
    private static final String DMSDP_STARTDISCOVERY = "com.huawei.dmsdp.DMSDP_STARTDISCOVERY";
    private static final int VIEWHEIGHT = 248;
    private static final int VIEWWIDTH = 256;
    private static final int LOCATION_X = 102;
    private static final int LOCATION_Y = 40;
    private static final int LOCATION_STARTY = 24;
    private static CaasKitHelper sCaasKitHelper;
    private DeviceDiscoverReceiver mDiscoverReceiver;
    private HwCaasServiceManager mHwCaasServiceManager;
    private HwCaasHandler mHwCaasHandler;
    private Context mContext;
    private boolean mIsSendShowFail;
    private boolean mIsCaasKitInit;
    private boolean mIsHasCaaSContacts;

    private HwCaasServiceCallBack mCallBack = new HwCaasServiceCallBack() {
        @Override
        public void initSuccess(HwCaasHandler handler) {
            // Callback after successful initialization of HwCaasHandler.
            mHwCaasHandler = handler;
            if (mHwCaasHandler != null) {
                boolean isSetSuccess = false;
                // query if there are contacts to call.
                mIsHasCaaSContacts = mHwCaasHandler.hasCaaSContacts(HwCaasUtils.ContactsType.NORMAL_CONTACTS);
                isSetSuccess = mHwCaasHandler.setContactViewSize(VIEWWIDTH, VIEWHEIGHT);
                Log.i(TAG, " isSetSuccess: " + isSetSuccess);
                isSetSuccess = mHwCaasHandler.setAppMode(HwCaasUtils.LANDSCAPE);
                Log.i(TAG, " isSetSuccess: " + isSetSuccess);
                isSetSuccess = mHwCaasHandler.setFloatViewLocation(HwCaasUtils.STARTVIEW,
                        HwCaasUtils.POINT_RIGHTANDDOWN, LOCATION_X, LOCATION_STARTY);
                Log.i(TAG, "viewType: " + HwCaasUtils.STARTVIEW + " isSetSuccess: " + isSetSuccess);
                isSetSuccess =
                        mHwCaasHandler.setFloatViewLocation(HwCaasUtils.CONTACTVIEW, HwCaasUtils.POINT_RIGHTANDUP, LOCATION_X, LOCATION_Y);
                Log.i(TAG, "viewType: " + HwCaasUtils.CONTACTVIEW + " isSetSuccess: " + isSetSuccess);
                isSetSuccess = mHwCaasHandler.setFloatViewLocation(HwCaasUtils.CALLVIEW, HwCaasUtils.POINT_RIGHTANDUP,
                        LOCATION_X, LOCATION_Y);
                Log.i(TAG, "viewType: " + HwCaasUtils.CALLVIEW + " isSetSuccess: " + isSetSuccess);
                isSetSuccess = mHwCaasHandler.setFloatViewLocation(HwCaasUtils.VIDEOVIEW, HwCaasUtils.POINT_RIGHTANDUP,
                        LOCATION_X, LOCATION_Y);
                Log.i(TAG, "viewType: " + HwCaasUtils.VIDEOVIEW + " isSetSuccess: " + isSetSuccess);
                if (mIsSendShowFail) {
                    sendShow();
                    mIsSendShowFail = false;
                }
            }
        }

        @Override
        public void initFail(int retCode) {
            // Callback if init Handler fail.
            Log.i(TAG, "retCode: " + retCode);
            if (retCode == HwCaasUtils.SERVICE_EXCEPTION) {
                stopRendering();
            }
        }

        @Override
        public void releaseSuccess() {
            // Callback after successful release of mHwCaasServiceManager.
            mHwCaasHandler = null;
            mIsSendShowFail = false;
        }
    };

    private CaasKitHelper() {
        mContext = CaasKitApplication.getContext();
    }

    public static CaasKitHelper getInstance() {
        if (sCaasKitHelper == null) {
            synchronized (CaasKitHelper.class) {
                if (sCaasKitHelper == null) {
                    sCaasKitHelper = new CaasKitHelper();
                }
            }
        }
        return sCaasKitHelper;
    }

    private void registerDiscoverReceiver() {
        Log.d(TAG, "registerDiscoverReceiver.");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DMSDP_STARTDISCOVERY);
        if (mDiscoverReceiver == null) {
            Log.d(TAG, "mDiscoverReceiver.");
            mDiscoverReceiver = new DeviceDiscoverReceiver();
            mContext.registerReceiver(mDiscoverReceiver, intentFilter);
        }
    }

    private void unregisterDiscoverReceiver() {
        Log.d(TAG, "unregisterDiscoverReceiver.");
        if (mDiscoverReceiver != null) {
            mContext.unregisterReceiver(mDiscoverReceiver);
            mDiscoverReceiver = null;
        }
    }

    /**
     * identifies whether the rendering thread is working.
     *
     * @return Returns true if recording has been started.
     */
    public boolean isRendering() {
        return ExtSurfaceRender.getInstance().isRendering();
    }

    /**
     * tells the surface render to stop rendering.
     */
    public void stopRendering() {
        Log.d(TAG, "stopRendering.");
        if (isRendering()) {
            ExtSurfaceRender.getInstance().stopRendering();
        }
    }

    /**
     * Handles an available frame.
     *
     * @param textureId textureId available for rendering.
     */
    public void frameAvailable(int textureId) {
        ExtSurfaceRender.getInstance().frameAvailable(textureId);
    }

    /**
     * set the shared EGLContext.
     *
     * @param sharedContext The context to share.
     */
    public void setSharedContext(EGLContext sharedContext) {
        ExtSurfaceRender.getInstance().setSharedContext(sharedContext);
    }

    /**
     * Initialization before using CaaSKitLite.
     */
    public void caasKitInit() {
        Log.d(TAG, "caasKitInit." + mIsCaasKitInit);
        if (!mIsCaasKitInit) {
            registerDiscoverReceiver();
            // Initialize mHwCaasServiceManager instance.
            mHwCaasServiceManager = HwCaasServiceManager.init();
            // Initialize HwCaasHandler instance through handlerType.
            mHwCaasServiceManager.initHandler(mContext, HwCaasUtils.VIRTUAL_CAMERA_TYPE, mCallBack);
            mIsCaasKitInit = true;
        }
    }

    /**
     * Virtualize Camera and show float ball.
     */
    public void sendShow() {
        Log.d(TAG, "sendShow.");
        if (mHwCaasHandler != null && mIsHasCaaSContacts) {
            // show float ball.
            int ret = mHwCaasHandler.initVirtualCamera(VirtualCameraListener.DEVICE_ID, VirtualCameraListener.VIRCAMERA_ID);
            Log.d(TAG, "ret: " + ret);
        } else {
            // Prevent first call, mHwCaasHandler hasn't returned yet.
            mIsSendShowFail = true;
            Log.e(TAG, "sendShow fail.");
        }
    }

    /**
     * application back to background, release virtual Camera.
     */
    public void releaseVirtualCamera() {
        Log.d(TAG, "releaseVirtualCamera.");
        if (mHwCaasHandler != null) {
            // release virtual camera.
            int ret = mHwCaasHandler.releaseVirtualCamera(VirtualCameraListener.DEVICE_ID, VirtualCameraListener.VIRCAMERA_ID);
            Log.d(TAG, "ret: " + ret);
        }
    }

    /**
     * Called when the application is in the foreground and some scenes do not want to display the float ball.
     *
     * @return returns true if sent successfully.
     */
    public boolean sendHide() {
        Log.d(TAG, "sendHide.");
        if (mHwCaasHandler != null) {
            // Send HIDE event to hide float ball.
            boolean isSendoK = mHwCaasHandler.sendEventToCaasService(HwCaasUtils.HIDE);
            Log.d(TAG, "isSendoK: " + isSendoK);
            return isSendoK;
        }
        Log.e(TAG, "sendHide fail.");
        return false;
    }

    /**
     * called when the app exits.
     */
    public void caasKitRelease() {
        Log.d(TAG, "caasKitRelease." + mIsCaasKitInit);
        if (mIsCaasKitInit) {
            if (mHwCaasServiceManager != null) {
                // Source release.
                mHwCaasServiceManager.release();
                mHwCaasServiceManager = null;
            }
            unregisterDiscoverReceiver();
            mIsCaasKitInit = false;
        }
    }
}
