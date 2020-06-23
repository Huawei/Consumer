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
 */

package com.huawei.hms.audiokitdemo.utils;

import android.content.Context;

import com.huawei.hms.audiokitdemo.R;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

/**
 * String utils
 *
 * @since 2020-06-01
 */
public final class StringUtils {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private StringUtils() {
    }

    /**
     * [get formatted string]<BR>
     *
     * @param context context
     * @param secs time in second
     * @return formatted string
     */
    public static String makeTimeString(Context context, long secs) {
        StringBuilder sFormatBuilder = new StringBuilder();
        Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
        LOCK.lock();
        String sTime;
        try {
            String durationFormat =
                context.getString(secs >= 216000 ? R.string.durationformatlong : R.string.durationformatshort);
            final Object[] timeArgs = new Object[5];
            timeArgs[0] = secs / 3600;
            timeArgs[1] = secs / 60;
            timeArgs[2] = (secs / 60) % 60;
            timeArgs[3] = secs;
            timeArgs[4] = secs % 60;
            sTime = sFormatter.format(durationFormat, timeArgs).toString();
        } finally {
            LOCK.unlock();
            sFormatter.close();
        }

        return sTime;
    }

    /**
     * [local time]<BR>
     *
     * @param context context
     * @param secs time in second
     * @return local time string
     */
    public static String localeString(Context context, long secs) {
        String s = StringUtils.makeTimeString(context, secs);
        return localeString(s);
    }

    /**
     * [local time]<BR>
     *
     * @param s time info
     * @return local time string
     */
    public static String localeString(String s) {
        int zero = 0;
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setGroupingUsed(false);
        char localeZero = nf.format(zero).charAt(0);
        if (localeZero != '0') {
            int length = s.length();
            int offsetToLocalizedDigits = localeZero - '0';
            StringBuilder result = new StringBuilder(length);
            for (int i = 0; i < length; ++i) {
                char ch = s.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    ch += offsetToLocalizedDigits;
                }
                result.append(ch);
            }
            return result.toString();
        }
        return s;
    }

}
