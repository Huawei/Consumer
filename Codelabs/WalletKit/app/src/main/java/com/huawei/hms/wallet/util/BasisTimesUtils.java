/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

package com.huawei.hms.wallet.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasisTimesUtils {
    private static DatePickerDialog mDatePickerDialog;
    public static Long getLongTimeOfYMD(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
        }
        return 0L;
    }

    public static BasisTimesUtils showDatePickerDialog(Context context, String title, int year, int month, int day, OnDatePickerListener onDateTimePickerListener) {
        return showDatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, title, year, month, day, onDateTimePickerListener);
    }

    public static BasisTimesUtils showDatePickerDialog(Context context, int themeId, String title, int year, int month, int day,
                                                       final OnDatePickerListener onDateTimePickerListener) {
        mDatePickerDialog = new DatePickerDialog(context, themeId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onConfirm(year, month, dayOfMonth);
                }
            }
        }, year, month - 1, day);

        mDatePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onCancel();
                }
            }
        });

        if (!TextUtils.isEmpty(title)) {
            mDatePickerDialog.setTitle(title);
        }
        mDatePickerDialog.show();
        return new BasisTimesUtils();
    }

    public interface OnDatePickerListener {
        void onConfirm(int year, int month, int dayOfMonth);

        void onCancel();
    }
}