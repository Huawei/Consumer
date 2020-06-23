/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.caaskitdemo.caaskit;

import android.content.Context;
import android.util.Log;

import com.huawei.caas.caasservice.HwCaasHandler;
import com.huawei.caas.caasservice.HwCaasServiceCallBack;
import com.huawei.caas.caasservice.HwCaasServiceManager;
import com.huawei.caas.caasservice.HwCaasUtils;
import com.huawei.caas.caasservice.HwCallAbilityCallBack;
import com.huawei.caas.caasservice.HwMakeCallCallBack;
import com.huawei.caaskitdemo.CaasKitApplication;

/**
 * CaasKit Helper.
 *
 * @since 2020-3-21
 */
public class CaasKitHelper {
    private static final String TAG = "CaasKitHelper";

    private static CaasKitHelper mCaasKitHelper;

    private HwCaasServiceManager mHwCaasServiceManager;

    private HwCaasHandler mHwCaasHandler;

    private Context mContext;

    private boolean mIsCaasKitInit;

    private CaasKitHelper() {
        mContext = CaasKitApplication.getContext();
    }

    public static CaasKitHelper getInstance() {
        if (mCaasKitHelper == null) {
            synchronized (CaasKitHelper.class) {
                if (mCaasKitHelper == null) {
                    mCaasKitHelper = new CaasKitHelper();
                }
            }
        }
        return mCaasKitHelper;
    }

    public void caasKitInit(HwCaasServiceCallBack callback) {
        Log.d(TAG, "caasKitInit.");
        if (!mIsCaasKitInit) {
            /** Initialize mHwCaasServiceManager instance. */
            mHwCaasServiceManager = HwCaasServiceManager.init();
            /** Initialize HwCaasHandler instance through handlerType. */
            mHwCaasServiceManager.initHandler(mContext, HwCaasUtils.NORMAL_CALL_TYPE, callback);
            mIsCaasKitInit = true;
        }
    }

    public void setHwCaasHandler(HwCaasHandler handler) {
        Log.d(TAG, "setHwCaasHandler.");
        mHwCaasHandler = handler;
    }

    public void queryHiCallAbility(String phoneNumber) {
        Log.d(TAG, "queryHiCallAbility.");
        /** The format for querying phoneNumber after Sha256 encryption is required must be a number with a 
        country code Example: +861320750xxxx */
        String phoneNumberSha256 = Sha256Util.encryptNumber(phoneNumber);
        if (mHwCaasHandler != null) {
            mHwCaasHandler.queryCallAbility(phoneNumberSha256, HwCaasUtils.CallAbilityType.NORMAL_CALL);
        }
    }

    public void setCallAbilityCallBack(HwCallAbilityCallBack callback) {
        Log.d(TAG, "setCallAbilityCallBack.");
        if (mHwCaasHandler != null) {
            mHwCaasHandler.setCallAbilityCallBack(callback);
        }
    }

    public void setMakeCallCallBack(HwMakeCallCallBack callback) {
        Log.d(TAG, "setMakeCallCallBack.");
        if (mHwCaasHandler != null) {
            mHwCaasHandler.setMakeCallCallBack(callback);
        }
    }

    public int makeCall(String phoneNumber, HwCaasUtils.CallType callType) {
        Log.d(TAG, "makeCall.");
        if (mHwCaasHandler != null) {
            int retCode = mHwCaasHandler.makeCall(phoneNumber, callType);
            Log.d(TAG, "makeCall retCode: " + retCode);
            return retCode;
        }
        return -1;
    }

    public void caasKitRelease() {
        Log.d(TAG, "caasKitRelease." + mIsCaasKitInit);
        if (mIsCaasKitInit) {
            if (mHwCaasServiceManager != null) {
                /** Source release */
                mHwCaasServiceManager.release();
                mHwCaasServiceManager = null;
            }
            mIsCaasKitInit = false;
        }
    }
}
