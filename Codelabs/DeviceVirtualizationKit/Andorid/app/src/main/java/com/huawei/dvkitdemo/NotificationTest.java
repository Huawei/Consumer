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
/**
 * 功能描述
 *
 * @author
 * @since
 */
public class NotificationTest extends Activity {
    private int mOperationMode;
    private String mPackageName = null;
    private String mTitle = null;
    private String mSubTitle = null;
    private String mContent = null;
    private String mGuideDistanceUnit = null;
    private String mGudieText = null;
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
        final TextView notifyStatus =
            (TextView) findViewById(R.id.notifyStatus);
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

        // 包名设置
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
                String res = packageName_edit_text.getText().toString();//获取内容
                mPackageName = res;
                Log.i("mPackageName :", mPackageName);
            }
        });

        // NotificationId设置
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
                String res = notificationId_edit_text.getText().toString();//获取内容
                try {
                    mNotificationId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mNotificationId = -1;
                    e.printStackTrace();
                }
                Log.i("mNotificationId :", mNotificationId + "");
            }
        });

        //operationMode设置
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
                String res = operation_edit_text.getText().toString();//获取内容
                try {
                    mOperationMode = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mOperationMode = -1;
                    e.printStackTrace();
                }
                Log.i("mOperationMode :", mOperationMode + "");
            }
        });

        //模板设置
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
                String res = template_edit_text.getText().toString();//获取内容

                try {
                    mTemplate = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mTemplate = -1;
                    e.printStackTrace();
                }
                Log.i("mTemplate :", mTemplate + "");
            }
        });

        //图标ID设置
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
                String res = iconID_edit_text.getText().toString();//获取内容

                try {
                    mIconId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mIconId = 0;
                    e.printStackTrace();
                }
                Log.i("mIconId :", mIconId + "");
            }
        });

        // 标题设置
        title_edit_text.setText("导航");
        mTitle = "导航";
        title_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = title_edit_text.getText().toString();//获取内容
                mTitle = res;
                Log.i("mTitle :", mTitle);
            }
        });

        // 子标题设置
        subTitle_edit_text.setText("子标题");
        mSubTitle = "子标题";
        subTitle_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = subTitle_edit_text.getText().toString();//获取内容
                mSubTitle = res;
                Log.i("mSubTitle :", mSubTitle);
            }
        });

        // 内容设置
        content_edit_text.setText("1234");
        mContent = "1234";
        content_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = content_edit_text.getText().toString();//获取内容
                mContent = res;
                Log.i("mContent :", mContent);
            }
        });

        //导向距离设置
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
                String res = guideDistance_edit_text.getText().toString();//获取内容

                try {
                    mGuideDistance = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mGuideDistance = 0;
                    e.printStackTrace();
                }
                Log.i("mGuideDistance :", mGuideDistance + "");
            }
        });

        //date设置
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
                String res = date_edit_text.getText().toString();//获取内容

                try {
                    mDate = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mDate = 0;
                    e.printStackTrace();
                }
                Log.i("mDate :", mDate + "");
            }
        });

        //导向距离单位
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
                mGuideDistanceUnit = guideDistanceUnit_edit_text.getText().toString();//获取内容
                Log.i("mGuideDistanceUnit :", mGuideDistanceUnit + "");
            }
        });

        //DirectionId设置
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
                String res = guideDirectionId_edit_text.getText().toString();//获取内容

                try {
                    mGuideDirectionId = Integer.parseInt(res);
                } catch (NumberFormatException e) {
                    mGuideDirectionId = 0;
                    e.printStackTrace();
                }
                Log.i("mGuideDirectionId :", mGuideDirectionId + "");
            }
        });

        //GuideText设置
        guideText_edit_text.setText("GuideText");
        mGudieText = "GuideText";
        guideText_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGudieText = guideText_edit_text.getText().toString();//获取内容
                Log.i("mGudieText :", mGudieText + "");
            }
        });
        //vibrate设置
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
                String res = vibrate_edit_text.getText().toString();//获取内容

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
                Map<String, Object> notificaitonProperty = new HashMap<>();
                if (mPackageName == null || mPackageName.isEmpty()) {
                    mPackageName = null;
                } else {
                    Log.i("mPackageName :", mPackageName);
                }
                notificaitonProperty.put(DvNotification.KEY_PACKAGE_NAME, mPackageName); // 设置三方应用的包名，用于区分消息的来源
                Log.i("mTemplate :", mTemplate + "");
                notificaitonProperty.put(DvNotification.KEY_GUIDE_DISTANCE, mGuideDistance); // 设置导航的距离
                Log.i("mGuideDistance :", mGuideDistance + "");
                if (mGuideDistanceUnit == null || mGuideDistanceUnit.isEmpty()) {
                    mGuideDistanceUnit = null;
                } else {
                    Log.i("mGuideDistanceUnit :", mGuideDistanceUnit);
                }
                notificaitonProperty.put(DvNotification.KEY_DISTANCE_UNIT, mGuideDistanceUnit); // 设置导航的单位
                notificaitonProperty.put(DvNotification.KEY_DIRECTION_INDEX, mGuideDirectionId); // 设置导航图标的索引
                Log.i("mGuideDirectionId :", mGuideDirectionId + "");
                if (mGudieText == null || mGudieText.isEmpty()) {
                    mGudieText = null;
                } else {
                    Log.i("mGudieText :", mGudieText);
                }
                notificaitonProperty.put(DvNotification.KEY_GUIDE_TEXT, mGudieText); // 设置消息导航的文本
                if (mTitle == null || mTitle.isEmpty()) {
                    mTitle = null;
                } else {
                    Log.i("mTitle :", mTitle);
                }
                notificaitonProperty.put(DvNotification.KEY_TITLE, mTitle); //通用模板使用，导航类模板无需设置
                if (mSubTitle == null || mSubTitle.isEmpty()) {
                    mSubTitle = null;
                } else {
                    Log.i("mSubTitle :", mSubTitle);
                }
                notificaitonProperty.put(DvNotification.KEY_SUBTITLE, mSubTitle); //通用模板使用，导航类模板无需设置
                if (mContent == null || mContent.isEmpty()) {
                    mContent = null;
                } else {
                    Log.i("mContent :", mContent);
                }
                notificaitonProperty.put(DvNotification.KEY_CONTENT, mContent); //通用模板使用，导航类模板无需设置
                notificaitonProperty.put(DvNotification.KEY_ICON_INDEX, mIconId); //通用模板使用，导航类模板无需设置
                notificaitonProperty.put(DvNotification.KEY_DATE, mDate); //通用模板使用，导航类模板无需设置
                notificaitonProperty.put(DvNotification.KEY_VIBRATE, mVibrate); // 设置消息提醒的震动方式
                Log.i("mIconId :", mIconId + "");
                Log.i("mNotificationId :", mNotificationId + "");
                Log.i("mOperationMode :", mOperationMode + "");
                Log.i("mVibrate :", mVibrate + "");
                notification.setNotificationProperty(mTemplate,notificaitonProperty); // 通过该接口设置消息的属性
                Log.i("notification :", notification.getNotificationProperty(mTemplate));
                int res = notificationService.doNotification(mUdid, mNotificationId, notification,
                    mOperationMode); // 发送消息通知到设备的接口

                if (mUdid == null) {
                    Toast.makeText(getApplicationContext(), "udid为空",
                        Toast.LENGTH_SHORT).show();
                }
                if (res == 0) {
                    Toast.makeText(getApplicationContext(), "发起消息推送入参校验成功",
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "发起消息推送入参校验失败",
                        Toast.LENGTH_SHORT).show();
                }
                notifyStatus.setText(String.valueOf(res));
            }
        });
    }
}

