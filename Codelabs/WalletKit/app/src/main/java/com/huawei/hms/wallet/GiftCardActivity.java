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

package com.huawei.hms.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;

import com.huawei.hms.wallet.apptest.R;
import com.huawei.hms.wallet.constant.WalletPassConstant;
import com.huawei.hms.wallet.pass.AppendField;
import com.huawei.hms.wallet.pass.BarCode;
import com.huawei.hms.wallet.pass.CommonField;
import com.huawei.hms.wallet.pass.PassObject;
import com.huawei.hms.wallet.pass.PassStatus;
import com.huawei.hms.wallet.util.BasisTimesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GiftCardActivity extends Activity implements HuaweiApiClient.OnConnectionFailedListener {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    int index;
    private Spinner barCodeStyleSpinner;
    private List<String> barCodeStyleList;
    private ArrayAdapter<String> barCodeStyleAdapter;
    int barCodeStyleIndex;
    private String updateTime = "";
    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_card_info);
        Button button = findViewById(R.id.clickSaveGiftData);
        spinner = (Spinner) findViewById(R.id.spinnerGift);
        data_list = new ArrayList<String>();
        data_list.add("ACTIVE");
        data_list.add("COMPLETED");
        data_list.add("EXPIRED");
        data_list.add("INACTIVE");
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                index = arg2;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //barCode下拉框
        barCodeStyleSpinner = (Spinner) findViewById(R.id.gitfBarcodeStyle);
        barCodeStyleList = new ArrayList<String>();
        barCodeStyleList.add("codabar");
        barCodeStyleList.add("qrCode");
        barCodeStyleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, barCodeStyleList);
        barCodeStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barCodeStyleSpinner.setAdapter(barCodeStyleAdapter);

        barCodeStyleSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                barCodeStyleIndex = arg2;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText passStyleIdentifierGift = findViewById(R.id.passStyleIdentifierGift);
                String styleId = passStyleIdentifierGift.getText().toString();
                if (TextUtils.isEmpty(styleId)) {
                    Toast.makeText(GiftCardActivity.this, "Template ID can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //Construct gift card data.
                PassObject.Builder passBuilder = PassObject.getBuilder();
                //commonFields
                ArrayList<CommonField> commonField = new ArrayList<>();
                //appendFields
                ArrayList<AppendField> appendFields = new ArrayList<>();

                //Background and Description
                EditText backGroundColorEdit = findViewById(R.id.giftBackGroundImage);
                EditText backGroundColorDescEdit = findViewById(R.id.giftBackGroundImageDesc);

                //1	Background image of the card
                CommonField backgroundImageCommonFild = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BACKGROUND_IMG)
                        .setLabel(backGroundColorDescEdit.getText().toString())
                        .setValue(backGroundColorEdit.getText().toString())
                        .build();
                commonField.add(backgroundImageCommonFild);

                //2	Logo on the card
                //Logo
                EditText giftCardLogoEdit = findViewById(R.id.giftCardLogo);
                CommonField logoCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_LOGO)
                        .setLabel("this is logo label")
                        .setValue(giftCardLogoEdit.getText().toString())
                        .build();
                commonField.add(logoCommonField);

                //3	Merchant name
                EditText merchantNameEdit = findViewById(R.id.giftMerchantName);

                CommonField merchantNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_MERCHANT_NAME)
                        .setLabel("this is merchantName label")
                        .setValue(merchantNameEdit.getText().toString())
                        .build();
                commonField.add(merchantNameCommonField);

                //4	Card name
                EditText giftCardnameEdit = findViewById(R.id.giftCardname);
                CommonField cardNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_NAME)
                        .setLabel("this is cardName label")
                        .setValue(giftCardnameEdit.getText().toString())
                        .build();
                commonField.add(cardNameCommonField);

                //5	Card number
                EditText giftCardNumberEdit = findViewById(R.id.giftCardNumber);
                if (TextUtils.isEmpty(giftCardNumberEdit.getText().toString())) {
                    giftCardNumberEdit.setError("please input Card number");
                    return;
                }
                CommonField cardNumberCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_CARD_NUMBER)
                        .setLabel("this is cardNumberCommonField label")
                        .setValue(giftCardNumberEdit.getText().toString())
                        .build();
                commonField.add(cardNumberCommonField);

                //6	Balance

                EditText giftBalanceEdit = findViewById(R.id.giftBalance);
                if (TextUtils.isEmpty(giftBalanceEdit.getText().toString())) {
                    giftBalanceEdit.setError("please select startTime");
                    return;
                }
                CommonField balanceCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BALANCE)
                        .setLabel("this is balanceCommonField label")
                        .setValue(giftBalanceEdit.getText().toString())
                        .build();
                commonField.add(balanceCommonField);

                //7*Barcode or QR code 8*Remarks
                EditText passBarcodeAlternateText = findViewById(R.id.passBarcodeAlternateText);
             //   EditText passBarcodeType = findViewById(R.id.passBarcodeType);
                String barType = BarCode.BARCODE_TYPE_QR_CODE;
                EditText passBarcodeValue = findViewById(R.id.passBarcodeValue);
               switch (barCodeStyleList.get(barCodeStyleIndex)) {
                    case "codabar":
                        barType = BarCode.BARCODE_TYPE_QR_CODE;
                      break;
                    case "qrCode":
                        barType = BarCode.BARCODE_TYPE_CODABAR;
                        break;
                    }

                BarCode barCode = BarCode.getBuilder()
                        .setType(barType)
                        .setValue(passBarcodeValue.getText().toString())
                        .settext(passBarcodeAlternateText.getText().toString())
                        .build();
                passBuilder.setBarCode(barCode);

                //9*Balance update time
                //balance updateTime
                Long update = 0L;
                if (TextUtils.isEmpty(updateTime)) {
                    Toast.makeText(GiftCardActivity.this, "please select balance updateTime ", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    update = BasisTimesUtils.getLongTimeOfYMD(updateTime);
                }

                // SimpleDateFormat
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                // create SimpleDateFormat
                  String  updateTimeValue = format.format(new Date(update));
                CommonField banlanceUpdateTimeAppendField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BALANCE_REFRESH_TIME)
                        .setLabel("Updated")
                        .setValue(updateTimeValue)
                        .build();
                commonField.add(banlanceUpdateTimeAppendField);

                 //10* PIN
                EditText giftCardPinEdit = findViewById(R.id.giftCardPin);
                CommonField pinCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BLANCE_PIN)
                        .setLabel("PIN Number")
                        .setValue(giftCardPinEdit.getText().toString())
                        .build();
                commonField.add(pinCommonField);

                //11*Event number
                EditText giftEventNumberEdit = findViewById(R.id.giftEventNumber);
                AppendField eventNumber = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_EVENT_NUMBER)
                        .setLabel("Event Number")
                        .setValue(giftEventNumberEdit.getText().toString())
                        .build();
                 appendFields.add(eventNumber);

                //12	Message
                //message
                EditText messageHeaderGift = findViewById(R.id.messageHeaderGift);
                EditText messageBodyGift = findViewById(R.id.messageBodyGift);
                EditText messageHeaderGift1 = findViewById(R.id.messageHeaderGift1);
                EditText messageBodyGift1 = findViewById(R.id.messageBodyGift1);
                ArrayList<AppendField> messageList = new ArrayList<>();
                messageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(messageHeaderGift.getText().toString())
                        .setValue(messageBodyGift.getText().toString())
                        .build());
                messageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(messageHeaderGift1.getText().toString())
                        .setValue(messageBodyGift1.getText().toString())
                        .build());
                passBuilder.addMessageList(messageList);

                //13* Scrolling images
                ArrayList<AppendField> imageList = new ArrayList<>();
                EditText scrollingImages1Edit = findViewById(R.id.giftScrollingImages1);
                EditText giftScrollingDesc1Edit = findViewById(R.id.giftScrollingDesc1);
                EditText scrollingImages2Edit = findViewById(R.id.giftScrollingImages2);
                EditText giftScrollingDesc2Edit = findViewById(R.id.giftScrollingDesc2);
                imageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(giftScrollingDesc1Edit.getText().toString())
                        .setValue(scrollingImages1Edit.getText().toString())
                        .build());
                imageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(giftScrollingDesc2Edit.getText().toString())
                        .setValue(scrollingImages2Edit.getText().toString())
                        .build());
                passBuilder.addImageList(imageList);

                // 14* Nearby stores
                EditText giftNearbyStoresUrlEdit = findViewById(R.id.giftNearbyStoresUrl);
                EditText giftNearbyStoresNameEdit = findViewById(R.id.giftNearbyStoresName);
                AppendField nearbyAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_NEARBY_LOCATIONS)
                        .setLabel(giftNearbyStoresNameEdit.getText().toString())
                        .setValue(giftNearbyStoresUrlEdit.getText().toString())
                        .build();
                appendFields.add(nearbyAppendField);
                //15	Main page
                EditText giftMainPageUrlEdit = findViewById(R.id.giftMainPageUrl);
                EditText giftMainPageNameEdit = findViewById(R.id.giftMainPageName);

                AppendField mainPageAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_MAINPAGE)
                        .setLabel(giftMainPageNameEdit.getText().toString())
                        .setValue(giftMainPageUrlEdit.getText().toString())
                        .build();
                appendFields.add(mainPageAppendField);
                //16	Hotline
                EditText giftHotlinePoneEdit = findViewById(R.id.giftHotlinePone);
                EditText giftHotlineNameEdit = findViewById(R.id.giftHotlineName);
                AppendField hotlineAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_HOTLINE)
                        .setLabel(giftHotlineNameEdit.getText().toString())
                        .setValue(giftHotlinePoneEdit.getText().toString())
                        .build();
                //公共常量
                //time check
                Long start = 0L;
                Long end = 0L;
                Date date = new Date();
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(GiftCardActivity.this, "please select startTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    start = BasisTimesUtils.getLongTimeOfYMD(startTime);
                }

                if (TextUtils.isEmpty(endTime)) {
                    Toast.makeText(GiftCardActivity.this, "please select endTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    end = BasisTimesUtils.getLongTimeOfYMD(endTime);
                }

                if (end <= start || end <= date.getTime()) {
                    Toast.makeText(GiftCardActivity.this, "please check （endTime > startTime）&& (endTime > current Time)", Toast.LENGTH_LONG).show();
                    return;
                }
                //serinumber
                TextView organizationPassIdGift = findViewById(R.id.organizationPassIdGift);
                String organizationPassId = organizationPassIdGift.getText().toString();
                if (TextUtils.isEmpty(organizationPassId)) {
                    Toast.makeText(GiftCardActivity.this, "SerialNumber can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                TextView passTypeId = findViewById(R.id.passTypeIdentifier);
                String typeId = passTypeId.getText().toString();
                if (TextUtils.isEmpty(typeId)) {
                    Toast.makeText(GiftCardActivity.this, "Pass Type can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                TextView issuerIdView = findViewById(R.id.issuerIdGift);
                String issuerId = issuerIdView.getText().toString();
                if (TextUtils.isEmpty(issuerId)) {
                    Toast.makeText(GiftCardActivity.this, "issuerId can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //state
                String state = WalletPassConstant.PASS_STATE_ACTIVE;
                switch (data_list.get(index)) {
                    case "ACTIVE":
                        state = WalletPassConstant.PASS_STATE_ACTIVE;
                        break;
                    case "COMPLETED":
                        state = WalletPassConstant.PASS_STATE_COMPLETED;
                        break;
                    case "EXPIRED":
                        state = WalletPassConstant.PASS_STATE_EXPIRED;
                        break;
                    case "INACTIVE":
                        state = WalletPassConstant.PASS_STATE_INACTIVE;
                }

                appendFields.add(hotlineAppendField);
                //页面输入的PassStyleIdentifier
                EditText mPassStyleIdentifier = findViewById(R.id.passStyleIdentifierGift);
                passBuilder.setOrganizationPassId(giftCardNumberEdit.getText().toString());
                passBuilder.setPassStyleIdentifier(mPassStyleIdentifier.getText().toString());
                passBuilder.setPassTypeIdentifier(typeId);
                passBuilder.setSerialNumber(organizationPassId);
                passBuilder.setStatus(PassStatus.getBuilder().setState(state).setEffectTime(format.format(new Date(start))).setExpireTime(format.format(new Date(end))).build());
                passBuilder.addAppendFields(appendFields);
                passBuilder.addCommonFields(commonField);
                PassObject passObject = passBuilder.build();
                Log.i("PassTestActivity", "passObject:" + passObject.toJson());
                Intent intent = new Intent(GiftCardActivity.this, PassTestActivity.class);
                intent.putExtra("passObject", passObject.toJson());
                intent.putExtra("passId", organizationPassId);
                intent.putExtra("issuerId", issuerId);
                intent.putExtra("typeId", typeId);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void passBalanceUpdateTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        BasisTimesUtils.showDatePickerDialog(GiftCardActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                updateTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(GiftCardActivity.this, "cancel DatePickerDialog", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void selectStartTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        BasisTimesUtils.showDatePickerDialog(GiftCardActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                startTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(GiftCardActivity.this, "cancel DatePickerDialog ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectEndTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        BasisTimesUtils.showDatePickerDialog(GiftCardActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                endTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(GiftCardActivity.this, "DatePickerDialog取消 ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
