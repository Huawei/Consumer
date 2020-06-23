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

package com.huawei.hms.fido.sample.fido2.server;

import com.huawei.hms.fido.sample.fido2.server.param.ServerAssertionResultRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerAttestationResultRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerAuthenticatorSelectionCriteria;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialCreationOptionsRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialCreationOptionsResponse;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialDescriptor;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialParameters;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialRpEntity;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialUserEntity;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegDeleteRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfo;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfoRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfoResponse;
import com.huawei.hms.fido.sample.fido2.server.param.ServerResponse;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulating a Fido Server
 *
 * @author h00431101
 * @since 2020-03-08
 */
public class FidoServerSimulator implements IFidoServer {
    private static List<ServerRegInfo> regInfos = new ArrayList<>();

    @Override
    public ServerPublicKeyCredentialCreationOptionsResponse
        getAttestationOptions(ServerPublicKeyCredentialCreationOptionsRequest request) {
        final ServerPublicKeyCredentialCreationOptionsResponse response =
            new ServerPublicKeyCredentialCreationOptionsResponse();
        response.setAttestation(request.getAttestation());

        ServerAuthenticatorSelectionCriteria selectionCriteria = request.getAuthenticatorSelection();
        if (selectionCriteria != null) {
            response.setAuthenticatorSelection(selectionCriteria);
        }

        response.setChallenge(ByteUtils.byte2base64(getChallege()));

        List<ServerPublicKeyCredentialDescriptor> excludeCredentialList = new ArrayList<>();
        for (ServerRegInfo info : regInfos) {
            ServerPublicKeyCredentialDescriptor desc = new ServerPublicKeyCredentialDescriptor();
            desc.setId(info.getCredentialId());
            desc.setType("public-key");
            excludeCredentialList.add(desc);
        }
        response.setExcludeCredentials(
            excludeCredentialList.toArray(new ServerPublicKeyCredentialDescriptor[excludeCredentialList.size()]));

        List<ServerPublicKeyCredentialParameters> pubKeyCredParamList = new ArrayList<>();
        ServerPublicKeyCredentialParameters cp = new ServerPublicKeyCredentialParameters();
        cp.setAlg(-7);
        cp.setType("public-key");
        pubKeyCredParamList.add(cp);
        cp = new ServerPublicKeyCredentialParameters();
        cp.setAlg(-257);
        cp.setType("public-key");
        pubKeyCredParamList.add(cp);
        response.setPubKeyCredParams(
            pubKeyCredParamList.toArray(new ServerPublicKeyCredentialParameters[pubKeyCredParamList.size()]));

        ServerPublicKeyCredentialRpEntity rpEntity = new ServerPublicKeyCredentialRpEntity();
        rpEntity.setName("www.huawei.fidodemo");
        response.setRp(rpEntity);

        response.setRpId("www.huawei.fidodemo");

        response.setTimeout(60L);
        ServerPublicKeyCredentialUserEntity user = new ServerPublicKeyCredentialUserEntity();
        user.setId(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        response.setUser(user);
        return response;
    }

    @Override
    public ServerResponse getAttestationResult(ServerAttestationResultRequest attestationResultRequest) {
        final ServerResponse response = new ServerResponse();
        ServerRegInfo info = new ServerRegInfo();
        info.setCredentialId(attestationResultRequest.getId());
        regInfos.add(info);
        return response;
    }

    @Override
    public ServerPublicKeyCredentialCreationOptionsResponse getAssertionOptions(
        ServerPublicKeyCredentialCreationOptionsRequest serverPublicKeyCredentialCreationOptionsRequest) {
        final ServerPublicKeyCredentialCreationOptionsResponse response =
            new ServerPublicKeyCredentialCreationOptionsResponse();

        List<ServerPublicKeyCredentialDescriptor> allowCredentials = new ArrayList<>();
        for (ServerRegInfo info : regInfos) {
            ServerPublicKeyCredentialDescriptor desc = new ServerPublicKeyCredentialDescriptor();
            desc.setId(info.getCredentialId());
            desc.setType("public-key");
            allowCredentials.add(desc);
        }
        response.setAllowCredentials(
            allowCredentials.toArray(new ServerPublicKeyCredentialDescriptor[allowCredentials.size()]));

        response.setChallenge(ByteUtils.byte2base64(getChallege()));

        response.setRpId("www.huawei.fidodemo");

        response.setTimeout(60L);

        return response;
    }

    @Override
    public ServerResponse getAssertionResult(ServerAssertionResultRequest assertionResultRequest) {
        final ServerResponse response = new ServerResponse();
        return response;
    }

    @Override
    public ServerRegInfoResponse getRegInfo(ServerRegInfoRequest regInfoRequest) {
        final ServerRegInfoResponse response = new ServerRegInfoResponse();
        List<ServerRegInfo> infos = new ArrayList<>();
        for (ServerRegInfo regInfo : regInfos) {
            ServerPublicKeyCredentialDescriptor desc = new ServerPublicKeyCredentialDescriptor();
            ServerRegInfo info = new ServerRegInfo();
            info.setCredentialId(regInfo.getCredentialId());
            infos.add(info);
        }
        response.setInfos(infos);
        return response;
    }

    @Override
    public ServerResponse delete(ServerRegDeleteRequest regDeleteRequest) {
        final ServerResponse response = new ServerResponse();
        regInfos.clear();
        return response;
    }

    private static byte[] getChallege() {
        return SecureRandom.getSeed(16);
    }
}
