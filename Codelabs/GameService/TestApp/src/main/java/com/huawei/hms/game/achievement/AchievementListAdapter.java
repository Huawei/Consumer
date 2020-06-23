
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

package com.huawei.hms.game.achievement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huawei.hms.R;
import com.huawei.hms.jos.games.achievement.Achievement;

import java.util.ArrayList;
import java.util.List;


public class AchievementListAdapter extends RecyclerView.Adapter<AchievementListAdapter.ViewHolder> {
    private static final String TAG = "AchievementListAdapter";
    private final Context context;
    private OnBtnClickListener mBtnClickListener;
    private List<Achievement> achievementList;

    static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView achievementImage;
            CheckBox cBox;

            TextView achievementName;
            TextView achievementDes;
            TextView achievementUnlock;
            TextView achievementReveal;
            TextView achievementIncrement;
            TextView achievementStep;

            public ViewHolder(View view) {
                super(view);

                cBox =  view.findViewById(R.id.cbox);
                achievementImage = view.findViewById(R.id.achievement_image);

                achievementName = view.findViewById(R.id.achievement_name);
                achievementDes = view.findViewById(R.id.achievement_des);
                achievementUnlock = view.findViewById(R.id.achievement_unlock);
                achievementReveal = view.findViewById(R.id.achievement_reveal);
                achievementIncrement = view.findViewById(R.id.achievement_increment);
                achievementStep = view.findViewById(R.id.achievement_setstep);
            }

        }


        public AchievementListAdapter(Context mContext, List<Achievement> achievements,OnBtnClickListener btnClickListener) {
            context = mContext;
            achievementList = achievements;
            mBtnClickListener = btnClickListener;
        }

        public void setData(ArrayList<Achievement> achievements) {
            achievementList = achievements;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.achievement_list_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Achievement achievement = achievementList.get(position);
            int type = achievement.getType();
            if (type ==2){
                holder.achievementIncrement.setVisibility(View.VISIBLE);
                holder.achievementStep.setVisibility(View.VISIBLE);
            }else {
                holder.achievementIncrement.setVisibility(View.GONE);
                holder.achievementStep.setVisibility(View.GONE);
            }

            int state = achievement.getState();
            if (state ==2){
                holder.achievementUnlock.setVisibility(View.GONE);
                holder.achievementReveal.setVisibility(View.VISIBLE);
            } else if (state ==1){
                holder.achievementUnlock.setVisibility(View.VISIBLE);
                holder.achievementReveal.setVisibility(View.GONE);
            } else {
                holder.achievementUnlock.setVisibility(View.GONE);
                holder.achievementReveal.setVisibility(View.GONE);
                holder.achievementIncrement.setVisibility(View.GONE);
                holder.achievementStep.setVisibility(View.GONE);
            }
            if (allBtnInvisibility(holder.achievementUnlock,holder.achievementReveal,holder.achievementIncrement,holder.achievementStep)) {
                holder.cBox.setVisibility(View.GONE);
            }else {
                holder.cBox.setVisibility(View.VISIBLE);
            }
            final String achievementId = achievement.getId();

            Glide.with(context).load(achievement.getReachedThumbnailUri()).into(holder.achievementImage);

            holder.achievementName.setText(achievement.getDisplayName());
            holder.achievementDes.setText(achievement.getDescInfo());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnClickListener.onItemClick(position);
                }
            });

            holder.achievementUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnClickListener.Unlock(achievementId,holder.cBox.isChecked());
                }
            });
            holder.achievementReveal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnClickListener.reveal(achievementId, holder.cBox.isChecked());
                }
            });
            holder.achievementIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnClickListener.increment(achievementId, holder.cBox.isChecked());
                }
            });
            holder.achievementStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnClickListener.setStep(achievementId, holder.cBox.isChecked());
                }
            });
        }

    private boolean allBtnInvisibility(TextView achievementUnlock, TextView achievementReveal, TextView achievementIncrement, TextView achievementStep) {
        if (achievementUnlock.getVisibility() ==View.GONE
            && achievementReveal.getVisibility() ==View.GONE
            && achievementIncrement.getVisibility() ==View.GONE
            && achievementStep.getVisibility() ==View.GONE){
            return true;
        }
        return false;
    }


    @Override
        public int getItemCount() {
            return achievementList.size();

        }
    /**
     * 自定义接口
     */
    public interface OnBtnClickListener {
        void onItemClick(int postion);

        void Unlock(String achievementId, boolean isChecked);
        void reveal(String achievementId, boolean isChecked);
        void increment(String achievementId, boolean isChecked);
        void setStep(String achievementId, boolean isChecked);
    }
}
