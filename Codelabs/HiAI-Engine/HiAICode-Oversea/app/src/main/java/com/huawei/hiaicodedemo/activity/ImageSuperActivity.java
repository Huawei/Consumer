package com.huawei.hiaicodedemo.activity;

import androidx.annotation.Nullable;

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
import com.huawei.hiai.vision.image.sr.ImageSuperResolution;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.ImageResult;
import com.huawei.hiai.vision.visionkit.image.sr.SuperResolutionConfiguration;
import com.huawei.hiaicodedemo.BaseActivity;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;
import com.huawei.hiaicodedemo.widget.TitleBar;

public class ImageSuperActivity extends BaseActivity implements View.OnClickListener {

	private final static String TAG = ImageSuperActivity.class.getSimpleName();
	private final static int SUPERRESOLUTION_RESULT = 110;
	private android.widget.ImageView superOrigin;
	private android.widget.ImageView superImage;
	private Bitmap bitmap;
	private TitleBar titleBar;
	private Bitmap bmp;

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



		this.bmp = bmp;
		handler.sendEmptyMessage(SUPERRESOLUTION_RESULT);
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
		return R.layout.activity_image_super;
	}

	private void initView() {
		superOrigin = findViewById(R.id.super_origin);
		superImage = findViewById(R.id.super_image);
		titleBar = (TitleBar) findViewById(R.id.titleBar);
		findViewById(R.id.btn_album).setOnClickListener(this);
		findViewById(R.id.btn_material).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_material:
				selectMaterial("material/image_super_resolution");
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
			if (data != null && requestCode == REQUEST_PHOTO) {
				try {
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

				} catch (Exception e) {
					e.printStackTrace();
					toast("Exception:" + e.getMessage());

				}
			} else if (requestCode == REQUEST_SELECT_MATERIAL_CODE) {
				bitmap = AssetsFileUtil.getBitmapByFilePath(this, data.getStringExtra(KEY_FILE_PATH));
			}
			if (bitmap != null) {
				setBitmap();
			} else {
				toast(getString(R.string.isr_toast_1));
			}
		}

	}

	private void setBitmap() {
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		Log.e(TAG, "width:" + width + ";height:" + height);
		if (width <= 800 && height <= 600) {
			superOrigin.setImageBitmap(bitmap);
			new Thread() {
				@Override
				public void run() {
					setHiAi();
				}
			}.start();
		} else {
			toast(getString(R.string.isr_toast_2));
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SUPERRESOLUTION_RESULT:
					superImage.setImageBitmap(bmp);
					break;
			}
		}
	};

}
