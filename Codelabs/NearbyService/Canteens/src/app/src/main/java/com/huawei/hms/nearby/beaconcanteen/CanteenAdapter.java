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

package com.huawei.hms.nearby.beaconcanteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hms.nearby.beaconcanteen.model.CanteenAdapterInfo;

import java.util.List;

/**
 * Canteen Adapter
 *
 * @since 2019-12-13
 */
public class CanteenAdapter extends BaseAdapter {
    /**
     * View Holder
     *
     * @since 2019-12-13
     */
    static class ViewHolder {
        ImageView canteenIv;
        TextView canteenNameTv;
        TextView canteenDescTv;
        TextView canteenPriceTv;
        TextView discountDesTv;
        LinearLayout discountLayout;
    }

    private Context mContext;
    private List<CanteenAdapterInfo> mCanteenAdapterInfoList;

    /**
     * Canteen Adapter Construct
     *
     * @param context Context
     * @param canteenAdapterInfoList CanteenAdapterInfoList
     */
    public CanteenAdapter(Context context, List<CanteenAdapterInfo> canteenAdapterInfoList) {
        this.mContext = context;
        this.mCanteenAdapterInfoList = canteenAdapterInfoList;
    }

    @Override
    public int getCount() {
        return mCanteenAdapterInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCanteenAdapterInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView = view;
        ViewHolder holder = new ViewHolder();
        CanteenAdapterInfo item = mCanteenAdapterInfoList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.canteen_list_item, null, false);
            holder.canteenIv = convertView.findViewById(R.id.iv_canteen);
            holder.canteenDescTv = convertView.findViewById(R.id.tv_canteen_desc);
            holder.canteenNameTv = convertView.findViewById(R.id.tv_canteen_name);
            holder.canteenPriceTv = convertView.findViewById(R.id.tv_canteen_price);
            holder.discountLayout = convertView.findViewById(R.id.ll_discount);
            holder.discountDesTv = convertView.findViewById(R.id.tv_discount_des);
            convertView.setTag(holder);
        } else {
            Object object = convertView.getTag();
            if (object instanceof ViewHolder) {
                holder = (ViewHolder) object;
            }
        }
        holder.canteenIv.setImageResource(item.getCanteenImage());
        holder.canteenDescTv.setText(item.getCanteenDesc());
        holder.canteenNameTv.setText(item.getCanteenName());
        holder.canteenPriceTv.setText(item.getCanteenScore());
        if (item.isShowNotice()) {
            holder.discountLayout.setVisibility(View.VISIBLE);
            holder.discountDesTv.setText(item.getNotice());
        } else {
            holder.discountLayout.setVisibility(View.GONE);
            holder.discountDesTv.setText("");
        }
        return convertView;
    }

    /**
     * Set Canteen Adapter Info Data
     *
     * @param canteenAdapterInfoList Canteen Adapter Info List
     */
    public void setDatas(List<CanteenAdapterInfo> canteenAdapterInfoList) {
        mCanteenAdapterInfoList = canteenAdapterInfoList;
        notifyDataSetChanged();
    }
}
