package com.huawei.hihealthkit.codelabdemo;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.huawei.hihealth.listener.ResultCallback;
import com.huawei.hihealthkit.HiHealthDataQuery;
import com.huawei.hihealthkit.HiHealthDataQueryOption;
import com.huawei.hihealthkit.codelabdemo.utils.DateUtil;
import com.huawei.hihealthkit.codelabdemo.widget.MyMarkerView;
import com.huawei.hihealthkit.data.HiHealthPointData;
import com.huawei.hihealthkit.data.HiHealthSetData;
import com.huawei.hihealthkit.data.store.HiHealthDataStore;
import com.huawei.hihealthkit.data.type.HiHealthPointType;
import com.huawei.hihealthkit.data.type.HiHealthSetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends BaseActivity {

	private static final String TAG = HistoryActivity.class.getSimpleName();
	private static final int CALORIES_META = 10;
	private static final int STEP_META = 11;
	private static final int HEART_META = 12;
	private List<String> dateList;
	private ArrayList<BarEntry> stepValues = new ArrayList<BarEntry>();
	private ArrayList<BarEntry> caloriesValues = new ArrayList<BarEntry>();
	private ArrayList<BarEntry> heartRateValues = new ArrayList<BarEntry>();
	private BarChart stepBarChart;
	private BarChart heartRateBarChart;
	private BarChart calorieBarChart;
	private TextView stepTextView;
	private TextView calorieTextView;
	private TextView heartRateTextView;

	@Override
	protected void init() {
		initView();
		initData();
	}

	@Override
	protected int layout() {
		return R.layout.activity_history;
	}

	private void execQuery() {



	}

	private void initView() {
		stepBarChart = findViewById(R.id.step_barChart);
		initStepBarChart(stepBarChart);

		heartRateBarChart = findViewById(R.id.heartRate_barChart);
		initStepBarChart(heartRateBarChart);

		calorieBarChart = findViewById(R.id.calorie_barChart);
		initStepBarChart(calorieBarChart);

		stepTextView = findViewById(R.id.step_text);
		calorieTextView = findViewById(R.id.calorie_text);
		heartRateTextView = findViewById(R.id.heartRate_text);
	}

	private void initData() {
		dateList = DateUtil.getListDate();
		execQuery();
	}

	private void execQueryCalories() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = endTime - 86400000L * 30;
		HiHealthDataQuery hiHealthDataQuery = new HiHealthDataQuery(HiHealthPointType
				.DATA_POINT_CALORIES_SUM,
				startTime, endTime, new HiHealthDataQueryOption());
		Log.i("", "sampletype = " + hiHealthDataQuery.getSampleType());
		HiHealthDataStore.execQuery(this, hiHealthDataQuery, timeout, new ResultCallback() {
			@Override
			public void onResult(int i, Object data) {
				Log.i("", "enter query onSuccess");
				if (data != null) {
					Log.i("", "enter query not null");
					List dataList = (ArrayList) data;
				}
				sendMessage(CALORIES_META, i, data);
			}
		});

	}

	private void execQueryHeartRate() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = endTime - 86400000L * 30;
		HiHealthDataQuery hiHealthDataQuery = new HiHealthDataQuery(HiHealthSetType.DATA_SET_HEART,
				startTime, endTime, new HiHealthDataQueryOption());
		Log.i("", "sampletype = " + hiHealthDataQuery.getSampleType());
		HiHealthDataStore.execQuery(this, hiHealthDataQuery, timeout, new ResultCallback() {
			@Override
			public void onResult(int i, Object data) {
				Log.i("", "enter query onSuccess");
				if (data != null) {
					Log.i("", "enter query not null");
					List dataList = (ArrayList) data;
				}
				sendMessage(HEART_META, i, data);
			}
		});

	}

	public void sendMessage(int what, int resultCode, Object data) {
		Message message = new Message();
		message.what = what;
		message.arg1 = resultCode;
		message.obj = data;
		handler.sendMessage(message);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case STEP_META:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null) {
							stepValues.clear();
							float sum = 0f;
							for (int i = 0; i < dateList.size(); i++) {
								float value = 0f;
								for (int j = 0; j < dataList.size(); j++) {
									HiHealthPointData hiHealthPointData = (HiHealthPointData) dataList.get(j);
									String date = DateUtil.getDateFormat(hiHealthPointData.getStartTime());
									if (dateList.get(i).equals(date)) {
										value = (float) hiHealthPointData.getValue();
										sum += value;
									}
								}
								stepValues.add(new BarEntry((i + 1), value));
							}
							setBarChart(stepBarChart, stepValues);
							int max = (int) getMax(stepValues);
							int avg = (int) (sum / stepValues.size());
							stepTextView.setText("最大步数" + max + "步，日均步数" + avg + "步");
						}
					}
					execQueryCalories();
					break;
				case CALORIES_META:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null) {
							caloriesValues.clear();
							int sum = 0;
							for (int i = 0; i < dateList.size(); i++) {
								int calories = 0;
								for (int j = 0; j < dataList.size(); j++) {
									HiHealthPointData hiHealthPointData = (HiHealthPointData) dataList.get(j);
									String date = DateUtil.getDateFormat(hiHealthPointData.getStartTime());
									if (dateList.get(i).equals(date)) {
										calories = Math.round(hiHealthPointData.getValue() / 1000f);
										sum += calories;
									}
								}
								caloriesValues.add(new BarEntry((i + 1), calories));
							}
							setBarChart(calorieBarChart, caloriesValues);
							calorieTextView.setText("总热量" + sum + "千卡");
						}
					}
					execQueryHeartRate();
					break;
				case HEART_META:
					if (msg.obj != null) {
						List dataList = (ArrayList) msg.obj;
						if (dataList != null) {
							heartRateValues.clear();
							for (int i = 0; i < dateList.size(); i++) {
								int heartRate = 0;
								for (int j = 0; j < dataList.size(); j++) {
									HiHealthSetData hiHealthSetData = (HiHealthSetData) dataList.get(j);
									String date = DateUtil.getDateFormat(hiHealthSetData.getStartTime());
									Map map = hiHealthSetData.getMap();
									if (dateList.get(i).equals(date)) {
										heartRate = Math.round((float) map.get(46018));
									}
								}
								heartRateValues.add(new BarEntry((i + 1), heartRate));
							}
							setBarChart(heartRateBarChart, heartRateValues);
							int max = (int) getMax(heartRateValues);
							int min = (int) getMin(heartRateValues);
							heartRateTextView.setText("心率范围" + min + "~" + max + "次/分钟");
						}
					}
					break;
			}
		}
	};

	/**
	 * 初始化折线图控件属性
	 */
	private void initStepBarChart(BarChart barChart) {
		barChart.getDescription().setEnabled(false);
		barChart.setBackgroundColor(Color.WHITE);
		barChart.getLegend().setEnabled(false);

		XAxis xAxis = barChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setGranularity(1f);
		xAxis.setLabelCount(7, false);
		xAxis.setValueFormatter(new IAxisValueFormatter() {
			@Override
			public String getFormattedValue(float value, AxisBase axis) {
				int time = (int) value;
				if (time == 0 || dateList.get(time - 1) == null) {
					return " ";
				}
				return dateList.get(time - 1);
			}
		});
		xAxis.setAxisMinimum(0f);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(false);

		YAxis leftAxis = barChart.getAxisLeft();
		leftAxis.setLabelCount(8, false);
		leftAxis.setValueFormatter(new IAxisValueFormatter() {
			@Override
			public String getFormattedValue(float value, AxisBase axis) {
				return ((int) value) + "";
			}
		});
		leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setSpaceTop(15f);
		leftAxis.setAxisMinimum(0f);

		barChart.getAxisRight().setEnabled(false);
	}

	private void setBarChart(BarChart barChart, List<BarEntry> valsComp) {

		//这里，每重新new一个LineDataSet，相当于重新画一组折线
		BarDataSet setComp1 = new BarDataSet(valsComp, "");
		setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
		setComp1.setColor(ColorTemplate.rgb("#F6B707"));
		setComp1.setDrawValues(false);

		List<IBarDataSet> dataSets = new ArrayList<>();
		dataSets.add(setComp1);

		BarData barData = new BarData(dataSets);

		//折线图点的标记
		MyMarkerView mv = new MyMarkerView(this);
		barChart.setMarker(mv);
		barChart.setData(barData);
		barChart.invalidate();
	}

	private float getMax(ArrayList<BarEntry> list) {
		float max = list.get(0).getY();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getY() >= max) {
				max = list.get(i).getY();
			}
		}
		return max;
	}

	private float getMin(ArrayList<BarEntry> list) {
		float min = list.get(0).getY();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getY() <= min) {
				min = list.get(i).getY();
			}
		}
		return min;
	}


}