package jp.ac.tokushima_u.is.ll.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatUtil {
	private FormatUtil(){};
	public static final String DATE_TIME_FORMAT_YYYYMMDD = "yyyy/MM/dd";
	public static final String DATE_TIME_SQL_FORMAT_YYYYMMDD = "yyyy-MM-dd";

	public static String formatString(String msg, String[] arguments){
		return MessageFormat.format(msg, (Object[])arguments);
	}
	public static String formatYYYYMMDD(Timestamp v){
		return formatDate(DATE_TIME_FORMAT_YYYYMMDD, v);
	}

	public static String formatYYYYMMDD(Date v){
		return formatDate(DATE_TIME_FORMAT_YYYYMMDD, v);
	}

	public static String formatYYYYMMDD(java.util.Date v){
		return formatDate(DATE_TIME_FORMAT_YYYYMMDD, v);
	}

	private static String formatDate(String pattern, java.util.Date v){
		if (v != null) {
			return new SimpleDateFormat(pattern).format(v);
		} else {
			return "";
		}
	}
	
	public static Integer getSeconds(Time t1){
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int s1 = c1.get(Calendar.SECOND);
		return h1*60*60+m1*60+s1;
	}
}
