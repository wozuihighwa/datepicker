package me.wildchaos.datepicker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 孙俊伟 on 2016/2/27.
 */
public class DateUtil {

    private static Calendar mCalendar = Calendar.getInstance();

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        mCalendar.clear();
        mCalendar.setTime(new Date());
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        mCalendar.clear();
        mCalendar.setTime(new Date());
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前天
     *
     * @return
     */
    public static int getCurrentDay() {
        return mCalendar.get(Calendar.DATE);
    }

    /**
     * 获取某月共有多少天
     *
     * @param month
     * @return
     */
    public static int getDayOfMonth(int year, int month) {
        mCalendar.clear();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1); // Java 月份从 0 开始
        return mCalendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取某日是星期几
     *
     * @param year
     * @param month
     * @return
     * @throws ParseException
     */
    public static int getCurrentDate(int year, int month) throws ParseException {
        String date = year + "-" + month + "-" + "01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = sdf.parse(date);
        if (currentDate != null) {
            mCalendar.setTime(currentDate);
        }
        int w = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 返回当前 日期 （天）
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static int getDayInDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        Date date = sdf.parse(getYYMMDDTime(dateStr));
        mCalendar.clear();
        mCalendar.setTime(date);
        return mCalendar.get(Calendar.DATE);
    }

    private static String getYYMMDDTime(String time) {
        String[] strs = time.split("T");
        return strs[0];
    }

}
