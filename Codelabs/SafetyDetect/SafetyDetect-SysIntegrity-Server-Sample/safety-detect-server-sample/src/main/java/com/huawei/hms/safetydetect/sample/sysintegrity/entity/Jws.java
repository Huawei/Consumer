/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.huawei.hms.safetydetect.sample.sysintegrity.entity;

import com.alibaba.fastjson.JSON;

import java.util.Base64;
import java.util.List;

/**
 * Description: JSON Web Signature
 *
 */
public class Jws {

    private JwsHeader header;

    private JwsPayload payload;

    private String signContent;

    private byte[] signature;

    public Jws(String jwsStr) {
        String[] jwsSplit = jwsStr.split("\\.");

        header = JSON.parseObject(Base64.getUrlDecoder().decode(jwsSplit[0]), JwsHeader.class);
        payload = JSON.parseObject(Base64.getUrlDecoder().decode(jwsSplit[1]), JwsPayload.class);
        int index = jwsStr.lastIndexOf(".");
        signContent = jwsStr.substring(0, index);
        signature = Base64.getUrlDecoder().decode(jwsSplit[2]);
    }

    public JwsHeader getHeader() {
        return header;
    }

    public void setHeader(JwsHeader header) {
        this.header = header;
    }

    public JwsPayload getPayload() {
        return payload;
    }

    public void setPayload(JwsPayload payload) {
        this.payload = payload;
    }

    public String getSignContent() {
        return signContent;
    }

    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public static class JwsHeader {

        private String alg;

        private List<String> x5c;

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public List<String> getX5c() {
            return x5c;
        }

        public void setX5c(List<String> x5c) {
            this.x5c = x5c;
        }
    }

    public static class JwsPayload {

        private String nonce;

        private String apkPackageName;

        private String apkDigestSha256;

        private String[] apkCertificateDigestSha256;

        private boolean basicIntegrity;

        private String timestampMs;

        private String advice;

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getApkPackageName() {
            return apkPackageName;
        }

        public void setApkPackageName(String apkPackageName) {
            this.apkPackageName = apkPackageName;
        }

        public String getApkDigestSha256() {
            return apkDigestSha256;
        }

        public void setApkDigestSha256(String apkDigestSha256) {
            this.apkDigestSha256 = apkDigestSha256;
        }

        public String[] getApkCertificateDigestSha256() {
            return apkCertificateDigestSha256;
        }

        public void setApkCertificateDigestSha256(String[] apkCertificateDigestSha256) {
            this.apkCertificateDigestSha256 = apkCertificateDigestSha256;
        }


        public boolean isBasicIntegrity() {
            return basicIntegrity;
        }

        public void setBasicIntegrity(boolean basicIntegrity) {
            this.basicIntegrity = basicIntegrity;
        }

        public String getTimestampMs() {
            return timestampMs;
        }

        public void setTimestampMs(String timestampMs) {
            this.timestampMs = timestampMs;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
