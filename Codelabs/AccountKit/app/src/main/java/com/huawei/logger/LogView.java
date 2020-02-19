/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.huawei.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.huawei.logger.Log.LogNode;

public class LogView extends TextView implements LogNode {

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
        case Log.DEBUG:
            priorityStr = "D";
            break;
        case Log.INFO:
            priorityStr = "I";
            break;
        case Log.WARN:
            priorityStr = "W";
            break;
        case Log.ERROR:
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

        //String delimiter = "|";
        //appendIfNotNull(outputBuilder, priorityStr, delimiter);
        //appendIfNotNull(outputBuilder, tag, delimiter);
        //appendIfNotNull(outputBuilder, msg, delimiter);
        //appendIfNotNull(outputBuilder, exceptionStr, "");
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
