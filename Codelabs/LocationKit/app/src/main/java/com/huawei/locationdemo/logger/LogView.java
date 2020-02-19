/*
 * Copyright (C) Huawei Technologies Co., Ltd. 2016. All rights reserved.
 * See LICENSE.txt for this sample's licensing information.
 */

package com.huawei.locationdemo.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class LogView extends AppCompatEditText implements LogNode {

    private LogNode mNext;

    public LogView(Context context) {
        super(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LogNode getNext() {
        return mNext;
    }

    public void setNext(LogNode node) {
        mNext = node;
    }

    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {

        String priorityStr = null;

        switch (priority) {
            case LocationLog.DEBUG:
                priorityStr = "D";
                break;
            case LocationLog.INFO:
                priorityStr = "I";
                break;
            case LocationLog.WARN:
                priorityStr = "W";
                break;
            case LocationLog.ERROR:
                priorityStr = "E";
                break;
            default:
                break;
        }

        String exceptionStr = null;
        if (tr != null) {
            exceptionStr = android.util.Log.getStackTraceString(tr);
        }

        final StringBuilder outputBuilder = new StringBuilder();

        // String delimiter = "|";
        // appendIfNotNull(outputBuilder, priorityStr, delimiter);
        // appendIfNotNull(outputBuilder, tag, delimiter);
        // appendIfNotNull(outputBuilder, msg, delimiter);
        // appendIfNotNull(outputBuilder, exceptionStr, "");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        outputBuilder.append(str);
        outputBuilder.append(" ");
        outputBuilder.append(msg);
        outputBuilder.append("\r\n");

        ((Activity) getContext()).runOnUiThread((new Thread(new Runnable() {
            @Override
            public void run() {
                appendToLog(outputBuilder.toString());
            }
        })));

        if (mNext != null) {
            mNext.println(priority, tag, msg, tr);
        }
    }

    public void appendToLog(String s) {
        append("\n" + s);
    }

    private StringBuilder appendIfNotNull(StringBuilder source, String addStr, String delimiter) {
        if (addStr != null) {
            if (addStr.length() == 0) {
                delimiter = "";
            }

            return source.append(addStr).append(delimiter);
        }
        return source;
    }

}
