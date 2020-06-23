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

package com.huawei.safetydetect.userdetect.service.impl;

import com.huawei.safetydetect.userdetect.service.UserDetectService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

@Service
public class UserDetectServiceImpl implements UserDetectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetectServiceImpl.class);

    private static final int SUCCESS_CODE = 200;

    private static final String OAUTH2_URL = "https://oauth-login.cloud.huawei.com/oauth2/v2/token";
    //TODO(developer):replace the APP_ID id with your own app id
    private static final String APP_ID = "101496927";
    //TODO(developer):replace the SECRET_KEY id with your own secret key
    private static final String SECRET_KEY = "18ec7dda1b2e599f14e4c9b98e78fc0e1515f5b3c4271df28c80209dc772dd02";
    //TODO(developer):replace the VERIFY_URL id with rms website depends on area
    private static final String VERIFY_URL = "https://rms-drru.platform.dbankcloud.com/rms/v1/userRisks/verify";

    @Override
    public String verify(String responseToken) {
        // apply access token from OAUTH2
        String accessToken = applyAccessToken(OAUTH2_URL, APP_ID, SECRET_KEY);
        // get user detect result
        return verifyUserRisks(VERIFY_URL, APP_ID, accessToken, responseToken);
    }

    /**
     * apply access token
     *
     * @param baseUrl   the address of OAUTH2
     * @param appId     app id
     * @param secretKey Secret Key
     */
    private static String applyAccessToken(String baseUrl, String appId, String secretKey) {
        HttpPost httpPostRequest = new HttpPost(baseUrl);
        httpPostRequest.setHeader("content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> entityData = new ArrayList<>();
        entityData.add(new BasicNameValuePair("grant_type", "client_credentials"));
        entityData.add(new BasicNameValuePair("client_id", appId));
        entityData.add(new BasicNameValuePair("client_secret", secretKey));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(entityData, StandardCharsets.UTF_8);
        httpPostRequest.setEntity(urlEncodedFormEntity);

        String response = execute(httpPostRequest);
        return JSON.parseObject(response).get("access_token").toString();
    }

    private static String execute(HttpPost httpPostRequest) {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            LOGGER.error("fail to build sslcontext, msg:{}, class:{}", e.getMessage(), e.getClass().getSimpleName());
        }
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslcontext, null, null, new NoopHostnameVerifier());

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPostRequest);
        } catch (IOException e) {
            LOGGER.error("fail to create HttpResponse, msg:{}, class:{}", e.getMessage(), e.getClass().getSimpleName());
            return "";
        }

        String responseContent = "";
        if (httpResponse.getStatusLine().getStatusCode() == SUCCESS_CODE) {
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                try {
                    responseContent = EntityUtils.toString(httpEntity, "UTF-8");
                } catch (IOException e) {
                    LOGGER.error("fail to get the entity content as a String, msg:{}, class:{}", e.getMessage(),
                            e.getClass().getSimpleName());
                }
                try {
                    EntityUtils.consume(httpEntity);
                } catch (IOException e) {
                    LOGGER.error("fail to consume HttpEntity, msg:{}, class:{}", e.getMessage(),
                            e.getClass().getSimpleName());
                }
            }
        }
        return responseContent;
    }

    /**
     * get user detect result
     *
     * @param appId         app id
     * @param accessToken   the access token which apply from OAUTH2
     * @param responseToken the response token that returned by userDetection function
     */
    private String verifyUserRisks(String verifyUrl, String appId, String accessToken, String responseToken) {
        URIBuilder uriBuilder;
        URI uri;
        try {
            uriBuilder = new URIBuilder(verifyUrl);
            uriBuilder.addParameter("appId", appId);
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            LOGGER.error("fail to create URI, msg:{}, class:{}", e.getMessage(), e.getClass().getSimpleName());
            return "";
        }

        HttpPost httpPostRequest;
        httpPostRequest = new HttpPost(uri);
        httpPostRequest.addHeader("content-type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accessToken", accessToken);
        jsonObject.put("response", JSONObject.parseObject(responseToken).get("response"));
        StringEntity entityData;
        try {
            entityData = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("fail to new StringEntity, msg:{}, class:{}", e.getMessage(), e.getClass().getSimpleName());
            return "";
        }
        httpPostRequest.setEntity(entityData);

        return execute(httpPostRequest);
    }
}
