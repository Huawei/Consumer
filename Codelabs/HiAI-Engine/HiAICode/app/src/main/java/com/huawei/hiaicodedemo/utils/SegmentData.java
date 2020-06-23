package com.huawei.hiaicodedemo.utils;

public class SegmentData {
    public byte[] src;

    public byte[] result;

    int width;

    int height;

    public SegmentData(byte[] s, byte[] r, int w, int h) {
        src = s;
        result = r;
        width = w;
        height = h;
    }
}
