/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.caaskitdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huawei.caas.caasservice.HwCaasHandler;
import com.huawei.caas.caasservice.HwCaasServiceCallBack;
import com.huawei.caas.caasservice.HwCaasUtils;
import com.huawei.caas.caasservice.HwCallAbilityCallBack;
import com.huawei.caas.caasservice.HwMakeCallCallBack;
import com.huawei.caaskitdemo.caaskit.CaasKitHelper;


public class CaasKitActivity extends AppCompatActivity {
    private static final String TAG = "CaasKitActivity";

    private static final int PHONE_NOT_SUPPORT_HICALL = 3002;

    private static final int SHOW_TOAST = 0;

    private static final int SET_ALL_VISIBALE = 1;

    private static final int SET_ALL_INVISIBALE = 2;

    private static final int SET_AUDIO_VISIBALE = 3;

    private static final int SET_AUDIO_INVISIBALE = 4;

    private EditText mPhoneNumber;

    private Button mStartQuery;

    private Button mMakeVideoCall;

    private Button mMakeAudioCall;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int index = msg.what;
            switch (index) {
                case SHOW_TOAST:
                    toast("caas not support.");
                    break;
                case SET_ALL_VISIBALE:
                    setVisibility(true, 0);
                    break;
                case SET_ALL_INVISIBALE:
                    setVisibility(false, 0);
                    break;
                case SET_AUDIO_VISIBALE:
                    setVisibility(true, 1);
                    break;
                case SET_AUDIO_INVISIBALE:
                    setVisibility(false, 1);
                    break;
                default:
                    break;
            }
        }
    };

    private HwCaasServiceCallBack mCallBack = new HwCaasServiceCallBack() {
        @Override
        public void initSuccess(HwCaasHandler handler) {
            Log.i(TAG, "HwCaasServiceCallBack:initSuccess.");
            /** Callback after successful initialization of HwCaasHandler. */
            CaasKitHelper.getInstance().setHwCaasHandler(handler);
            CaasKitHelper.getInstance().setCallAbilityCallBack(mAbilityCallBack);
            CaasKitHelper.getInstance().setMakeCallCallBack(new HwMakeCallCallBack() {
                @Override
                public void makeCallResult(String number, int retCode) {
                    Log.d(TAG, "number: " + number + " retCode: " + retCode);
                }
            });
        }

        @Override
        public void initFail(int retCode) {
            /** Callback if init Handler fail. */
            Log.i(TAG, "HwCaasServiceCallBack:initFail:retCode: " + retCode);
            setVisibility(false, 0);
            toast("HwCaasServiceCallBack initFail");
        }

        @Override
        public void releaseSuccess() {
            /** Callback after successful release of mHwCaasServiceManager. */
            Log.i(TAG, "HwCaasServiceCallBack:releaseSuccess");
            CaasKitHelper.getInstance().setHwCaasHandler(null);
        }
    };

    private HwCallAbilityCallBack mAbilityCallBack = new HwCallAbilityCallBack() {
        @Override
        public void callAbilityResult(String phoneNumberSha256, int retCode) {
            Log.i(TAG, "HwCallAbilityCallBack:phoneNumberSha256:" + phoneNumberSha256 + " retCode:" + retCode);
            if (retCode == 0) {
                handler.sendMessage(handler.obtainMessage(SET_ALL_VISIBALE));
            } else if (retCode == 1) {
                handler.sendMessage(handler.obtainMessage(SET_AUDIO_VISIBALE));
            } else if (retCode == PHONE_NOT_SUPPORT_HICALL) {
                handler.sendMessage(handler.obtainMessage(SHOW_TOAST));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caaskitquickcall);
        initView();
        initData();
        CaasKitHelper.getInstance().caasKitInit(mCallBack);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume.");
    }

    private void initView() {
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mStartQuery = (Button) findViewById(R.id.start_query);
        mMakeVideoCall = (Button) findViewById(R.id.make_video_call);
        mMakeAudioCall = (Button) findViewById(R.id.make_audio_call);
    }

    private void initData() {
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setVisibility(false, 0);
            }
        });

        mStartQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaasKitHelper.getInstance().queryHiCallAbility(mPhoneNumber.getText().toString());
            }
        });

        mMakeVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(HwCaasUtils.CallType.VIDEO_CALL);
            }
        });

        mMakeAudioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(HwCaasUtils.CallType.VOICE_CALL);
            }
        });
        setVisibility(false, 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy.");
        CaasKitHelper.getInstance().caasKitRelease();
    }

    private void makeCall(HwCaasUtils.CallType callType) {
        int retCode = CaasKitHelper.getInstance().makeCall(mPhoneNumber.getText().toString(), callType);
        Log.d(TAG, "makeCall:retCode: " + retCode);
    }

    private void setVisibility(boolean visible, int callType) {
        int videoCallState = mMakeVideoCall.getVisibility();
        int audioCallState = mMakeAudioCall.getVisibility();
        if (callType == 0) {
            if (visible && (videoCallState != 0 || audioCallState != 0)) {
                mMakeVideoCall.setVisibility(View.VISIBLE);
                mMakeAudioCall.setVisibility(View.VISIBLE);
            } else if (!visible && (videoCallState == 0 || audioCallState == 0)) {
                mMakeVideoCall.setVisibility(View.INVISIBLE);
                mMakeAudioCall.setVisibility(View.INVISIBLE);
            }
        } else if (callType == 1) {
            if (visible && (audioCallState != 0)) {
                mMakeAudioCall.setVisibility(View.VISIBLE);
            } else if (!visible && (audioCallState == 0)) {
                mMakeAudioCall.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * show toast
     *
     * @param msg show msg
     */
    private void toast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(CaasKitApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
