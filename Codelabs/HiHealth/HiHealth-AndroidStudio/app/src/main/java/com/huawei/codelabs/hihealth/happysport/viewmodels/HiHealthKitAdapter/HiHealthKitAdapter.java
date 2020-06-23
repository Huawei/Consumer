package com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

//TODO: import

public class HiHealthKitAdapter {
    private ISportListener mSportListener;
    private Context mContext;
    private static final String Tag = "HiHealthKitAdapter";

    public boolean registerSportListener(ISportListener listener) {
        Log.i(Tag, "begin to register to hihealthkit");
        this.mSportListener = listener;

        //TODO: register

        return true;
    }

    public boolean unregisterSportListener() {
        //TODO: unregister

        return true;
    }

    public HiHealthKitAdapter(Context context) {
        this.mContext = context;
    }
}
