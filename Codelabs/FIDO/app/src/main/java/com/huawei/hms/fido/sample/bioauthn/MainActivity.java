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

package com.huawei.hms.fido.sample.bioauthn;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.huawei.hms.support.api.fido.bioauthn.BioAuthnCallback;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnManager;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnPrompt;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnResult;
import com.huawei.hms.support.api.fido.bioauthn.CryptoObject;
import com.huawei.hms.support.api.fido.bioauthn.FaceManager;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Huawei HMS Core FIDO BioAuthn Sample
 *
 * @author Huawei HMS
 * @since 2019-11-19
 */
public class MainActivity extends AppCompatActivity {

    // In the scenario where BioAuthnPrompt.PromptInfo.Builder.setDeviceCredentialAllowed(true) is used in EMUI 9.x or
    // earlier, fingerprint authentication may work once only. You can solve this problem in one of the following ways:
    // 1. The activity has only one singleton BioAuthnPrompt object. Do not create the object repeatedly.
    // 2. After the authentication is complete, call the recreate() method of the activity.
    // 3. Close the activity and open it again.
    private BioAuthnPrompt bioAuthnPrompt;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        bioAuthnPrompt = createBioAuthnPrompt();
    }

    private BioAuthnPrompt createBioAuthnPrompt() {
        // call back
        BioAuthnCallback callback = new BioAuthnCallback() {
            @Override
            public void onAuthError(int errMsgId, CharSequence errString) {
                showResult("Authentication error. errorCode=" + errMsgId + ",errorMessage=" + errString);
            }

            @Override
            public void onAuthSucceeded(BioAuthnResult result) {
                showResult("Authentication succeeded. CryptoObject=" + result.getCryptoObject());
            }

            @Override
            public void onAuthFailed() {
                showResult("Authentication failed.");
            }
        };
        return new BioAuthnPrompt(this, ContextCompat.getMainExecutor(this), callback);
    }

    /**
     * Shows the fingerprint prompt without CryptoObject and allows the user to use the device PIN and password for
     * authentication.
     *
     * @param view View
     */
    public void btnFingerAuthenticateWithoutCryptoObjectClicked(View view) {
        // Checks whether fingerprint authentication is available.
        BioAuthnManager bioAuthnManager = new BioAuthnManager(this);
        int errorCode = bioAuthnManager.canAuth();
        if (errorCode != 0) {
            resultTextView.setText("");
            showResult("Can not authenticate. errorCode=" + errorCode);
            return;
        }

        // build prompt info
        BioAuthnPrompt.PromptInfo.Builder builder =
            new BioAuthnPrompt.PromptInfo.Builder().setTitle("This is the title.")
                .setSubtitle("This is the subtitle")
                .setDescription("This is the description");

        // The user will first be prompted to authenticate with biometrics, but also given the option to
        // authenticate with their device PIN, pattern, or password. setNegativeButtonText(CharSequence) should
        // not be set if this is set to true.
        builder.setDeviceCredentialAllowed(true);

        // Set the text for the negative button. setDeviceCredentialAllowed(true) should not be set if this button text
        // is set.
        // builder.setNegativeButtonText("This is the 'Cancel' button.");

        BioAuthnPrompt.PromptInfo info = builder.build();
        resultTextView.setText("Start fingerprint authentication without CryptoObject.\nAuthenticating......\n");
        bioAuthnPrompt.auth(info);
    }

    /**
     * Shows the fingerprint prompt with CryptoObject.
     *
     * @param view View
     */
    public void btnFingerAuthenticateWithCryptoObjectClicked(View view) {
        // Checks whether fingerprint authentication is available.
        BioAuthnManager bioAuthnManager = new BioAuthnManager(this);
        int errorCode = bioAuthnManager.canAuth();

        if (errorCode != 0) {
            resultTextView.setText("");
            showResult("Can not authenticate. errorCode=" + errorCode);
            return;
        }

        // build prompt info
        BioAuthnPrompt.PromptInfo.Builder builder =
            new BioAuthnPrompt.PromptInfo.Builder().setTitle("This is the title.")
                .setSubtitle("This is the subtitle.")
                .setDescription("This is the description.");

        // The user will first be prompted to authenticate with biometrics, but also given the option to
        // authenticate with their device PIN, pattern, or password. setNegativeButtonText(CharSequence) should
        // not be set if this is set to true.
        // builder.setDeviceCredentialAllowed(true);

        // Set the text for the negative button. setDeviceCredentialAllowed(true) should not be set if this
        // button text is set.
        builder.setNegativeButtonText("This is the 'Cancel' button.");

        BioAuthnPrompt.PromptInfo info = builder.build();

        // Construct CryptoObject.
        Cipher cipher = new HwBioAuthnCipherFactory("hw_test_fingerprint", true).getCipher();
        if (cipher == null) {
            showResult("Failed to create Cipher object.");
            return;
        }
        CryptoObject crypto = new CryptoObject(cipher);

        resultTextView.setText("Start fingerprint authentication with CryptoObject.\nAuthenticating......\n");

        // When user CryptoObject, BiometricPrompt.PromptInfo.Builder.setDeviceCredentialAllowed(true) is not allow.
        // Call BiometricPrompt.authenticate(BiometricPrompt.PromptInfo info) if
        // BiometricPrompt.PromptInfo.Builder.setDeviceCredentialAllowed(true) is set to true.
        bioAuthnPrompt.auth(info, crypto);
    }

    /**
     * Sends a 3D facial authentication request to the user device.
     *
     * @param view View
     */
    public void btnFaceAuthenticateWithoutCryptoObjectClicked(View view) {
        // check camera permission
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            showResult("The camera permission is not enabled. Please enable it.");

            // request camera permissions
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 1);
            return;
        }

        // call back
        BioAuthnCallback callback = new BioAuthnCallback() {
            @Override
            public void onAuthError(int errMsgId, CharSequence errString) {
                showResult("Authentication error. errorCode=" + errMsgId + ",errorMessage=" + errString
                    + (errMsgId == 1012 ? " The camera permission may not be enabled." : ""));
            }

            @Override
            public void onAuthHelp(int helpMsgId, CharSequence helpString) {
                resultTextView
                    .append("Authentication help. helpMsgId=" + helpMsgId + ",helpString=" + helpString + "\n");
            }

            @Override
            public void onAuthSucceeded(BioAuthnResult result) {
                showResult("Authentication succeeded. CryptoObject=" + result.getCryptoObject());
            }

            @Override
            public void onAuthFailed() {
                showResult("Authentication failed.");
            }
        };

        // Cancellation Signal
        CancellationSignal cancellationSignal = new CancellationSignal();

        FaceManager faceManager = new FaceManager(this);

        // Checks whether 3D facial authentication can be used.
        int errorCode = faceManager.canAuth();
        if (errorCode != 0) {
            resultTextView.setText("");
            showResult("Can not authenticate. errorCode=" + errorCode);
            return;
        }

        // flags
        int flags = 0;

        // Authentication messsage handler.
        Handler handler = null;

        // Recommended CryptoObject to be set to null. KeyStore is not associated with face authentication in current
        // version. KeyGenParameterSpec.Builder.setUserAuthenticationRequired() must be set false in this scenario.
        CryptoObject crypto = null;

        resultTextView.setText("Start face authentication.\nAuthenticating......\n");
        faceManager.auth(crypto, cancellationSignal, flags, callback, handler);
    }

    private void showResult(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Authentication Result");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.show();
        resultTextView.append(msg + "\n");
    }
}

/**
 * Cipher Factory
 *
 * @author Huawei HMS
 * @since 2019-11-19
 */
class HwBioAuthnCipherFactory {
    private static final String TAG = "HwBioAuthnCipherFactory";

    private String storeKey;

    private KeyStore keyStore;

    private KeyGenerator keyGenerator;

    private Cipher defaultCipher;

    private boolean isUserAuthenticationRequired;

    /**
     * constructed function
     *
     * @param storeKey story key name
     * @param isUserAuthenticationRequired Sets whether the key is authorized to be used only if the user has been
     *        authenticated.
     */
    HwBioAuthnCipherFactory(String storeKey, boolean isUserAuthenticationRequired) {
        this.storeKey = storeKey;
        this.isUserAuthenticationRequired = isUserAuthenticationRequired;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                initDefaultCipherObject();
            } catch (Exception e) {
                defaultCipher = null;
                Log.e(TAG, "Failed to init Cipher. " + e.getMessage());
            }
        } else {
            defaultCipher = null;
            Log.e(TAG, "Failed to init Cipher.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initDefaultCipherObject() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore(AndroidKeyStore). " + e.getMessage(), e);
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator(AndroidKeyStore)." + e.getMessage(),
                e);
        }

        createSecretKey(storeKey, true);

        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC
                + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        initCipher(defaultCipher, storeKey);
    }

    private void initCipher(Cipher cipher, String storeKeyName) {
        try {
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(storeKeyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
            | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher. " + e.getMessage(), e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createSecretKey(String storeKeyName, boolean isInvalidatedByBiometricEnrollment) {
        try {
            keyStore.load(null);
            KeyGenParameterSpec.Builder keyParamBuilder = new KeyGenParameterSpec.Builder(storeKeyName,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // This key is authorized to be used only if the user has been authenticated.
                    .setUserAuthenticationRequired(isUserAuthenticationRequired)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                keyParamBuilder.setInvalidatedByBiometricEnrollment(isInvalidatedByBiometricEnrollment);
            }
            keyGenerator.init(keyParamBuilder.build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException("Failed to create secret key. " + e.getMessage(), e);
        }
    }

    public Cipher getCipher() {
        return defaultCipher;
    }
}