/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.huawei.hms.nearbyconnectiondemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ChatAdapter
 *
 * @since 2020-01-13
 */
public class ChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageBean> msgList;

    public ChatAdapter(Context mContext, List<MessageBean> msgList) {
        this.mContext = mContext;
        this.msgList = msgList;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MessageBean item = msgList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, parent, false);
            holder = new ViewHolder();
            holder.sendTv = convertView.findViewById(R.id.tv_send);
            holder.receiveTv = convertView.findViewById(R.id.tv_receive);
            convertView.setTag(holder);
        } else {
            if (convertView.getTag() instanceof ViewHolder) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
            }
        }
        if (item.isSend()) {
            holder.sendTv.setVisibility(View.VISIBLE);
            holder.receiveTv.setVisibility(View.GONE);
            holder.sendTv.setText(item.getMsg());
        } else {
            holder.sendTv.setVisibility(View.GONE);
            holder.receiveTv.setVisibility(View.VISIBLE);
            holder.receiveTv.setText(item.getMsg());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView sendTv;
        TextView receiveTv;
    }

    public void refreshData(List<MessageBean> list) {
        this.msgList = list;
        notifyDataSetChanged();
    }
}
