package com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter;

import com.huawei.codelabs.hihealth.happysport.data.RunningRepository;

public class RunningSportData extends BaseSportData{
    public int heartRate; // n/min
    public int velocity;  // mm/s
    public int distance;  // m
    public int duration;  // second

    public RunningSportData(int h, int v, int d, int dr) {
        this.heartRate = h;
        this.velocity= v;
        this.distance = d;
        this.duration = dr;
    }
}
