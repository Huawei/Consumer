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
package com.huawei.hmssample;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.hmssample.common.ICallBack;
import com.huawei.logger.Log;
import com.huawei.logger.LoggerActivity;

/**
 *  Codelab
 *  Demonstration of HuaweiId
 */
public class HuaweiIdActivity extends LoggerActivity implements OnClickListener {

    //Log tag
    public static final String TAG = "HuaweiIdActivity";
    private HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huaweiid);
        findViewById(R.id.hwid_signin).setOnClickListener(this);
        findViewById(R.id.hwid_signout).setOnClickListener(this);
        findViewById(R.id.hwid_signInCode).setOnClickListener(this);

        //sample log Please ignore
        addLogFragment();
        //Initialize the HuaweiIdSignInClient object by calling the getClient method of HuaweiIdSignIn
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(HuaweiIdActivity.this, mAuthParam);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Codelab Code
     * Pull up the authorization interface by getSignInIntent
     */
    private void signIn() {
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN);
    }

    private void signInCode() {
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setAuthorizationCode()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(HuaweiIdActivity.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN_CODE);
    }

    /**
     * Codelab Code
     * sign Out by signOut
     */
    private void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
            }
        });
    }

    private void validateIdToken(String idToken) {
        if (TextUtils.isEmpty(idToken)) {
            Log.i(TAG, "ID Token is empty");
        } else {
            IDTokenParser idTokenParser = new IDTokenParser();
            try {
                idTokenParser.verify(idToken, new ICallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onSuccess(String idTokenJsonStr) {
                        if (!TextUtils.isEmpty(idTokenJsonStr)) {
                            Log.i(TAG, "id Token Validate Success, verify signature: " + idTokenJsonStr);
                        } else {
                            Log.i(TAG, "Id token validate failed.");
                        }
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "Id token validate failed.");
                    }
                });
            } catch (Exception e) {
                Log.i(TAG, "id Token validate failed." + e.getClass().getSimpleName());
            } catch (Error e) {
                Log.i(TAG, "id Token validate failed." + e.getClass().getSimpleName());
                if (Build.VERSION.SDK_INT < 23) {
                    Log.i(TAG, "android SDK Version is not support. Current version is: " + Build.VERSION.SDK_INT);
                }
            }
        }
    }


    /**
     * Codelab Code
     * Silent SignIn by silentSignIn
     */
    private void silentSignIn() {
        Task<AuthHuaweiId> task = mAuthManager.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId authHuaweiId) {
                Log.i(TAG, "silentSignIn success");
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //if Failed use getSignInIntent
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    signIn();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hwid_signin:
                signIn();
                break;
            case R.id.hwid_signout:
                signOut();
                break;
            case R.id.hwid_signInCode:
                signInCode();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN) {
            //login success
            //get user message by parseAuthResultFromIntent
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Log.i(TAG, huaweiAccount.getDisplayName() + " signIn success ");
                Log.i(TAG,"AccessToken: " + huaweiAccount.getAccessToken());
                validateIdToken(huaweiAccount.getIdToken());
            } else {
                Log.i(TAG, "signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Log.i(TAG, "signIn get code success.");
                Log.i(TAG,"ServerAuthCode: " + huaweiAccount.getAuthorizationCode());

                /**** english doc:For security reasons, the operation of changing the code to an AT must be performed on your server. The code is only an example and cannot be run. ****/
                /**********************************************************************************************/
            } else {
                Log.i(TAG, "signIn get code failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
    }

    /**
     * sample log Please ignore
     */
    private void addLogFragment() {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        final LogFragment fragment = new LogFragment();
        transaction.replace(R.id.framelog, fragment);
        transaction.commit();
    }

}
