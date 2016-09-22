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
	 *  周几
	 */
	public static int getWeek(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		dayOfWeek=dayOfWeek==0?7:dayOfWeek;
		return dayOfWeek;
	}

	/**
	 *          2016年5月
	 * 一  二  三  四  五  六  七
	 *                          1
	 * 2   3   4   5   6   7    8
	 * 9   10  11  12  13  14   15
	 * 16  17  18  19  20  21   22
	 * 23  24  25  26  27  28   29
	 * 30  31
	 *
	 * 什么是自然月、自然周？
	 * 自然月：每月的第一天开始到每月的最后一天为止为一个自然月，如3月1日到3月31日。
	 * 非自然月：是从一个月份中的任一天开始,到下个月份的相同日期的那一天为止,也算一个月,但就不是自然月，如3月15日到4月15日为一个非自然月。
	 * 自然周：周一到周日。
	 * 非自然周：本周三到下周三。
	 *
	 * e.g. 201605-->自然月中的自然周数：6；
	 *                第一周天数：1
	 *                第二周天数：7
	 *                第三周天数：7
	 *                第四周天数：7
	 *                第四周天数：7
	 *                第四周天数：2
	 */

	/**
	 * 共几周（限定在月中）
	 */
	public static int getWeekTotalInMonth(Date d){
		Date startDayOfMonth=getFirstDayOfMonth(d);//月第一天 日期
		Date startDayOfWeek1=getFirstDayOfWeek(startDayOfMonth);//周一
		Date endDateOfMonth=getLastDayOfMonth(d);//月最后一天
		Date endDayOfWeek2=getFirstDayOfWeek(endDateOfMonth);//周一

		int days=daysBetween(startDayOfWeek1,endDayOfWeek2);
		int weekTotal=days/7+1;
		return weekTotal;
	}

	/**
	 * 第几周（限定在月中）
	 */
	public static int getWeekNoInMonth(Date d){
		Date startDayOfMonth=getFirstDayOfMonth(d);//日所在月第一天
		Date beginDate=getFirstDayOfWeek(startDayOfMonth);//日所在月第一天对应的周一
		Date startDayOfWeek=getFirstDayOfWeek(d);//日所在周的周一
		int days=daysBetween(beginDate,startDayOfWeek);
		int weekNo=days/7+1;
		return weekNo;
	}

	/**
	 * 周天数（限定在月中）
	 */
	public static int getWeekDayNumInMonth(Date d){
		int weekTotal=getWeekTotalInMonth(d);
		int weekNo=getWeekNoInMonth(d);
		int days=0;
		if(weekNo==1){
			Date startDayOfMonth=DateUtil.getFirstDayOfMonth(d);
			int week=getWeek(startDayOfMonth);
			days=7-week+1;
		}else if(weekNo==weekTotal){
			Date endDayOfMonth=DateUtil.getLastDayOfMonth(d);
			int week=getWeek(endDayOfMonth);
			days=week;
		}else{
			days=7;
		}
		return days;
	}



	/**
	 * 周日期集合（限定在月中）
	 */
	public static List<Date> getWeekDayListInMonth(Date d){
		Date startDayOfMonth=date_date(d,"yyyyMM");
		int weekNo=getWeekNoInMonth(d);

		Date beginDate;
		if(weekNo==1){
			beginDate=startDayOfMonth;
		}else{
			Date startDayOfWeek=getFirstDayOfWeek(startDayOfMonth);//周一
			beginDate=addDays(startDayOfWeek,7*(weekNo-1));
		}
		List <Date> result=new ArrayList<Date>();
		int dayNum=getWeekDayNumInMonth(d);
		for(int i=0;i<dayNum;i++){
			result.add(addDays(beginDate,i));
		}
		return result;
	}

	/**
	 * 月天数
	 */
	public static int getMonthDayNum(Date date){
		Calendar time=Calendar.getInstance();
		time.setTime(date);
		int days=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
		return days;
	}


	//---------------------------------------------------------------------
	// 第一天、最后一天
	//---------------------------------------------------------------------

	/**
	 * 日所在周第一天（周一）
	 */
	public static Date getFirstDayOfWeek(Date d){
		int weekInt=getWeek(d);//获得周几
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

	//---------------------------------------------------------------------
	// 间隔
	//---------------------------------------------------------------------

	/**
	 * 日间隔
	 */
	public static double daysBetween0(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
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





	//---------------------------------------------------------------------
	// 加 /减
	//---------------------------------------------------------------------

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
		
	}
	
}
