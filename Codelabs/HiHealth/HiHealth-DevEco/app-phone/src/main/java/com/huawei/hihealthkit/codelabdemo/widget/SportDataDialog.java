package com.huawei.hihealthkit.codelabdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.huawei.hihealthkit.codelabdemo.R;

import java.util.Map;

public class SportDataDialog extends Dialog{

	private Context context;
	private TextView distanceTV;
	private TextView durationTV;
	private TextView speedTV;
	private TextView heartRateTV;
	private ImageView stopIV;
	private ImageView startIV;
	private boolean isStoped = false;


	public SportDataDialog(Context context) {
		super(context, R.style.CustomDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sport_data_dialog);
		//按空白处不能取消动画
		setCanceledOnTouchOutside(false);
		//初始化界面控件
		initView();
		//初始化界面控件的事件
		initEvent();
	}

	/**
	 * 初始化界面的确定和取消监听器
	 */
	private void initEvent() {
		stopIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ( onClickBottomListener!= null) {
					onClickBottomListener.onStopClick();
				}
				isStoped = true;
				showImage();
			}
		});

		startIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ( onClickBottomListener!= null) {
					onClickBottomListener.onStartClick();
				}
				isStoped = false;
				showImage();
			}
		});
	}

	/**
	 * 初始化界面控件的显示数据
	 */
	public void refreshView(Map<String,String> dataMap) {
		distanceTV.setText(dataMap.get("distance"));
		durationTV.setText(dataMap.get("duration"));
		speedTV.setText(dataMap.get("speed"));
		heartRateTV.setText(dataMap.get("heartRate"));

	}

	@Override
	public void show() {
		super.show();
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.gravity= Gravity.BOTTOM;
		layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(layoutParams);
		showImage();
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		distanceTV = (TextView) findViewById(R.id.sport_data_distance);
		durationTV = (TextView) findViewById(R.id.sport_data_duration);
		speedTV = (TextView) findViewById(R.id.sport_data_speed);
		heartRateTV = (TextView) findViewById(R.id.sport_data_heartRate);
		stopIV = (ImageView) findViewById(R.id.stop_image);
		startIV = (ImageView) findViewById(R.id.start_image);
		showImage();
	}

	private void showImage(){
		if(isStoped){
			startIV.setVisibility(View.VISIBLE);
			stopIV.setVisibility(View.GONE);
		}else {
			startIV.setVisibility(View.GONE);
			stopIV.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置确定取消按钮的回调
	 */
	public OnClickBottomListener onClickBottomListener;
	public SportDataDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
		this.onClickBottomListener = onClickBottomListener;
		return this;
	}
	public interface OnClickBottomListener{
		/**
		 * 点击开始事件
		 */
		void onStartClick();
		void onStopClick();
	}

	@Override
	public void dismiss(){
		super.dismiss();
		isStoped = false;
	}

}
