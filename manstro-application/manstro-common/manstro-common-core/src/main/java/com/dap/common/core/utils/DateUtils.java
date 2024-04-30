package com.dap.common.core.utils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 *
 * @author diaozhaojian
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final String YYYY = "yyyy";

    private static final String YYYY_MM = "yyyy-MM";
    private static final String YYYYMM = "yyyyMM";

    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60l;
        long nh = 1000 * 60 * 60l;
        long nm = 1000 * 60l;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 日期string 转 string  yyyy-MM-dd HH:mm:ss  --- yyyyMM
     *
     * @return
     */
    public static String strTostr(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (Exception e) {
            logger.error("strTostr error,data:{}", date, e);
        }
        return parseDateToStr(YYYYMM, parse);
    }

    /**
     * 日期string 转 string  yyyy-MM-dd HH:mm:ss  --- yyyy
     *
     * @return
     */
    public static String strTostrYear(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (Exception e) {
            logger.error("strTostrYear error,data:{}", date, e);

        }
        return parseDateToStr(YYYY, parse);
    }

    /**
     * 日期date 转 string  yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String dateTostr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        try {
            dateStr = sdf.format(date.getTime());
        } catch (Exception e) {
            logger.error("dateTostr error,data:{}", date, e);

        }
        return dateStr;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 1970-01-01 加天数 得到日期（日期格式）
     */
    public static Date getDaysDate(Integer days) {
        Date date = dateTime(YYYY_MM_DD, "1970-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 1970-01-01 加天数 得到日期
     */
    public static String getDaysString(Integer days) {
        Date date = dateTime(YYYY_MM_DD, "1970-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return parseDateToStr(YYYY_MM_DD, calendar.getTime());
    }

    /**
     * 计算 1970-01-01 到 规定日期的天数
     */
    public static Integer betweenDays(Date endDate) {

        Date dateStart = dateTime(YYYY_MM_DD, "1970-01-01");
        long betweenDays = (endDate.getTime() - dateStart.getTime()) / (1000L * 3600L * 24L);
        return (int) betweenDays;
    }

    /**
     * 传入分钟  返回时分秒 HH:mm:ss
     */
    public static String getHourToSeconds(Integer time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time * 60 * 1000l);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String returnFormat = formatter.format(date);
        return returnFormat;
    }

    /**
     * 传入HH:mm:ss  获取 分钟数
     */
    public static Integer getMinutesNum(Date nowDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String format = formatter.format(nowDate);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = formatter.parse(format);
        long l = date.getTime() / 1000 / 60;
        return (int) l;
    }

    /**
     * 拼接参数 yyyy-MM-dd HH:mm:ss
     */
    public static Date getDateAndTime(Integer days, Integer time) {
        String daysDate = getDaysString(days);
        String hourToSeconds = getHourToSeconds(time);
        String date = daysDate + " " + hourToSeconds;
        return dateTime(YYYY_MM_DD_HH_MM_SS, date);
    }

    /**
     * 获取当前月第一天日期  天数
     */
    public static Integer getFirstMonthDay() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, 0);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return betweenDays(instance.getTime());
    }

    /**
     * 获取当前月第一天日期  string
     */
    public static String getFirstMonthDayString() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, 0);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, instance.getTime());
    }

    /**
     * 获取当前月最后一天日期  天数
     */
    public static Integer getEndMonthDay() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return betweenDays(instance.getTime());
    }

    /**
     * 获取当前月最后一天日期  string
     */
    public static String getEndMonthDayString() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, instance.getTime());
    }

    public static Date getEndMonthDayDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    /**
     * 获取下个月日期  yyyyMM
     */
    public static String getPerFirstDayOfMonth() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }

    /**
     * 获取指定日期的下个月日期  yyyyMM
     */
    public static String getPerFirstDayOfMonth(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        // 设置日期为传入参数
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }

    /**
     * 传入时间 小时，天，周，月，季度，年 加减  返回
     * 一天开始时间
     * 一周开始时间
     * 一月开始时间
     * 一季度开始时间
     * 一年开始时间
     */
    public static String getDateType(Date date, String type, Integer number) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (type) {
            case "1"://小时
//                calendar.add(Calendar.HOUR, number);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                break;
            case "2"://天
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.DATE, number);
                break;
            case "3"://周
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//                calendar.add(Calendar.WEDNESDAY, number);
                break;
            case "4"://月
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.MONTH, number);
                break;
            case "5"://季度
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                if (currentMonth >= 1 && currentMonth <= 3) {
                    calendar.set(Calendar.MONTH, 0);
                } else if (currentMonth >= 4 && currentMonth <= 6) {
                    calendar.set(Calendar.MONTH, 3);
                } else if (currentMonth >= 7 && currentMonth <= 9) {
                    calendar.set(Calendar.MONTH, 4);
                } else if (currentMonth >= 10 && currentMonth <= 12) {
                    calendar.set(Calendar.MONTH, 9);
                }
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.MONTH, number*3);
                break;
            case "6"://年
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.YEAR, number);
                break;
        }

        return dft.format(calendar.getTime());
    }

    /**
     * 传入时间 小时，天，周，月，季度，年 加减  返回 时间格式
     * 一天开始时间
     * 一周开始时间
     * 一月开始时间
     * 一季度开始时间
     * 一年开始时间
     */
    public static Date getDateTypeDate(Date date, String type, Integer number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (type) {
            case "1"://小时
//                calendar.add(Calendar.HOUR, number);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                break;
            case "2"://天
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.DATE, number);
                break;
            case "3"://周
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//                calendar.add(Calendar.WEDNESDAY, number);
                break;
            case "4"://月
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.MONTH, number);
                break;
            case "5"://季度
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                if (currentMonth >= 1 && currentMonth <= 3) {
                    calendar.set(Calendar.MONTH, 0);
                } else if (currentMonth >= 4 && currentMonth <= 6) {
                    calendar.set(Calendar.MONTH, 3);
                } else if (currentMonth >= 7 && currentMonth <= 9) {
                    calendar.set(Calendar.MONTH, 4);
                } else if (currentMonth >= 10 && currentMonth <= 12) {
                    calendar.set(Calendar.MONTH, 9);
                }
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.MONTH, number*3);
                break;
            case "6"://年
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.YEAR, number);
                break;
        }

        return calendar.getTime();
    }

    /**
     * 将时间改为开始时间零点
     *
     * @param data
     * @return
     */
    public static Date getZero(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某个时间的的前后N小时、N分钟、N秒钟、N毫秒的时间并转换成String
     *
     * @param data
     * @return
     */
    public static String getDynamicDateToString(Date data, int hour, int minute, int second, int milliSecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(data);
        ca.add(Calendar.HOUR, hour);
        ca.set(Calendar.MINUTE, minute);
        ca.set(Calendar.SECOND, second);
        ca.set(Calendar.MILLISECOND, milliSecond);
        return sdf.format(ca.getTime());
    }

    /**
     * 获取当天半小时间隔的时间字符串数组
     *
     * @return
     */
    public static List<String> getIntervalDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long interval = 30 * 60 * 1000L;
        List<String> list = new ArrayList<>();
        Date zero = getZero(new Date());
        try {
            while (true) {
                if (zero.getTime() > System.currentTimeMillis()) {
                    break;
                }
                list.add(formatter.format(zero));
                zero = new Date(zero.getTime() + interval);
            }
        } catch (Exception e) {
            logger.error("getIntervalDate error", e);
        }
        return list;
    }

    /**
     * 传入时间 分钟,小时，天，周，月，季度，年 加减  返回日期
     */
    public static Date getDateTypeToDate(Date date, String type, Integer number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (type) {
            case "0"://分钟
                calendar.add(Calendar.MINUTE, number);
                break;
            case "1"://小时
                calendar.add(Calendar.HOUR, number);
                break;
            case "2"://天
                calendar.add(Calendar.DATE, number);
                break;
            case "3"://周
                calendar.add(Calendar.WEDNESDAY, number);
                break;
            case "4"://月
                calendar.add(Calendar.MONTH, number);
                break;
            case "5"://季度
                calendar.add(Calendar.MONTH, number * 3);
                break;
            case "6"://年
                calendar.add(Calendar.YEAR, number);
                break;
        }

        return calendar.getTime();
    }

    /**
     * 查询两个日期之间所有月份   yyyyMM - yyyyMM
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<String> getDateList(String startTime, String endTime) {
        List<String> list = new ArrayList<String>();
        try {


            Date startDate = new SimpleDateFormat("yyyyMM").parse(startTime);
            Date endDate = new SimpleDateFormat("yyyyMM").parse(endTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取开始年份和开始月份
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            // 获取结束年份和结束月份
            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            //
            for (int i = startYear; i <= endYear; i++) {
                String date = "";
                if (startYear == endYear) {
                    for (int j = startMonth; j <= endMonth; j++) {
                        if (j < 9) {
                            date = i + "0" + (j + 1);
                        } else {
                            date = i + "" + (j + 1);
                        }
                        list.add(date);
                    }

                } else {
                    if (i == startYear) {
                        for (int j = startMonth; j < 12; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else if (i == endYear) {
                        for (int j = 0; j <= endMonth; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else {
                        for (int j = 0; j < 12; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(date);
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error("getDateList error,startTime:{},endTime:{}", startTime, endTime, e);
        }
        return list;
    }

    /**
     * 查询两个日期之间所有年份   yyyy - yyyy
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<String> getDateYearList(String startTime, String endTime) {
        List<String> list = new ArrayList<String>();
        try {


            Date startDate = new SimpleDateFormat("yyyy").parse(startTime);
            Date endDate = new SimpleDateFormat("yyyy").parse(endTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取开始年份和开始月份
            int startYear = calendar.get(Calendar.YEAR);
            // 获取结束年份和结束月份
            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            //
            for (int i = startYear; i <= endYear; i++) {
                list.add(String.valueOf(i));
            }
        } catch (Exception e) {
            logger.error("getDateYearList error,startTime:{},endTime:{}", startTime, endTime, e);

        }
        return list;
    }

    /**
     * 返回当前年
     */
    public static int getTodayYear() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        return ca.get(Calendar.YEAR);
    }

    /**
     * 返回当前月份之前的月份  汉字
     */
    public static List<String> getMonthList(int year) {
        int todayYear = getTodayYear();
        String[] num_lower = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayYear == year ? new Date() : getYearLast(year));
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 1; i <= month; i++) {
            list.add(num_lower[i - 1] + "月");
        }
        return list;
    }

    /**
     * 返回当前月份之前的月份  数字
     */
    public static List<String> getMonthNumberList(int year) {
        int todayYear = getTodayYear();
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayYear == year ? new Date() : getYearLast(year));
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 1; i <= month; i++) {
            list.add(i + "月");
        }
        return list;
    }

    /**
     * 返回当前日期返回本月天列表  数字
     */
    public static List<String> getDayNumberList() {
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= day; i++) {
            list.add(i + "日");
        }
        return list;
    }

    /**
     * 查询当前日期返回本天时列表  数字
     */
    public static List<String> getHourNumberList() {
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        for (int i = 0; i <= hour; i++) {
            list.add(i + "时");
        }
        return list;
    }

    /**
     * 判断是否是整点
     */
    public static boolean isFullDian(Date date) {
        boolean flag = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断两个时间是否相等  小时
     */
    public static boolean isEqalsDate(Date one, Date two) {
        boolean flag = false;
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH");
        String oneTime = dft.format(one);
        String twoTime = dft.format(two);
        if (oneTime.equals(twoTime)) {
            flag = true;
        }

        return flag;
    }

    /**
     * 判断两个时间是否相等  天
     */
    public static boolean isEqalsDayDate(Date one, Date two) {
        boolean flag = false;
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String oneTime = dft.format(one);
        String twoTime = dft.format(two);
        if (oneTime.equals(twoTime)) {
            flag = true;
        }

        return flag;
    }

    /**
     * 查询两个日期之间所有日期 分钟
     */
    public static List<String> getMinutes(Date startTime, Date endTime) {

        // 返回的日期集合
        List<String> minutes = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(startTime);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endTime);
        while (tempStart.before(tempEnd)) {
            minutes.add(dateFormat.format(tempStart.getTime()).substring(11, 16));
            tempStart.add(Calendar.MINUTE, 1);
        }

        return minutes;
    }

    /**
     * 查询两个日期之间所有日期 分钟
     */
    public static List<String> getMinutesList(Date startTime, Date endTime, Integer number) {

        // 返回的日期集合
        List<String> minutes = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(startTime);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endTime);
        while (tempStart.before(tempEnd)) {
            minutes.add(dateFormat.format(tempStart.getTime()).substring(11, 16));
            tempStart.add(Calendar.MINUTE, number);
        }

        return minutes;
    }

    /**
     * 获取每隔多少分钟时间点
     */
    public static List<String> getTimeLagMinute(int i) {
        //创建集合存储所有时间点
        ArrayList<String> list = new ArrayList<String>();
        //创建循环，指定间隔五分钟
        for (int h = 0, m = 0; h < 24; m += i) {
            //判断分钟累计到60时清零，小时+1
            if (m >= 60) {
                h++;
                m = 0;
            }
            //判断小时累计到24时跳出循环，不添加到集合
            if (h >= 24) {
                break;
            }
            /*转换为字符串*/
            String hour = String.valueOf(h);
            String minute = String.valueOf(m);

            /*判断如果为个位数则在前面拼接‘0’*/
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            if (minute.length() < 2) {
                minute = "0" + minute;
            }
            //拼接为HH:mm格式，添加到集合
            list.add(hour + ":" + minute);
        }

        return list;
    }
}
