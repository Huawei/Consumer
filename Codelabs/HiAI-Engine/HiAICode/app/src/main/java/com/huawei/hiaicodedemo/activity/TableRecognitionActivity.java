package com.huawei.hiaicodedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.image.detector.LabelDetector;
import com.huawei.hiai.vision.text.TableDetector;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.detector.Label;
import com.huawei.hiai.vision.visionkit.text.table.Table;
import com.huawei.hiai.vision.visionkit.text.table.TableCell;
import com.huawei.hiai.vision.visionkit.text.table.TableContent;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;
import org.json.JSONObject;

import java.util.List;

public class TableRecognitionActivity extends BaseActivity implements View.OnClickListener {

    private final static int TABLEDETECTOR_RESULT = 130;
    private ImageView textOrigin;
    private EditText textEdit;
    private Bitmap bitmap;
    private Table table;

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



        this.table = table;
        handler.sendEmptyMessage(TABLEDETECTOR_RESULT);

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
        return R.layout.activity_table_recognition;
    }

    private void initView() {
        textOrigin = findViewById(R.id.text_origin);
        textEdit = findViewById(R.id.text_edit);
        findViewById(R.id.btn_album).setOnClickListener(this);
        findViewById(R.id.btn_material).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_material:
                String dirPath = "material/table_recognition/";
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
            } else if (requestCode == REQUEST_SELECT_MATERIAL_CODE) {
                bitmap = AssetsFileUtil.getBitmapByFilePath(this, data.getStringExtra(KEY_FILE_PATH));
            }
            textOrigin.setImageBitmap(bitmap);
            new Thread() {
                @Override
                public void run() {
                    setHiAi();
                }
            }.start();
        } else {
            toast(getString(R.string.gtr_toast_2));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TABLEDETECTOR_RESULT:
                    if (table != null) {
                        List<TableContent> list = table.getTableContent();
                        if (list != null) {
                            StringBuilder sbTableCell = new StringBuilder();
                            List<TableCell> tableCells = list.get(0).getBody();
                            for (TableCell c : tableCells) {
                                List<String> words = c.getWord();
                                StringBuilder sb = new StringBuilder();
                                for (String s : words) {
                                    sb.append(s).append(",");
                                }
                                String cell = c.getStartRow() + ":" + c.getEndRow() + ": " + c.getStartColumn() + ":" +
                                        c.getEndColumn() + "; " + sb.toString();
                                sbTableCell.append(cell).append("\n");
                            }
                            textEdit.setText(sbTableCell.toString());
                        } else {
                            toast(getString(R.string.gtr_toast_3));
                            textEdit.setText("");
                        }
                    } else {
                        toast(getString(R.string.gtr_toast_3));
                        textEdit.setText("");
                    }
                    break;
            }
        }
    };

}