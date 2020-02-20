/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.hms.wallet;

import com.huawei.hms.wallet.apptest.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.wallet.constant.WalletPassConstant;
import com.huawei.hms.wallet.util.JwtUtil;

public class PassTestActivity extends FragmentActivity {
    private static final String TAG = "TestActivity";
    public static final int SAVE_TO_ANDROID = 888;
    private WalletPassClient walletObjectsClient;
    private String issuerId;
    private String passObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sec_main);
        Intent i = this.getIntent();
        passObject = i.getStringExtra("passObject");
        Log.i(TAG, "passObject" + passObject);
        issuerId = i.getStringExtra("issuerId");
        Log.i(TAG, "issuerId:" + issuerId);
    }

    //add by wallet kit sdk
    public void saveToHuaWeiWallet(View view) {
        String jwtStr = getJwtFromAppServer(passObject);
        CreateWalletPassRequest request = CreateWalletPassRequest.getBuilder()
                .setJwt(jwtStr)
                .build();
        Log.i("testwalletKIT", "getWalletObjectsClient");
        walletObjectsClient = Wallet.getWalletPassClient(PassTestActivity.this);
        Task<AutoResolvableForegroundIntentResult> task = walletObjectsClient.createWalletPass(request);
        ResolveTaskHelper.excuteTask(task, PassTestActivity.this, SAVE_TO_ANDROID);
    }

    public void clickLinkToPay(View view) {
        String jwtStr = getJwtFromAppServer(passObject);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("hms://www.huawei.com/payapp/{" + jwtStr + "}"));
        try {
            startActivityForResult(intent, SAVE_TO_ANDROID);
        } catch (ActivityNotFoundException e) {
            Log.println(Log.ERROR, "HMS", "HMS error:ActivityNotFoundException");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SAVE_TO_ANDROID:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(this, "save success", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "(Reason, 1：cancel by user 2：HMS not install or register)", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        if (data != null) {
                            int errorCode =
                                    data.getIntExtra(
                                            WalletPassConstant.EXTRA_ERROR_CODE, -1);
                            Toast.makeText(this, "fail, [" + errorCode + "]：" + analyzeErrorCode(errorCode), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "fail ：data is null ", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }

    private String analyzeErrorCode(int errorCode) {
        String tips = "";
        switch (errorCode) {
            case WalletPassConstant.ERROR_CODE_SERVICE_UNAVAILABLE:
                tips = "server unavailable（net error）";
                break;
            case WalletPassConstant.ERROR_CODE_INTERNAL_ERROR:
                tips = "internal error";
                break;
            case WalletPassConstant.ERROR_CODE_INVALID_PARAMETERS:
                tips = "invalid parameters or card is added";
                break;
            case WalletPassConstant.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
                tips = "JWT verify fail";
                break;
            case WalletPassConstant.ERROR_CODE_USER_ACCOUNT_ERROR:
                tips = "hms account error（invalidity or Authentication failed）";
                break;
            case WalletPassConstant.ERROR_CODE_UNSUPPORTED_API_REQUEST:
                tips = "unSupport API";
                break;
            case WalletPassConstant.ERROR_CODE_OTHERS:
            default:
                tips = "unknown Error";
                break;
        }
        return tips;
    }

    /**
     * in this demo,method getJwtFromAppServer just simulate how to get jwt form passObject
     * in product environment,
     * issuerId, privateKye and SessionPublicKey is saved on the developer's Server
     * developer should send passObject to developer's Server(pls use Https)
     * Server should generateJwt by passObject ,and send back jwt to app
     *
     * @param passObject passObject
     * @return JWT
     */
    private String getJwtFromAppServer(String passObject) {
        String jwtStr = "";
        try {
            jwtStr = JwtUtil.generateJwt(issuerId, passObject);
        } catch (Exception e) {
            Toast.makeText(this, "fail ：jwt trans error", Toast.LENGTH_LONG).show();
            Log.e(TAG, "jwt trans error");
            return null;
        }
        Log.i(TAG, "jwtStr:" + jwtStr);
        return jwtStr;
    }
}