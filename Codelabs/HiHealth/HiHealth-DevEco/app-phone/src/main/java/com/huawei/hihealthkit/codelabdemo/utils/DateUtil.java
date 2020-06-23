package com.huawei.hihealthkit.codelabdemo.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String formatPace(int time) {
		String timeStr = null;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return "0'00''";
		} else {
			minute = time / 60;
			second = time % 60;
			if(second >= 0 && second < 10){
				timeStr = minute + "'0" + second +"''";
			}else {
				timeStr = minute + "'" + second +"''";
			}

		}
		return timeStr;
	}

	public static Long getToday(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static String getDateFormat(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return month + "/" + day;
	}

	public static List<String> getListDate(){
		Calendar calendar =Calendar.getInstance();
		Date date = new Date();
		List<String> dateList=new ArrayList<String>();
		try {
			for(int i=30;i>=0;i--){
				calendar.setTime(date);
				calendar.add(calendar.DATE, -i);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String minDateStr = month + "/" + day;
				dateList.add(minDateStr);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return dateList;
	}

}
