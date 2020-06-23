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


package com.huawei.hms.safetydetect.sample.sysintegrity.utils;

import com.huawei.hms.safetydetect.sample.sysintegrity.Main;
import com.huawei.hms.safetydetect.sample.sysintegrity.entity.Jws;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

/**
 * Description: Verify cert chain, signature and hostname
 *
 */
public class VerifySignatureUtil {

    private static final DefaultHostnameVerifier HOSTNAME_VERIFIER = new DefaultHostnameVerifier();

    private static X509Certificate caCert;

    public static boolean verifySignature(Jws jws,String filepath)
            throws NoSuchAlgorithmException {

        try (FileInputStream in = new FileInputStream(filepath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caCert = (X509Certificate) cf.generateCertificate(in);
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        String algorithm = jws.getHeader().getAlg();
        if ("RS256".equals(algorithm)) {
            Signature signatureAlg = Signature.getInstance("SHA256withRSA");
            return verify(signatureAlg, jws);
        }
        return false;
    }

    private static boolean verify(Signature signatureAlgorithm, Jws jws) {
        // Verify cert chain
        List<String> certs = jws.getHeader().getX5c();
        X509Certificate[] certificates = new X509Certificate[certs.size()];
        try {
            for (int i = 0; i < certs.size(); i++) {
                X509Certificate tmp = readCert(certs.get(i));
                certificates[i] = tmp;
            }
            verifyCertChain(certificates);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
            return false;
        }

        //verify hostname
        try {
            HOSTNAME_VERIFIER.verify("sysintegrity.platform.hicloud.com", certificates[0]);
        } catch (SSLException e) {
            return false;
        }

        //verify signature
        PublicKey pubKey = certificates[0].getPublicKey();
        try {
            signatureAlgorithm.initVerify(pubKey);
            signatureAlgorithm.update(jws.getSignContent().getBytes(StandardCharsets.UTF_8));
            return signatureAlgorithm.verify(jws.getSignature());
        } catch (InvalidKeyException | SignatureException e) {
            return false;
        }
    }

    private static void verifyCertChain(X509Certificate[] certs)
            throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException,
            SignatureException {
        // verify except the last
        for (int i = 0; i < certs.length - 1; ++i) {
            // Verify that the certificate has not expired.
            certs[i].checkValidity();
            PublicKey pubKey = certs[i + 1].getPublicKey();
            certs[i].verify(pubKey);
        }
        PublicKey caPubKey = caCert.getPublicKey();
        certs[certs.length - 1].verify(caPubKey);
    }

    private static X509Certificate readCert(String cert)
            throws CertificateException {
        try (InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(cert))) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certFactory.generateCertificate(in);
        } catch (IOException e) {
            throw new CertificateException("io exception when read cert", e);
        }
    }
}
