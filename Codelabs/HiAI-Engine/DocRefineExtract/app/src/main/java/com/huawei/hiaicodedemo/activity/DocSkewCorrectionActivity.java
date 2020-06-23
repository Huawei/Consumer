/*
 * Copyright (c) HuaWei Technologies Co., Ltd.
 * 2018-2019.
 * All rights reserved.
 */

package com.huawei.hiaicodedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.ImageResult;
import com.huawei.hiai.vision.visionkit.image.detector.DocCoordinates;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.MyApplication;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.widget.TitleBar;
import com.huawei.hiaicodedemo.widget.cropview.CropImageView;
import org.json.JSONObject;

public class DocSkewCorrectionActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = DocSkewCorrectionActivity.class.getSimpleName();
    private String mData;
    private ImageView mLabel;
    private Bitmap bitmap;
    private CropImageView cropImageView;
    private TitleBar titleBar;
    private Button docDetect;
    private Button docRefine;

    @Override
    protected void init() {
        initData();
        initView();
    }

    private void initData(){
        mData = getIntent().getStringExtra("data");
        if(!TextUtils.isEmpty(mData)){
            getOriginalImage();
        }else {
            toast(getString(R.string.toast_2));
        }
    }

    private void getOriginalImage(){
        try{
            Uri uri = Uri.parse(mData);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        titleBar = findViewById(R.id.titleBar);
        mLabel = findViewById(R.id.image_result);
        mLabel.setImageBitmap(bitmap);
        cropImageView = findViewById(R.id.image_crop);
        cropImageView.setCropMode(CropImageView.FREE_SHAPE);
        docDetect = findViewById(R.id.btn_doc_detect);
        docDetect.setOnClickListener(this);
        docRefine = findViewById(R.id.btn_doc_refine);
        docRefine.setOnClickListener(this);

    }

    @Override
    protected int layout() {
        return R.layout.activity_note_image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_doc_detect:
                setCut();
                break;
            case R.id.btn_doc_refine:
                nextActivity();
                break;
            default:
        }
    }

    private void setCut() {
        if (cropImageView.getVisibility() == View.GONE) {
            titleBar.setTitle(getString(R.string.ni_title_2));
            docDetect.setVisibility(View.GONE);
            docRefine.setVisibility(View.VISIBLE);
            cropImageView.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.GONE);
            cropImageView.setDocCoordinates(handleDocDetect(bitmap));
            cropImageView.setImageBitmap(bitmap);
        }

    }

    private void nextActivity() {
        cropImageView.getCroppedImage(new CropImageView.CroppedImageCall() {
            @Override
            public void doCroppedImage(final Bitmap croppedImage,DocCoordinates docCoordinates) {
                correctedBitmap = handleDocRefine(croppedImage,docCoordinates);
                Intent intent = new Intent(DocSkewCorrectionActivity.this,TextRecognitionActivity.class);
                startActivity(intent);

            }
        });
    }

    private DocCoordinates handleDocDetect(Bitmap bitmap){
        if (null == bitmap) {
            Log.e(TAG, "handleDocDetect, bitmap is null! ");
            return null;
        }
        Frame frame = new Frame();
        frame.setBitmap(bitmap);

        JSONObject jsonDoc = MyApplication.mDocResolution.docDetect(frame, null);
        return MyApplication.mDocResolution.convertResult(jsonDoc);
    }

    private Bitmap handleDocRefine(Bitmap bitmap,DocCoordinates docCoordinates) {
        if (null == docCoordinates) {
            Log.e(TAG, "handleDocRefine, docCoordinates is null! ");
            return null;
        }
        Frame frame = new Frame();
        frame.setBitmap(bitmap);

        ImageResult sr = MyApplication.mDocResolution.docRefine(frame, docCoordinates, null);
        Bitmap mNewBitmap = sr.getBitmap();
        return   (null != mNewBitmap) ? mNewBitmap : bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
