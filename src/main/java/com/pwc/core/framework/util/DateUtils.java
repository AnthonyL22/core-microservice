package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    /**
     * Switch the date format from one pattern to another pattern
     *
     * @param sourceDateTime source date/time value
     * @param sourceFormat   Originating date/time Pattern
     * @param targetFormat   Desired date/time Pattern
     * @return switched date/time format in new target format
     */
    public static String switchDateTimeFormat(String sourceDateTime, String sourceFormat, String targetFormat) {
        String result;
        SimpleDateFormat sourceDateFormatter;
        SimpleDateFormat targetDateFormatter;
        try {
            sourceDateFormatter = new SimpleDateFormat(sourceFormat);
            targetDateFormatter = new SimpleDateFormat(targetFormat);
            result = targetDateFormatter.format(sourceDateFormatter.parse(sourceDateTime));
        } catch (Exception e) {
            return sourceDateTime;
        }
        return result;
    }

    /**
     * Utility method which returns a Date and time <code>Date</code> for a specified DATE offset
     *
     * @param dateOffset date and time offset
     * @return dateOffset date and time
     */
    public static Date getDateByOffset(final int dateOffset) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        cal.add(Calendar.DATE, dateOffset);
        return cal.getTime();
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified DATE offset
     *
     * @param dateOffset date and time offset
     * @return dateOffset date and time
     */
    public static String getDateTime(final int dateOffset) {
        return getDateTime(FrameworkConstants.SYSTEM_DEFAULT_DATETIME_PATTERN, System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE), dateOffset);
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified DATE offset
     *
     * @param dateOffset date and time offset
     * @param pattern    date/time pattern
     * @return dateOffset date and time
     */
    public static String getDateTime(final int dateOffset, final String pattern) {
        return getDateTime(pattern, System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE), dateOffset);
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified DATE offset
     *
     * @param pattern date/time pattern
     * @return offset date and time offset
     */
    public static String getDateTime(final String pattern) {
        return getDateTime(pattern, System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE));
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified DATE offset in a
     * given date formatted pattern for a particular TimeZone <code>String</code>
     *
     * @param pattern  date/time pattern
     * @param timeZone timezone to use for date generation
     * @return offset date and time offset
     */
    public static String getDateTime(final String pattern, final String timeZone) {
        return getDateTime(pattern, timeZone, 0);
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified DATE offset in a
     * given date formatted pattern for a particular TimeZone <code>String</code>
     *
     * @param pattern    date/time pattern
     * @param timeZone   timezone to use for date generation
     * @param dateOffset date and time offset
     * @return offset date and time
     */
    public static String getDateTime(final String pattern, final String timeZone, final int dateOffset) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.add(Calendar.DATE, dateOffset);
        return getDateTime(pattern, cal, timeZone);
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified Calendar field type.
     *
     * @param pattern the date/time pattern
     * @param field   the calendar field. Calendar ENUM
     * @param offset  the offset
     * @return formatted calendar time
     */
    public static String getDateTime(final String pattern, final int field, final int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(field, offset);
        return getDateTime(pattern, cal, System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE));
    }

    /**
     * Utility method which returns a Date and time <code>String</code> for a specified Calendar instance.
     *
     * @param pattern  date/time pattern
     * @param calendar calendar instance
     * @param timeZone timezone to use for date generation
     * @return formatted calendar time
     */
    public static String getDateTime(final String pattern, final Calendar calendar, final String timeZone) {
        DateFormat formatter;
        try {
            formatter = new SimpleDateFormat(pattern);
        } catch (Exception e) {
            formatter = new SimpleDateFormat(FrameworkConstants.SYSTEM_DEFAULT_DATETIME_PATTERN);
        }
        formatter.setCalendar(calendar);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.format(calendar.getTime());
    }

}
