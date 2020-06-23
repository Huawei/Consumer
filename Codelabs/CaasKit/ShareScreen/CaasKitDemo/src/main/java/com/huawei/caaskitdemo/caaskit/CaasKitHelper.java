/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.caaskit;

import android.content.Context;
import android.util.Log;

import com.huawei.caas.caasservice.HwCaasHandler;
import com.huawei.caas.caasservice.HwCaasServiceCallBack;
import com.huawei.caas.caasservice.HwCaasServiceManager;
import com.huawei.caas.caasservice.HwCaasUtils;
import com.huawei.caas.caasservice.HwCallStateCallBack;
import com.huawei.caaskitdemo.CaasKitApplication;

/**
 * CaasKit Helper.
 *
 * @since 2019-10-21
 */
public class CaasKitHelper {
    private static final String TAG = "CaasKitHelper";

    private static final int VIEWHEIGHT = 248;

    private static final int VIEWWIDTH = 256;

    private static final int LOCATION_X = 102;

    private static final int LOCATION_Y = 140;

    private static CaasKitHelper sCaasKitHelper;

    private HwCaasServiceManager mHwCaasServiceManager;

    private HwCaasHandler mHwCaasHandler;

    private Context mContext;

    private boolean mIsSendShowFail;

    private boolean mIsCaasKitInit;

    private boolean mIsHasCaaSContacts = false;

    private HwCallStateCallBack mCallStateCallBack = new HwCallStateCallBack() {
        @Override
        public void notifyCallState(HwCaasUtils.CallState state) {
            Log.d(TAG, "callState: " + state);
        }
    };

    private HwCaasServiceCallBack mCallBack = new HwCaasServiceCallBack() {
        @Override
        public void initSuccess(HwCaasHandler handler) {
            /** callback after successful initialization of HwCaasHandler. */
            mHwCaasHandler = handler;
            if (mHwCaasHandler != null) {
                /** The name of the incoming call application displayed on the incoming call interface of the peer must be set,
                 otherwise the portal cannot be displayed */
                mHwCaasHandler.setCallerAppName("Application");
                /** Contact selection floating window can also choose full screen list (similar to system contact interface)  */
                mHwCaasHandler.setContactViewStyle(HwCaasUtils.ContactsViewStyle.FULL_SCREEN);

                /** Monitor the call status, during the call, during the call, no call, don't care if you don't care */
                mHwCaasHandler.setCallStateCallBack(mCallStateCallBack);
                /** Check if there is a contact who meets the requirements, ContactsType.NORMAL_CONTACTS-ordinary contactless contact; 
                ContactsType.SCREEN_SHARING_CONTACTS-contact that supports screen sharing */
                mIsHasCaaSContacts = mHwCaasHandler.hasCaaSContacts(HwCaasUtils.ContactsType.SCREEN_SHARING_CONTACTS);
                if (mIsSendShowFail) {
                    sendShow();
                    mIsSendShowFail = false;
                }
            }
        }

        @Override
        public void initFail(int retCode) {
            /** callback if init Handler fail. */
            Log.i(TAG, "retCode: " + retCode);
        }

        @Override
        public void releaseSuccess() {
            /** callback after successful release of mHwCaasServiceManager. */
            mHwCaasHandler = null;
            mIsSendShowFail = false;
        }
    };

    private CaasKitHelper() {
        mContext = CaasKitApplication.getContext();
    }

    public synchronized static CaasKitHelper getInstance() {
        if (sCaasKitHelper == null) {
            sCaasKitHelper = new CaasKitHelper();
        }
        return sCaasKitHelper;
    }

    public void caasKitInit() {
        Log.d(TAG, "caasKitInit.");
        if (!mIsCaasKitInit) {
            /** initialize mHwCaasServiceManager instance. */
            mHwCaasServiceManager = HwCaasServiceManager.init();
            /** initialize HwCaasHandler instance through handlerType. */
            mHwCaasServiceManager.initHandler(mContext, HwCaasUtils.SCREEN_SHARING_TYPE, mCallBack);
            mIsCaasKitInit = true;
        }
    }

    public void sendShow() {
        Log.d(TAG, "sendShow.");
        if (mHwCaasHandler != null) {
            Log.d(TAG, "isHasCaaSContacts: " + mIsHasCaaSContacts);
            if (mIsHasCaaSContacts) {
                /** Click the entrance on the application side to directly display the contact list */
                boolean ret = mHwCaasHandler.sendEventToCaasService(HwCaasUtils.SHOW_CONTACTS);
                Log.d(TAG, "ret: " + ret);
            }
        } else {
            mIsSendShowFail = true;
            Log.e(TAG, "sendShow fail.");
        }
    }

    public boolean sendHide() {
        Log.d(TAG, "sendHide.");
        if (mHwCaasHandler != null) {
            /** Hide contact list Use with HwCaasUtils.SHOW_CONTACTS */
            boolean ret = mHwCaasHandler.sendEventToCaasService(HwCaasUtils.HIDE_CONTACTS);
            Log.d(TAG, "ret: " + ret);
            return ret;
        }
        Log.e(TAG, "sendHide fail.");
        return false;
    }

    public void pauseShare() {
        Log.d(TAG, "pauseShare.");
        if (mHwCaasHandler != null) {
            boolean isSendoK = mHwCaasHandler.sendEventToCaasService(HwCaasUtils.SCREEN_SHARING_PAUSE);
            Log.d(TAG, "isSendoK: " + isSendoK);
        }
    }

    public void resumeShare() {
        Log.d(TAG, "resumeShare.");
        if (mHwCaasHandler != null) {
            boolean isSendoK = mHwCaasHandler.sendEventToCaasService(HwCaasUtils.SCREEN_SHARING_RESUME);
            Log.d(TAG, "isSendoK: " + isSendoK);
        }
    }

    public void caasKitRelease() {
        Log.d(TAG, "caasKitRelease.");
        if (mIsCaasKitInit) {
            if (mHwCaasServiceManager != null) {
                /** source release. */
                mHwCaasServiceManager.release();
                mHwCaasServiceManager = null;
            }
            mIsCaasKitInit = false;
        }
    }
}
