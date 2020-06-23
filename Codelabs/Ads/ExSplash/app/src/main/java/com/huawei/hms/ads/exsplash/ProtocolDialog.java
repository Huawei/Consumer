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

package com.huawei.hms.ads.exsplash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Example of privacy dialog.
 */
public class ProtocolDialog extends Dialog {
    private static final String ACTION_SIMPLE_PRIVACY = "com.huawei.hms.ppskit.ACTION.SIMPLE_PRIVACY";

    private static final String ACTION_OAID_SETTING = "com.huawei.hms.action.OAID_SETTING";

    private static final String SP_NAME = "ExSplashSharedPreferences";

    private static final String SP_PROTOCOL_KEY = "user_consent_status";

    private Context mContext;

    private TextView titleTv;

    private TextView protocolTv;

    private Button confirmButton;

    private Button cancelButton;

    private LayoutInflater inflater;

    private ProtocolDialogCallback mCallback;

    /**
     * Privacy protocol dialog callback interface.
     */
    public interface ProtocolDialogCallback {
        void agree();
        void cancel();
    }

    /**
     * Constructor.
     *
     * @param context context
     */
    public ProtocolDialog(@NonNull Context context) {
        // Customize a dialog style.
        super(context, R.style.dialog);
        mContext = context;
    }

    /**
     * Set a dialog callback.
     *
     * @param callback callback
     */
    public void setCallback(ProtocolDialogCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window dialogWindow = getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);

        inflater = LayoutInflater.from(mContext);
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.dialog_protocol, null);
        setContentView(rootView);

        titleTv = findViewById(R.id.uniform_dialog_title);
        titleTv.setText(mContext.getString(R.string.protocol_title));
        protocolTv = findViewById(R.id.protocol_center_content);

        initClickableSpan();
        initButtonBar(rootView);
    }

    /**
     * Initialize the page of the button bar.
     *
     * @param rootView rootView
     */
    private void initButtonBar(LinearLayout rootView) {
        confirmButton = rootView.findViewById(R.id.base_okBtn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =
                    mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(SP_PROTOCOL_KEY, 1).commit();
                dismiss();

                if (mCallback != null) {
                    mCallback.agree();
                }
            }
        });

        cancelButton = rootView.findViewById(R.id.base_cancelBtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =
                        mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(SP_PROTOCOL_KEY, 0).commit();
                dismiss();
                if (mCallback != null) {
                    mCallback.cancel();
                }
            }
        });
    }

    private void initClickableSpan() {
        // Add a text-based tapping event.
        protocolTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        String privacyInfoText = mContext.getString(R.string.protocol_content_text);
        final SpannableStringBuilder spanPrivacyInfoText = new SpannableStringBuilder(privacyInfoText);

        // Set the listener on the event for tapping some text.
        ClickableSpan adsAndPrivacyTouchHere = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(ACTION_SIMPLE_PRIVACY);
            }
        };
        ClickableSpan personalizedAdsTouchHere = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(ACTION_OAID_SETTING);
            }
        };

        ForegroundColorSpan colorPrivacy = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        ForegroundColorSpan colorPersonalize = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        int privacyTouchHereStart = mContext.getResources().getInteger(R.integer.privacy_start);
        int privacyTouchHereEnd = mContext.getResources().getInteger(R.integer.privacy_end);
        int personalizedTouchHereStart = mContext.getResources().getInteger(R.integer.personalized_start);
        int personalizedTouchHereEnd = mContext.getResources().getInteger(R.integer.personalized_end);
        spanPrivacyInfoText.setSpan(adsAndPrivacyTouchHere, privacyTouchHereStart, privacyTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanPrivacyInfoText.setSpan(colorPrivacy, privacyTouchHereStart, privacyTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanPrivacyInfoText.setSpan(personalizedAdsTouchHere, personalizedTouchHereStart, personalizedTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanPrivacyInfoText.setSpan(colorPersonalize, personalizedTouchHereStart, personalizedTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolTv.setText(spanPrivacyInfoText);
        protocolTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startActivity(String action) {
        Intent enterNative = new Intent(action);
        PackageManager pkgMng = mContext.getPackageManager();
        if (pkgMng != null) {
            List<ResolveInfo> list = pkgMng.queryIntentActivities(enterNative, 0);
            if (!list.isEmpty()) {
                enterNative.setPackage("com.huawei.hwid");
                mContext.startActivity(enterNative);
            } else {
                // A message is displayed, indicating that no function is available and asking users to install HMS Core of the latest version.
                addAlertView();
            }
        }
    }

    /**
     * Prompt dialog, indicating that no function is available and asking users to install Huawei Mobile Services (APK) of the latest version.
     */
    private void addAlertView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.alert_title));
        builder.setMessage(mContext.getString(R.string.alert_content));
        builder.setPositiveButton(mContext.getString(R.string.alert_confirm), null);
        builder.show();
    }
}
