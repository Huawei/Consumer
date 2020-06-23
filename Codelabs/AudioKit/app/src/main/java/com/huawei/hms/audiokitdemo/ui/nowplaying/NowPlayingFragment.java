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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * NowPlayingFragment
 *
 * @since 2020-06-01
 */
public class NowPlayingFragment extends Fragment implements OnScrollListener {
    private static final String TAG = "NowPlayingFragment";

    private Activity mActivity = null;

    private View mContextView = null;

    private ListView mListView;

    private int mVisiblePos;

    private int mVisibleCount;

    private List<HwAudioPlayItem> mSongs = null;

    private NowPlayingAdapter mOnlineAdapter;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            showResult();
        }
    };

    /**
     *  get NowPlayingFragment Instance
     *
     * @param serviceInit is service init
     * @return NowPlayingFragment
     */
    public static NowPlayingFragment newInstance(boolean serviceInit) {
        NowPlayingFragment fragment = new NowPlayingFragment();
        Bundle args = new Bundle();
        args.putBoolean("serviceInit", serviceInit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mContextView = inflater.inflate(R.layout.nowplaying_content, container, false);
        initListView();
        return mContextView;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        initNowPlayingList();
    }

    private void initListView() {
        mListView = ViewUtils.findViewById(mContextView, R.id.nowplaying_view);
        mListView.setCacheColorHint(0);
        if (mOnlineAdapter == null) {
            mOnlineAdapter = new NowPlayingAdapter(mSongs, mActivity);
        }

        mListView.setAdapter(mOnlineAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayHelper.getInstance().playAt(position - mListView.getHeaderViewsCount());
            }
        });
        mListView.setOnScrollListener(this);
    }

    /**
     * init playing queue list
     */
    public void initNowPlayingList() {
        Log.i(TAG, "initNowPlayingList");
        if (mListView != null) {
            mSongs = PlayHelper.getInstance().getAllPlaylist();
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    }

    /**
     * Init playlist if current is empty
     */
    public void initIfEmpty() {
        if (mSongs == null || mSongs.isEmpty()) {
            initNowPlayingList();
        }
    }

    private boolean hasSongs() {
        return mSongs != null && mSongs.size() > 0;
    }

    /**
     * update playing item
     */
    public void updatePlayingPos() {
        if (mListView != null) {
            int pos = PlayHelper.getInstance().getCurrentIndex();
            mListView.invalidateViews();
            if (pos > mVisiblePos && pos < mVisiblePos + mVisibleCount) {
                return;
            }
            mListView.setSelection(pos);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mVisiblePos = firstVisibleItem;
        mVisibleCount = visibleItemCount;
    }

    private void showResult() {
        if (hasSongs()) {
            mListView.setVisibility(View.VISIBLE);
            mContextView.findViewById(R.id.nowplaying_no_songs).setVisibility(View.GONE);
            mOnlineAdapter.setDataSource(mSongs);
        } else {
            mListView.setVisibility(View.GONE);
            mContextView.findViewById(R.id.nowplaying_no_songs).setVisibility(View.VISIBLE);
        }
        updatePlayingPos();
    }
}
