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

public class HwHex {
    public static final Charset CHARSET;
    private static final char[] LOWER_CASE;
    private static final char[] UPPER_CASE;
    static {
        CHARSET = Charset.forName("UTF-8");
        LOWER_CASE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        UPPER_CASE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    public static char[] encodeHex(byte[] input, boolean isLower) {
        return encodeHex(input, isLower ? LOWER_CASE : UPPER_CASE);
    }

    private static char[] encodeHex(byte[] input, char[] caseStyle) {
        int l = input.length;
        char[] result = new char[l << 1];
        int i = 0;
        for(int index = 0; i < l; ++i) {
            result[index++] = caseStyle[(240 & input[i]) >>> 4];
            result[index++] = caseStyle[15 & input[i]];
        }
        return result;
    }

    public static byte[] decodeHex(char[] inputChars) throws Exception {
        int len = inputChars.length;
        int i = 0;
        if ((len & 1) != 0) {
            throw new Exception("decode Hex Error");
        } else {
            byte[] out = new byte[len >> 1];
            for(int j = 0; j < len; ++i) {
                int f = toDigit(inputChars[j], j) << 4;
                ++j;
                f |= toDigit(inputChars[j], j);
                ++j;
                out[i] = (byte)(f & 255);
            }
            return out;
        }
    }

    protected static int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal character");
        } else {
            return digit;
        }
    }
}
