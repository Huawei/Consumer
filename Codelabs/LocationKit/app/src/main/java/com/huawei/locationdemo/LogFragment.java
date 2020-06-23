/*
 * Copyright (C) Huawei Technologies Co., Ltd. 2016. All rights reserved.
 * See LICENSE.txt for this sample's licensing information.
 */

package com.huawei.locationdemo;


import com.huawei.locationdemo.logger.LogView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * util
 */
public class LogFragment extends Fragment {

    private LogView mLogView;

    private ScrollView mScrollView;

    public LogFragment() {
    }

    private View inflateViews() {
        mScrollView = new ScrollView(getActivity());

        mScrollView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mLogView = new LogView(getActivity());
        mLogView.setMinLines(8);
        mLogView.setClickable(true);
        mLogView.setGravity(Gravity.TOP);
        mLogView.setId(R.id.output_information_id);
        mLogView.setCursorVisible(false);
        mLogView.setFocusable(false);
        mLogView.setFocusableInTouchMode(false);

        mScrollView.addView(mLogView,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return mScrollView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflateViews();

        mLogView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mScrollView.post(new Runnable() {

                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }

                });
            }

        });

        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mLogView.setText("");
                return true;
            }
        });

        mLogView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        return result;
    }

    public LogView getLogView() {
        return mLogView;
    }

}
