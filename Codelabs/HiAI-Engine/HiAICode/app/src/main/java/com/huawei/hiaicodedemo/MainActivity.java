package com.huawei.hiaicodedemo;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.image.detector.AestheticsScoreDetector;
import com.huawei.hiai.vision.image.segmentation.ImageSegmentation;
import com.huawei.hiai.vision.image.segmentation.SegConfiguration;
import com.huawei.hiai.vision.image.sr.ImageSuperResolution;
import com.huawei.hiai.vision.text.TableDetector;
import com.huawei.hiai.vision.text.TextDetector;
import com.huawei.hiaicodedemo.activity.AestheticScoreActivity;
import com.huawei.hiaicodedemo.activity.ImageSuperActivity;
import com.huawei.hiaicodedemo.activity.TableRecognitionActivity;
import com.huawei.hiaicodedemo.activity.TextDetectActivity;
import com.huawei.hiaicodedemo.activity.VideoSegmentActivity;
import com.huawei.hiaicodedemo.widget.BackgroundDrawable;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private boolean isSuccess = false;
    private int IMAGE_SUPER;
    private int TEXT_DETECT;
    private int AESTHETICS_SCORE;
    private int TABLE_DETECT;
    private int VIDEO_SEGMENT;
    private final String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private ConstraintLayout view;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {
        requestPermissions(permission, REQUEST_CODE);
        initView();
        initHiAI();
    }

    private void initHiAI() {
        VisionBase.init(mContext, new ConnectionCallback() {
            @Override
            public void onServiceConnect() {
                isSuccess = true;
                isSupport();
            }

            @Override
            public void onServiceDisconnect() {
                isSuccess = false;
            }
        });
    }

    private void isSupport() {
        imageSuper();
        textDetect();
        aestheticScore();
        tableRecognition();
        videoSegment();
    }

    private void aestheticScore() {
        AestheticsScoreDetector aestheticsScoreDetector = new AestheticsScoreDetector(mContext);
        int availability = aestheticsScoreDetector.getAvailability();
        AESTHETICS_SCORE = availability;
        result(availability, aestheticsScoreDetector);
    }

    private void tableRecognition() {
        TableDetector tableDetector = new TableDetector(mContext);
        int availability = tableDetector.getAvailability();
        TABLE_DETECT = availability;
        result(availability, tableDetector);
    }

    private void textDetect() {
        TextDetector textDetector = new TextDetector(mContext);
        int availability = textDetector.getAvailability();
        TEXT_DETECT = availability;
        result(availability, textDetector);
    }

    private void videoSegment() {
        ImageSegmentation imageSegmentation = new ImageSegmentation(mContext);
        SegConfiguration build = new SegConfiguration.Builder().setProcessMode(SegConfiguration.MODE_IN)
                .setSegmentationType(SegConfiguration.TYPE_PORTRAIT_SEGMENTATION_VIDEO)
                .setOutputType(SegConfiguration.OUTPUT_TYPE_BYTEARRAY)
                .build();
        imageSegmentation.setConfiguration(build);
        int availability = imageSegmentation.getAvailability();
        VIDEO_SEGMENT = availability;
        result(availability, imageSegmentation);
    }

    private void result(int availability,final VisionBase base) {
        if (availability == 0) {//support

        }else if (availability == -6) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    VideoSegmentActivity.loadPlugin(base);
                }
            }).start();
        }  else {

        }
        releaseEngine(base);
    }

    private void imageSuper() {
        ImageSuperResolution superResolution = new ImageSuperResolution(mContext);
        int availability = superResolution.getAvailability();
        IMAGE_SUPER = availability;
        result(availability, superResolution);

    }


    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    private void initView() {
        view = findViewById(R.id.main_view);
        BackgroundDrawable drawable = BackgroundDrawable.builder()
                .left(25)
                .right(75)
                .topColor(Color.parseColor("#c8b5dd"))
                .bottomColor(Color.parseColor("#c5d5f5"))
                .build();
        view.setBackground(drawable);

        findViewById(R.id.image_super).setOnClickListener(this);
        findViewById(R.id.text_detector).setOnClickListener(this);
        findViewById(R.id.aesthetic_score).setOnClickListener(this);
        findViewById(R.id.table_recognition).setOnClickListener(this);
        findViewById(R.id.video_seg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isSuccess) {
            switch (v.getId()) {
                case R.id.image_super:
                    isGoTo(IMAGE_SUPER, ImageSuperActivity.class);
                    break;
                case R.id.text_detector:
                    isGoTo(TEXT_DETECT, TextDetectActivity.class);
                    break;
                case R.id.video_seg:
                    isGoTo(VIDEO_SEGMENT, VideoSegmentActivity.class);
                    break;
                case R.id.aesthetic_score:
                    isGoTo(AESTHETICS_SCORE, AestheticScoreActivity.class);
                    break;
                case R.id.table_recognition:
                    isGoTo(TABLE_DETECT, TableRecognitionActivity.class);
                    break;
                default:
            }
        } else {
            toast("HiAI Engine initialization or failure to initialize!");
        }
    }

    private void isGoTo(int code, Class clazz) {
        if (code == 0) {
            goTo(false, clazz);
        } else {
            toast("errorCode:" + code);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
