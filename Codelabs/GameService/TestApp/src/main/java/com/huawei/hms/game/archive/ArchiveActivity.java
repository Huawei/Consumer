
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

package com.huawei.hms.game.archive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.ArchivesClient;
import com.huawei.hms.jos.games.GameScopes;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.GamesStatusCodes;
import com.huawei.hms.jos.games.archive.ArchiveConstants;
import com.huawei.hms.jos.games.archive.ArchiveSummary;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArchiveActivity extends BaseActivity {
    ArchivesClient client;

    @BindView(R.id.cb_add)
    public CheckBox checkBoxAdd;

    @BindView(R.id.cb_delete)
    public CheckBox checkBoxDelete;

    @BindView(R.id.et_max_size)
    public EditText etMaxSize;

    private ArchivesClient getArchivesClient() {
        if (client == null) {
            client = Games.getArchiveClient(this, getAuthHuaweiId());
        }
        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public HuaweiIdAuthParams getHuaweiIdParams() {
        List<Scope> scopes = new ArrayList<>();
        scopes.add(GameScopes.DRIVE_APP_DATA);
        return new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).setScopeList(scopes).createParams();
    }

    @OnClick(R.id.btn_signin_drive)
    public void init() {
        JosAppsClient appsClient = JosApps.getJosAppsClient(this, getAuthHuaweiId());
        appsClient.init();
        showLog("init success");

        Task<AuthHuaweiId> AuthHuaweiIdTask =
                HuaweiIdAuthManager.getService(this, getHuaweiIdParams()).silentSignIn();
        AuthHuaweiIdTask.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId AuthHuaweiId) {
                showLog("signIn success");
                showLog("display:" + AuthHuaweiId.getDisplayName());
                SignInCenter.get().updateAuthHuaweiId(AuthHuaweiId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    showLog("signIn failed:" + apiException.getStatusCode());
                    signInNewWay();
                }
            }
        });
    }

    private final static int SIGN_IN_INTENT = 3000;
    public void signInNewWay() {
        Intent intent = HuaweiIdAuthManager.getService(ArchiveActivity.this, getHuaweiIdParams()).getSignInIntent();
        startActivityForResult(intent, SIGN_IN_INTENT);
    }

    private void handleSignInResult(Intent data) {
        if (null == data) {
            showLog("signIn inetnt is null");
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

    @OnClick(R.id.btn_guide_drive_protocol)
    public void agreeDriveProtocol() {
         guideToAgreeDriveProtocol();
    }

    @OnClick(R.id.btn_archive_add)
    public void addArchive() {
        Intent intent = new Intent(this, CommitArchiveActivity.class);
        startActivityForResult(intent, 1000);
    }

    @OnClick(R.id.btn_archive_get_max_image_size)
    public void getMaxImageSize() {
        Task<Integer> task = getArchivesClient().getLimitThumbnailSize();
        task.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                show("MaxImageSize:" + integer);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                String result = "MaxImageSize rtnCode:" + ((ApiException) e).getStatusCode();
                showLog(result);
            }
        });
    }
    @OnClick(R.id.btn_archive_get_max_content_size)
    public void getMaxFileSize() {
        Task<Integer> task = getArchivesClient().getLimitDetailsSize();
        task.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                show("MaxData:" + integer);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                String result = "MaxData rtnCode:" + ((ApiException) e).getStatusCode();
                showLog(result);
            }
        });
    }

    @OnClick(R.id.btn_archive_load)
    public void loadArchive() {
        Intent intent = new Intent(this, ArchiveListActivity.class);
        intent.putExtra("isRealTime", true);
        startActivityForResult(intent, 1000);
    }

    @OnClick(R.id.btn_archive_load_cache)
    public void loadArchiveCache() {
        Intent intent = new Intent(this, ArchiveListActivity.class);
        intent.putExtra("isRealTime", false);
        startActivityForResult(intent, 1000);
    }

    private int getMaxSize() {
        try {
            return Integer.valueOf(etMaxSize.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @OnClick(R.id.btn_archive_get_select_intent)
    public void selectArchiveFromAppAssistant() {
        getArchiveIntent(this.getResources().getString(R.string.app_name), checkBoxAdd.isChecked(), checkBoxDelete.isChecked(),getMaxSize());
    }

    public void getArchiveIntent(String title, boolean allowAddBtn, boolean allowDeleteBtn, int maxArchive) {

        Task<Intent> task = getArchivesClient().getShowArchiveListIntent(title, allowAddBtn, allowDeleteBtn, maxArchive);
        task.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent == null) {
                    showLog("intent == null");
                } else {
                    try {
                        startActivityForResult(intent, 5000);
                    } catch (Exception e) {
                        showLog("Archive Activity is Invalid");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                    if (((ApiException) e).getStatusCode() == GamesStatusCodes.GAME_STATE_ARCHIVE_NO_DRIVE) {
                        guideToAgreeDriveProtocol();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (SIGN_IN_INTENT == requestCode) {
                handleSignInResult(data);
            } else if (requestCode == 5000) {
                if (data == null) {
                   return;
                }

                if (data.hasExtra(ArchiveConstants.ARCHIVE_SELECT)) {
                    Bundle bundle = data.getParcelableExtra(ArchiveConstants.ARCHIVE_SELECT);
                    Task<ArchiveSummary> task = getArchivesClient().parseSummary(bundle);
                    task.addOnSuccessListener(new OnSuccessListener<ArchiveSummary>() {
                        @Override
                        public void onSuccess(ArchiveSummary archiveSummary) {
                            if (archiveSummary != null) {
                                showLog("UniqueName:" + archiveSummary.getFileName());
                                showLog("ArchiveId:" + archiveSummary.getId());
                                showLog("Description:" + archiveSummary.getDescInfo());
                                showLog("ModifyTime:" + archiveSummary.getRecentUpdateTime());
                                showLog("PlayedTime:" + archiveSummary.getActiveTime());
                                showLog("ImageAspectRatio:" + archiveSummary.getThumbnailRatio());
                                showLog("progressValue:" + archiveSummary.getThumbnailRatio());
                                showLog("hasThumbnail:" + archiveSummary.hasThumbnail());

                                showPlayerAndGame(archiveSummary);
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
                } else if(data.hasExtra(ArchiveConstants.ARCHIVE_ADD)) {
                    addArchive();
                }
            }
        }
    }

}
