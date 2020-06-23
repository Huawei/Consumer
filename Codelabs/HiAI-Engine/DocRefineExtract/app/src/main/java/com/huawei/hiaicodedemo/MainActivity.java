package com.huawei.hiaicodedemo;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.Manifest;
import android.os.Build;
import android.view.View;

import com.huawei.hiaicodedemo.activity.DocSkewCorrectionActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private Uri imageUri;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {
        requestPermissions(permission, REQUEST_CODE);
        initView();
    }


    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    private void initView() {
        ImageView imageView = findViewById(R.id.iv_bg);
        if(isCN()){
            imageView.setImageResource(R.mipmap.note_bg_up);
        }else {
            imageView.setImageResource(R.mipmap.note_bg_up_en);
        }
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_album).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(MyApplication.isSupport){
            switch (v.getId()) {
                case R.id.btn_camera:
                    takePhoto();
                    break;
                case R.id.btn_album:
                    selectImage();
                    break;
                default:
            }
        }else {
            toast("The device does not support this API");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO && data != null) {
                try {
                    imageUri = data.getData();
                } catch (Exception e) {
                    toast(getString(R.string.toast_1));
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    imageUri = photoUri;
                } catch (Exception e) {
                    toast(getString(R.string.toast_1));
                    e.printStackTrace();
                }
            }
            intentString(false, DocSkewCorrectionActivity.class, imageUri.toString());
        } else {
            toast(getString(R.string.toast_2));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
