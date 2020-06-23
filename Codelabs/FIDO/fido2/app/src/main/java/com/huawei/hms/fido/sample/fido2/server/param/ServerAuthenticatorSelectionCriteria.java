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

/**
 * Server Authenticator Selection Criteria
 *
 * @author h00431101
 * @since 2020-03-08
 */

public class ServerAuthenticatorSelectionCriteria {
    private String authenticatorAttachment;

    private Boolean requireResidentKey;

    private String userVerification;

    public String getAuthenticatorAttachment() {
        return authenticatorAttachment;
    }

    public void setAuthenticatorAttachment(String authenticatorAttachment) {
        this.authenticatorAttachment = authenticatorAttachment;
    }

    public Boolean isRequireResidentKey() {
        return requireResidentKey;
    }

    public void setRequireResidentKey(Boolean requireResidentKey) {
        this.requireResidentKey = requireResidentKey;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }
}
