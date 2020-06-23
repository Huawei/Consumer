
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

package com.huawei.hms.game;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.autotest.AutomatedTestActivity;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.game.common.ConnectClientSupport;
import com.huawei.hms.game.common.IAPSupport;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.jos.AppUpdateClient;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.AppPlayerInfo;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.GamesStatusCodes;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.jos.games.player.PlayerExtraInfo;
import com.huawei.hms.jos.games.player.PlayersClientImpl;
import com.huawei.hms.jos.product.ProductClient;
import com.huawei.hms.jos.product.ProductOrderInfo;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.game.CertificateIntentResult;
import com.huawei.hms.support.api.game.GameLoginHandler;
import com.huawei.hms.support.api.game.GameLoginResult;
import com.huawei.hms.support.api.game.HardwareCapabilityResult;
import com.huawei.hms.support.api.game.HuaweiGame;
import com.huawei.hms.support.api.game.PlayerCertificationInfo;
import com.huawei.hms.support.api.game.TemperatureResult;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";
    private final static int SIGN_IN_INTENT = 3000;
    private final static int PAY_PROTOCOL_INTENT = 3001;
    private final static int PAY_INTENT = 3002;
    private final static int HEARTBEAT_TIME = 15 * 60 * 1000;

    private String playerId;

    private String sessionId = null;

    private boolean hasInit = false;

    private Handler handler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // back
        gameEnd();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // hideFloatWindow();
        hideFloatWindowNewWay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // front
        gameBegin();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFloatWindowNewWay();
        Log.e(TAG, "onResume");
    }

    @OnClick(R.id.btn_init)
    public void init() {
        JosAppsClient appsClient = JosApps.getJosAppsClient(this, getAuthHuaweiId());
        appsClient.init();
        showLog("init success");
        hasInit = true;
    }

    @OnClick(R.id.btn_login_in)
    public void login() {
        ConnectClientSupport.get().connect(this, new ConnectClientSupport.IConnectCallBack() {
            @Override
            public void onResult(HuaweiApiClient apiClient) {
                if (apiClient != null) {
                    PendingResult<GameLoginResult> pendingRst = HuaweiGame.HuaweiGameApi.login(apiClient, MainActivity.this, 1, new GameLoginHandler() {
                        @Override
                        public void onResult(int retCode, GameUserData userData) {
                            showLog("login result:" + retCode);
                            if (retCode == GamesStatusCodes.GAME_STATE_SUCCESS && userData != null) {
                                showLog("displayName:" + userData.getDisplayName());
                                showLog("playerId:" + userData.getPlayerId());
                            }
                        }

                        @Override
                        public void onChange() {

                        }
                    });
                    pendingRst.setResultCallback(new ResultCallback<GameLoginResult>() {
                        @Override
                        public void onResult(GameLoginResult result) {
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.btn_get_miss_product)
    public void getMissProductList() {
        ProductClient appsClient = JosApps.getProductClient(this);
        Task<List<ProductOrderInfo>> task = appsClient.getMissProductOrder(this);
        task.addOnSuccessListener(new OnSuccessListener<List<ProductOrderInfo>>() {
            @Override
            public void onSuccess(List<ProductOrderInfo> productOrderInfos) {
                if (productOrderInfos != null) {
                    for (ProductOrderInfo productOrderInfo : productOrderInfos) {
                        String productNo = productOrderInfo.getProductNo();
                        String tradeId = productOrderInfo.getTradeId();
                        showLog("productNo：" + productNo + ",tradeId：" + tradeId);
                    }
                } else {
                    showLog("product list is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    showLog("get miss product info failed:" + apiException.getStatusCode());
                }
            }
        });
    }


    @OnClick(R.id.btn_sign_in)
    public void signIn() {
        Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.getService(this, getHuaweiIdParams()).silentSignIn();
        authHuaweiIdTask.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId authHuaweiId) {
                showLog("signIn success");
                showLog("display:" + authHuaweiId.getDisplayName());
                SignInCenter.get().updateAuthHuaweiId(authHuaweiId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    showLog("signIn failed:" + apiException.getStatusCode());
                    showLog("start getSignInIntent");
                    signInNewWay();
                }
            }
        });
    }

    public void signInNewWay() {
        Intent intent = HuaweiIdAuthManager.getService(MainActivity.this, getHuaweiIdParams()).getSignInIntent();
        startActivityForResult(intent, SIGN_IN_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SIGN_IN_INTENT == requestCode) {
            handleSignInResult(data);
        } else if (PAY_PROTOCOL_INTENT == requestCode) {
            iapBuy();
        } else if (PAY_INTENT == requestCode) {
            handlePayResult(data);
        } else {
            showLog("unknown requestCode in onActivityResult");
        }
    }

    private void handlePayResult(Intent data) {
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(this).parsePurchaseResultInfoFromIntent(data);
        if (purchaseResultInfo != null) {
            int iapRtnCode = purchaseResultInfo.getReturnCode();
            showLog("iapRtnCode：" + iapRtnCode);
        } else {
            showLog("iap failed");
        }
    }

    private void handleSignInResult(Intent data) {
        if (null == data) {
            showLog("signIn inetnt is null");
            return;
        }
//        HuaweiIdSignIn.getSignedInAccountFromIntent(data);
        String jsonSignInResult = data.getStringExtra("HUAWEIID_SIGNIN_RESULT");
        if (TextUtils.isEmpty(jsonSignInResult)) {
            showLog("SignIn result is empty");
            return;
        }
        try {
            HuaweiIdAuthResult
                    signInResult = new HuaweiIdAuthResult
                    ().fromJson(jsonSignInResult);
            if (0 == signInResult.getStatus().getStatusCode()) {
                showLog("Sign in success.");
                showLog("Sign in result: " + signInResult.toJson());
                SignInCenter.get().updateAuthHuaweiId(signInResult.getHuaweiId());
            } else {
                showLog("Sign in failed: " + signInResult.getStatus().getStatusCode());
            }
        } catch (JSONException var7) {
            showLog("Failed to convert json from signInResult.");
        }
    }

    @OnClick(R.id.btn_get_player)
    public void getCurrentPlayer() {
        PlayersClientImpl client = (PlayersClientImpl) Games.getPlayersClient(this, getAuthHuaweiId());

        Task<Player> task = client.getCurrentPlayer();
        task.addOnSuccessListener(new OnSuccessListener<Player>() {
            @Override
            public void onSuccess(Player player) {
                String result = "display:" + player.getDisplayName() + "\n" + "playerId:" + player.getPlayerId() + "\n" + "playerLevel:"
                        + player.getLevel() + "\n" + "timestamp:" + player.getSignTs()
                        + "\n" + "playerSign:" + player.getPlayerSign();
                showLog(result);
                playerId = player.getPlayerId();
                gameBegin();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        gamePlayExtra();
                    }
                };
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }, HEARTBEAT_TIME, HEARTBEAT_TIME);
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

    @OnClick(R.id.btn_save_player)
    public void savePlayerInfo() {
        if (TextUtils.isEmpty(playerId)) {
            showLog("GetCurrentPlayer first.");
            return;
        }
        PlayersClient client = Games.getPlayersClient(this, getAuthHuaweiId());
        AppPlayerInfo appPlayerInfo = new AppPlayerInfo();
        appPlayerInfo.area = "20";
        appPlayerInfo.rank = "level 56";
        appPlayerInfo.role = "hunter";
        appPlayerInfo.sociaty = "Red Cliff II";
        appPlayerInfo.playerId = playerId;
        Task<Void> task = client.savePlayerInfo(appPlayerInfo);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                showLog("save player info successfully ");
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

    @OnClick(R.id.btn_game_begin)
    public void gameBegin() {
        if (TextUtils.isEmpty(playerId)) {
            showLog("GetCurrentPlayer first.");
            return;
        }
        String uid = UUID.randomUUID().toString();
        PlayersClient client = Games.getPlayersClient(this, getAuthHuaweiId());
        Task<String> task = client.submitPlayerEvent(playerId, uid, "GAMEBEGIN");
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String jsonRequest) {
                if (jsonRequest == null) {
                    showLog("jsonRequest is null");
                    return;
                }
                try {
                    JSONObject data = new JSONObject(jsonRequest);
                    sessionId = data.getString("transactionId");
                } catch (JSONException e) {
                    showLog("parse jsonArray meet json exception");
                    return;
                }
                showLog("submitPlayerEvent traceId: " + jsonRequest);
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

    @OnClick(R.id.btn_game_end)
    public void gameEnd() {
        if (TextUtils.isEmpty(playerId)) {
            showLog("GetCurrentPlayer first.");
            return;
        }
        if (TextUtils.isEmpty(sessionId)) {
            showLog("SessionId is empty.");
            return;
        }
        PlayersClient client = Games.getPlayersClient(this, getAuthHuaweiId());
        Task<String> task = client.submitPlayerEvent(playerId, sessionId, "GAMEEND");
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                showLog("submitPlayerEvent traceId: " + s);
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

    @OnClick(R.id.btn_play_extra)
    public void gamePlayExtra() {
        if (TextUtils.isEmpty(playerId)) {
            showLog("GetCurrentPlayer first.");
            return;
        }
        PlayersClient client = Games.getPlayersClient(this, getAuthHuaweiId());
        Task<PlayerExtraInfo> task = client.getPlayerExtraInfo(sessionId);
        task.addOnSuccessListener(new OnSuccessListener<PlayerExtraInfo>() {
            @Override
            public void onSuccess(PlayerExtraInfo extra) {
                if (extra != null) {
                    showLog("IsRealName: " + extra.getIsRealName() + ", IsAdult: " + extra.getIsAdult()
                            + ", PlayerId: " + extra.getPlayerId() + ", PlayerDuration: " + extra.getPlayerDuration());
                } else {
                    showLog("Player extra info is empty.");
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

    @OnClick(R.id.btn_test)
    public void jumpTestActivity() {
        startActivity(new Intent(this, AutomatedTestActivity.class));
    }

    @OnClick(R.id.btn_get_certificate_info)
    public void getCertificateInfo() {
        ConnectClientSupport.get().connect(this, new ConnectClientSupport.IConnectCallBack() {
            @Override
            public void onResult(HuaweiApiClient apiClient) {
                if (apiClient != null) {
                    PendingResult<PlayerCertificationInfo> pendingRst =
                            HuaweiGame.HuaweiGameApi.getPlayerCertificationInfo(apiClient);
                    pendingRst.setResultCallback(new ResultCallback<PlayerCertificationInfo>() {
                        @Override
                        public void onResult(PlayerCertificationInfo result) {
                            if (result == null || result.getStatus() == null) {
                                showLog("result is null");
                                return;
                            }
                            Status status = result.getStatus();
                            int rstCode = status.getStatusCode();
                            if (rstCode == CommonCode.OK) {
                                showLog("User is Adault:" + result.hasAdault());
                            } else {
                                showLog("result:" + rstCode);
                            }
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.btn_get_certificate_intent)
    public void getCertificateIntent() {
        ConnectClientSupport.get().connect(this, new ConnectClientSupport.IConnectCallBack() {
            @Override
            public void onResult(HuaweiApiClient apiClient) {
                if (apiClient != null) {
                    PendingResult<CertificateIntentResult> pendingRst = HuaweiGame.HuaweiGameApi.getPlayerCertificationIntent(apiClient);
                    pendingRst.setResultCallback(new ResultCallback<CertificateIntentResult>() {
                        @Override
                        public void onResult(CertificateIntentResult result) {
                            if (result == null || result.getStatus() == null) {
                                showLog("result is null");
                                return;
                            }
                            int rstCode = result.getStatus().getStatusCode();
                            if (rstCode == CommonCode.OK) {
                                Intent intent = result.getCertificationIntent();
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            } else {
                                showLog("result:" + rstCode);
                            }
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.btn_register_game)
    public void registerGame() {
        ConnectClientSupport.get().connect(this, new ConnectClientSupport.IConnectCallBack() {
            @Override
            public void onResult(HuaweiApiClient apiClient) {
                if (apiClient != null) {
                    PendingResult<HardwareCapabilityResult> pendingRst =
                            HuaweiGame.HuaweiGameApi.registerHardwareCapability(apiClient);
                    pendingRst.setResultCallback(new ResultCallback<HardwareCapabilityResult>() {
                        @Override
                        public void onResult(HardwareCapabilityResult result) {
                            if (result == null || result.getStatus() == null) {
                                showLog("result is null");
                                return;
                            }
                            Status status = result.getStatus();
                            int rstCode = status.getStatusCode();
                            showLog("register result:" + rstCode);
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.btn_get_phone_info)
    public void getPhoneInfo() {
        ConnectClientSupport.get().connect(this, new ConnectClientSupport.IConnectCallBack() {
            @Override
            public void onResult(HuaweiApiClient apiClient) {
                if (apiClient != null) {
                    PendingResult<TemperatureResult> pendingRst = HuaweiGame.HuaweiGameApi.getTemperature(apiClient);
                    pendingRst.setResultCallback(new ResultCallback<TemperatureResult>() {
                        @Override
                        public void onResult(TemperatureResult result) {
                            if (result == null || result.getStatus() == null) {
                                showLog("result is null");
                                return;
                            }
                            int rstCode = result.getStatus().getStatusCode();
                            if (rstCode == CommonCode.OK) {
                                String phoneInfo = result.getTemperature();
                                showLog("get phone result:" + phoneInfo);
                            } else {
                                showLog("get phone result:" + rstCode);
                            }
                        }
                    });
                }
            }
        });
    }

    private void showFloatWindowNewWay() {
        if (!hasInit) {
            init();
        }
        Games.getBuoyClient(this).showFloatWindow();
    }

    private void hideFloatWindowNewWay() {
        Games.getBuoyClient(this).hideFloatWindow();
    }

    @OnClick(R.id.btn_iap_buy)
    public void iapBuy() {
        getBuyIntentWithPrice(this);
    }

    public void getBuyIntentWithPrice(final Activity activity) {
        IapClient mClient = Iap.getIapClient(activity);
        Task<PurchaseIntentResult> task =
                mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq(String.valueOf(getPayCash())));
        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
                dealSuccess(result, activity);
                showLog("getBuyIntentWithPrice success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    dealIAPFailed(statusCode, ((IapApiException) e).getStatus());
                }
            }
        });
    }

    public void dealIAPFailed(int statusCode, Status status) {
        if (statusCode == OrderStatusCode.ORDER_NOT_ACCEPT_AGREEMENT) {
            startActivityForResult(this, status, PAY_PROTOCOL_INTENT);
        } else {
            showLog("getBuyIntentWithPrice failed");
        }
    }

    public void dealSuccess(PurchaseIntentResult result, Activity activity) {
        if (result == null) {
            showLog("dealSuccess, result is null");
            return;
        }
        Status status = result.getStatus();
        if (status.getResolution() == null) {
            showLog("intent is null");
            return;
        }
        showLog("paymentData" + result.getPaymentData());
        showLog("paymentSignature" + result.getPaymentSignature());
        if (result.getPaymentSignature() != null && result.getPaymentData() != null) {
            // check sign
            boolean success =
                    IAPSupport.doCheck(result.getPaymentData(), result.getPaymentSignature());
            if (success) {
                startActivityForResult(activity, status, PAY_INTENT);
            } else {
                showLog("check sign failed");
            }
        }
    }

    private void startActivityForResult(Activity activity, Status status, int reqCode) {
        if (status.hasResolution()) {
            try {
                status.startResolutionForResult(activity, reqCode);
            } catch (IntentSender.SendIntentException exp) {
                showLog(exp.getMessage());
            }
        } else {
            showLog("intent is null");
        }
    }

    private float getPayCash() {
        Spinner etAmount = findViewById(R.id.et_amount);
        float amount = 0.01f;
        if (etAmount != null) {
            String text = etAmount.getSelectedItem().toString();
            if (!TextUtils.isEmpty(text)) {
                try {
                    amount = Float.valueOf(text);
                } catch (NumberFormatException e) {
                    return (float) 0.01;
                }
            }
        } else {
            return (float) 0.01;
        }
        return amount;
    }

    private ApkUpgradeInfo apkUpgradeInfo;

    @OnClick(R.id.btn_check_update)
    public void checkUpdate() {
        AppUpdateClient client = JosApps.getAppUpdateClient(this);
        client.checkAppUpdate(this, new UpdateCallBack(this));
    }

    @OnClick(R.id.btn_check_update_pop)
    public void checkUpdatePop() {
        AppUpdateClient client = JosApps.getAppUpdateClient(this);
        client.showUpdateDialog(this, apkUpgradeInfo, false);
    }

    private static class UpdateCallBack implements CheckUpdateCallBack {
        private MainActivity apiActivity;

        private UpdateCallBack(MainActivity apiActivity) {
            this.apiActivity = apiActivity;
        }

        public void onUpdateInfo(Intent intent) {
            if (intent != null) {
                Serializable info = intent.getSerializableExtra("updatesdk_update_info");
                if (info instanceof ApkUpgradeInfo) {
                    apiActivity.showLog("check update success");
                    apiActivity.apkUpgradeInfo = (ApkUpgradeInfo) info;
                } else {
                    apiActivity.showLog("check update failed");
                }
            }
        }

        public void onMarketInstallInfo(Intent intent) {
            Log.w("AppUpdateManager", "info not instanceof ApkUpgradeInfo");
            apiActivity.showLog("check update failed");
        }

        public void onMarketStoreError(int responseCode) {
            apiActivity.showLog("check update failed");
        }

        public void onUpdateStoreError(int responseCode) {
            apiActivity.showLog("check update failed");
        }
    }
}
