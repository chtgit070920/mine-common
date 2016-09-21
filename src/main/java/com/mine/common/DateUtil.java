package com.mine.common;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author edward
 * @version 2014-4-15
 */
public class DateUtil extends DateUtils {
	
	private static String[] parsePatterns = {
		 "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",  "yyyy-MM-dd",   "yyyy-MM", 
		 "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",   "yyyy/MM/dd",  "yyyy/MM", 
		 "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm",   "yyyy.MM.dd",  "yyyy.MM",  
		 "yyyyMMddHHmmss",      "yyyyMMddHHmm",      "yyyyMMdd",     "yyyyMM"       
	};

	
	public static Date string_date(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date string_date(Object str,String pattern) {
		if (str == null||null==pattern) {
			return null;
		}
		try {
			return parseDate(str.toString(), new String[]{pattern});
		} catch (Exception e) {
			return null;
		}
	}
	public static String date_string(Date date) {
		for(String pattern:parsePatterns){
			String d=date_string(date,pattern);
			if(!StringUtil.isBlank(d)) return d;
		}
		return null;
	}
	public static String date_string(Date date, String pattern) {
		String formatDate = null;
		if (null!=date&&pattern != null ) {
			formatDate = DateFormatUtils.format(date, pattern);
		}
		return formatDate;
	}

	public static Date date_date(Date date,String pattern) {
		if (date == null||null==pattern) {
			return null;
		}
		try {
			String s=date_string(date,pattern);
			return string_date(s, pattern);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 得到日期年份字符串 格式（yyyy）
	 */
	public static String getYear(Date d) {
		return date_string(d, "yyyy");
	}

	/**
	 * 得到日期月份字符串 格式（MM）
	 */
	public static String getMonth(Date d) {
		return  date_string(d, "MM");
	}

	/**
	 * 得到日期字符串 格式（dd）
	 */
	public static String getDay(Date d) {
		return date_string(d, "dd");
	}

	/**
	 * 得到 日期星期字符串 格式（E）星期几
	 */
	public static String getWeek(Date d) {
		return date_string(d, "E");
	}
	
	/**
	 * 得到 日期星期字符串 格式（E）星期几
	 */
	public static Integer getWeekInt(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		dayOfWeek=dayOfWeek==0?7:dayOfWeek;
		return dayOfWeek;
	}

	/**
	 * 两个日期之间的天数
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 增加/减小 月
	 */
	public static Date addMonths(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		c.getTime();
		return c.getTime();
	}

	/**
	 *  增加/减小 天
	 */
	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		c.getTime();
		return c.getTime();
	}

	/**
	 *  增加/减小 小时
	 */
	public static Date addHours(Date date, int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hours);
		c.getTime();
		return c.getTime();
	}

	/**
	 *  增加/减小 分钟
	 */
	public static Date addMin(Date date, int min) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, min);
		c.getTime();
		return c.getTime();
	}

	/**
	 *  增加/减小 秒
	 */
	public static Date addSecond(Date date, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, second);
		c.getTime();
		return c.getTime();
	}


	/**
	 * 当前日所在月的第几周（自然周）
	 */
	public static int getWeekNo(Date d){
		Date startDayOfMonth=getFirstDayOfMonth(d);//日所在月第一天
		Date beginDate=getFirstDayOfWeek(startDayOfMonth);//日所在月第一天对应的周一
		Date startDayOfWeek=getFirstDayOfWeek(d);//日所在周的周一
		int days=daysBetween(beginDate,startDayOfWeek);
		int weekNo=days/7+1;
		return weekNo;
	}
	/**
	 * 当前日期所在月周数（自然周）
	 */
	public static int getWeekNum(Date d){
		Date startDayOfMonth=getFirstDayOfMonth(d);//月第一天 日期
		Date startDayOfWeek1=getFirstDayOfWeek(startDayOfMonth);//周一
		Date endDateOfMonth=getLastDayOfMonth(d);//月最后一天
		Date endDayOfWeek2=getFirstDayOfWeek(endDateOfMonth);//周一

		int days=daysBetween(startDayOfWeek1,endDayOfWeek2);
		int weekNum=days/7+1;
		return weekNum;

	}
	/**
	 * 当前日期所在周次的天数（自然周）
	 */
	public static int getWeekDayNum(Integer ym,int weekNo){
		Date d=string_date(ym.toString());
		int weekNum=getWeekNum(d);
		int days=0;
		if(weekNo==1){
			Date startDayOfMonth=DateUtil.getFirstDayOfMonth(d);
			int weekInt=getWeekInt(startDayOfMonth);
			days=7-weekInt+1;
		}else if(weekNo==weekNum){
			Date endDayOfMonth=DateUtil.getLastDayOfMonth(d);
			int weekInt=getWeekInt(endDayOfMonth);
			days=weekInt;
		}else{
			days=7;
		}
		return days;
	}
	/**
	 * 某月份某周次的日期集合（自然周）
	 */
	public static List<Date> getWeekDayList(Integer ym,Integer weekNo){
		Date startDayOfMonth=string_date(ym.toString());
		Date beginDate;
		if(weekNo==1){
			beginDate=startDayOfMonth;
		}else{
			Date startDayOfWeek=getFirstDayOfWeek(startDayOfMonth);//周一
			beginDate=addDays(startDayOfWeek,7*(weekNo-1));
		}
		List <Date> result=new ArrayList<Date>();
		int dayNum=getWeekDayNum(ym,weekNo);
		for(int i=0;i<dayNum;i++){
			result.add(addDays(beginDate,i));
		}
		return result;
	}
	

	/**
	 * 日所在月天数
     */
	public static int getDays(Date date){
		Calendar time=Calendar.getInstance(); 
		time.setTime(date);
		int days=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
		return days;
	}


	/**
	 * 日所在周第一天（周一）
	 */
	public static Date getFirstDayOfWeek(Date d){
		int weekInt=getWeekInt(d);//获得周几
		return addDays(d,-weekInt+1);//获得周所在周的周一
	}

	/**
	 * 日所在周最后一天（周日）
	 */
	public static Date getLastDayOfWeek(Date d){
		Date firstDay=getFirstDayOfWeek(d);
		return addDays(firstDay,6);
	}

	/**
	 * 日所在月第一天
	 */
	public static Date getFirstDayOfMonth(Date date){
		 Date d=date_date(date,"yyyyMM");
		 return d;
	}

	/**
	 * 日所在月最后一天
	 */
	public static Date getLastDayOfMonth(Date date){
		 Date d=getFirstDayOfMonth(date);
		 d=addMonths(d,1);
		 d=addDays(d,-1);
	     return d;
	}

	/**
	 * 日间隔
	 *e.g. 1.1~1.1->0
	 *      1.1~1.3->2
     */
	public static int daysBetween(Date sdate,Date edate) {    
		sdate=date_date(sdate,"yyyy-MM-dd");  
		edate=date_date(edate,"yyyy-MM-dd");   
        
        long between_days=(edate.getTime()-sdate.getTime())/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days));           
    }

	/**
	 * 月间隔
	 * e.g. 1.1~1.2->0
	 *      1.1~2.29->1
	 */
	public static int monthsBetween(Date sdate,Date edate)  {    
		sdate=date_date(sdate,"yyyy-MM");  
		edate=date_date(edate,"yyyy-MM");   
		int i=0;
        for(Date d=sdate;d.getTime()<=edate.getTime();d=addMonths(d,1),i++){ }
        return i-1;           
    }


	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
				* 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "."
				+ sss;
	}


	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		
//		List<Date> result=getWeekDayList(201702,5);
//		for(Date d:result){
//			System.out.println(date_string(d));
//		}
		
		
//		 System.out.println(formatDate(parseDate("2010/3/6")));
//		 System.out.println(getDate("yyyy年MM月dd日 E"));
//		 long time = new Date().getTime()-string_date("2012-11-19").getTime();
//		 System.out.println(time/(24*60*60*1000));
//		System.out.println(date_string(addMin(new Date(), 1),"yyyy-MM-dd HH:mm:ss"));
//		System.out.println(getWeekInt(new Date()));
		
		
//		String s="20160608";
//		Date d=DateUtil.string_date(s);
//		System.out.println(DateUtil.getWeekInt(d));
		
		//for()
//		System.out.println(time.get(Calendar.DAY_OF_MONTH));//当前日期所在月的第几天，从1开始
//		System.out.println(time.get(Calendar.WEEK_OF_MONTH));//当前日期在月中的第几周，从1开始
		
//		String[] yms=new String[]{"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//				"20160102","20160102","20160102","20160102","20160102","20160102","20160102","20160102",
//		
//		};
//		for(String ym1:yms){
//			System.out.println(getWeekInt(string_date(ym1)));
//			System.out.println(getWeekNo(string_date(ym1)));
//			System.out.println(getWeekTotal(string_date(ym1)));
//			System.out.println(getWeekDays(string_date(ym1),6));
//		}
		
//	Date d=string_date("20160501");
		
//	   System.out.println(getMonDayOfWeek(d));

//		Calendar time1 = Calendar.getInstance();
//		time1.setTime(string_date(ym1));
//		System.out.println(getWeekInt(string_date(ym1)));
//		System.out.println(time1.get(Calendar.WEEK_OF_MONTH));//当前日期在月中的第几周
		//System.out.println(time.getActualMaximum(Calendar.WEEK_OF_MONTH));//一月共有几周
		
		
		//		    Calendar cal = Calendar.getInstance();
//	        cal.clear();
//	        cal.set(Calendar.YEAR, 2016);
//	        cal.set(Calendar.WEEK_OF_YEAR,1);
//	        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//	        System.out.println(DateUtil.date_string(cal.getTime()));
//	        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//	        System.out.println(DateUtil.date_string(cal.getTime()));
//	        System.out.println();
		
		int i=monthsBetween(string_date("20160501"),string_date("20160630"));
		System.out.println(i);

		System.out.println(getWeekNo(string_date("20160501")));
		System.out.println(getWeekNo(string_date("20160502")));
		System.out.println(getWeekNo(string_date("20160508")));
		System.out.println(getWeekNo(string_date("20160509")));
		System.out.println(getWeekNo(string_date("20160530")));
		System.out.println(getWeekNo(string_date("20160531")));
	}
	
}
