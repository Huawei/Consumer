package com.huawei.hihealthkit.codelabdemo.widget;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.huawei.hihealthkit.codelabdemo.R;

import java.util.Map;

public class MyMarkerView extends MarkerView {

	private TextView textView;
	private Map<Integer, String> datas;

	public void setDatas(Map<Integer, String> map){
		this.datas = map;
	}

	public MyMarkerView(Context context) {
		super(context, R.layout.layout_markerview);//这个布局自己定义
		textView = (TextView) findViewById(R.id.marker_text);
	}

	//显示的内容
	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		String text = (int)e.getY() + "";
		textView.setText(text);
		super.refreshContent(e, highlight);
	}

	//标记相对于折线图的偏移量
	@Override
	public MPPointF getOffset() {
		return new MPPointF(-(getWidth() / 2), -getHeight());
	}

}
