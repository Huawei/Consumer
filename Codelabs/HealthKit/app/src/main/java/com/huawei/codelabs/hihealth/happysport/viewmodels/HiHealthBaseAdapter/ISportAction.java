package com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthBaseAdapter;

public interface ISportAction {
    void onStart();

    void onPause();

    void onResume();

    void onStop();

    void onRunning();
}

