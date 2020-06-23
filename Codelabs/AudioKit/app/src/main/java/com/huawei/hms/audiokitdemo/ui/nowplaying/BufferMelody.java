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

package com.huawei.hms.audiokitdemo.ui.nowplaying;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

/**
 * BufferMelody
 *
 * @since 2020-06-01
 */
public class BufferMelody extends RelativeLayout {
    private MelodyView mMelodyView;

    private ProgressBar mProgress;

    /**
     * BufferMelody
     *
     * @param context context
     */
    public BufferMelody(Context context) {
        super(context);
        setWillNotDraw(false);
        init();
    }

    /**
     * BufferMelody
     *
     * @param context context
     * @param attrs AttributeSet
     */
    public BufferMelody(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    /**
     * BufferMelody
     *
     * @param context context
     * @param attrs attrs
     * @param defStyle style
     */
    public BufferMelody(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        init();
    }

    protected void init() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate.inflate(R.layout.buffer_melody_layout, this, true);

        mMelodyView = ViewUtils.findViewById(this, R.id.melody_view);
        mProgress = ViewUtils.findViewById(this, R.id.loading_progress);
    }

    /**
     * [updateState]<BR>
     *
     * @param songBean songBean
     */
    public void updateState(HwAudioPlayItem songBean) {
        if (songBean == null) {
            hideAll();
            return;
        }

        if (songBean.getAudioId().equals(PlayHelper.getInstance().getCurrentPlayItem().getAudioId())) {
            if (PlayHelper.getInstance().isBuffering()) {
                mProgress.setVisibility(View.VISIBLE);
                mMelodyView.setVisibility(View.GONE);
            } else {
                mProgress.setVisibility(View.GONE);
                mMelodyView.setVisibility(View.VISIBLE);
            }
            setVisibility(View.VISIBLE);
        } else {
            hideAll();
        }
    }

    /**
     * [hideAll]<BR>
     */
    public void hideAll() {
        mProgress.setVisibility(View.GONE);
        mMelodyView.setVisibility(View.GONE);
        setVisibility(View.GONE);
    }

    /**
     * [setColor]<BR>
     *
     * @param color color
     */
    public void setColor(int color) {
        if (null != mMelodyView) {
            mMelodyView.setColor(color);
        }
    }
}
