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

import java.security.SecureRandom;

public final class CommCryptUtil {
    public CommCryptUtil() {
    }

    public static String byte2HexStr(byte[] array) {
        return array == null ? null : new String(HwHex.encodeHex(array, false));
    }

    public static byte[] hexStr2Byte(String hexStr) throws Exception {
        return hexStr == null ? new byte[0] : HwHex.decodeHex(hexStr.toCharArray());
    }

    public static byte[] genSecureRandomByte(int byteSize) {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[byteSize];
        sr.nextBytes(bytes);
        return bytes;
    }
}
