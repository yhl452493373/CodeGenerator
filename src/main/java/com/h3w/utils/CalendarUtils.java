package com.h3w.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yanghuanglin on 2018/05/28 at 上午11:07
 */
@SuppressWarnings({"Duplicates", "unused", "WeakerAccess"})
public class CalendarUtils {
    public static CalendarUtils INSTANCE = new CalendarUtils();

    private CalendarUtils() {
    }

    public Boolean isLeapYear(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public Integer compareYearMonthDate(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer yearResult = compareYear(thisCalendar, anotherCalendar);
        Integer monthResult = compareMonth(thisCalendar, anotherCalendar);
        Integer dateResult = compareDate(thisCalendar, anotherCalendar);
        if (yearResult.equals(monthResult) && monthResult.equals(dateResult)) {
            return yearResult;
        } else {
            if (yearResult != 0) {
                return yearResult;
            } else {
                if (monthResult != 0) {
                    return monthResult;
                } else {
                    return dateResult;
                }
            }
        }
    }

    public Integer compareYear(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisYear = getYearOf(thisCalendar);
        Integer anotherYear = getYearOf(anotherCalendar);
        if (thisYear > anotherYear) return 1;
        else if (thisYear.equals(anotherYear)) return 0;
        else return -1;
    }

    public Integer compareMonth(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisMonth = getMonthOf(thisCalendar);
        Integer anotherMonth = getMonthOf(anotherCalendar);
        if (thisMonth > anotherMonth) return 1;
        else if (thisMonth.equals(anotherMonth)) return 0;
        else return -1;
    }

    public Integer compareDate(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisDate = getDateOf(thisCalendar);
        Integer anotherDate = getDateOf(anotherCalendar);
        if (thisDate > anotherDate) return 1;
        else if (thisDate.equals(anotherDate)) return 0;
        else return -1;
    }

    private Integer getYearOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.YEAR);
    }

    private Integer getMonthOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.MONTH);
    }

    private Integer getDateOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.DATE);
    }

    public Integer compareYearMonth(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer yearResult = compareYear(thisCalendar, anotherCalendar);
        Integer monthResult = compareMonth(thisCalendar, anotherCalendar);
        if (yearResult.equals(monthResult)) {
            return yearResult;
        } else {
            if (yearResult != 0) {
                return yearResult;
            } else {
                return monthResult;
            }
        }
    }

    public Integer compareHour(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisHour = getHourOf(thisCalendar);
        Integer anotherHour = getHourOf(anotherCalendar);
        if (thisHour > anotherHour) return 1;
        else if (thisHour.equals(anotherHour)) return 0;
        else return -1;
    }

    private Integer getHourOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.HOUR);
    }

    public Integer compareMinute(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisMinute = getMinuteOf(thisCalendar);
        Integer anotherMinute = getMinuteOf(anotherCalendar);
        if (thisMinute > anotherMinute) return 1;
        else if (thisMinute.equals(anotherMinute)) return 0;
        else return -1;
    }

    private Integer getMinuteOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.MINUTE);
    }

    public Integer compareSecond(Calendar thisCalendar, Calendar anotherCalendar) {
        Integer thisSecond = getSecondOf(thisCalendar);
        Integer anotherSecond = getSecondOf(anotherCalendar);
        if (thisSecond > anotherSecond) return 1;
        else if (thisSecond.equals(anotherSecond)) return 0;
        else return -1;
    }

    private Integer getSecondOf(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.setLenient(true);
        return cal.get(Calendar.SECOND);
    }

    public Integer compareMillis(Calendar thisCalendar, Calendar anotherCalendar) {
        return thisCalendar.compareTo(anotherCalendar);
    }

    public Integer calculateDaysBetween(Calendar thisCalendar, Calendar anotherCalendar) {
        return calculateDaysBetween(thisCalendar, anotherCalendar, true).intValue();
    }

    public Double calculateDaysBetween(Calendar thisCalendar, Calendar anotherCalendar, Boolean ignoreHourMinuteSecond) {
        Calendar thisCalendarClone = (Calendar) thisCalendar.clone();
        Calendar anotherCalendarClone = (Calendar) anotherCalendar.clone();
        if (ignoreHourMinuteSecond) {
            setTimeToDayStart(thisCalendarClone);
            setTimeToDayStart(anotherCalendarClone);
        }
        Long thisTime = thisCalendarClone.getTimeInMillis();
        Long anotherTime = anotherCalendarClone.getTimeInMillis();
        return (thisTime - anotherTime) / (24.0 * 6E1 * 6E1 * 1E3);
    }

    public void setTimeToDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public String translateDayCodeToString(Integer dayCode) throws Exception {
        if (dayCode < 1 || dayCode > 7) {
            throw new Exception("每周星期的数字代码只能为1到7");
        }
        switch (dayCode) {
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            case Calendar.SUNDAY:
                return "星期日";
        }
        return null;
    }

    public Integer translateDayStringToCode(String dayString) {
        switch (dayString) {
            case "星期一":
                return Calendar.MONDAY;
            case "星期二":
                return Calendar.TUESDAY;
            case "星期三":
                return Calendar.WEDNESDAY;
            case "星期四":
                return Calendar.THURSDAY;
            case "星期五":
                return Calendar.FRIDAY;
            case "星期六":
                return Calendar.SATURDAY;
            case "星期日":
                return Calendar.SUNDAY;
        }
        return null;
    }

    public void setDateToMonthStart(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setTimeToDayStart(calendar);
    }

    /**
     * 将日期字符串解析到Calendar中
     *
     * @param dateString        日期字符串
     * @param simpleDateFormats 用于解析日期字符串的日期格式
     */
    public Calendar parseToCalendar(String dateString, SimpleDateFormat... simpleDateFormats) {
        Calendar calendar = Calendar.getInstance();
        for (SimpleDateFormat simpleDateFormat : simpleDateFormats) {
            try {
                calendar.setTime(simpleDateFormat.parse(dateString));
            } catch (ParseException ignored) {
            }
        }
        return calendar;
    }
}
