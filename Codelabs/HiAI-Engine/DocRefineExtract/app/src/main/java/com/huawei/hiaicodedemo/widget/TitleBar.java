package com.huawei.hiaicodedemo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hiaicodedemo.R;

import androidx.annotation.Nullable;

public class TitleBar extends LinearLayout {

	private Context context;
	private ImageView goBack;
	private TextView tvTitle;
	private ImageView ivConfirm;

	private String titleName;
	private int titleTextColor;
	private float titleTextSize;
	private boolean showConfirm;
	private View.OnClickListener listener;

	public TitleBar(Context context) {
		this(context, null);
	}

	public TitleBar(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
		initView(array);
		array.recycle();
	}

	private void initView(TypedArray array){
		LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this);
		goBack = (ImageView) findViewById(R.id.go_back);
		tvTitle = (TextView) findViewById(R.id.title);
		goBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				((Activity)getContext()).finish();
			}
		});
		ivConfirm = (ImageView)findViewById(R.id.iv_confirm);
		ivConfirm.setOnClickListener(listener);

		titleName = array.getString(R.styleable.TitleBar_titleName);
		titleTextColor = array.getColor(R.styleable.TitleBar_titleTextColor, Color.TRANSPARENT);
		titleTextSize = array.getDimension(R.styleable.TitleBar_titleTextSize, dip2px(getContext(), 15));
		showConfirm = array.getBoolean(R.styleable.TitleBar_showConfirm,false);

		ivConfirm.setVisibility(showConfirm ? View.VISIBLE : View.GONE);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
		setTitle(titleName);
		setTitleTextColor(titleTextColor);
	}

	public void isShowConfirm(boolean showConfirm){
		this.showConfirm = showConfirm;
		ivConfirm.setVisibility(showConfirm ? View.VISIBLE : View.GONE);
	}

	public void setRightClickListener(View.OnClickListener listener){
		if(listener != null){
			this.listener = listener;
			ivConfirm.setOnClickListener(listener);
		}
	}

	public void setTitleTextSize(float size) {
		tvTitle.setTextSize(size);
	}

	public void setTitle(int titleId) {
		tvTitle.setText(titleId);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setTitleTextColor(int color) {
		tvTitle.setTextColor(color);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
