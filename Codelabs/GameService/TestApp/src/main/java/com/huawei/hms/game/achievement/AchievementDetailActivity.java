
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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huawei.hms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AchievementDetailActivity extends Activity {
    private static final String TAG = "AchievementDetaile";
    @BindView(R.id.achievement_image)
    public ImageView achievementImage;

    @BindView(R.id.achievement_image2)
    public ImageView achievementImage2;

    @BindView(R.id.achievement_name)
    public TextView achievementName;

    @BindView(R.id.achievement_des)
    public TextView achievementDes;

    @BindView(R.id.achievement_step)
    public TextView achievementStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("achievementName");
        String des = intent.getStringExtra("achievementDes");
        Uri unlockedImageUri =(Uri)intent.getParcelableExtra("unlockedImageUri");
        Uri rvealedImageUri =(Uri)intent.getParcelableExtra("rvealedImageUri");
        Glide.with(this).load(unlockedImageUri).into(achievementImage);
        Glide.with(this).load(rvealedImageUri).into(achievementImage2);
        achievementName.setText(name);
        achievementDes.setText(des);
    }

    @OnClick(R.id.iv_back)
    public void backHome() {
        finish();
    }
}
