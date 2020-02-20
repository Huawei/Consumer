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

import java.nio.charset.Charset;

import android.util.Log;

public class EncodeUtil {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public EncodeUtil() {
    }

    public static String byte2Hex(byte[] array) {
        return CommCryptUtil.byte2HexStr(array);
    }

    public static byte[] hex2Byte(String hex) {
        try {
            return CommCryptUtil.hexStr2Byte(hex);
        } catch (Exception var2) {
            Log.i("EncodeUtil",var2.getMessage());
        }
        return null;
    }
}
