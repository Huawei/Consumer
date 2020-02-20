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

package com.huawei.hms.wallet.util;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class JwtUtil {
    private static final String PRIVATE_KEY_RUSSIA_DEBUG = "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";
    private static final String SESSION_KEY_PUBLIC =  "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAgBJB4usbO33Xg5vhJqfHJsMZj44f7rxpjRuPhGy37bUBjSLXN+dS6HpxnZwSVJCtmiydjl3Inq3Mzu4SCGxfb9RIjqRRfHA7ab5p3JnJVQfTEHMHy8XcABl6EPYIJMh26kztPOKU2Mkn6yhRaCurhVUD3n9bD8omiNrR4rg442AJlNamA7vgKs65AoqBuU4NBkGHg0VWWpEHCUx/xyX6hIwqc1aD7P2f62ZHsKpNZBOek/riWhaVx3dTAa9ZS+Av3IGLOZiplhYIow9f8dlWyqs8nff9FZoJO03QhXLvOORT+lPAkW6gFzaoeMaGb40HakkZn3uvlAEKrKrtR0rZEok+N1hnboaAu8oaKK0rF1W6iNrXcFrO0rcrCsFTVF8qCa/1dFmIXwUd2M6cUzT9W0YkNyb6ZBbwEhjwBL4DNW4JfeF2Dzj0eZYlSuYV7e7e1e+XEO8lwPLAiy4bEFAWCaeuDVIhbIoBaU6xHNVQoyzct98gaOYxE4mVDqAUVmhfAgMBAAE=";
    private static final String RAS_MODEL = "RSA/NONE/OAEPwithSHA-256andMGF1Padding";
    public static String generateJwt(String issuerId, String passObject) throws Exception {
        String id = "jwt";
        String privateKey = PRIVATE_KEY_RUSSIA_DEBUG;
        String sessionKeyPublicKey = SESSION_KEY_PUBLIC;
        String sessionKey = RandomUtils.generateSecureRandomFactor(16);
        JSONObject jsonObject = JSONObject.parseObject(passObject);
        jsonObject.put("jti", "jwt");
        jsonObject.put("iss", issuerId);
        byte[] sessionKeyBytes = EncodeUtil.hex2Byte(sessionKey);
        String payLoadEncrypt = AESUtils.encryptAESCBC(jsonObject.toJSONString().getBytes(EncodeUtil.UTF_8), sessionKeyBytes);

        Map<String, Object> headerMap = new HashMap<>();
        String sessionKeyPlaintext = RSA.encrypt(sessionKey.getBytes(), sessionKeyPublicKey, RAS_MODEL, "UTF-8");
        headerMap.put("sessionKey", sessionKeyPlaintext);
        JwtBuilder builder = Jwts.builder().setHeader(headerMap).setPayload(payLoadEncrypt);
        String userToken = builder.compact();
        String content = userToken.substring(0, userToken.length() - 1);
        String sign = RSA.sign(content, privateKey, "");
        Log.i("JwtUtil", "JWT:" + userToken + sign);
        return userToken + sign;
    }

}
