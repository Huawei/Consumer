package com.huawei.dvkitdemo;


import com.huawei.dmsdp.devicevirtualization.VirtualSensor;
import com.huawei.dmsdp.devicevirtualization.VirtualVibrator;

import java.util.Objects;


public class WearSensor {
    private String sensorName = "";
    private String deviceId;
    private int sensorId;
    private VirtualSensor virtualSensor;
    private VirtualVibrator vibrator;



    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String toString() {
        return  sensorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WearSensor that = (WearSensor) o;
        return sensorId == that.sensorId &&
                Objects.equals(sensorName, that.sensorName) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(virtualSensor, that.virtualSensor) &&
                Objects.equals(vibrator, that.vibrator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorName, deviceId, sensorId, virtualSensor, vibrator);
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public VirtualSensor getVirtualSensor() {
        return virtualSensor;
    }

    public void setVirtualSensor(VirtualSensor virtualSensor) {
        this.virtualSensor = virtualSensor;
    }

    public VirtualVibrator getVibrator() {
        return vibrator;
    }

    public void setVibrator(VirtualVibrator vibrator) {
        this.vibrator = vibrator;
    }

    public WearSensor(String sensorName, String deviceId, int sensorId) {
        this.sensorName = sensorName;
        this.deviceId = deviceId;
        this.sensorId = sensorId;
    }
}
