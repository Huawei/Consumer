
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
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.games.AchievementsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.achievement.Achievement;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AchievementListActivity extends Activity implements AchievementListAdapter.OnBtnClickListener {

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private ArrayList<Achievement> achievements = new ArrayList<>();
    private AchievementsClient client;
    private AchievementListActivity mContext;
    AuthHuaweiId authHuaweiId =null;
    private boolean forceReload;
    private AchievementListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);
        mContext = this;
        ButterKnife.bind(mContext);
        initViews();
        initData();
        requestData();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AchievementListAdapter(mContext, achievements, mContext);
        recyclerView.setAdapter(adapter);
    }

    private void requestData() {
        Task<List<Achievement>> task = client.getAchievementList(forceReload);
        task.addOnSuccessListener(new OnSuccessListener<List<Achievement>>() {
            @Override
            public void onSuccess(List<Achievement> data) {
                if (data == null) {
                    showLog("achievementBuffer is null");
                    return;
                }
                Iterator<Achievement> iterator = data.iterator();
                achievements.clear();
                while (iterator.hasNext()) {
                    Achievement achievement = iterator.next();
                    achievements.add(achievement);
                }
                adapter.setData(achievements);
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

    private void initData() {
        Intent intent = getIntent();
        forceReload = intent.getBooleanExtra("forceReload", false);
        String mSignString =intent.getStringExtra("mSign");

        try {
            authHuaweiId =AuthHuaweiId.fromJson(mSignString);
        } catch (JSONException e) {
        }
        client = Games.getAchievementsClient(this, authHuaweiId);
    }

    @OnClick(R.id.iv_back)
    public void backHome() {
        finish();
    }

    private void showLog(String result) {
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int postion) {
        Intent intent = new Intent(this, AchievementDetailActivity.class);
        Achievement achievement = achievements.get(postion);
        intent.putExtra("achievementName",achievement.getDisplayName());
        intent.putExtra("achievementDes",achievement.getDescInfo());
        intent.putExtra("unlockedImageUri",achievement.getReachedThumbnailUri());
        intent.putExtra("rvealedImageUri",achievement.getVisualizedThumbnailUri());
        startActivity(intent);

    }

    @Override
    public void Unlock(String achievementId, boolean isChecked) {
        if (!isChecked){
            client.reach(achievementId);
        }else {
            performUnlockImmediate(client,achievementId);
        }
    }

    @Override
    public void reveal(String achievementId, boolean isChecked) {
        if (!isChecked){
            client.visualize(achievementId);
        }else {
            performRevealImmediate(client,achievementId);
        }
    }

    @Override
    public void increment(String achievementId, boolean isChecked) {
        if (!isChecked){
            client.grow(achievementId,1);
        }else {
            performIncrementImmediate(client,achievementId,1);
        }
    }

    @Override
    public void setStep(String achievementId, boolean isChecked) {
        if (!isChecked){
            client.makeSteps(achievementId,3);
        }else {
            performSetStepsImmediate(client,achievementId,3);
        }
    }



    private void performSetStepsImmediate(AchievementsClient client, String achievementId, int stepsNum) {
        Task<Boolean> task = client.makeStepsWithResult(achievementId, stepsNum);
        task.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isSucess) {
                if (isSucess) {
                    showLog("setAchievementSteps isSucess");
                } else {
                    showLog("achievement can not makeSteps");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog("step num is invalid"+result);
                }
            }
        });
    }

    private void performIncrementImmediate(AchievementsClient client, String achievementId, int stepsNum) {
        Task<Boolean> task = client.growWithResult(achievementId, stepsNum);

        task.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isSucess) {
                if (isSucess) {
                    showLog("incrementAchievement isSucess");
                    requestData();
                }else {
                    showLog("achievement can not grow");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog("has bean already unlocked"+result);
                }
            }
        });
    }

    private void performRevealImmediate(AchievementsClient client, String achievementId) {
        Task<Void> task = client.visualizeWithResult(achievementId);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                showLog("revealAchievemen isSucess");
                requestData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog("achievement is not hidden"+result);
                }
            }
        });
    }

    private void performUnlockImmediate(AchievementsClient client, String achievementId) {
        Task<Void> task = client.reachWithResult(achievementId);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                showLog("UnlockAchievemen isSucess");
                requestData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog("achievement has been already unlocked"+result);
                }
            }
        });
    }
}
