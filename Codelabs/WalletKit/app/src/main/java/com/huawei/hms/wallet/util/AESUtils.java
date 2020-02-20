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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

import android.util.Log;

public class AESUtils {

    public AESUtils() {
    }

    public static String encryptAESCBC(byte[] plainData, byte[] secretKey) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] salt = RandomUtils.generateSecureRandomByte(16);
            cipher.init(1, skeySpec, new IvParameterSpec(salt));
            byte[] encryptData = cipher.doFinal(plainData);
            return EncodeUtil.byte2Hex(salt) + ":" + EncodeUtil.byte2Hex(encryptData);
        } catch (GeneralSecurityException var6) {
            Log.i("AESUtils", var6.getMessage());
        }
        return "";
    }
}
