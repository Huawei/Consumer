package com.huawei.hiaicodedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.image.docrefine.DocRefine;
import com.huawei.hiai.vision.text.TextDetector;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.ImageResult;
import com.huawei.hiai.vision.visionkit.image.detector.DocCoordinates;
import com.huawei.hiai.vision.visionkit.text.Text;
import com.huawei.hiai.vision.visionkit.text.TextBlock;
import com.huawei.hiai.vision.visionkit.text.TextDetectType;
import com.huawei.hiai.vision.visionkit.text.TextLine;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;
import com.huawei.hiaicodedemo.widget.TitleBar;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;

public class TextDetectActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = TextDetectActivity.class.getSimpleName();
    private final static int TEXTDETECTOR_RESULT = 120;
    private final static int DOCRESOLUTION_RESULT = 121;
    private android.widget.ImageView textOrigin;
    private android.widget.EditText textEdit;
    private TitleBar titleBar;

    private Bitmap bitmap;
    private DocRefine mDocResolution;
    private Text text;

    @Override
    protected void init() {
        initView();
        initHiAI();

    }

    /**
     * init HiAI interface
     */
    private void initHiAI() {



    }

    /**
     * Capability Interfaces
     *
     * @return
     */
    private void setHiAi() {



        this.text = text;
        handler.sendEmptyMessage(TEXTDETECTOR_RESULT);
    }

    /**
     * Release
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected int layout() {
        return R.layout.activity_text_detect;
    }

    private void initView() {
        textOrigin = findViewById(R.id.text_origin);
        textEdit = findViewById(R.id.text_edit);
        titleBar = findViewById(R.id.titleBar);
        findViewById(R.id.btn_album).setOnClickListener(this);
        findViewById(R.id.btn_material).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_material:
                String dirPath = "material/general_text_recognition/";
                if (isCN()) {
                    dirPath += "cn";
                } else {
                    dirPath += "en";
                }
                selectMaterial(dirPath);
                break;
            case R.id.btn_album:
                selectImage();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO && data != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (Exception e) {
                    toast(getString(R.string.gtr_toast_1));
                    e.printStackTrace();
                }
            }  else if (requestCode == REQUEST_SELECT_MATERIAL_CODE) {
                bitmap = AssetsFileUtil.getBitmapByFilePath(this, data.getStringExtra(KEY_FILE_PATH));
            }
            textOrigin.setImageBitmap(bitmap);
            new Thread() {
                @Override
                public void run() {
                    bitmap = handleDocCorrection(bitmap);
                    setHiAi();
                }
            }.start();
        } else {
            toast(getString(R.string.gtr_toast_2));
        }
    }

    private Bitmap handleDocCorrection(Bitmap bitmap) {
        if (null == bitmap) {
            Log.e(TAG, "handleOCR, bitmap is null! ");
            return null;
        }

        mDocResolution = new DocRefine(this);
        Frame frame = new Frame();
        frame.setBitmap(bitmap);

        JSONObject jsonDoc = mDocResolution.docDetect(frame, null);
        DocCoordinates sc = mDocResolution.convertResult(jsonDoc);
        ImageResult sr = mDocResolution.docRefine(frame, sc, null);
        Bitmap mNewBitmap = sr.getBitmap();
        Bitmap saveBitmap = (null != mNewBitmap) ? mNewBitmap : bitmap;
        Message message = new Message();
        message.what = DOCRESOLUTION_RESULT;
        message.obj = saveBitmap;
        handler.sendMessage(message);
        return saveBitmap;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TEXTDETECTOR_RESULT:
                    if (text != null) {
                        if (text.getBlocks() != null) {
                            StringBuffer buffer = new StringBuffer();
                            List<TextBlock> textBlocks = text.getBlocks();
                            for (int i = 0; i < textBlocks.size(); i++) {
                                TextBlock textBlock = textBlocks.get(i);
                                List<TextLine> textLines = textBlock.getTextLines();
                                Log.e(TAG, "textBlock:" + textBlock.getValue());
                                for (int j = 0; j < textLines.size(); j++) {
                                    String value = textLines.get(j).getValue();
                                    buffer.append(value + "\n");
                                    Log.e(TAG, "text:" + value + ";" + textLines.size());
                                }
                            }
                            textEdit.setText(buffer.toString());
                        } else {
                            toast(getString(R.string.gtr_toast_3));
                            textEdit.setText("");
                        }
                    } else {
                        toast(getString(R.string.gtr_toast_3));
                        textEdit.setText("");
                    }
                    break;
                case DOCRESOLUTION_RESULT:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    //textOrigin.setImageBitmap(bitmap);
                    break;

            }
        }
    };


}
