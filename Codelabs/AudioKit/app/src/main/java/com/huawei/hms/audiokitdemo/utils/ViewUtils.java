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

import android.app.Activity;
import android.view.View;

/**
 * View utils
 *
 * @since 2020-06-01
 */
public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * set view visibility
     *
     * @param view view
     * @param visibility visibility
     */
    public static void setVisibility(View view, int visibility) {
        if (null != view) {
            view.setVisibility(visibility);
        }
    }

    /**
     * set view visibility
     *
     * @param view view
     * @param isVisibility true:View.VISIBLE false:View.GONE
     */
    public static void setVisibility(View view, boolean isVisibility) {
        if (null != view) {
            view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * Look for a child view with the given id. If this view has the given id, return this view.<BR>
     *
     * @param <T> template
     *
     * @param view parentview
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View view, int id) {
        if (null != view) {
            return (T) view.findViewById(id);
        }
        return null;
    }

    /**
     * Look for a child view with the given id. If this view has the given id, return this view.<BR>
     *
     * @param <T> template
     *
     * @param activity activity
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, int id) {
        if (null != activity) {
            return (T) activity.findViewById(id);
        }
        return null;
    }

}
