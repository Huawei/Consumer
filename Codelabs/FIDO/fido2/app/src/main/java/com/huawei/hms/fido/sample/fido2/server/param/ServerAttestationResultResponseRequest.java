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
 * Server Attestation Result Response Request
 *
 * @author h00431101
 * @since 2020-03-08
 */
public class ServerAttestationResultResponseRequest {
    private String clientDataJSON;

    private String attestationObject;

    private HashMap<String, Object> extensions;

    public String getClientDataJSON() {
        return clientDataJSON;
    }

    public void setClientDataJSON(String clientDataJSON) {
        this.clientDataJSON = clientDataJSON;
    }

    public String getAttestationObject() {
        return attestationObject;
    }

    public void setAttestationObject(String attestationObject) {
        this.attestationObject = attestationObject;
    }

    public HashMap<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(HashMap<String, Object> extensions) {
        this.extensions = extensions;
    }
}
