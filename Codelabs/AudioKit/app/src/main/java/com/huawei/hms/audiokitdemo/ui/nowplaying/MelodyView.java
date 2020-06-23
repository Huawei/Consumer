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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;

import java.lang.ref.WeakReference;
import java.security.SecureRandom;

import androidx.annotation.NonNull;

/**
 * MelodyView
 *
 * @since 2020-06-01
 */
public class MelodyView extends View {
    /**
     * MelodyView max count
     */
    private static final int MELODY_COUNT = 5;

    /**
     * refresh msg id
     */
    private static final int FRESH = 1;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int DATA_LEN = 8;

    private static byte[] mPausedBytes;

    static class MyHandler extends Handler {
        private final WeakReference<MelodyView> mView;

        MyHandler(MelodyView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            MelodyView view = mView.get();
            if (view != null) {
                view.handleMessage();
            }
        }
    }

    private byte[] model = new byte[MELODY_COUNT + 1];

    private float factor = 0.5f;

    /**
     * handler
     */
    private Handler mHandler;

    private byte[] mBytes;

    private float[] mPoints;

    private Paint mForePaint = new Paint();

    private byte[] mOriginData = new byte[DATA_LEN];

    private float mHeight;

    private float mWidth;

    private float mGap;

    private byte minHeight;

    private int mCount = MELODY_COUNT;

    private boolean isVisable = false;

    /**
     * [MelodyView]
     *
     * @param context context
     */
    public MelodyView(Context context) {
        super(context);
        init();
    }

    /**
     * [MelodyView]
     *
     * @param context context
     * @param attrs AttributeSet
     */
    public MelodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * [MelodyView]
     *
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public MelodyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private static void saveToStaticByte(byte[] bytes) {
        mPausedBytes = bytes;
    }

    private void init() {
        mHeight = getResources().getDimension(R.dimen.melody_height);
        factor = mHeight / 128;
        mForePaint.setColor(16718153);
        mWidth = getResources().getDimension(R.dimen.melody_width);
        mGap = getResources().getDimension(R.dimen.melody_gap);
        minHeight = (byte) getResources().getDimensionPixelSize(R.dimen.melody_gap);
        mForePaint.setStrokeWidth(mWidth);
        mForePaint.setAntiAlias(true);

        RANDOM.nextBytes(mOriginData);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mHandler) {
            mHandler.removeMessages(FRESH);
            mHandler = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBytes == null) {
            updateFFT(PlayHelper.getInstance().isPlaying());
            if (mBytes == null) {
                return;
            }
        }

        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        final float margin = (getWidth() - mCount * mWidth - (mCount - 1) * mGap) / 2;
        final float height = getHeight() - (getHeight() - mHeight) / 2;
        final float gapBetween = mWidth + mGap;
        for (int i = 0; i < mCount; i++) {
            final float xi = gapBetween * i + margin;

            mPoints[i * 4] = xi;
            mPoints[i * 4 + 1] = height;
            mPoints[i * 4 + 2] = xi;
            mPoints[i * 4 + 3] = height - mBytes[i];
        }
        canvas.drawLines(mPoints, mForePaint);
    }

    /**
     * [updateFFT]<BR>
     *
     * @param isPlaying whether playing
     */
    private void updateFFT(boolean isPlaying) {
        if (!isVisable) {
            return;
        }

        byte[] fft = mOriginData;

        if (isPlaying) {
            saveToStaticByte(null);
            for (int i = 0; i < mCount; i++) {
                int sep = RANDOM.nextInt(64);
                boolean add = RANDOM.nextBoolean();
                fft[i] = (byte) (add ? Math.abs(fft[i]) + sep : Math.abs(fft[i]) - sep);
            }
        } else {
            if (mPausedBytes == null) {
                saveToStaticByte(mOriginData);
            }
            fft = mPausedBytes;
        }

        model[0] = (byte) (Math.abs(fft[0]) * factor);

        if (model[0] < minHeight) {
            model[0] = minHeight;
        }

        for (int j = 1; j < mCount;) {
            byte result = (byte) (Math.abs(fft[j]) * factor);
            if (result < minHeight) {
                result = minHeight;
            }

            model[j] = result;
            j++;
        }

        mBytes = model;
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        if (null != mHandler) {
            mHandler.removeMessages(FRESH);
        }

        if (visibility == View.VISIBLE) {
            if (mHandler == null) {
                mHandler = new MyHandler(this);
            }
            mHandler.sendEmptyMessage(FRESH);
        }

        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (getVisibility() == VISIBLE) {
            if (mHandler == null) {
                mHandler = new MyHandler(this);
            }
            if (!mHandler.hasMessages(FRESH)) {
                mHandler.sendEmptyMessage(FRESH);
            }
        }
    }


    private void handleMessage() {
        boolean isPlaying = PlayHelper.getInstance().isPlaying();
        updateFFT(isPlaying);
        mHandler.removeMessages(FRESH);
        if (isVisable && isPlaying) {
            mHandler.sendEmptyMessageDelayed(FRESH, 120);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        isVisable = visibility == View.VISIBLE;
        super.onVisibilityChanged(changedView, visibility);
    }

    /**
     * [setColor]<BR>
     *
     * @param color color
     */
    public void setColor(int color) {
        mForePaint.setColor(color);
    }
}
