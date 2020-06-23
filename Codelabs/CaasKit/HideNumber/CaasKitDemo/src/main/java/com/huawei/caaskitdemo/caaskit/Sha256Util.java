/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.huawei.caaskitdemo.caaskit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Sha256Util
 *
 * @since 2019-02-12
 */
public class Sha256Util {
    private static final int DEFAULT_STRING_BUILDER_LENGTH = 128;

    private static final int HEX_LENGTH = 2;

    private static final int BYTE = 0xFF;

    private Sha256Util() {
    }

    /**
     * sha256 encryption
     *
     * @param strSrc the string to be encrypted
     * @return encrypted string
     */
    public static String encryptNumber(String strSrc) {
        if (strSrc == null) {
            return null;
        }
        MessageDigest md;
        String strDes;
        String encName = "SHA-256";
        try {
            md = MessageDigest.getInstance(encName);
            byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);
            md.update(bt);
            strDes = getString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer(DEFAULT_STRING_BUILDER_LENGTH);
        String hex;
        for (byte b1 : b) {
            hex = Integer.toHexString(b1 & BYTE);
            if (hex.length() < HEX_LENGTH) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
