package com.huawei.hiaicodedemo;

import android.app.Application;

import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.image.docrefine.DocRefine;
import com.huawei.hiai.vision.text.TextDetector;

public class MyApplication extends Application {

	public static boolean isSupport = false;
	public static DocRefine mDocResolution;
	public static TextDetector mTextDetector;

	@Override
	public void onCreate() {
		super.onCreate();
		initHiAIEngine();
	}

	/**
	 * init HiAI interface
	 */
	private void initHiAIEngine() {

		VisionBase.init(this, new ConnectionCallback() {
			@Override
			public void onServiceConnect() {
				initVisionService();
			}

			@Override
			public void onServiceDisconnect() {
				isSupport = false;
			}
		});
	}

	private void initVisionService(){
		mDocResolution = new DocRefine(getApplicationContext());
		mTextDetector  = new TextDetector(getApplicationContext());
		isSupport      = mDocResolution.prepare() == 0;
	}

	/**
	 * Release
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		if(mDocResolution != null){
			mDocResolution.release();
		}
		if(mTextDetector != null){
			mTextDetector.release();
		}
	}

}
