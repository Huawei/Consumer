package com.huawei.dvkitdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.dmsdp.devicevirtualization.DvKit;
import com.huawei.dmsdp.devicevirtualization.DvNotification;
import com.huawei.dmsdp.devicevirtualization.DvNotificationService;

import android.app.Activity;

import com.huawei.dvkitdemoone.R;

import static com.huawei.dmsdp.devicevirtualization.DvKit.VIRTUAL_NOTIFICATION_SERVICE;

import java.util.Map;
import java.util.HashMap;

public class NotificationTest extends Activity {
    private int mOperationMode;

    private String mPackageName = null;

    private String mTitle = null;

    private String mSubTitle = null;

    private String mContent = null;

    private String mGuideDistanceUnit = null;

    private String mGuideText = null;

    private int mTemplate;

    private int mIconId;

    private int mDate;

    private int mGuideDistance;

    private int mGuideDirectionId;

    private int mNotificationId;

    private int mVibrate;

    private Context mContext;

    private String mUdid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = getApplicationContext();
        Intent intent = getIntent();
        if (intent != null) {
            try {
                mUdid = intent.getStringExtra("udid");
                if (mUdid != null) {
                    Log.i("getIntent mUdid :", mUdid);
                } else {
                    Log.i("getIntent", "mUdid is null");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("getIntent", "error");
            }
        }
        Button notifyTest = (Button) findViewById(R.id.notifyTest);
        final TextView notifyStatus = (TextView) findViewById(R.id.notifyStatus);
        final EditText operation_edit_text = findViewById(R.id.operation_edit_text);
        final EditText packageName_edit_text = findViewById(R.id.packageName_edit_text);
        final EditText template_edit_text = findViewById(R.id.template_edit_text);
        final EditText iconID_edit_text = findViewById(R.id.iconID_edit_text);
        final EditText title_edit_text = findViewById(R.id.title_edit_text);
        final EditText subTitle_edit_text = findViewById(R.id.subTitle_edit_text);
        final EditText content_edit_text = findViewById(R.id.content_edit_text);
        final EditText date_edit_text = findViewById(R.id.date_edit_text);
        final EditText guideDistance_edit_text = findViewById(R.id.guideDistance_edit_text);
        final EditText guideDistanceUnit_edit_text = findViewById(R.id.guideDistanceUnit_edit_text);
        final EditText guideDirectionId_edit_text = findViewById(R.id.guideDirectionId_edit_text);
        final EditText guideText_edit_text = findViewById(R.id.guideText_edit_text);
        final EditText notificationId_edit_text = findViewById(R.id.notificationId_edit_text);
        final EditText vibrate_edit_text = findViewById(R.id.vibrate_edit_text);

        /**
         * set packageName
         */
        packageName_edit_text.setText("com.huawei.health");
        mPackageName = "com.huawei.health";
        packageName_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = packageName_edit_text.getText().toString();
                mPackageName = res;
                Log.i("mPackageName :", mPackageName);
            }
        });

        /**
         * set NotificationId
         */
        notificationId_edit_text.setText("1");
        mNotificationId = 1;
        notificationId_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = notificationId_edit_text.getText().toString();
                try {
                    mNotificationId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mNotificationId = -1;
                    e.printStackTrace();
                }
                Log.i("mNotificationId :", mNotificationId + "");
            }
        });

        /**
         * set operationMode
         */
        operation_edit_text.setText("1");
        mOperationMode = 1;
        operation_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = operation_edit_text.getText().toString();
                try {
                    mOperationMode = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mOperationMode = -1;
                    e.printStackTrace();
                }
                Log.i("mOperationMode :", mOperationMode + "");
            }
        });

        /**
         * set template
         */
        template_edit_text.setText("2");
        mTemplate = 2;
        template_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = template_edit_text.getText().toString();

                try {
                    mTemplate = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mTemplate = -1;
                    e.printStackTrace();
                }
                Log.i("mTemplate :", mTemplate + "");
            }
        });

        /**
         * set iconID
         */
        iconID_edit_text.setText("1");
        mIconId = 1;
        iconID_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = iconID_edit_text.getText().toString();

                try {
                    mIconId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mIconId = 0;
                    e.printStackTrace();
                }
                Log.i("mIconId :", mIconId + "");
            }
        });

        /**
         * set title
         */
        title_edit_text.setText("title");
        mTitle = "title";
        title_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = title_edit_text.getText().toString();
                mTitle = res;
                Log.i("mTitle :", mTitle);
            }
        });

        /**
         * set subTitle
         */
        subTitle_edit_text.setText("subTitle");
        mSubTitle = "subTitle";
        subTitle_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = subTitle_edit_text.getText().toString();
                mSubTitle = res;
                Log.i("mSubTitle :", mSubTitle);
            }
        });

        /**
         * set content
         */
        content_edit_text.setText("content");
        mContent = "content";
        content_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = content_edit_text.getText().toString();
                mContent = res;
                Log.i("mContent :", mContent);
            }
        });

        /**
         * set guide distance
         */
        guideDistance_edit_text.setText("10");
        mGuideDistance = 10;
        guideDistance_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = guideDistance_edit_text.getText().toString();

                try {
                    mGuideDistance = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mGuideDistance = 0;
                    e.printStackTrace();
                }
                Log.i("mGuideDistance :", mGuideDistance + "");
            }
        });

        /**
         * set date
         */
        date_edit_text.setText("1");
        mDate = 1;
        date_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = date_edit_text.getText().toString();
                try {
                    mDate = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mDate = 0;
                    e.printStackTrace();
                }
                Log.i("mDate :", mDate + "");
            }
        });

        /**
         * set guide distance unit
         */
        guideDistanceUnit_edit_text.setText("m");
        mGuideDistanceUnit = "m";
        guideDistanceUnit_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuideDistanceUnit = guideDistanceUnit_edit_text.getText().toString();
                Log.i("mGuideDistanceUnit :", mGuideDistanceUnit + "");
            }
        });

        /**
         * set guide direction Id
         */
        guideDirectionId_edit_text.setText("1");
        mGuideDirectionId = 1;
        guideDirectionId_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = guideDirectionId_edit_text.getText().toString();

                try {
                    mGuideDirectionId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mGuideDirectionId = 0;
                    e.printStackTrace();
                }
                Log.i("mGuideDirectionId :", mGuideDirectionId + "");
            }
        });

        /**
         * set guide text
         */
        guideText_edit_text.setText("GuideText");
        mGuideText = "GuideText";
        guideText_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuideText = guideText_edit_text.getText().toString();
                Log.i("mGuideText :", mGuideText + "");
            }
        });

        /**
         * set vibrate mode
         */
        vibrate_edit_text.setText("3");
        mVibrate = 3;
        vibrate_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = vibrate_edit_text.getText().toString();
                try {
                    mVibrate = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mVibrate = 3;
                    e.printStackTrace();
                }
                Log.i("mGuideDirectionId :", mGuideDirectionId + "");
            }
        });

        notifyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DvNotificationService notificationService =
                    (DvNotificationService) DvKit.getInstance().getKitService(VIRTUAL_NOTIFICATION_SERVICE);
                DvNotification notification = new DvNotification();
                Map<String, Object> notificationProperty = new HashMap<>();
                if (mPackageName == null || mPackageName.isEmpty()) {
                    mPackageName = null;
                } else {
                    Log.i("mPackageName :", mPackageName);
                }
                notificationProperty.put(DvNotification.KEY_PACKAGE_NAME, mPackageName);
                Log.i("mTemplate :", mTemplate + "");
                notificationProperty.put(DvNotification.KEY_GUIDE_DISTANCE, mGuideDistance);
                Log.i("mGuideDistance :", mGuideDistance + "");
                if (mGuideDistanceUnit == null || mGuideDistanceUnit.isEmpty()) {
                    mGuideDistanceUnit = null;
                } else {
                    Log.i("mGuideDistanceUnit :", mGuideDistanceUnit);
                }
                notificationProperty.put(DvNotification.KEY_DISTANCE_UNIT, mGuideDistanceUnit);
                notificationProperty.put(DvNotification.KEY_DIRECTION_INDEX, mGuideDirectionId);
                Log.i("mGuideDirectionId :", mGuideDirectionId + "");
                if (mGuideText == null || mGuideText.isEmpty()) {
                    mGuideText = null;
                } else {
                    Log.i("mGuideText :", mGuideText);
                }
                notificationProperty.put(DvNotification.KEY_GUIDE_TEXT, mGuideText);
                if (mTitle == null || mTitle.isEmpty()) {
                    mTitle = null;
                } else {
                    Log.i("mTitle :", mTitle);
                }
                notificationProperty.put(DvNotification.KEY_TITLE, mTitle);
                if (mSubTitle == null || mSubTitle.isEmpty()) {
                    mSubTitle = null;
                } else {
                    Log.i("mSubTitle :", mSubTitle);
                }
                notificationProperty.put(DvNotification.KEY_SUBTITLE, mSubTitle);
                if (mContent == null || mContent.isEmpty()) {
                    mContent = null;
                } else {
                    Log.i("mContent :", mContent);
                }
                notificationProperty.put(DvNotification.KEY_CONTENT, mContent);
                notificationProperty.put(DvNotification.KEY_ICON_INDEX, mIconId);
                notificationProperty.put(DvNotification.KEY_DATE, mDate);
                notificationProperty.put(DvNotification.KEY_VIBRATE, mVibrate);
                Log.i("mIconId :", mIconId + "");
                Log.i("mNotificationId :", mNotificationId + "");
                Log.i("mOperationMode :", mOperationMode + "");
                Log.i("mVibrate :", mVibrate + "");
                notification.setNotificationProperty(mTemplate, notificationProperty);
                Log.i("notification :", notification.getNotificationProperty(mTemplate));
                int res = notificationService.doNotification(mUdid, mNotificationId, notification, mOperationMode);

                if (mUdid == null) {
                    Toast.makeText(getApplicationContext(), "udid is null", Toast.LENGTH_SHORT).show();
                }
                if (res == 0) {
                    Toast
                        .makeText(getApplicationContext(),
                            "Message notification's input parameter verification is successful", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    Toast
                        .makeText(getApplicationContext(),
                            "Message notification's input parameter verification is failed", Toast.LENGTH_SHORT)
                        .show();
                }
                notifyStatus.setText(String.valueOf(res));
            }
        });
    }
}

