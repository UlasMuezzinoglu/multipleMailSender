package com.ulas.springcoretemplate.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ulas.springcoretemplate.exception.CustomException;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import static com.ulas.springcoretemplate.constant.ErrorConstants.TIME_COULD_NOT_CREATED;
import static com.ulas.springcoretemplate.constant.GeneralConstants.MAX;
import static com.ulas.springcoretemplate.constant.GeneralConstants.MIN;

public final class DateUtil {
    public static final int DAY = 24;
    public static final int HOUR = 60;
    public static final int SECOND = 1000;

    public static DayOfWeek getDayOfWeek() {
        return DayOfWeek.from(LocalDateTime.now());
    }

    public static MonthDay getDayOfMonth() {
        return MonthDay.from(LocalDateTime.now());
    }

    public static Date subsDaysFromDate(final int day) {
        return java.sql.Date.from(getCurrentTimeStamp().toInstant().minusSeconds(HOUR * HOUR * DAY * day));
    }

    public static Date subsDaysFromDate(final Date date, final int day) {
        return java.sql.Date.from(date.toInstant().minusSeconds(HOUR * HOUR * DAY * day));
    }

    public static Date addDaysToDate(final Date date, final int day) {
        return java.sql.Date.from(date.toInstant().plusSeconds(DAY * DAY * HOUR * day));
    }

    public static Date addDaysToDate(final int day) {
        return java.sql.Date.from(getCurrentDate().toInstant().plusSeconds(DAY * DAY * HOUR * day));
    }

    public static Date addMinutesToDate2(Date date, int minutes) {
        return DateUtils.addMinutes(date, minutes);
    }

    public static Date addMinutesToDate(final Calendar date, final int minutes) {
        long timeInSecs = date.getTimeInMillis();
        return addMinutesToDate(timeInSecs, minutes);
    }

    public static Date addMinutesToDate(final long timeInSecs, final int minutes) {
        return new Date(timeInSecs + (minutes * DAY * SECOND));
    }

    private static Long dayToMs(final int days) {
        Long result = Long.valueOf(days * DAY * HOUR * HOUR * HOUR);
        return result;
    }

    public static Timestamp addDaysToTimestamp(final Timestamp t1, final int days) throws JsonProcessingException {
        if (days < 0) {
            throw new CustomException(TIME_COULD_NOT_CREATED);
        }
        Long ms = dayToMs(days);
        return new Timestamp(t1.getTime() + ms);
    }

    public static Date convert(final ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date atEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    public static Date atStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Timestamp getCurrentTimeStamp() {
        return new Timestamp(getCurrentDate().getTime());
    }

    public static String getDateFormatted(final Timestamp time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(time);
    }

    public static String currentZonedDateTimeToDateWithPattern() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(getCurrentDate());
    }

    public static String getRandomCode() {
        int random_int = (int) Math.floor(Math.random() * (MAX - MIN + 1) + MIN);
        return Integer.toString(random_int);
    }

    public static Date getCurrentDate() {
        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static Date getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static Date getDescDays(Integer descDay) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, descDay);
        return cal.getTime();
    }

    public static Date getLastWeek() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static Date getLastMonth() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static Date addSecondsToTime(int seconds) {
        return Date.from(getCurrentDate().toInstant().plusSeconds(seconds));
    }

    public static Date addSecondsToTime(Date date, int seconds) {
        return Date.from(date.toInstant().plusSeconds(seconds));
    }

    public static LocalTime getHourAndMinuteOfDate(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return LocalTime.of(hour, minute);
    }
}

