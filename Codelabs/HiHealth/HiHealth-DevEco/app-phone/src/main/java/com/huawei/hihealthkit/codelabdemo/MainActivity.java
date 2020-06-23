package com.huawei.hihealthkit.codelabdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.huawei.hihealth.error.HiHealthError;
import com.huawei.hihealth.listener.ResultCallback;
import com.huawei.hihealthkit.HiHealthDataQuery;
import com.huawei.hihealthkit.HiHealthDataQueryOption;
import com.huawei.hihealthkit.auth.HiHealthAuth;
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType;
import com.huawei.hihealthkit.auth.IAuthorizationListener;
import com.huawei.hihealthkit.codelabdemo.utils.DateUtil;
import com.huawei.hihealthkit.codelabdemo.widget.CommonDialog;
import com.huawei.hihealthkit.codelabdemo.widget.SportDataDialog;
import com.huawei.hihealthkit.data.HiHealthKitConstant;
import com.huawei.hihealthkit.data.HiHealthPointData;
import com.huawei.hihealthkit.data.HiHealthSetData;
import com.huawei.hihealthkit.data.store.HiHealthDataStore;
import com.huawei.hihealthkit.data.store.HiRealTimeListener;
import com.huawei.hihealthkit.data.store.HiSportDataCallback;
import com.huawei.hihealthkit.data.type.HiHealthPointType;
import com.huawei.hihealthkit.data.type.HiHealthRealTimeType;
import com.huawei.hihealthkit.data.type.HiHealthSetType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private final static String TAG = MainActivity.class.getSimpleName();
	private static final int TODAY_STEP = 1;
	private static final int TODAY_DISTANCE = 2;
	private static final int TODAY_CALORIES = 3;
	private static final int TODAY_HEART = 4;
	private static final int START_REALTIMESPORT = 5;
	private static final int REALTIMESPORT_DATA = 6;
	private static final int STOP_REALTIMESPORT = 7;
	private boolean isStart = false;
	private boolean isStop = false;
	private TextView historyTV;
	private TextView todayStepSum;
	private TextView todayDistance;
	private TextView todayCalorie;
	private Button btnBindDevice;
	private ImageView startRealTimeSportIV;
	private TextView dataStepSum;
	private TextView dataDistance;
	private TextView dataCalorie;
	private TextView dataHeartRate;
	private CommonDialog startSportDialog;
	private SportDataDialog sportDataDialog;
	private Map<String, String> sportDataMap;

	@Override
	protected void init() {
		initView();
		initData();
	}

	@Override
	protected int layout() {
		return R.layout.activity_main;
	}

	private void startRealTimeSportData() {



	}

	private void stopRealTimeSportData() {



	}

	private void initView() {
		noStatusBar();
		historyTV = (TextView) findViewById(R.id.history);
		historyTV.setOnClickListener(this);
		todayStepSum = (TextView) findViewById(R.id.today_step_sum);
		todayDistance = (TextView) findViewById(R.id.today_distance);
		changeText(todayDistance, R.string.distance_content, "0");
		todayCalorie = (TextView) findViewById(R.id.today_calorie);
		changeText(todayCalorie, R.string.calorie_content, "0");

		dataStepSum = (TextView) findViewById(R.id.data_step_sum);
		dataDistance = (TextView) findViewById(R.id.data_distance);
		dataCalorie = (TextView) findViewById(R.id.data_calorie);
		dataHeartRate = (TextView) findViewById(R.id.data_heartRate);

		btnBindDevice = (Button) findViewById(R.id.btn_bind_device);
		btnBindDevice.setOnClickListener(this);
		startRealTimeSportIV = (ImageView) findViewById(R.id.switch_realTimeSport);
		startRealTimeSportIV.setOnClickListener(this);

	}

	private void initData() {
		requestAllAuthorization();
		startSportDialog = new CommonDialog(MainActivity.this);
		startSportDialog.setTitle("打开实时运动")
				.setMessage("打开实时运动，需要开启【运动健康-运动】是否确定前往开启？")
				.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
					@Override
					public void onPositiveClick() {
						startSportDialog.dismiss();
						goToHealthApp();
					}

					@Override
					public void onNegtiveClick() {
						startSportDialog.dismiss();
					}
				});
		sportDataDialog = new SportDataDialog(MainActivity.this);
		sportDataDialog.setOnClickBottomListener(new SportDataDialog.OnClickBottomListener() {
			@Override
			public void onStartClick() {
				startRealTimeSport();
			}

			@Override
			public void onStopClick() {
				startRealTimeSport();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.history:
				Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_bind_device:
				final CommonDialog dialog = new CommonDialog(MainActivity.this);
				dialog.setTitle("绑定设备")
						.setMessage("您的APP还未绑定设备，是否前往【运动健康-设备】绑定设备？")
						.setRemark("*注：绑定设备，可获得更多运动数据")
						.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
							@Override
							public void onPositiveClick() {
								dialog.dismiss();
								goToHealthApp();
							}

							@Override
							public void onNegtiveClick() {
								dialog.dismiss();
							}
						}).show();
				break;
			case R.id.switch_realTimeSport:
				startRealTimeSport();
			default:
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getTodayData();
	}

	private void startRealTimeSport() {
		if (isStart) {
			stopRealTimeSportData();
		} else {
			isStop = false;
			startRealTimeSportData();
		}
	}

	public void sendMessage(int what, int resultCode, Object data) {
		Message message = new Message();
		message.what = what;
		message.arg1 = resultCode;
		message.obj = data;
		handler.sendMessage(message);
	}

	private void requestAllAuthorization() {
		int[] readPermissionArray = new int[]{
				HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
				HiHealthRealTimeType.REALTIME_HEARTRATE,
				HiHealthPointType.HEALTH_KIT_NEW_DATA_TYPE_REAL_TIME_SPORT_DATA,
				HiHealthPointType.DATA_POINT_DISTANCE_SUM,
				HiHealthPointType.DATA_POINT_CALORIES_SUM,
				HiHealthSetType.DATA_SET_HEART,

		};
		int[] writePermissionArray = new int[]{};
		HiHealthAuth.requestAuthorization(this, writePermissionArray, readPermissionArray,
				new IAuthorizationListener() {
					@Override
					public void onResult(int errorCode, Object data) {
						Log.i(TAG, "requestAuthorization onResult:" + errorCode);
					}
				});
	}

	private void getTodayData() {
		getTodayStepSum();
		getTodayDistance();
		getTodayCalories();
		getTodayHeartRate();
	}

	private void getTodayStepSum() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = DateUtil.getToday();
		HiHealthDataQuery hiHealthDataQuery = new HiHealthDataQuery(HiHealthPointType
				.DATA_POINT_STEP_SUM,
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
				sendMessage(TODAY_STEP, i, data);
			}
		});

	}

	private void getTodayDistance() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = DateUtil.getToday();
		HiHealthDataQuery hiHealthDataQuery = new HiHealthDataQuery(HiHealthPointType
				.DATA_POINT_DISTANCE_SUM,
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
				sendMessage(TODAY_DISTANCE, i, data);
			}
		});


	}

	private void getTodayCalories() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = DateUtil.getToday();
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
				sendMessage(TODAY_CALORIES, i, data);
			}
		});
	}

	private void getTodayHeartRate() {
		int timeout = 0;
		long endTime = System.currentTimeMillis();
		long startTime = DateUtil.getToday();
		HiHealthDataQuery hiHealthDataQuery = new HiHealthDataQuery(HiHealthSetType
				.DATA_SET_HEART,
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
				sendMessage(TODAY_HEART, i, data);
			}
		});

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case TODAY_STEP:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null && dataList.size() > 0) {
							HiHealthPointData hiHealthPointData = (HiHealthPointData) dataList.get(0);
							int stepSum = hiHealthPointData.getValue();
							if (stepSum > 0) {
								String text = String.valueOf(stepSum);
								todayStepSum.setText(text);
								dataStepSum.setText(text + "步");
							} else {
								todayStepSum.setText("0");
								dataStepSum.setText("——");
							}
						}
					} else {
						todayStepSum.setText("0");
						dataStepSum.setText("——");
					}
					break;
				case TODAY_DISTANCE:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null && dataList.size() > 0) {
							HiHealthPointData hiHealthPointData = (HiHealthPointData) dataList.get(0);
							float distance = (float) (Math.round(hiHealthPointData.getValue() / 1000f * 100)) / 100;
							if (distance > 0f) {
								String text = String.valueOf(distance);
								changeText(todayDistance, R.string.distance_content, text);
								dataDistance.setText(text + "公里");
							} else {
								dataDistance.setText("——");
							}
						}
					} else {
						changeText(todayDistance, R.string.distance_content, "0");
						dataDistance.setText("——");
					}
					break;
				case TODAY_CALORIES:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null && dataList.size() > 0) {
							HiHealthPointData hiHealthPointData = (HiHealthPointData) dataList.get(0);
							int calories = Math.round(hiHealthPointData.getValue() / 1000f);
							if (calories > 0) {
								String text = String.valueOf(calories);
								changeText(todayCalorie, R.string.calorie_content, text);
								dataCalorie.setText(text + "kcal");
							} else {
								dataCalorie.setText("——");
							}
						}
					} else {
						changeText(todayCalorie, R.string.calorie_content, "0");
						dataCalorie.setText("——");
					}
					break;
				case TODAY_HEART:
					if (msg.obj != null) {
						List dataList = (List) msg.obj;
						if (dataList != null && dataList.size() > 0) {
							HiHealthSetData hiHealthSetData = (HiHealthSetData) dataList.get(0);
							Map map = hiHealthSetData.getMap();
							int heartRate = Math.round((float) map.get(46018));
							if (heartRate > 0) {
								String text = String.valueOf(heartRate);
								dataHeartRate.setText(text + "次/分钟");
							} else {
								dataHeartRate.setText("——");
							}
						}
					} else {
						dataHeartRate.setText("——");
					}
					break;
				case START_REALTIMESPORT:
					if (msg.arg1 == 0) {
						//没有运动数据说明没有进入运动状态，需要去【运动健康-运动】中开启运动模式
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (sportDataMap == null) {
									if (!startSportDialog.isShowing()) {
										startSportDialog.show();
									}
								}
							}
						}, 700);

					} else {
						toast("errorCode：" + msg.arg1);
					}
					break;
				case REALTIMESPORT_DATA:
					if (msg.obj != null) {
						if (startSportDialog.isShowing()) {
							startSportDialog.dismiss();
						}

						Bundle bundle = (Bundle) msg.obj;
						int distance = bundle.getInt(HiHealthKitConstant.BUNDLE_KEY_DISTANCE); //单位：米
						int duration = bundle.getInt(HiHealthKitConstant.BUNDLE_KEY_DURATION); //单位：秒
						int heartRate = bundle.getInt(HiHealthKitConstant.BUNDLE_KEY_HEARTRATE);//单位：次/分钟
						int pace = bundle.getInt(HiHealthKitConstant.BUNDLE_KEY_PACE); //单位：秒/公里
						if (sportDataMap == null) {
							sportDataMap = new HashMap<>();
						}

						float distance1 = (float) (Math.round(distance / 1000f * 100)) / 100;
						sportDataMap.put("distance", String.valueOf(distance1));
						sportDataMap.put("duration", DateUtil.secToTime(duration));
						if (pace > 0) {
							sportDataMap.put("speed", DateUtil.formatPace(pace) + "/公里");
						} else {
							sportDataMap.put("speed", "——");
						}

						//没有心率数据提醒绑定设备
						if (heartRate == 0) {
							btnBindDevice.setVisibility(View.VISIBLE);
							sportDataMap.put("heartRate", "——");
						} else {
							btnBindDevice.setVisibility(View.GONE);
							sportDataMap.put("heartRate", String.valueOf(heartRate) + "次/分钟");
						}

						//显示运动数据
						if (!isStop) {
							if (!sportDataDialog.isShowing()) {
								sportDataDialog.show();
							}
							isStart = true;
							sportDataDialog.refreshView(sportDataMap);
							startRealTimeSportIV.setImageResource(R.mipmap.off);
						}
					}
					break;
				case STOP_REALTIMESPORT:
					isStart = false;
					isStop = true;
					sportDataMap = null;
					startRealTimeSportIV.setImageResource(R.mipmap.on);
					if (sportDataDialog.isShowing()) {
						sportDataDialog.dismiss();
					}
					break;
			}
		}
	};

	private void changeText(TextView textView, int name, String content) {
		String text = String.format(getResources().getString(name), content);
		int index = text.indexOf(content);
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new ForegroundColorSpan(Color.WHITE), index, index + content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new AbsoluteSizeSpan(22, true), index, index + content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);     //设置字体大小
		textView.setText(style);
	}

}