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

package com.huawei.hms.audiokitdemo.ui.seek;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.utils.StringUtils;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * SeekBarFragment
 *
 * @since 2020-06-01
 */
public class SeekBarFragment extends Fragment {
    private static final int MSG_REFRESH = 1;

    private TextView mPlayTimeTextView = null;

    private TextView mTotalTimeTextView = null;

    private SeekBar mPlaySeekBar = null;

    private long mPosOverride = -1;

    private long mDuration = -1;

    private boolean mFromTouch = false;

    private long mTempPosition = -5;

    private int mCurrentBufferPercent = 0;

    private Runnable mClearTask = new Runnable() {

        @Override
        public void run() {
            mTotalTimeTextView.setText(StringUtils.localeString("00:00"));
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_REFRESH == msg.what) {
                refreshNow(false);
            }
        }
    };

    /**
     * [seekbar listener]<BR>
     */
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        private int mProgress;

        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mFromTouch = true;
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }
            mProgress = progress;
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {

            mPosOverride = mDuration * mProgress / 1000;
            PlayHelper.getInstance().seek(mPosOverride);

            if (!mFromTouch) {
                refreshNow(true);
            }
            mFromTouch = false;
            mPosOverride = -1;

            if (mDuration == -1 && mTempPosition == -1) {
                setProgressValue(0);
            }
        }
    };

    /**
     * [RefreshTask]<BR>
     */
    @SuppressLint("StaticFieldLeak")
    private class RefreshTask extends AsyncTask<Boolean, Void, Boolean> {

        String curPosString;

        String durationString;

        int progressValue;

        @Override
        protected Boolean doInBackground(Boolean... params) {
            if (getActivity() == null) {
                return false;
            }

            if (mFromTouch || getActivity().isFinishing()) {
                return false;
            }
            boolean force = params[0];
            int percent = PlayHelper.getInstance().getBufferPercentage();
            long position = mPosOverride < 0 ? PlayHelper.getInstance().getPosition() : mPosOverride;
            long duration = PlayHelper.getInstance().getDuration();
            if (!force && mTempPosition == position && mDuration == duration && percent == mCurrentBufferPercent) {
                return false;
            }

            mTempPosition = position;
            mDuration = duration;
            mCurrentBufferPercent = percent;
            if (duration > 0) {
                if (position > (duration - 500)) {
                    position = duration;
                }
                double currentTime = ((double) position) / 1000;
                long timePassed =
                    (currentTime - (long) currentTime) > 0.8 ? (long) currentTime + 1 : (long) currentTime;
                long totalTime = duration / 1000;
                if (timePassed > totalTime) {
                    timePassed = totalTime;
                }
                curPosString = StringUtils.localeString(getContext(), timePassed);
                durationString = StringUtils.localeString(getContext(), totalTime);
                progressValue = 0 == totalTime ? 0 : (int) (1000 * timePassed / totalTime);
            } else {
                curPosString = StringUtils.localeString("00:00");
                durationString = curPosString;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            queueNextRefresh(result != null && result ? doFresh() : 500);
        }

        private long doFresh() {
            mHandler.removeCallbacks(mClearTask);
            long remaining = 1000 - (mTempPosition % 1000);
            mTotalTimeTextView.setText(durationString);
            mPlayTimeTextView.setText(curPosString);
            setProgressValue(progressValue);
            mPlaySeekBar.setSecondaryProgress(mCurrentBufferPercent * 10);
            return remaining;
        }
    }

    /**
     * [SeekBarFragment]<BR>
     * @return SeekBarFragment
     */
    public static SeekBarFragment newInstance() {
        return new SeekBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seekbar_fragment_layout, null);
        mPlaySeekBar = view.findViewById(R.id.play_seekbar);
        mPlayTimeTextView = view.findViewById(R.id.playtime_text);
        mTotalTimeTextView = ViewUtils.findViewById(view, R.id.totaltime_text);
        mPlaySeekBar.setOnSeekBarChangeListener(mSeekListener);
        mPlaySeekBar.setMax(1000);
        mPlaySeekBar.setProgress(0);
        mPlaySeekBar.setSecondaryProgress(0);
        String zero = StringUtils.localeString("00:00");
        mPlayTimeTextView.setText(zero);
        mTotalTimeTextView.setText(zero);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onStop() {
        mHandler.removeMessages(MSG_REFRESH);
        super.onStop();
    }

    /**
     * [refresh]<BR>
     */
    public void refresh() {
        if (PlayHelper.getInstance().isQueueEmpty()) {
            return;
        }
        refreshNow(true);
    }

    private void refreshNow(boolean force) {
        new RefreshTask().execute(force);
    }

    private void setProgressValue(int pos) {
        mPlaySeekBar.setProgress(pos);
    }

    private void queueNextRefresh(long delay) {
        if (null != getActivity() && isResumed() && PlayHelper.getInstance().isPlaying()) {
            Message msg = mHandler.obtainMessage(MSG_REFRESH);
            mHandler.removeMessages(MSG_REFRESH);
            mHandler.sendMessageDelayed(msg, delay);
        }
    }
}
