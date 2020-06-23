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
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;

/**
 * Play mode utils
 *
 * @since 2020-06-01
 */
public final class PlayModeUtils {

    /**
     * mode size
     */
    private static final int MODE_SIZE = 4;

    /**
     * Normal model
     */
    private static final int MODE_SHUFFLE_OFF = 0;

    /**
     * SHUFFLE model
     */
    private static final int MODE_SHUFFLE_ON = 1;

    /**
     * REPEAT_ALL model
     */
    private static final int MODE_REPEAT_ALL = 2;

    /**
     * REPEAT_SELF model
     */
    private static final int MODE_REPEAT_SELF = 3;

    private static final String TAG = "PlayModeUtils";

    private static final PlayModeUtils INSTANCE = new PlayModeUtils();

    private PlayModeUtils() {

    }

    /**
     * get static instance
     * @return instance
     */
    public static PlayModeUtils getInstance() {
        return INSTANCE;
    }

    /**
     * [change play mode]<BR>
     *
     * @param view play mode imageview
     */
    public void changePlayMode(Context context, ImageView view) {
        Log.i(TAG, "changePlayMode");
        if (null == view || context == null) {
            return;
        }
        int playMode = PlayHelper.getInstance().getPlayMode();
        playMode = (playMode + 1) % PlayModeUtils.MODE_SIZE;
        switch (playMode) {
            case MODE_SHUFFLE_OFF:
                view.setContentDescription(context.getString(R.string.mode_shuffle_off));
                toastShortMsg(context, R.string.mode_shuffle_off);
                view.setImageResource(R.drawable.menu_order_normal);
                PlayHelper.getInstance().setPlayMode(MODE_SHUFFLE_OFF);
                break;
            case MODE_SHUFFLE_ON:
                view.setContentDescription(context.getString(R.string.mode_shuffle_on));
                toastShortMsg(context, R.string.mode_shuffle_on);
                view.setImageResource(R.drawable.menu_shuffle_normal);
                PlayHelper.getInstance().setPlayMode(MODE_SHUFFLE_ON);
                break;
            case MODE_REPEAT_ALL:
                view.setContentDescription(context.getString(R.string.mode_repeat_all));
                toastShortMsg(context, R.string.mode_repeat_all);
                view.setImageResource(R.drawable.menu_loop_normal);
                PlayHelper.getInstance().setPlayMode(MODE_REPEAT_ALL);
                break;
            case MODE_REPEAT_SELF:
                view.setContentDescription(context.getString(R.string.mode_repeat_current));
                toastShortMsg(context, R.string.mode_repeat_current);
                view.setImageResource(R.drawable.menu_loop_one_normal);
                PlayHelper.getInstance().setPlayMode(MODE_REPEAT_SELF);
                break;
            default:
                break;
        }
    }

    private void toastShortMsg(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * update play mode
     * @param context context
     * @param view show imageview
     */
    public void updatePlayMode(Context context, ImageView view) {
        Log.i(TAG, "updatePlayMode");
        if (null == view || context == null) {
            return;
        }
        int mode = PlayHelper.getInstance().getPlayMode();
        if (mode < 0) {
            return;
        }
        switch (mode) {
            case MODE_SHUFFLE_OFF:
                view.setContentDescription(context.getString(R.string.mode_shuffle_off));
                view.setImageResource(R.drawable.menu_order_normal);
                break;
            case MODE_SHUFFLE_ON:
                view.setContentDescription(context.getString(R.string.mode_shuffle_on));
                view.setImageResource(R.drawable.menu_shuffle_normal);
                break;
            case MODE_REPEAT_ALL:
                view.setContentDescription(context.getString(R.string.mode_repeat_all));
                view.setImageResource(R.drawable.menu_loop_normal);
                break;
            case MODE_REPEAT_SELF:
                view.setContentDescription(context.getString(R.string.mode_repeat_current));
                view.setImageResource(R.drawable.menu_loop_one_normal);
                break;
            default:
                Log.w(TAG, "Unknown mode:" + mode);
                break;
        }
    }
}
