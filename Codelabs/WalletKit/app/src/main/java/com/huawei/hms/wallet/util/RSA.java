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

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
public class RSA {

    private static final int BASELENGTH = 128;

    private static final int LOOKUPLENGTH = 64;

    private static final int TWENTYFOURBITGROUP = 24;

    private static final int EIGHTBIT = 8;

    private static final int SIXTEENBIT = 16;

    private static final int FOURBYTE = 4;

    private static final int SIGN = -128;

    private static final char PAD = '=';

    private static final byte[] BASE64_ALPHABET = new byte[BASELENGTH];

    private static final char[] LOOK_UP_BASE64_ALPHABET = new char[LOOKUPLENGTH];

    /**
     * Signature algorithm.
     */
    private static final String SIGN_ALGORITHMS256 = "SHA256WithRSA";

    private static final String RSA_ECB_OAEP_SHA256_ALGORITHM = "RSA/ECB/OAEPwithSHA-256andMGF1Padding";

    /**
     * Sign content.
     *
     * @param content data to be signed.
     * @param privateKey merchant's private key.
     * @return Signed value.
     */
    public static String sign(String content, String privateKey, String signType) {
        String charset = "utf-8";
        try {
            PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey,Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(privatePKCS8);
            java.security.Signature signatureObj = java.security.Signature.getInstance(SIGN_ALGORITHMS256);
            signatureObj.initSign(priKey);
            signatureObj.update(content.getBytes(charset));
            byte[] signed = signatureObj.sign();
            return Base64.encodeToString(signed,Base64.DEFAULT);
        } catch (Exception ex) {
            Log.i("RSA", "SIGN Fail");
        }

        return "";
    }

    /**
     * Verify signature.
     *
     * @param content content to be signed.
     * @param sign the signature to verified.
     * @param publicKey public key.
     * @param signType sign type.
     * @return if verifying signature succeed.
     */
    public static boolean doCheck(byte[] content, String sign, String publicKey, String signType) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey,Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS256);
            signature.initVerify(pubKey);
            signature.update(content);
            return signature.verify(Base64.decode(sign,Base64.DEFAULT));
        } catch (Exception e) {
            Log.i("RSA", "doCheck Fail");
        }
        return false;
    }

    public static String encrypt(String source, String publicKey) throws Exception {
        return encrypt(source, publicKey, RSA.RSA_ECB_OAEP_SHA256_ALGORITHM, "utf-8");
    }

    public static String encrypt(String source, String publicKey, String algorithm, String input_charset)
            throws Exception {
        Key key = getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        if (CommonUtil.isNull(input_charset)) {
            input_charset = "utf-8";
        }
        byte[] bytes = hex2Byte(source);
        byte[] b1 = cipher.doFinal(bytes);
        String encoded = Base64encode(b1);
        return encoded;
    }

    /**
     * encrypt bytes： src data
     *
     * @param bytes bytes
     * @param publicKey publicKey
     * @param algorithm algorithm
     * @param input_charset input_charset
     * @return encoded
     * @throws Exception
     */
    public static String encrypt(byte[] bytes, String publicKey, String algorithm, String input_charset)
            throws Exception {
        Key key = getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        if (CommonUtil.isNull(input_charset)) {
            input_charset = "utf-8";
        }
        byte[] b1 = cipher.doFinal(bytes);
        String encoded = Base64Hw.encode(b1);
        return encoded;
    }


    public static byte[] hex2Byte(String hex) throws Exception {
        return hexStr2Byte(hex);
    }

    public static byte[] hexStr2Byte(String hexStr) throws Exception {
        return hexStr == null ? new byte[0] : HwHex.decodeHex(hexStr.toCharArray());
    }

    public static String Base64encode(byte[] binaryData) {
        if (CommonUtil.isNull(binaryData)) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];

        byte k1 = 0;
        byte l1 = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;
        byte val1 = 0;
        byte val2 = 0;
        byte val3 = 0;
        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            l1 = (byte) (b2 & 0x0f);
            k1 = (byte) (b1 & 0x03);

            val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val2 | (k1 << 4)];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[(l1 << 2) | val3];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        assembleInteger(binaryData, fewerThan24bits, encodedData, encodedIndex, dataIndex);

        return new String(encodedData);
    }

    private static void assembleInteger(byte[] binaryData, int fewerThan24bits, char[] encodedData, int encodedIndex,
            int dataIndex) {
        byte b1;
        byte k1;
        byte b2;
        byte l1;
        byte val4 = 0;
        byte val5 = 0;
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k1 = (byte) (b1 & 0x03);

            val4 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val4];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[k1 << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l1 = (byte) (b2 & 0x0f);
            k1 = (byte) (b1 & 0x03);

            val4 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            val5 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val4];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val5 | (k1 << 4)];
            encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[l1 << 2];
            encodedData[encodedIndex++] = PAD;
        }
    }

    /**
     * getPublicKey
     *
     * @param key key(by Base64)
     * @throws Exception
     */
    private static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Hw.decode(key));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     * remove WhiteSpace from MIME containing encoded Base64Hw data.
     *
     * @param data the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (CommonUtil.isNull(data)) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }


    private static boolean isData(char octect) {
        return (octect < BASELENGTH && BASE64_ALPHABET[octect] != -1);
    }

    private static byte[] checkCharacters(char[] base64Data, char d1, char d2, int idx, int encodedIndex, int dataIndex,
            byte[] decodedData) {
        byte b1;
        byte b2;
        char d3;
        char d4;
        byte b3;
        byte b4;
        b1 = BASE64_ALPHABET[d1];
        b2 = BASE64_ALPHABET[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (isNotD3OrD4(d3, d4)) { // Check if they are PAD characters
            if (isPadD3AndD4(d3, d4)) {
                if ((b2 & 0xf) != 0) {
                    // last 4 bits should be zero
                    return new byte[0];
                }
                byte[] tmp = new byte[idx * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, idx * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (isNotPadD3AndPadD4(d3, d4)) {
                b3 = BASE64_ALPHABET[d3];
                if ((b3 & 0x3) != 0) {
                    // last 2 bits should be zero
                    return new byte[0];
                }
                byte[] tmp = new byte[idx * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, idx * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return new byte[0];
            }
        } else { // No PAD e.g 3cQl
            b3 = BASE64_ALPHABET[d3];
            b4 = BASE64_ALPHABET[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }
        return null;
    }

    private static boolean isNotD3OrD4(char d3, char d4) {
        return !isData((d3)) || !isData((d4));
    }

    private static boolean isNotPadD3AndPadD4(char d3, char d4) {
        return !isPad(d3) && isPad(d4);
    }

    private static boolean isPadD3AndD4(char d3, char d4) {
        return isPad(d3) && isPad(d4);
    }

    private static boolean isPad(char octect) {
        return (octect == PAD);
    }
}
