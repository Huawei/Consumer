
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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.AchievementsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutomatedTestActivity extends BaseActivity implements ApiManager.ApiResult{

    @BindView(R.id.method_id)
    public EditText editTextMethod;
    @BindView(R.id.request_id)
    public EditText editTextRequest;

    private final static int SIGN_IN_INTENT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_ok)
    public void performAchievementAction() {

        String method = editTextMethod.getText().toString().trim();
        String request = editTextRequest.getText().toString().trim();
        if (TextUtils.isEmpty(method)){
            showLog("method name cannot be null");
            return;
        }


        AchievementsClient client = Games.getAchievementsClient(this, ApiManager.mAuthHuaweiId);

        String achievementId = null;
        boolean forceReload = true;
        boolean mustBtnOne = false;
        int stepNum = 0;
        if(!TextUtils.isEmpty(request)){
            try {
                JSONObject objRequest = new JSONObject(request);
                if(objRequest.has("id")){
                    achievementId = objRequest.getString("id");
                }
                if(objRequest.has("numSteps")){
                    stepNum = objRequest.getInt("numSteps");
                }
                if(objRequest.has("forceReload")){
                    forceReload = objRequest.getBoolean("forceReload");
                }
                if(objRequest.has("mustBtnOne")){
                    mustBtnOne = objRequest.getBoolean("mustBtnOne");
                }
            }catch(Exception e){
                showLog("invalid json");
                return;
            }
        }

        switch (method) {
            case "reachWithResult" :
                performUnlockImmediate(client,achievementId);
                break;
            case "reach" :
                performUnlock(client,achievementId);
                showLog("unlock success");
                break;
            case "visualizeWithResult" :
                performRevealImmediate(client,achievementId);
                break;
            case "visualize" :
                performReveal(client,achievementId);
                showLog("reveal success");
                break;
            case "growWithResult" :
                performIncrementImmediate(client,achievementId,stepNum);
                break;
            case "grow" :
                performIncrement(client,achievementId,stepNum);
                showLog("increment success");
                break;
            case "makeStepsWithResult" :
                performSetStepsImmediate(client,achievementId,stepNum);
                break;
            case "makeSteps" :
                performSetSteps(client,achievementId,stepNum);
                showLog("setSteps success");
                break;
            case "init" :
                ApiManager.init(this, getAuthHuaweiId());
                showLog("init success");
                break;
            case "signIn" :
                Intent intent = HuaweiIdAuthManager.getService(this, getHuaweiIdParams()).getSignInIntent();
                startActivityForResult(intent, SIGN_IN_INTENT);
                break;
            case "loadAchievement" :
                ApiManager.loadAchievement(this, this, forceReload);
                break;
            case "getCurrentPlayer" :
                ApiManager.getCurrentPlayer(this, this);
                break;
            case "getAppId" :
                ApiManager.getAppId(this, getAuthHuaweiId(),this);
                break;
            case "checkAppUpdate" :
                ApiManager.checkAppUpdate(this, this);
                break;
            case "showUpdateDialog" :
                ApiManager.showUpdateDialog(this, this, mustBtnOne);
                break;
            case "releaseCallBack":
                ApiManager.releaseCallBack(this, this);
                break;
            case "getShowAchievementListIntent":
                getAchievementIntent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (SIGN_IN_INTENT == requestCode) {
            handleSignInResult(data);
        } else {
            showLog("unknown requestCode in onActivityResult");
        }
    }

    private void getAchievementIntent() {
        Games.getAchievementsClient(this, ApiManager.mAuthHuaweiId)
                .getShowAchievementListIntent().addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent, 1);
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

    private void handleSignInResult(Intent data) {

        if (null == data) {
            showLog("signIn intent is null");
            return;
        }

        String jsonSignInResult = data.getStringExtra("HUAWEIID_SIGNIN_RESULT");
        if (TextUtils.isEmpty(jsonSignInResult)) {
            showLog("SignIn result is empty");
            return;
        }
        try {
            HuaweiIdAuthResult signInResult = new HuaweiIdAuthResult().fromJson(jsonSignInResult);
            if (0 == signInResult.getStatus().getStatusCode()) {
                showLog("Sign in result: "+signInResult.toJson());
                ApiManager.mAuthHuaweiId = signInResult.getHuaweiId();
            } else {
                showLog("Sign in failed: "+signInResult.getStatus().getStatusCode());
                ApiManager.mAuthHuaweiId = null;
            }
        } catch (JSONException var7) {
            showLog("Failed to convert json from signInResult.");
            ApiManager.mAuthHuaweiId = null;
        }
    }

    private void performSetStepsImmediate(AchievementsClient client, String achievementId, int stepsNum) {
        Task<Boolean> task = client.makeStepsWithResult(achievementId,stepsNum);
        task.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isSucess) {
                if (isSucess)
                    showLog("setAchievementSteps isSucess");
                else
                    showLog("achievement can not makeSteps");
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

    private void performSetSteps(AchievementsClient client, String achievementId, int stepsNum) {
        client.makeSteps(achievementId,stepsNum);
    }

    private void performIncrementImmediate(AchievementsClient client, String achievementId, int stepsNum) {
        Task<Boolean> task = client.growWithResult(achievementId,stepsNum);

        task.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isSucess) {
                if (isSucess)
                    showLog("incrementAchievement isSucess");
                else
                    showLog("achievement can not grow");
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

    private void performIncrement(AchievementsClient client, String achievementId, int stepsNum) {
        client.grow(achievementId,stepsNum);

    }

    private void performReveal(AchievementsClient client, String achievementId) {
        client.visualize(achievementId);

    }

    private void performRevealImmediate(AchievementsClient client, String achievementId) {
        Task<Void> task = client.visualizeWithResult(achievementId);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                showLog("revealAchievemen isSucess");
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

    private void performUnlockImmediate(AchievementsClient client, String achievementId) {
        Task<Void> task = client.reachWithResult(achievementId);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                showLog("revealAchievemen isSucess");
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

    private void performUnlock(AchievementsClient client, String achievementId) {
        client.reach(achievementId);
    }

    @Override
    public void onResult(String result) {
        showLog(result);
    }

}
