
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

import android.content.Intent;
import android.os.Bundle;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.AchievementsClient;
import com.huawei.hms.jos.games.Games;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AchievementActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_get_achieve_intent)
    public void getAchievementIntent() {
        AchievementsClient client = Games.getAchievementsClient(this, getAuthHuaweiId());
        Task<Intent> task = client.getShowAchievementListIntent();
        task.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent == null) {
                    showLog("intent = null");
                } else {
                    try {
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        showLog("Achievement Activity is Invalid");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_load_achievement)
    public void loadAchievement() {
        loadAchievement(true);
    }

    @OnClick(R.id.btn_load_achievement_off)
    public void loadAchievementOff() {
        loadAchievement(false);
    }

    private void loadAchievement(boolean forceReload) {
        if (getAuthHuaweiId() == null) {
            showLog("signIn first");
            return;
        }
        String jString = "";
        try {
            jString = getAuthHuaweiId().toJson();
        } catch (JSONException e) {
            showLog("signIn first");
        }
        Intent intent = new Intent(this, AchievementListActivity.class);
        intent.putExtra("forceReload", forceReload);
        intent.putExtra("mSign", jString);
        startActivity(intent);
    }
}
