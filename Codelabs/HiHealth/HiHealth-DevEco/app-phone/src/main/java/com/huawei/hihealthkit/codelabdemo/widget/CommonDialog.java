package com.huawei.hihealthkit.codelabdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.huawei.hihealthkit.codelabdemo.R;

public class CommonDialog extends Dialog {

	/**
	 * 显示的标题
	 */
	private TextView titleTv ;

	/**
	 * 显示的消息
	 */
	private TextView messageTv ;

	/**
	 * 备注的消息
	 */
	private TextView remarkTv ;

	/**
	 * 确认和取消按钮
	 */
	private Button negtiveBn ,positiveBn;

	/**
	 * 按钮之间的分割线
	 */
	private View columnLineView ;
	public CommonDialog(Context context) {
		super(context, R.style.CustomDialog);
	}

	/**
	 * 都是内容数据
	 */
	private String title;
	private String message;
	private String remark;
	private String positive,negtive ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_common_dialog);
		//按空白处不能取消动画
		setCanceledOnTouchOutside(true);
		//初始化界面控件
		initView();
		//初始化界面数据
		refreshView();
		//初始化界面控件的事件
		initEvent();
	}

	/**
	 * 初始化界面的确定和取消监听器
	 */
	private void initEvent() {
		//设置确定按钮被点击后，向外界提供监听
		positiveBn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( onClickBottomListener!= null) {
					onClickBottomListener.onPositiveClick();
				}
			}
		});
		//设置取消按钮被点击后，向外界提供监听
		negtiveBn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( onClickBottomListener!= null) {
					onClickBottomListener.onNegtiveClick();
				}
			}
		});
	}

	/**
	 * 初始化界面控件的显示数据
	 */
	private void refreshView() {
		//如果用户自定了title和message
		if (!TextUtils.isEmpty(title)) {
			titleTv.setText(title);
			titleTv.setVisibility(View.VISIBLE);
		}else {
			titleTv.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(message)) {
			messageTv.setText(message);
		}
		if (!TextUtils.isEmpty(remark)) {
			remarkTv.setText(remark);
		}
		//如果设置按钮的文字
		if (!TextUtils.isEmpty(positive)) {
			positiveBn.setText(positive);
		}else {
			positiveBn.setText("确定");
		}
		if (!TextUtils.isEmpty(negtive)) {
			negtiveBn.setText(negtive);
		}else {
			negtiveBn.setText("取消");
		}

	}

	@Override
	public void show() {
		super.show();
		refreshView();
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		negtiveBn = (Button) findViewById(R.id.negtive);
		positiveBn = (Button) findViewById(R.id.positive);
		titleTv = (TextView) findViewById(R.id.title);
		messageTv = (TextView) findViewById(R.id.message);
		remarkTv = (TextView) findViewById(R.id.remark);
		columnLineView = findViewById(R.id.column_line);
	}

	/**
	 * 设置确定取消按钮的回调
	 */
	public OnClickBottomListener onClickBottomListener;
	public CommonDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
		this.onClickBottomListener = onClickBottomListener;
		return this;
	}
	public interface OnClickBottomListener{
		/**
		 * 点击确定按钮事件
		 */
		public void onPositiveClick();
		/**
		 * 点击取消按钮事件
		 */
		public void onNegtiveClick();
	}

	public String getMessage() {
		return message;
	}

	public CommonDialog setMessage(String message) {
		this.message = message;
		return this ;
	}

	public String getRemark() {
		return message;
	}

	public CommonDialog setRemark(String remark) {
		this.remark = remark;
		return this ;
	}

	public String getTitle() {
		return title;
	}

	public CommonDialog setTitle(String title) {
		this.title = title;
		return this ;
	}

	public String getPositive() {
		return positive;
	}

	public CommonDialog setPositive(String positive) {
		this.positive = positive;
		return this ;
	}

	public String getNegtive() {
		return negtive;
	}

	public CommonDialog setNegtive(String negtive) {
		this.negtive = negtive;
		return this ;
	}


}
