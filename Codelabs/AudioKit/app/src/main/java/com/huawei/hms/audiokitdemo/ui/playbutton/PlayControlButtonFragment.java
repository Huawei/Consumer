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

package com.huawei.hms.audiokitdemo.ui.playbutton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * PlayControlButtonFragment
 *
 * @since 2020-06-01
 */
public class PlayControlButtonFragment extends Fragment implements OnClickListener {

    private static final String TAG = "PlayControlButton";

    private static final float BUTTON_DEFAULT_ALPHA = 0.2f;

    private ImageView mPlay;

    private View rootView;

    private ProgressBar mProgressBar;

    /**
     * [get PlayControlButtonFragment]<BR>
     *
     * @return PlayControlButtonFragment
     */
    public static PlayControlButtonFragment newInstance() {
        return new PlayControlButtonFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = R.layout.playcontorlbutton_fragment_layout;
        rootView = inflater.inflate(layoutId, null);
        initView();
        setPauseButtonImage();
        return rootView;
    }

    private void initView() {
        ImageView mPre = ViewUtils.findViewById(rootView, R.id.pre_imagebutton);
        mPre.setOnClickListener(this);
        ImageView mNext = ViewUtils.findViewById(rootView, R.id.next_iamgebutton);
        mNext.setOnClickListener(this);
        mPlay = ViewUtils.findViewById(rootView, R.id.play_imagebutton);
        RelativeLayout mPlayLayout = ViewUtils.findViewById(rootView, R.id.play_button_layout);
        mPlayLayout.setOnClickListener(this);
        ImageView mPlayBackground = ViewUtils.findViewById(rootView, R.id.play_background);
        mPlayBackground.setAlpha(BUTTON_DEFAULT_ALPHA);

        mProgressBar = ViewUtils.findViewById(rootView, R.id.loading_progressbar);
        mProgressBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_imagebutton:
                PlayHelper.getInstance().prev();
                break;
            case R.id.next_iamgebutton:
                PlayHelper.getInstance().next();
                break;
            case R.id.play_button_layout:
            case R.id.loading_progressbar:
                if (PlayHelper.getInstance().isPlaying()) {
                    PlayHelper.getInstance().pause();
                } else {
                    PlayHelper.getInstance().play();
                }
                break;
            default:
                break;
        }
    }

    /**
     * [setPauseButtonImage]<BR>
     */
    public void setPauseButtonImage() {
        if (null == mPlay) {
            Log.i(TAG, "setPauseButtonImage err");
            return;
        }
        boolean isPlaying = PlayHelper.getInstance().isPlaying();
        Log.i(TAG, "setPauseButtonImage,isPlaying: " + isPlaying);
        ViewUtils.setVisibility(mPlay, true);
        if (isPlaying) {
            mPlay.setImageResource(R.drawable.btn_playback_pause_normal);
        } else {
            mPlay.setImageResource(R.drawable.btn_playback_play_normal);
        }
        setLoadingImage();
    }

    /**
     * [setLoadingImage]<BR>
     */
    private void setLoadingImage() {
        int visible = PlayHelper.getInstance().isBuffering() ? View.VISIBLE : View.INVISIBLE;
        ViewUtils.setVisibility(mProgressBar, visible);
    }
}
