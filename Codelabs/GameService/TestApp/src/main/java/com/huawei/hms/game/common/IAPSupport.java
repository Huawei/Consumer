
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

package com.huawei.hms.game.common;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.huawei.hms.iap.entity.PurchaseIntentWithPriceReq;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class IAPSupport {
    private static final String TAG = "HMS_LOG_CipherUtil";

    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    private static final String PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsJ0sDem2GXylvQSOYHIv108cLcXZ01bAObpx2gYas2WqtfMYA3VgADnM+80r+Gj4t5G4SyQlbAECE9ZfOGYVCDPvfh596ljK8o76jIg3xY9LLlu6Yfhe1HNLVir5xlsEFUoaZqhJ9Ioz65Ls32dv2mZc9GzkSM0yj3BaZXG4Qb/G2yTm5Zo1vIsk0CB8F4jdY1mgDWgrQr1h9vwkZEgx2Z1J6Vd+B5lk1H42Qu0hEzq1+68e+arb0pAiUqacDgAniF+v0ksxliULqsSSBsk/4Ivuw79rmzKIerbfzIrSzp6YIMlh5R81thlQtHCtR1dT3hu1bmyb4IAH2UprKNE/QQIDAQAB";

    public static boolean doCheck(String content, String sign) {
        if (TextUtils.isEmpty(PUBLIC_KEY)) {
            Log.e(TAG, "publicKey is null");
            return false;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            return signature.verify(Base64.decode(sign, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "doCheck NoSuchAlgorithmException" + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "doCheck InvalidKeySpecException" + e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "doCheck InvalidKeyException" + e);
        } catch (SignatureException e) {
            Log.e(TAG, "doCheck SignatureException" + e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "doCheck UnsupportedEncodingException" + e);
        }
        return false;
    }

    public static PurchaseIntentWithPriceReq createGetBuyIntentWithPriceReq(String cash) {
        PurchaseIntentWithPriceReq request = new PurchaseIntentWithPriceReq();
        request.setCurrency("CNY");
        request.setDeveloperPayload("test");
        request.setPriceType(0);
        request.setSdkChannel("1");
        request.setProductName("test: product");
        request.setAmount(cash);
        request.setProductId(String.valueOf(System.currentTimeMillis()));
        request.setServiceCatalog("X6");
        request.setCountry("CN");
        return request;
    }
}
