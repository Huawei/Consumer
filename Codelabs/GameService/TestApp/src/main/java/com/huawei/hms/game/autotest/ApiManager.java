
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

package com.huawei.hms.game.autotest;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.AppUpdateClient;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.AchievementsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.achievement.Achievement;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public abstract class ApiManager {
    public interface ApiResult{
        void onResult(String result);
    }

    public static AuthHuaweiId mAuthHuaweiId;

    public static void init(Activity activity, AuthHuaweiId authHuaweiId){
        JosAppsClient appsClient = JosApps.getJosAppsClient(activity, authHuaweiId);
        appsClient.init();
    }

    public static void getAppId(Activity activity, AuthHuaweiId authHuaweiId, @NonNull final ApiResult result){
        JosAppsClient appsClient = JosApps.getJosAppsClient(activity, authHuaweiId);
        Task<String> task = appsClient.getAppId();
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String appId) {

                JSONObject obj = new JSONObject();
                try{
                    obj.put("appId", appId);
                    result.onResult(obj.toString());
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String apiResult = "{rtnCode:" + ((ApiException) e).getStatusCode() + "}";
                    result.onResult(apiResult);
                }
            }
        });
    }

    public static void getCurrentPlayer(Activity activity, @NonNull final ApiResult result){
        PlayersClient client = Games.getPlayersClient(activity, mAuthHuaweiId);
        Task<Player> task = client.getCurrentPlayer();

        task.addOnSuccessListener(new OnSuccessListener<Player>() {
            @Override
            public void onSuccess(Player player) {

                JSONObject obj = new JSONObject();
                try{
                    obj.put("displayName", player.getDisplayName());
                    obj.put("hiResImageUri", player.getHiResImageUri().toString());
                    obj.put("iconImageUri", player.getIconImageUri().toString());
                    obj.put("playerId", player.getPlayerId());
                    obj.put("playerSign", player.getPlayerSign());
                    obj.put("signTs", player.getSignTs());
                    JSONObject playerLevelInfoObj = new JSONObject();
                    JSONObject playerLevelObj = new JSONObject();
                    playerLevelInfoObj.put("playerLevel", player.getLevel());
                    obj.put("playerLevelInfo", playerLevelInfoObj);
                    result.onResult(obj.toString());
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String playerInfo = "{rtnCode:" + ((ApiException) e).getStatusCode() + "}";
                    result.onResult(playerInfo);
                }
            }
        });
    }

    public static void loadAchievement(Activity activity, @NonNull final ApiResult result, boolean forceReload) {
        AchievementsClient client = Games.getAchievementsClient(activity, mAuthHuaweiId);
        Task<List<Achievement>> task = client.getAchievementList(forceReload);
        task.addOnSuccessListener(new OnSuccessListener<List<Achievement>>() {
            @Override
            public void onSuccess(List<Achievement> data) {

                if (data == null) {
                    result.onResult("achievementBuffer is null");
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                Iterator<Achievement> iterator = data.iterator();
                while (iterator.hasNext()) {
                    Achievement achievement = iterator.next();
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("achievementId", achievement.getId());
                        obj.put("type", achievement.getType());
                        obj.put("name", achievement.getDisplayName());
                        obj.put("description", achievement.getDescInfo());
                        if(achievement.getReachedThumbnailUri() != null){
                            obj.put("unlockedImageUri", achievement.getReachedThumbnailUri().toString());
                        }
                        if(achievement.getVisualizedThumbnailUri() != null) {
                            obj.put("revealedImageUri", achievement.getVisualizedThumbnailUri().toString());
                        }
                        obj.put("totalSteps", achievement.getAllSteps());
                        obj.put("formattedTotalSteps", achievement.getLocaleAllSteps());
                        obj.put("state", achievement.getState());
                        obj.put("currentSteps", achievement.getReachedSteps());
                        obj.put("lastUpdatedTimestamp", achievement.getRecentUpdateTime());
                        Player player = achievement.getGamePlayer();
                        if(player != null){
                            JSONObject playerObj = new JSONObject();
                            playerObj.put("displayName", player.getDisplayName());
                            if(player.getHiResImageUri() != null){
                                playerObj.put("hiResImageUri", player.getHiResImageUri().toString());
                            }
                            if(player.getIconImageUri() != null){
                                playerObj.put("iconImageUri", player.getIconImageUri().toString());
                            }

                            playerObj.put("playerId", player.getPlayerId());
                            playerObj.put("playerSign", player.getPlayerSign());
                            playerObj.put("signTs", player.getSignTs());
                            JSONObject playerLevelInfoObj = new JSONObject();
                            playerLevelInfoObj.put("playerLevel", player.getLevel());
                            playerObj.put("playerLevelInfo", playerLevelInfoObj);
                            obj.put("player", playerObj);
                        }
                        jsonArray.put(obj);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                result.onResult(jsonArray.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String ret = "rtnCode:" + ((ApiException) e).getStatusCode();
                    result.onResult(ret);
                }
            }
        });
    }

    private static class UpdateCallBack implements CheckUpdateCallBack {

        private ApkUpgradeInfo apkUpgradeInfo = null;

        private ApiResult result;

        public UpdateCallBack(ApiResult result) {
            this.result = result;
        }

        public ApkUpgradeInfo getApkUpgradeInfo() {
            return apkUpgradeInfo;
        }

        @Override
        public void onUpdateInfo(Intent intent) {
            if (intent != null) {
                Serializable info = intent.getSerializableExtra("updatesdk_update_info");
                if (info instanceof ApkUpgradeInfo) {
                    apkUpgradeInfo = (ApkUpgradeInfo) info;
                    result.onResult("Check update successful");
                } else {
                    result.onResult("Failed to check the update.");
                }
            }

        }

        @Override
        public void onMarketInstallInfo(Intent intent) {
            result.onResult("Failed to check the update.");
        }

        @Override
        public void onMarketStoreError(int i) {
            result.onResult("Failed to check the update.");
        }

        @Override
        public void onUpdateStoreError(int i) {
            result.onResult("Failed to check the update.");
        }

    }

    private static UpdateCallBack updateCallBack = null;

    public static void checkAppUpdate(Activity activity, @NonNull final ApiResult result) {
        AppUpdateClient client = JosApps.getAppUpdateClient(activity);
        updateCallBack = new UpdateCallBack(result);
        client.checkAppUpdate(activity, updateCallBack);
    }

    public static void showUpdateDialog(Activity activity, @NonNull final ApiResult result, boolean mustBtnOne) {
        AppUpdateClient client = JosApps.getAppUpdateClient(activity);
        if(updateCallBack == null || updateCallBack.getApkUpgradeInfo() == null){
            result.onResult("Failed to update the dialog box.");
        }
        client.showUpdateDialog(activity, updateCallBack.getApkUpgradeInfo(), mustBtnOne);
    }

    public static void releaseCallBack(Activity activity, @NonNull final ApiResult result) {
        AppUpdateClient client = JosApps.getAppUpdateClient(activity);
        client.releaseCallBack();
        result.onResult("The updated callback interface is released successfully.");
    }

}
