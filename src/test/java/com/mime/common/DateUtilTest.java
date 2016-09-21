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

    private String weekIntStr;
    private  String weekNoStr;
    private  String weekNumStr;
    private String weekDayNumStr;

    public DateUtilTest(String dateStr,String weekIntStr,String weekNoStr,String weekNumStr,String weekDayNumStr){
        this.dateStr=dateStr;
        this.weekIntStr=weekIntStr;
        this.weekNoStr=weekNoStr;
        this.weekNumStr=weekNumStr;
        this.weekDayNumStr=weekDayNumStr;
    }

    @Parameterized.Parameters
    public static List<Object[]> initParams(){
        return Arrays.asList(new Object[][]{
                {"20160501","7","1","6","2"}
        });
    }

    @Test
    public void test1(){
        Date date=DateUtil.string_date(dateStr);

        Integer weekInt=DateUtil.getWeekInt(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekInt"),weekIntStr,weekInt.toString());

        Integer weekNo=DateUtil.getWeekNo(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekNo"),weekNoStr,weekNo.toString());

        Integer weekNum=DateUtil.getWeekNum(date);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekNum"),weekNumStr,weekNum.toString());

        String year=DateUtil.getYear(date);
        String month=DateUtil.getMonth(date);
        Integer ym=Integer.valueOf(year+month);

        Integer weekDayNum=DateUtil.getWeekDayNum(ym,weekNo);
        Assert.assertEquals(failureMsg(DateUtil.class,"getWeekDayNum"),weekDayNumStr,weekDayNum.toString());

    }

    public String failureMsg(Class clazz,String method){
        return "Class:"+clazz.getName()+",Method:"+method+",result:error!";
    }

}
