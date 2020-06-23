/*
 * Copyright (c) HuaWei Technologies Co., Ltd.
 * 2018-2019.
 * All rights reserved.
 */

package com.huawei.hiaicodedemo.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ImageView;

import com.huawei.hiai.vision.common.VisionImage;
import com.huawei.hiai.vision.visionkit.text.Text;
import com.huawei.hiai.vision.visionkit.text.TextBlock;
import com.huawei.hiai.vision.visionkit.text.TextDetectType;
import com.huawei.hiai.vision.visionkit.text.TextLine;
import com.huawei.hiai.vision.visionkit.text.config.TextConfiguration;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.MyApplication;
import com.huawei.hiaicodedemo.R;

import java.util.List;

public class TextRecognitionActivity extends BaseActivity {

    private final static int TEXT_DETECTOR_RESULT = 120;
    private Bitmap bitmap;
    private EditText result;

    @Override
    protected void init() {
        initData();
        initView();

    }

    private void initData(){
        bitmap = correctedBitmap;
    }

    private void initView(){
        result = findViewById(R.id.ocr_result);
        ImageView notesView = findViewById(R.id.image_src);
        notesView.setAdjustViewBounds(true);
        notesView.setImageBitmap(bitmap);
		new Thread(new Runnable() {
			@Override
			public void run() {
                handleTextRecognition();
			}
		}).start();
    }

    @Override
    protected int layout() {
        return R.layout.notes_ocr_result;
    }

    /**
     * Capability Interfaces
     *
     */
    private void handleTextRecognition() {

        VisionImage visionImage = VisionImage.fromBitmap(bitmap);
        TextConfiguration config = new TextConfiguration();
        config.setEngineType(TextDetectType.TYPE_TEXT_DETECT_FOCUS_SHOOT);
        MyApplication.mTextDetector.setTextConfiguration(config);
        Text text =  new Text();
        MyApplication.mTextDetector.detect(visionImage,text, null);
        Message message = new Message();
        message.what = TEXT_DETECTOR_RESULT;
        message.obj = text;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TEXT_DETECTOR_RESULT:
                    Text text = (Text) msg.obj;
                    if (text != null) {
                        if (text.getBlocks() != null) {
                            StringBuilder builder = new StringBuilder();
                            List<TextBlock> textBlocks = text.getBlocks();
                            for (TextBlock block : textBlocks) {
                                List<TextLine> textLines = block.getTextLines();
                                for (TextLine textLine : textLines) {
                                    builder.append(textLine.getValue()).append("\n");
                                }
                            }
                            result.setText(builder.toString());
                        } else {
                            toast(getString(R.string.toast_3));
                            result.setText("");
                        }
                    } else {
                        toast(getString(R.string.toast_3));
                        result.setText("");
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}



