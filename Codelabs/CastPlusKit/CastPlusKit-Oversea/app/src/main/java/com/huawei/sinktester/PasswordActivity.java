package com.huawei.sinktester;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {
    private Button mConfirmButton;
    private Button mCancelButton;
    private LinearLayout mPasswordLayout;
    private EditText[] mPasswordEditTextArray = new EditText[6];
    private Drawable mButtonFocusDrawable;
    private Drawable mButtonUnfocusDrawable;
    private static final String TAG = "PasswordActivity";

    private SeparatedEditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_password);

        mConfirmButton = findViewById(R.id.confirm_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mPasswordLayout = findViewById(R.id.password_layout);

        mPasswordEditText = findViewById(R.id.password_edittext);
        mPasswordEditTextArray[0] = findViewById(R.id.password_1_edittext);
        mPasswordEditTextArray[1] = findViewById(R.id.password_2_edittext);
        mPasswordEditTextArray[2] = findViewById(R.id.password_3_edittext);
        mPasswordEditTextArray[3] = findViewById(R.id.password_4_edittext);
        mPasswordEditTextArray[4] = findViewById(R.id.password_5_edittext);
        mPasswordEditTextArray[5] = findViewById(R.id.password_6_edittext);

        mButtonFocusDrawable = getDrawable(R.drawable.svgbutton_selected);
        mButtonUnfocusDrawable = getDrawable(R.drawable.svgbutton_unselected);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO set password to aar and save the password into database.
                if (TextUtils.isEmpty(mPasswordEditText.getText()) || mPasswordEditText.getText().toString().length()!=6){
                    Toast.makeText(PasswordActivity.this, "Enter 6 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                String password = mPasswordEditText.getText().toString();
                if (checkPassword(password)) {
                    boolean isNewPassword =
                            !password.equals(SharedPreferenceUtil.getPassword(PasswordActivity.this));
                    Intent setAuthModeIntent = new Intent();
                    setAuthModeIntent.setAction(SinkTesterService.BROADCAST_ACTION_SET_AUTH_MODE);
                    setAuthModeIntent.putExtra("needpassword", true);
                    setAuthModeIntent.putExtra("password", password);
                    setAuthModeIntent.putExtra("isnewpassword", isNewPassword);
                    sendBroadcast(setAuthModeIntent);

                    SharedPreferenceUtil.setAuthMode(PasswordActivity.this, true);
                    SharedPreferenceUtil.setPassword(PasswordActivity.this, password);
                    finish();
                } else {
                    //TODO Password Illegal, UI Alert.
                    Toast.makeText(PasswordActivity.this, "The password can contain only letters and digits", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(int i = 0;i < 6;i++){
            mPasswordEditTextArray[i].addTextChangedListener(this);
            mPasswordEditTextArray[i].setInputType(EditorInfo.TYPE_CLASS_NUMBER);

            if (i>0){
                final int index = i;
                mPasswordEditTextArray[i].setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_DEL) {
                            Log.d(TAG,"input method delete");
                            if (TextUtils.isEmpty(mPasswordEditTextArray[index].getText())){
                                mPasswordEditTextArray[index].clearFocus();
                                mPasswordEditTextArray[index-1].requestFocus();
                            }
                        }
                        return false;
                    }
                });
            }

        }

        mPasswordEditText.setTextChangedListener(new SeparatedEditText.TextChangedListener() {
            @Override
            public void textChanged(CharSequence changeText) {

            }

            @Override
            public void textCompleted(CharSequence text) {
                mPasswordEditText.clearFocus();
                mConfirmButton.requestFocus();
                Utils.hideInputMethod(mPasswordEditText,PasswordActivity.this);
            }
        });

        mConfirmButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(mButtonFocusDrawable);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((Button) v).setTextColor(getColor(R.color.colorTextColorSelected));
                    } else {
                        ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColorSelected));
                    }
                } else {
                    v.setBackground(mButtonUnfocusDrawable);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((Button) v).setTextColor(getColor(R.color.colorTextColor));
                    } else {
                        ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColor));
                    }
                }
            }
        });

        mCancelButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(mButtonFocusDrawable);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((Button) v).setTextColor(getColor(R.color.colorTextColorSelected));
                    } else {
                        ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColorSelected));
                    }
                } else {
                    v.setBackground(mButtonUnfocusDrawable);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((Button) v).setTextColor(getColor(R.color.colorTextColor));
                    } else {
                        ((Button) v).setTextColor(getResources().getColor(R.color.colorTextColor));
                    }
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.toString().length() == 1) {
            if (mPasswordEditTextArray[0].isFocused()) {
                mPasswordEditTextArray[0].clearFocus();
                mPasswordEditTextArray[1].requestFocus();
            } else if (mPasswordEditTextArray[1].isFocused()) {
                mPasswordEditTextArray[1].clearFocus();
                mPasswordEditTextArray[2].requestFocus();
            } else if (mPasswordEditTextArray[2].isFocused()) {
                mPasswordEditTextArray[2].clearFocus();
                mPasswordEditTextArray[3].requestFocus();
            } else if (mPasswordEditTextArray[3].isFocused()) {
                mPasswordEditTextArray[3].clearFocus();
                mPasswordEditTextArray[4].requestFocus();
            } else if (mPasswordEditTextArray[4].isFocused()) {
                mPasswordEditTextArray[4].clearFocus();
                mPasswordEditTextArray[5].requestFocus();
            } else if (mPasswordEditTextArray[5].isFocused()) {
                mPasswordEditTextArray[5].clearFocus();
                mConfirmButton.requestFocus();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private boolean checkPassword(String password) {
        String regEx = "^.{6}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
