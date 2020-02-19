/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.huawei.hmsawarenesssample.logger;

import android.app.Activity;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;

import java.util.Date;

public class LogView extends TextView {
    public LogView(Context context) {
        super(context);
    }

    public LogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void printLog(final String msg) {
        final StringBuilder builder = new StringBuilder();
        DateFormat formatter = SimpleDateFormat.getDateTimeInstance();
        String time = formatter.format(new Date(System.currentTimeMillis()));
        builder.append(time);
        builder.append("\n");
        builder.append(msg);
        builder.append(System.lineSeparator());

        ((Activity) getContext())
                .runOnUiThread(new Thread(()-> append("\n" + builder.toString())));
    }
}
