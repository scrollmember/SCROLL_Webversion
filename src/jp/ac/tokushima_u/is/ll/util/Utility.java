package jp.ac.tokushima_u.is.ll.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utility {
	public static Timestamp getCurrentTime(){
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	private String getPath(){
		return getClass().getResource("/").getPath();
	}
	
	public static String getUuidKey(){
		 String uukey = UUID.randomUUID().toString();
		 uukey = uukey.replace("-", "");
		 return uukey; 
	}
	
	public static Time getNowTime(){
		Calendar cal = Calendar.getInstance();
		return new Time(cal.getTimeInMillis());
	}
	
	public static Date getToday(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -1);
		return new Date(cal.getTimeInMillis());
	}
	
	public static Time getMaxTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return new Time(cal.getTimeInMillis());
	}
	
	public static int isBigger(Time t1, Time t2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		c2.setTimeInMillis(t2.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int h2 = c2.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int m2 = c2.get(Calendar.MINUTE);
		int s1 = c1.get(Calendar.SECOND);
		int s2 = c2.get(Calendar.SECOND);
		int ts1 = h1*60*60+m1*60+s1;
		int ts2 = h2*60*60+m2*60+s2;
		if((ts1-ts2)>0)
			return 1;
		else if((ts1-ts2)==0)
			return 0;
		else
			return -1;
	}
	
	public static int getSeconds(Time t1){
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int s1 = c1.get(Calendar.SECOND);
		return h1*60*60+m1*60+s1;
	}
	
	public static boolean isEqual(Time t1, Time t2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		c2.setTimeInMillis(t2.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int h2 = c2.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int m2 = c2.get(Calendar.MINUTE);
		int s1 = c1.get(Calendar.SECOND);
		int s2 = c2.get(Calendar.SECOND);
		if((h1==h2)&&(m1==m2)&&(s1==s2))
			return true;
		else
			return false;
	}
	
	
	
	public static Time getMinTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Time(cal.getTimeInMillis());
	}

	public static Time getMiddleTime(Time t1,Time t2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		c2.setTimeInMillis(t2.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int h2 = c2.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int m2 = c2.get(Calendar.MINUTE);
		int totalMin = ((h1+h2)*60+m1+m2)/2;
		int h = totalMin/60;
		int m = totalMin%60;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		
		return new Time(c.getTimeInMillis());
	}
	
	public static Time getTime(Date day){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(day.getTime());
		return new Time(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND));
	}
	
	public static int getDifferMinuteTwoDays(Date d1, Date d2){
		long differ = Math.abs(d1.getTime()-d2.getTime());
		return (int)(differ/60000);
	}
	
	public static int getDifferDayTwoDays(Date d1, Date d2){
		long differ = Math.abs(d1.getTime()-d2.getTime());
		return (int)(differ/86400000);
	}
	
	public static int getDifferMinuteTwoTimes(Time t1, Time t2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		c2.setTimeInMillis(t2.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int h2 = c2.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int m2 = c2.get(Calendar.MINUTE);
		return Math.abs((h1*60+m1-h2*60-m2));
	
	}
	
	
	public static String getRootPath(){
		Utility u = new Utility();
		return u.getPath();
	}
	
	
	public static String analyzeTestId(String subject, String testtypetitle){
		int index = subject.indexOf(testtypetitle);
		return subject.substring(index+testtypetitle.length());
	}
	
	public static void main(String[]args){
		Time maxTime = Utility.getMinTime();
		Time minTime = Utility.getMaxTime();
		
		Date d1 = Calendar.getInstance().getTime();
		Time t1 = new Time(d1.getTime());
		
		
		Date d2 = new Date("2010/10/06 13:10:00");
		Time t2 = new Time(d2.getTime());
		
		System.out.println(isBigger(t2,maxTime));
		
		
		if(t2.before(maxTime))
			System.out.println("smaller");
		else if(t2.after(maxTime))
			System.out.println("bigger");
			 
		System.out.println();
	}
	
}
