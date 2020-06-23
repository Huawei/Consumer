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

package com.huawei.hms.fido.sample.fido2.server.param;

import java.util.HashMap;

/**
 * Server Public Key Credential Creation Options Response
 *
 * @author h00431101
 * @since 2020-03-08
 */
public class ServerPublicKeyCredentialCreationOptionsResponse extends ServerResponse {
    private ServerPublicKeyCredentialRpEntity rp;

    private ServerPublicKeyCredentialUserEntity user;

    private String challenge;

    private ServerPublicKeyCredentialParameters[] pubKeyCredParams;

    private long timeout;

    private ServerPublicKeyCredentialDescriptor[] excludeCredentials;

    private ServerAuthenticatorSelectionCriteria authenticatorSelection;

    private String attestation;

    private HashMap<String, Object> extensions;

    private String rpId;

    private ServerPublicKeyCredentialDescriptor[] allowCredentials;

    private String userVerification;

    public ServerPublicKeyCredentialRpEntity getRp() {
        return rp;
    }

    public void setRp(ServerPublicKeyCredentialRpEntity rp) {
        this.rp = rp;
    }

    public ServerPublicKeyCredentialUserEntity getUser() {
        return user;
    }

    public void setUser(ServerPublicKeyCredentialUserEntity user) {
        this.user = user;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public ServerPublicKeyCredentialParameters[] getPubKeyCredParams() {
        return pubKeyCredParams;
    }

    public void setPubKeyCredParams(ServerPublicKeyCredentialParameters[] pubKeyCredParams) {
        this.pubKeyCredParams = pubKeyCredParams;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ServerPublicKeyCredentialDescriptor[] getExcludeCredentials() {
        return excludeCredentials;
    }

    public void setExcludeCredentials(ServerPublicKeyCredentialDescriptor[] excludeCredentials) {
        this.excludeCredentials = excludeCredentials;
    }

    public ServerAuthenticatorSelectionCriteria getAuthenticatorSelection() {
        return authenticatorSelection;
    }

    public void setAuthenticatorSelection(ServerAuthenticatorSelectionCriteria authenticatorSelection) {
        this.authenticatorSelection = authenticatorSelection;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public HashMap getExtensions() {
        return extensions;
    }

    public void setExtensions(HashMap extensions) {
        this.extensions = extensions;
    }

    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
    }

    public ServerPublicKeyCredentialDescriptor[] getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(ServerPublicKeyCredentialDescriptor[] allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }
}
