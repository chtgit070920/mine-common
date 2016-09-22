package com.mime.common;

import com.mine.common.DateUtil;
import com.mine.common.ObjectUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Edward on 2016/9/21.
 */
@RunWith(Parameterized.class)
public class DateUtilTest {

    private  String dateStr;
    private String weekStr;
    private String weekDayNumStr;
    private  String weekTotalStr;
    private  String weekNoStr;
    private String monthDayNumStr;


    public DateUtilTest(String dateStr,
                        String weekStr,
                        String weekDayNumStr,
                        String weekTotalStr,
                        String weekNoStr,
                        String monthDayNumStr
    ){
        this.dateStr=dateStr;
        this.weekStr=weekStr;
        this.weekDayNumStr=weekDayNumStr;
        this.weekTotalStr=weekTotalStr;
        this.weekNoStr=weekNoStr;
        this.monthDayNumStr=monthDayNumStr;
    }

    @Parameterized.Parameters
    public static List<Object[]> initParams(){
        return Arrays.asList(new Object[][]{
                //日期、周几、所在周天数(限定在月中)、所在月周数、所在月第几周、所在月天数
                {"20160501","7","1","6","1","31"},
                {"20160502","1","7","6","2","31"}
        });
    }

    @Test
    public void test(){
        Date date=DateUtil.string_date(dateStr);

        Integer week=DateUtil.getWeek(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeek"),weekStr,week.toString());

        Integer weekTotal=DateUtil.getWeekTotalInMonth(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekTotalInMonth"),weekTotalStr,weekTotal.toString());

        Integer weekNo=DateUtil.getWeekNoInMonth(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekNoInMonth"),weekNoStr,weekNo.toString());

        Integer weekDayNum=DateUtil.getWeekDayNumInMonth(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekDayNumInMonth"),weekDayNumStr,weekDayNum.toString());

        Integer monthDayNum=DateUtil.getMonthDayNum(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getMonthDayNum"),monthDayNumStr,monthDayNum.toString());

        List<Date> result= DateUtil.getWeekDayListInMonth(date);
        for(Date d:result){
            System.out.print(DateUtil.date_string(d,"yyyyMMdd"));
            System.out.print("  ");
        }
        System.out.println();
    }

    public String failureMsg(Class clazz,String method){
        return "Class:"+clazz.getName()+",Method:"+method+",result:error!";
    }

}
