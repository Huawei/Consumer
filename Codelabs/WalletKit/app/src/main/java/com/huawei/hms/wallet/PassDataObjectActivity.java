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
import com.huawei.hms.wallet.pass.Location;
import com.huawei.hms.wallet.pass.PassObject;
import com.huawei.hms.wallet.pass.PassStatus;
import com.huawei.hms.wallet.pass.RelatedPassInfo;
import com.huawei.hms.wallet.util.BasisTimesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PassDataObjectActivity extends Activity
        implements HuaweiApiClient.OnConnectionFailedListener {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    int indext;
    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_info);
        Button button = findViewById(R.id.clickSaveData);
        spinner = (Spinner) findViewById(R.id.spinner);
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
                indext = arg2;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //serinumber
                EditText serinumberLoyalty = findViewById(R.id.serinumberLoyalty);
                String serinumber = serinumberLoyalty.getText().toString();
                if (TextUtils.isEmpty(serinumber)) {
                    Toast.makeText(PassDataObjectActivity.this, "serinumber can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText mPassStyleIdentifier = findViewById(R.id.passStyleIdentifier);
                TextView passTypeId = findViewById(R.id.passTypeId);
                String typeId = passTypeId.getText().toString();
                if (TextUtils.isEmpty(typeId)) {
                    Toast.makeText(PassDataObjectActivity.this, "Pass Type can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                TextView issuerIdView = findViewById(R.id.issuerId);
                String issuerId = issuerIdView.getText().toString();
                if (TextUtils.isEmpty(issuerId)) {
                    Toast.makeText(PassDataObjectActivity.this, "issuerId can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText mCardNumber = findViewById(R.id.cardNumberLoyalty);
                String cardNumber = mCardNumber.getText().toString();
                if (TextUtils.isEmpty(cardNumber)) {
                    Toast.makeText(PassDataObjectActivity.this, "Card Number can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText memberName = findViewById(R.id.memberName);
                String memberNameLoyalty = memberName.getText().toString();
                if (TextUtils.isEmpty(memberNameLoyalty)) {
                    Toast.makeText(PassDataObjectActivity.this, "Member Name can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //location
                EditText latitude = findViewById(R.id.latitude);
                EditText longitude = findViewById(R.id.longitude);
                EditText latitude1 = findViewById(R.id.latitude1);
                EditText longitude1 = findViewById(R.id.longitude1);

                //add location
                Double latitud = 0.0;
                Double longitud = 0.0;
                String latitudeStr = latitude.getText().toString();
                String longitudeStr = longitude.getText().toString();
                String latitudeStr1 = latitude1.getText().toString();
                String longitudeStr1 = longitude1.getText().toString();
                if (TextUtils.isEmpty(latitudeStr) || TextUtils.isEmpty(longitudeStr)) {
                    Toast.makeText(PassDataObjectActivity.this, "latitude & longitude can't be empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (-90.0D <= Double.valueOf(latitudeStr) && Double.valueOf(latitudeStr) <= 90.0D && -180.0D <= Double.valueOf(longitudeStr) && Double.valueOf(longitudeStr) <= 180.0D) {
                        latitud = Double.valueOf(latitudeStr);
                        longitud = Double.valueOf(longitudeStr);
                    } else {
                        Toast.makeText(PassDataObjectActivity.this, "latitude or longitude value is illegal", Toast.LENGTH_LONG).show();
                        return;
                    }
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

                PassObject.Builder passBuilder = PassObject.getBuilder();
                //commonFields
                ArrayList<CommonField> commonField = new ArrayList<>();
                //appendFields
                ArrayList<AppendField> appendFields = new ArrayList<>();

                //1	Background image of the card
                EditText backgroundImage = findViewById(R.id.backgroundImage);
                CommonField backgroundImageCommonFild = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BACKGROUND_IMG)
                        .setLabel("backgroundImageLable")
                        .setValue(backgroundImage.getText().toString())
                        .build();
                commonField.add(backgroundImageCommonFild);
                //2	Logo on the card
                EditText logoLoyalty = findViewById(R.id.logoLoyalty);
                CommonField logoCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_LOGO)
                        .setLabel("this is logo label")
                        .setValue(logoLoyalty.getText().toString())
                        .build();
                commonField.add(logoCommonField);

                //3	Merchant name
                EditText merchantNameLoyalty = findViewById(R.id.merchantNameLoyalty);
                CommonField merchantNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_MERCHANT_NAME)
                        .setLabel("this is merchantName label")
                        .setValue(merchantNameLoyalty.getText().toString())
                        .build();
                commonField.add(merchantNameCommonField);

                //4	Card name
                EditText nameLoyalty = findViewById(R.id.nameLoyalty);
                CommonField cardNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_NAME)
                        .setLabel("this is cardName label")
                        .setValue(nameLoyalty.getText().toString())
                        .build();
                commonField.add(cardNameCommonField);

                //5	Card number
                CommonField cardNumberCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_CARD_NUMBER)
                        .setLabel("Member Number")
                        .setValue(cardNumber)
                        .build();
                commonField.add(cardNumberCommonField);

                //6	Balance
                EditText balance = findViewById(R.id.balanceLoyalty);
                AppendField balanceCommonField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_BALANCE)
                        .setLabel("Balance")
                        .setValue(balance.getText().toString())
                        .build();
                appendFields.add(balanceCommonField);

                //7	Number of associated coupons
                EditText relatedPassId1 = findViewById(R.id.relatedPassId1);
                EditText relatedPassId2 = findViewById(R.id.relatedPassId2);
                EditText relatedPassId3 = findViewById(R.id.relatedPassId3);
                EditText relatedPassId4 = findViewById(R.id.relatedPassId4);
                ArrayList<RelatedPassInfo> relatedPassIds = new ArrayList<>();
                relatedPassIds.add(new RelatedPassInfo(relatedPassId1.getText().toString(), relatedPassId2.getText().toString()));
                relatedPassIds.add(new RelatedPassInfo(relatedPassId3.getText().toString(), relatedPassId4.getText().toString()));
                passBuilder.addRelatedPassIds(relatedPassIds);

                //8	Number of loyalty points
                EditText pointsLoyalty = findViewById(R.id.pointsLoyalty);
                AppendField pointsNubAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_POINTS)
                        .setLabel("Points")
                        .setValue(pointsLoyalty.getText().toString())
                        .build();
                appendFields.add(pointsNubAppendField);

                //9	Barcode or QR code
                //10	Remarks
                EditText mBarcodeText = findViewById(R.id.barcodeTextLoyalty);
                EditText mBarcodeValue = findViewById(R.id.barcodeValueLoyalty);
                BarCode barCode = BarCode.getBuilder()
                        .setType(BarCode.BARCODE_TYPE_QR_CODE)
                        .setValue(mBarcodeValue.getText().toString())
                        .settext(mBarcodeText.getText().toString())
                        .build();
                passBuilder.setBarCode(barCode);

                //11	Member name
                CommonField memberNameCommonField = CommonField.getBuilder()
                        .setKey(WalletPassConstant.PASS_COMMON_FIELD_KEY_MEMBER_NAME)
                        .setLabel("Member Name")
                        .setValue(memberNameLoyalty)
                        .build();
                commonField.add(memberNameCommonField);
                //12	Loyalty card number ->same to 5.Card number

                //13	Loyalty level
                EditText levelLoyalty = findViewById(R.id.levelLoyalty);
                AppendField levelAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_REWARDS_LEVEL)
                        .setLabel("Tier")
                        .setValue(levelLoyalty.getText().toString())
                        .build();
                appendFields.add(levelAppendField);

                //14	Message
                EditText messageHeader = findViewById(R.id.messageHeader);
                EditText messageBody = findViewById(R.id.messageBody);
                EditText messageHeader1 = findViewById(R.id.messageHeader1);
                EditText messageBody1 = findViewById(R.id.messageBody1);
                ArrayList<AppendField> messageList = new ArrayList<>();
                messageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(messageHeader.getText().toString())
                        .setValue(messageBody.getText().toString())
                        .build());
                messageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(messageHeader1.getText().toString())
                        .setValue(messageBody1.getText().toString())
                        .build());
                passBuilder.addMessageList(messageList);

                //15	Scrolling images
                //ImageUris
                EditText mImageModuleDataMainImageUris = findViewById(R.id.imageModuleDataMainImageUris);
                EditText mImageModuleDataMainImageUrisDes = findViewById(R.id.imageModuleDataMainImageUrisDes);
                EditText mImageModuleDataMainImageUris1 = findViewById(R.id.imageModuleDataMainImageUris1);
                EditText mImageModuleDataMainImageUrisDes1 = findViewById(R.id.imageModuleDataMainImageUrisDes1);
                ArrayList<AppendField> imageList = new ArrayList<>();
                imageList.add(AppendField.getBuilder()
                        .setKey("1")
                        .setLabel(mImageModuleDataMainImageUrisDes.getText().toString())
                        .setValue(mImageModuleDataMainImageUris.getText().toString())
                        .build());
                imageList.add(AppendField.getBuilder()
                        .setKey("2")
                        .setLabel(mImageModuleDataMainImageUrisDes1.getText().toString())
                        .setValue(mImageModuleDataMainImageUris1.getText().toString())
                        .build());
                passBuilder.addImageList(imageList);

                //16	Nearby stores
                EditText nearbyLocationsLable = findViewById(R.id.nearbyLocationsLable);
                EditText nearbyLocationsValue = findViewById(R.id.nearbyLocationsValue);
                String nearbyLocationsLableStr = nearbyLocationsLable.getText().toString();
                String nearbyLocationsValueStr = nearbyLocationsValue.getText().toString();
                AppendField nearbyAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_NEARBY_LOCATIONS)
                        .setLabel(nearbyLocationsLableStr)
                        .setValue(nearbyLocationsValueStr)
                        .build();
                appendFields.add(nearbyAppendField);

                //17	Main page
                EditText websiteLable = findViewById(R.id.websiteLable);
                EditText websiteValue = findViewById(R.id.websiteValue);
                String websiteLableStr = websiteLable.getText().toString();
                String websiteValueStr = websiteValue.getText().toString();
                AppendField mainPageAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_MAINPAGE)
                        .setLabel(websiteLableStr)
                        .setValue(websiteValueStr)
                        .build();
                appendFields.add(mainPageAppendField);

                //18	Hotline
                EditText hotlineLable = findViewById(R.id.hotlineLable);
                EditText hotlineValue = findViewById(R.id.hotlineValue);
                String hotlineLableStr = hotlineLable.getText().toString();
                String hotlineValueStr = hotlineValue.getText().toString();
                AppendField hotlineAppendField = AppendField.getBuilder()
                        .setKey(WalletPassConstant.PASS_APPEND_FIELD_KEY_HOTLINE)
                        .setLabel(hotlineLableStr)
                        .setValue(hotlineValueStr)
                        .build();
                appendFields.add(hotlineAppendField);
                //    LatLng
                ArrayList<Location> locationList = new ArrayList<>();
                locationList.add(new Location(latitudeStr, longitudeStr));
                locationList.add(new Location(latitudeStr1, longitudeStr1));
                passBuilder.addLocationList(locationList);

                //time
                Long start = 0L;
                Long end = 0L;
                Date date = new Date();
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(PassDataObjectActivity.this, "please select startTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    start = BasisTimesUtils.getLongTimeOfYMD(startTime);
                }

                if (TextUtils.isEmpty(endTime)) {
                    Toast.makeText(PassDataObjectActivity.this, "please select endTime", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    end = BasisTimesUtils.getLongTimeOfYMD(endTime);
                }

                if (end <= start || end <= date.getTime()) {
                    Toast.makeText(PassDataObjectActivity.this, "please check （endTime > startTime）&& (endTime > current Time)", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                passBuilder.setStatus(PassStatus.getBuilder().setState(state).setEffectTime(format.format(new Date(start))).setExpireTime(format.format(new Date(end))).build())
                        .setOrganizationPassId(cardNumber)
                        .setPassStyleIdentifier(mPassStyleIdentifier.getText().toString())
                        .setPassTypeIdentifier(typeId)
                        .setSerialNumber(serinumber)
                        .addAppendFields(appendFields)
                        .addCommonFields(commonField);
                PassObject passObject = passBuilder.build();
                Intent intent = new Intent(PassDataObjectActivity.this, PassTestActivity.class);
                intent.putExtra("passObject", passObject.toJson());
                intent.putExtra("passId", cardNumber);
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
        BasisTimesUtils.showDatePickerDialog(PassDataObjectActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                startTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(PassDataObjectActivity.this, "cancel DatePickerDialog", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void selectEndTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        BasisTimesUtils.showDatePickerDialog(PassDataObjectActivity.this, "true", year, month, day, new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                endTime = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            @Override
            public void onCancel() {
                Toast.makeText(PassDataObjectActivity.this, "cancel DatePickerDialog ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
