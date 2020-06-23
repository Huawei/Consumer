package com.huawei.dvkitdemo;

import com.huawei.dmsdp.devicevirtualization.Capability;
import com.huawei.dmsdp.devicevirtualization.VirtualDevice;

import java.util.EnumSet;
import java.util.Objects;

public class WearDevice {
    private String deviceId;

    private String deviceType;

    private String deviceName;

    private EnumSet<Capability> capabilities;

    /**
     * constructor of virtual device
     *
     * @param virtualDevice DMSDPDevice
     */
    public WearDevice(VirtualDevice virtualDevice) {
        this.deviceId = virtualDevice.getDeviceId();
        this.deviceType = virtualDevice.getDeviceType();
        this.deviceName = virtualDevice.getDeviceName();
        this.capabilities = virtualDevice.getDeviceCapability();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WearDevice that = (WearDevice) o;
        return Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }

    @Override
    public String toString() {
        return  deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public EnumSet<Capability> getCapabilities() {
        return capabilities;
    }
}
