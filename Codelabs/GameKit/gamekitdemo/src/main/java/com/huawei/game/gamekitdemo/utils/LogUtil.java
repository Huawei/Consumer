/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.game.gamekitdemo.utils;

import android.util.Log;

/**
 * Log with Switch
 *
 * @since 2019-08-26
 */
public class LogUtil {
    /**
     * nolog level
     */
    private static final int NOLOG = 0;
    /**
     * elog level
     */
    private static final int ERROR = 1;
    /**
     * wlog level
     */
    private static final int WARN = 2;
    /**
     * ilog level
     */
    private static final int INFO = 3;
    /**
     * dlog level
     */
    private static final int DEBUG = 4;
    /**
     * vlog level
     */
    private static final int VERBOSE = 5;

    private static int logLevel = INFO;

    private LogUtil() {
    }

    /**
     * Log.v with debug version check
     *
     * @param tag log tag
     * @param content log content
     */
    public static void v(String tag, String content) {
        if (logLevel >= VERBOSE) {
            Log.v(tag, content);
        }
    }

    /**
     * Log.d with debug version check
     *
     * @param tag log tag
     * @param content log content
     */
    public static void d(String tag, String content) {
        if (logLevel >= DEBUG) {
            Log.d(tag, content);
        }
    }

    /**
     * Log.i with debug version check
     *
     * @param tag log tag
     * @param content log content
     */
    public static void i(String tag, String content) {
        if (logLevel >= INFO) {
            Log.i(tag, content);
        }
    }

    /**
     * actually it is android.util.Log.w
     *
     * @param tag log tag
     * @param content log content
     */
    public static void w(String tag, String content) {
        if (logLevel >= WARN) {
            Log.w(tag, content);
        }
    }

    /**
     * actually it is android.util.Log.e
     *
     * @param tag log tag
     * @param content log content
     */
    public static void e(String tag, String content) {
        if (logLevel >= ERROR) {
            Log.e(tag, content);
        }
    }
}
