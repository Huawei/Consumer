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

package com.huawei.hms.fido.sample.fido2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.fido.sample.fido2.server.FidoServerSimulator;
import com.huawei.hms.fido.sample.fido2.server.IFidoServer;
import com.huawei.hms.fido.sample.fido2.server.ServerUtils;
import com.huawei.hms.fido.sample.fido2.server.param.ServerAssertionResultRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerAttestationResultRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerAuthenticatorSelectionCriteria;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialCreationOptionsRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerPublicKeyCredentialCreationOptionsResponse;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegDeleteRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfo;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfoRequest;
import com.huawei.hms.fido.sample.fido2.server.param.ServerRegInfoResponse;
import com.huawei.hms.fido.sample.fido2.server.param.ServerResponse;
import com.huawei.hms.fido.sample.fido2.server.param.ServerStatus;
import com.huawei.hms.support.api.fido.fido2.Fido2;
import com.huawei.hms.support.api.fido.fido2.Fido2AuthenticationRequest;
import com.huawei.hms.support.api.fido.fido2.Fido2AuthenticationResponse;
import com.huawei.hms.support.api.fido.fido2.Fido2Client;
import com.huawei.hms.support.api.fido.fido2.Fido2Intent;
import com.huawei.hms.support.api.fido.fido2.Fido2IntentCallback;
import com.huawei.hms.support.api.fido.fido2.Fido2RegistrationRequest;
import com.huawei.hms.support.api.fido.fido2.Fido2RegistrationResponse;
import com.huawei.hms.support.api.fido.fido2.Fido2Response;
import com.huawei.hms.support.api.fido.fido2.NativeFido2AuthenticationOptions;
import com.huawei.hms.support.api.fido.fido2.NativeFido2RegistrationOptions;
import com.huawei.hms.support.api.fido.fido2.PublicKeyCredentialCreationOptions;
import com.huawei.hms.support.api.fido.fido2.PublicKeyCredentialRequestOptions;

import java.util.List;
import java.util.Locale;

/**
 * Fido2 Demo MainActivity
 * For details about operations related to the app server and FIDO server, please refer to related standards.
 * https://www.w3.org/TR/webauthn/#webauthn-client
 *
 * @author h00431101
 * @since 2020-03-08
 */
public class Fido2DemoMainActivity extends AppCompatActivity {
    private static final String TAG = "Fido2DemoMainActivity";

    private static final String[] USER_REQUIRED_LEVEL_LIST = {"null", "required", "preferred", "discouraged"};

    private static final String[] AUTH_ATTACH_MODE = {"null", "platform", "cross-platform"};

    private static final String[] ATTEST_CONVEYANCE_PREFERENCE = {"null", "none", "indirect", "direct"};

    private static final String[] RESIDENT_KEY_TYPE = {"null", "true", "false"};

    private ArrayAdapter userVerificationLevelAdapter;

    private ArrayAdapter authAttachModeAdapter;

    private ArrayAdapter attestConveyancePreferenceAdapter;

    private ArrayAdapter residentKeyTypeAdapter;

    private TextView regInfoView;

    private Spinner userVerificationSp;

    private Spinner attachmentSp;

    private Spinner attestationSp;

    private Spinner residentKeySp;

    private Fido2Client fido2Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fido2_demo_main);
        initView();
        fido2Client = Fido2.getFido2Client(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            showError(getString(R.string.unknow_err));
            return;
        }
        switch (requestCode) {
            // Receive the registration response.
            case Fido2Client.REGISTRATION_REQUEST:
                Fido2RegistrationResponse fido2RegistrationResponse = fido2Client.getFido2RegistrationResponse(data);
                reg2Server(fido2RegistrationResponse);
                break;

            // Receive the authentication response.
            case Fido2Client.AUTHENTICATION_REQUEST:
                Fido2AuthenticationResponse fido2AuthenticationResponse =
                    fido2Client.getFido2AuthenticationResponse(data);
                auth2Server(fido2AuthenticationResponse);
                break;
            default:
                break;
        }
    }

    public void onClickRegistration(View view) {
        if (!fido2Client.isSupported()) {
            showMsg("FIDO2 is not supported.");
            return;
        }

        IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }

        ServerPublicKeyCredentialCreationOptionsRequest request =
            getRegServerPublicKeyCredentialCreationOptionsRequest();
        if (request == null) {
            return;
        }

        // Obtain the challenge value and related policy from the FIDO server, and initiate a Fido2RegistrationRequest
        // request.
        ServerPublicKeyCredentialCreationOptionsResponse response = fidoServer.getAttestationOptions(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.reg_fail) + response.getErrorMessage());
            showError(getString(R.string.reg_fail) + response.getErrorMessage());
        }
        PublicKeyCredentialCreationOptions publicKeyCredentialCreationOptions =
            ServerUtils.convert2PublicKeyCredentialCreationOptions(response);
        reg2Fido2Client(publicKeyCredentialCreationOptions);
    }

    private void reg2Fido2Client(PublicKeyCredentialCreationOptions publicKeyCredentialCreationOptions) {
        NativeFido2RegistrationOptions registrationOptions = NativeFido2RegistrationOptions.DEFAULT_OPTIONS;
        Fido2RegistrationRequest registrationRequest =
            new Fido2RegistrationRequest(publicKeyCredentialCreationOptions, null);

        // Call Fido2Client.getRegistrationIntent to obtain a Fido2Intent instance and start the FIDO client
        // registration process.
        fido2Client.getRegistrationIntent(registrationRequest, registrationOptions, new Fido2IntentCallback() {
            @Override
            public void onSuccess(Fido2Intent fido2Intent) {
                // Start the FIDO client registration process through Fido2Client.REGISTRATION_REQUEST.
                fido2Intent.launchFido2Activity(Fido2DemoMainActivity.this, Fido2Client.REGISTRATION_REQUEST);
            }

            @Override
            public void onFailure(int errorCode, CharSequence errString) {
                showError(getString(R.string.reg_fail) + errorCode + "=" + errString);
            }
        });
    }

    public void onClickAuthentication(View view) {
        if (!fido2Client.isSupported()) {
            showMsg("FIDO2 is not supported.");
            return;
        }

        IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }
        ServerPublicKeyCredentialCreationOptionsRequest request =
            getAuthnServerPublicKeyCredentialCreationOptionsRequest();
        if (request == null) {
            return;
        }

        // Obtain the challenge value and related policy from the FIDO server, and initiate a Fido2AuthenticationRequest
        // request.
        ServerPublicKeyCredentialCreationOptionsResponse response = fidoServer.getAssertionOptions(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.authn_fail) + response.getErrorMessage());
            showError(getString(R.string.authn_fail) + response.getErrorMessage());
            return;
        }
        PublicKeyCredentialRequestOptions publicKeyCredentialCreationOptions =
            ServerUtils.convert2PublicKeyCredentialRequestOptions(response);

        authn2Fido2Client(publicKeyCredentialCreationOptions);
    }

    private void authn2Fido2Client(PublicKeyCredentialRequestOptions publicKeyCredentialCreationOptions) {
        NativeFido2AuthenticationOptions authenticationOptions = NativeFido2AuthenticationOptions.DEFAULT_OPTIONS;
        Fido2AuthenticationRequest authenticationRequest =
            new Fido2AuthenticationRequest(publicKeyCredentialCreationOptions, null);

        // Call Fido2Client.getAuthenticationIntent to obtain a Fido2Intent instance and start the FIDO client
        // authentication process.
        fido2Client.getAuthenticationIntent(authenticationRequest, authenticationOptions, new Fido2IntentCallback() {
            @Override
            public void onSuccess(Fido2Intent fido2Intent) {
                // Start the FIDO client authentication process through Fido2Client.AUTHENTICATION_REQUEST.
                fido2Intent.launchFido2Activity(Fido2DemoMainActivity.this, Fido2Client.AUTHENTICATION_REQUEST);
            }

            @Override
            public void onFailure(int errorCode, CharSequence errString) {
                showError(getString(R.string.authn_fail) + errorCode + "=" + errString);
            }
        });
    }

    private ServerPublicKeyCredentialCreationOptionsRequest getAuthnServerPublicKeyCredentialCreationOptionsRequest() {
        ServerPublicKeyCredentialCreationOptionsRequest request = new ServerPublicKeyCredentialCreationOptionsRequest();
        String userName = getUserName();
        if (userName == null) {
            return null;
        }
        request.setUsername(userName);
        request.setDisplayName(userName);

        return request;
    }

    public void onClickGetRegInfo(View view) {
        IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }
        getRegInfo(fidoServer, true);
    }

    public void onClickDeregistration(View view) {
        final IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }
        String userName = getUserName();
        if (userName == null) {
            return;
        }
        ServerRegDeleteRequest request = new ServerRegDeleteRequest();
        request.setUsername(userName);

        ServerResponse response = fidoServer.delete(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.delete_register_info_fail) + response.getErrorMessage());
            showError(getString(R.string.delete_register_info_fail) + response.getErrorMessage());
            return;
        }
        regInfoView.setText("");
        showMsg(getString(R.string.delete_register_info_success));
    }

    private void reg2Server(Fido2RegistrationResponse fido2RegistrationResponse) {
        if (!fido2RegistrationResponse.isSuccess()) {
            showError(getString(R.string.reg_fail), fido2RegistrationResponse);
            return;
        }
        final IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }

        ServerAttestationResultRequest request = ServerUtils
            .convert2ServerAttestationResultRequest(fido2RegistrationResponse.getAuthenticatorAttestationResponse());

        ServerResponse response = fidoServer.getAttestationResult(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.reg_fail) + response.getErrorMessage());
            showError(getString(R.string.reg_fail) + response.getErrorMessage());
            return;
        }
        getRegInfo(fidoServer, false);
        showMsg(getString(R.string.reg_success));
    }

    private void auth2Server(Fido2AuthenticationResponse fido2AuthenticationResponse) {
        if (!fido2AuthenticationResponse.isSuccess()) {
            showError(getString(R.string.authn_fail), fido2AuthenticationResponse);
            return;
        }

        IFidoServer fidoServer = initFidoServer();
        if (fidoServer == null) {
            Log.e(TAG, getString(R.string.connect_server_err));
            return;
        }

        ServerAssertionResultRequest request = ServerUtils
            .convert2ServerAssertionResultRequest(fido2AuthenticationResponse.getAuthenticatorAssertionResponse());

        ServerResponse response = fidoServer.getAssertionResult(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.authn_fail) + response.getErrorMessage());
            showError(getString(R.string.authn_fail) + response.getErrorMessage());
            return;
        }
        showMsg(getString(R.string.authn_success));
    }

    private void showMsg(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Fido2DemoMainActivity.this);
                builder.setTitle(getString(R.string.message_title));
                builder.setMessage(message);
                builder.setPositiveButton(getString(R.string.confirm_btn), null);
                builder.show();
            }
        });
    }

    private void getRegInfo(IFidoServer fidoServer, final boolean showMsgDlg) {
        ServerRegInfoRequest request = new ServerRegInfoRequest();
        String username = getUserName();
        if (username == null) {
            return;
        }
        request.setUsername(username);

        ServerRegInfoResponse response = fidoServer.getRegInfo(request);
        if (!ServerStatus.OK.equals(response.getStatus())) {
            Log.e(TAG, getString(R.string.get_register_info_fail) + response.getErrorMessage());
            if (showMsgDlg) {
                showError(getString(R.string.get_register_info_fail));
            }
            return;
        }
        showRegInfo(response.getInfos());
        if (showMsgDlg) {
            showMsg(getString(R.string.get_register_info_success));
        }
    }

    private void showRegInfo(List<ServerRegInfo> regInfos) {
        StringBuilder infoStrb = new StringBuilder();
        infoStrb.append(getString(R.string.cp_reg_info)).append(System.lineSeparator());
        if (regInfos != null) {
            int index = 0;
            for (ServerRegInfo regInfo : regInfos) {
                infoStrb.append(++index)
                    .append(". ")
                    .append(getString(R.string.credential_id))
                    .append(regInfo.getCredentialId())
                    .append(System.lineSeparator());
            }
        }
        regInfoView.setText(infoStrb);
    }

    private void showError(String message, Fido2Response fido2Response) {
        final StringBuilder errMsgBuilder = new StringBuilder();
        errMsgBuilder.append(message)
            .append("Fido2Status: ")
            .append(fido2Response.getFido2Status())
            .append("=")
            .append(fido2Response.getFido2StatusMessage())
            .append(String.format(Locale.getDefault(), "(CtapStatus: 0x%x=%s)", fido2Response.getCtapStatus(),
                fido2Response.getCtapStatusMessage()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Fido2DemoMainActivity.this);
                builder.setTitle(getString(R.string.error_title));
                builder.setMessage(errMsgBuilder);
                builder.setPositiveButton(getString(R.string.confirm_btn), null);
                builder.show();
            }
        });
    }

    private void showError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Fido2DemoMainActivity.this);
                builder.setTitle(getString(R.string.error_title));
                builder.setMessage(message);
                builder.setPositiveButton(getString(R.string.confirm_btn), null);
                builder.show();
            }
        });
    }

    private static String getSpinnerSelect(Object select) {
        String data = (String) select;
        if (select == null || TextUtils.isEmpty(data) || "null".equals(data)) {
            return null;
        }
        return data;
    }

    private ServerPublicKeyCredentialCreationOptionsRequest getRegServerPublicKeyCredentialCreationOptionsRequest() {
        ServerPublicKeyCredentialCreationOptionsRequest request = new ServerPublicKeyCredentialCreationOptionsRequest();

        String userName = getUserName();
        if (userName == null) {
            return null;
        }
        request.setUsername(userName);
        request.setDisplayName(userName);

        String userVeriLevel = getSpinnerSelect(userVerificationSp.getSelectedItem());
        String attachmentMode = getSpinnerSelect(attachmentSp.getSelectedItem());

        Boolean residentKey = null;
        if (residentKeySp.getSelectedItem() != null) {
            String residentKeyString = getSpinnerSelect(residentKeySp.getSelectedItem());
            if (TextUtils.isEmpty(residentKeyString)) {
                residentKey = null;
            } else if ("false".equals(residentKeyString)) {
                residentKey = false;
            } else if ("true".equals(residentKeyString)) {
                residentKey = true;
            }
        }
        String attestConveyancePreference = getSpinnerSelect(attestationSp.getSelectedItem());

        ServerAuthenticatorSelectionCriteria selection =
            getAuthenticatorSelectionCriteria(userVeriLevel, attachmentMode, residentKey);
        request.setAuthenticatorSelection(selection);

        request.setAttestation(attestConveyancePreference);
        return request;
    }

    private ServerAuthenticatorSelectionCriteria getAuthenticatorSelectionCriteria(String userVerificationLevel,
        String attachmentMdoe, Boolean residentKey) {
        ServerAuthenticatorSelectionCriteria selectionCriteria = new ServerAuthenticatorSelectionCriteria();

        if (!TextUtils.isEmpty(userVerificationLevel)) {
            selectionCriteria.setUserVerification(userVerificationLevel);
        } else {
            selectionCriteria.setUserVerification(null);
        }

        if (!TextUtils.isEmpty(attachmentMdoe)) {
            selectionCriteria.setAuthenticatorAttachment(attachmentMdoe);
        } else {
            selectionCriteria.setAuthenticatorAttachment(null);
        }

        selectionCriteria.setRequireResidentKey(residentKey);
        return selectionCriteria;
    }

    private String getUserName() {
        return "FidoTestUser";
    }

    private IFidoServer initFidoServer() {
        return new FidoServerSimulator();
    }

    private void initView() {
        regInfoView = findViewById(R.id.txt_show_log_area);
        userVerificationSp = findViewById(R.id.sp_user_required_level);
        attachmentSp = findViewById(R.id.sp_auth_attach_mode);
        attestationSp = findViewById(R.id.sp_attest_conveyance_preference);
        residentKeySp = findViewById(R.id.sp_residentkey_type);
        getSupportActionBar().hide();
        userVerificationLevelAdapter =
            new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, USER_REQUIRED_LEVEL_LIST);
        userVerificationSp.setAdapter(userVerificationLevelAdapter);

        authAttachModeAdapter =
            new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, AUTH_ATTACH_MODE);
        attachmentSp.setAdapter(authAttachModeAdapter);

        attestConveyancePreferenceAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
            android.R.id.text1, ATTEST_CONVEYANCE_PREFERENCE);
        attestationSp.setAdapter(attestConveyancePreferenceAdapter);

        residentKeyTypeAdapter =
            new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, RESIDENT_KEY_TYPE);
        residentKeySp.setAdapter(residentKeyTypeAdapter);
    }
}
