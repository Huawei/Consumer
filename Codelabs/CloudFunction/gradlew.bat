package com.huawei.agc.quickstart.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.QQAuthProvider;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class QQActivity extends BaseAuthActivity {
    private Tencent mTencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTencent = Tencent.createInstance(getString(R.string.qq_app_id), this.getApplicationContext());
    }

    protected void login() {
        mTencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                String accessToken = jsonObject.optString("access_token");
                String openId = jsonObject.optString("openid");
                AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId);
                auth.signIn(credential).addOnSuccessListener(signInResult -> {
                    updateUI();
                }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
            }

            @Override
            public void onError(UiError uiError) {
                showToast(uiError.toString());
            }

            @Override
            public void onCancel() {
                showToast("Cancel");
            }
        });
    }

    protected void logout() {
        mTencent.logout(this);
        auth.signOut();
        updateUI();
    }

    protected void link() {
        mTencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                String accessToken = jsonObject.optString("access_token");
                String openId = jsonObject.optString("openid");
                AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId);
                auth.getCurre