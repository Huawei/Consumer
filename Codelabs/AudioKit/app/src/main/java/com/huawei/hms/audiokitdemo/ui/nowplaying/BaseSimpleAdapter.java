
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
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseAdapter,provide some public attributes
 *
 * @since 2020-06-01
 */
public abstract class BaseSimpleAdapter<E> extends BaseAdapter {
    List<E> mDataSource = new ArrayList<>();

    LayoutInflater mInflater;

    /**
     *
     * BaseSimpleAdapter
     *
     * @param context context
     */
    BaseSimpleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == mDataSource ? 0 : mDataSource.size();
    }

    @Override
    public E getItem(int position) {
        if (position < 0 || position >= mDataSource.size()) {
            return null;
        }
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * setDataSource<BR>
     *
     * @param dataSource dataSource
     */
    void setDataSource(List<E> dataSource) {
        mDataSource.clear();
        if (dataSource != null) {
            mDataSource.addAll(dataSource);
        }
    }
}
