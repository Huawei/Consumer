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

public class CouponCardActivity extends Activity implements HuaweiApiClient.OnConnectionFailedListener {
    // passObject state
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    int indext;
    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_card_info);
        Button button = findViewById(R.id.clickSaveCouponData);
        //passObject state spinner
        spinner = (Spinner) findViewById(R.id.spinnerCoupon);
        //state list
        data_list = new ArrayList<String>();
        data_list.add("ACTIVE");
        data_list.add("COMPLETED");
        data_list.add("EXPIRED");
        data_list.add("INACTIVE");
        arr_adapter = new ArrayAdapter<String>(CouponCardActivity.this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                indext = arg2;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //serinumber
                TextView serinumberCoupon = findViewById(R.id.serinumberCoupon);
                String serinumber = serinumberCoupon.getText().toString();
                if (TextUtils.isEmpty(serinumber)) {
                    Toast.makeText(CouponCardActivity.this, "serinumber can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //oraginaztionId cardNumberCoupon
                TextView organizationIdCoupon = findViewById(R.id.cardNumberCoupon);
                String organizationId = organizationIdCoupon.getText().toString();
                if (TextUtils.isEmpty(organizationId)) {
                    Toast.makeText(CouponCardActivity.this, "Cardnumber can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //styleId
                EditText passStyleIdentifierCoupon = findViewById(R.id.passStyleIdentifierCoupon);
                String styleId = passStyleIdentifierCoupon.getText().toString();
                if (TextUtils.isEmpty(styleId)) {
                    Toast.makeText(CouponCardActivity.this, "Template ID can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                TextView passTypeId = findViewById(R.id.typeIdentifierCoupon);
                String typeId = passTypeId.getText().toString();
                if (TextUtils.isEmpty(typeId)) {
                    Toast.makeText(CouponCardActivity.this, "Pass Type can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                TextView issuerIdView = findViewById(R.id.issuerIdCoupon);
                String issuerId = issuerIdView.getText().toString();
                if (TextUtils.isEmpty(issuerId)) {
                    Toast.makeText(CouponCardActivity.this, "issuerId can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText backgroundColor = findViewById(R.id.backgroundColorCoupon);
                EditText logoCoupon = findViewById(R.id.logoCoupon);

                //passMerchantName
                TextView merchantNameCoupon = findViewById(R.id.merchantNameCoupon);
                String merchantName = merchantNameCoupon.getText().toString();
                if (TextUtils.isEmpty(merchantName)) {
                    Toast.makeText(CouponCardActivity.this, "Merchant Name can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // Coupontitle
                EditText nameCoupon = findViewById(R.id.nameCoupon);
                String name = nameCoupon.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CouponCardActivity.this, "Coupontitle can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //state
                String state = WalletPassConstant.PASS_STATE_ACTIVE;
                switch (data_list.get(indext)) {
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

                //time check
                Long start = 0L;
                Long end = 0L;
                Date date = new Date();
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(CouponCardActivity.this, "please select startTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    start = BasisTimesUtils.getLongTimeOfYMD(startTime);
                }

                if (TextUtils.isEmpty(endTime)) {
                    Toast.makeText(CouponCardActivity.this, "please select endTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    end = BasisTimesUtils.getLongTimeOfYMD(endTime);
                }

                if (end <= start || end <= date.getTime()) {
                    Toast.makeText(CouponCardActivity.this, "please check （endTime > startTime）&& (endTime > current Time)", Toast.LENGTH_LONG).show();
                    return;
                }

                //provider name
                EditText merchantProvidesCoupon = findViewById(R.id.merchantProvidesCoupon);
                String providesCoupon = merchantProvidesCoupon.getText().toString();
                if (TextUtils.isEmpty(providesCoupon)) {
                    Toast.makeText(CouponCardActivity.this, "ProvidesName can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //bar code
                EditText passBarcodeText = findViewById(R.id.barcodeTextCoupon);
                EditText passBarcodeValue = findViewById(R.id.barcodeValueCoupon);

                //Details
                EditText detailsCoupon = findViewById(R.id.DetailsCoupon);

                //ImageUris
                EditText imageModuleDataMainImageUrisCoupon = findViewById(R.id.imageModuleDataMainImageUrisCoupon);
                EditText imageModuleDataMainImageUrisDesCoupon = findViewById(R.id.imageModuleDataMainImageUrisDesCoupon);
                EditText imageModuleDataMainImageUrisCoupon1 = findViewById(R.id.imageModuleDataMainImageUrisCoupon1);
                EditText imageModuleDataMainImageUrisDesCoupon1 = findViewById(R.id.imageModuleDataMainImageUrisDesCoupon1);
                String imageUris = imageModuleDataMainImageUrisCoupon.getText().toString();
                String imageDes = imageModuleDataMainImageUrisDesCoupon.getText().toString();
                String imageUris1 = imageModuleDataMainImageUrisCoupon1.getText().toString();
                String imageDes1 = imageModuleDataMainImageUrisDesCoupon1.getText().toString();

                //disclaimerCoupon
                EditText disclaimerCoupon = findViewById(R.id.disclaimerCoupon);

                //message
                EditText messageHeaderCoupon = findViewById(R.id.messageHeaderCoupon);
                EditText messageBodyCoupon = findViewById(R.id.messageBodyCoupon);
                EditText messageHeaderCoupon1 = findViewById(R.id.messageHeaderCoupon1);
                EditText messageBodyCoupon1 = findViewById(R.id.messageBodyCoupon1);

                String mgeHeader = messageHeaderCoupon.getText().toString();
                String mgeBody = messageBodyCoupon.getText().toString();
                String mgeHeader1 = messageHeaderCoupon1.getText().toString();
                String mgeBody1 = messageBodyCoupon1.getText().toString();

                PassObject.Builder passBuilder = PassObject.getBuilder();
                //commonFields
                ArrayList<CommonField> commonField = new ArrayList<>();
                //appendFields
                ArrayList<AppendField> appendFields = new ArrayList<>();

                //1	Background color of the outer frame
                CommonField backgroundColorCommonFild = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_BACKGROUND_COLOR)
                        .setLabel("backgroundColorLable")
                        .setValue(backgroundColor.getText().toString())
                        .build();
                commonField.add(backgroundColorCommonFild);
                //3	Logo on the coupon
                CommonField logoCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_LOGO)
                        .setLabel("this is logo label")
                        .setValue(logoCoupon.getText().toString())
                        .build();
                commonField.add(logoCommonField);

                //4	Merchant name
                CommonField merchantNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_MERCHANT_NAME)
                        .setLabel("this is merchantName label")
                        .setValue(merchantName)
                        .build();
                commonField.add(merchantNameCommonField);
                //5	Coupon title
                CommonField couponTitleCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_NAME)
                        .setLabel("this is Coupontitle label")
                        .setValue(name)
                        .build();
                commonField.add(couponTitleCommonField);
                //6	Expiration time
                // SimpleDateFormat
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                passBuilder.setStatus(PassStatus.getBuilder().setState(state).setEffectTime(format.format(new Date(start))).setExpireTime(format.format(new Date(end))).build());

                //7	Merchant that provides the coupon
                AppendField merchantProvidesCommonField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_PROVIDER_NAME)
                        .setLabel("this is merchantProvidesCommonField label")
                        .setValue(providesCoupon)
                        .build();
                appendFields.add(merchantProvidesCommonField);

                //8	Barcode or QR code
                //9	Remarks
                BarCode barCode = BarCode.getBuilder()
                        .setType(BarCode.BARCODE_TYPE_QR_CODE)
                        .setValue(passBarcodeValue.getText().toString())
                        .settext(passBarcodeText.getText().toString())
                        .build();
                passBuilder.setBarCode(barCode);

                //10	Details
                AppendField detailsppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_DETAILS)
                        .setLabel("this is detailsppendField label")
                        .setValue(detailsCoupon.getText().toString())
                        .build();
                appendFields.add(detailsppendField);

                //11	Scrolling images
                ArrayList<AppendField> imageList = new ArrayList<>();
                imageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(imageDes)
                        .setValue(imageUris)
                        .build());
                imageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(imageDes1)
                        .setValue(imageUris1)
                        .build());
                passBuilder.addImageList(imageList);

                //12	Disclaimer
                AppendField disclaimerAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_DISCLAIMER)
                        .setLabel("this is disclaimerAppendField label")
                        .setValue(disclaimerCoupon.getText().toString())
                        .build();
                appendFields.add(disclaimerAppendField);

                //13	Message
                ArrayList<AppendField> messageList = new ArrayList<>();
                messageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(mgeHeader)
                        .setValue(mgeBody)
                        .build());
                messageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(mgeHeader1)
                        .setValue(mgeBody1)
                        .build());
                passBuilder.addMessageList(messageList);

                passBuilder.setOrganizationPassId(organizationId);
                passBuilder.setPassStyleIdentifier(styleId);
                passBuilder.setPassTypeIdentifier(typeId);
                passBuilder.setSerialNumber(serinumber);
                passBuilder.addAppendFields(appendFields);
                passBuilder.addCommonFields(commonField);

                PassObject passObject = passBuilder.build();
                Intent intent = new Intent(CouponCardActivity.this, PassTestActivity.class);
                intent.putExtra("passObject", passObject.toJson());
                intent.putExtra("passId", organizationId);
                intent.putExtra("issuerId", issuerId);
                intent.putExtra("typeId", typeId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void selectStartTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        BasisTimesUtils.showDatePickerDialog(CouponCardActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                startTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(CouponCardActivity.this, "cancel DatePickerDialog", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void selectEndTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        BasisTimesUtils.showDatePickerDialog(CouponCardActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                endTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(CouponCardActivity.this, "cancel DatePickerDialog", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
