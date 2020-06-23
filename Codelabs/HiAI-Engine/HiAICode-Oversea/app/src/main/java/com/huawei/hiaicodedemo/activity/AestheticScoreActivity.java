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

import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.image.detector.AestheticsScoreDetector;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.detector.AestheticsScore;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;

import org.json.JSONObject;

public class AestheticScoreActivity extends BaseActivity implements View.OnClickListener {

	private final static String TAG = AestheticScoreActivity.class.getSimpleName();
	private final static int AESTHETICSCORE_RESULT = 130;
	private ImageView textOrigin;
	private EditText textEdit;
	private Bitmap bitmap;
	private float score;

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



		this.score = score;
		handler.sendEmptyMessage(AESTHETICSCORE_RESULT);
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
		return R.layout.activity_aesthetic_score;
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
				String dirPath = "material/aesthetic_score";
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
				case AESTHETICSCORE_RESULT:
					textEdit.setText(String.valueOf(score));
					break;
			}
		}
	};

}