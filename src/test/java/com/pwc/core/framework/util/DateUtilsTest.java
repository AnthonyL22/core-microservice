package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    final String TEST_DATETIME_PATTERN = "yyyy-MM-dd HH:mm";
    final String US_DATETIME_PATTERN = "MM-dd-yyyy";
    final String INTERNATIONAL_DATETIME_PATTERN = "dd-MMM-yyyy";
    private Calendar cal;

    @Before
    public void setUp() throws SQLException {
        cal = Calendar.getInstance(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
    }

    @Test
    public void switchDateTimeFormatTest() {
        String sourceDate = "06-08-1976";
        String switchedDate = DateUtils.switchDateTimeFormat(sourceDate, US_DATETIME_PATTERN, INTERNATIONAL_DATETIME_PATTERN);
        Assert.assertEquals(switchedDate, "08-Jun-1976");
    }

    @Test
    public void switchDateTimeFormatMismatchSourceFormatVsSourceDateTimeTest() {
        String sourceDate = "06/08/1976";
        String switchedDate = DateUtils.switchDateTimeFormat(sourceDate, US_DATETIME_PATTERN, INTERNATIONAL_DATETIME_PATTERN);
        Assert.assertEquals(switchedDate, "06/08/1976");
    }

    @Test
    public void getDateTimePatternTimezoneOffsetByMonthPastTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.MONTH, -12);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.MONTH, -12);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetByYearTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.YEAR, 4);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.YEAR, 4);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetByHourTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.HOUR, 4);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.HOUR, 4);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetOnlyTest() {
        cal.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(1);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetAndPatternTest() {
        cal.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(1, TEST_DATETIME_PATTERN);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetAndBadSimpleDatePatternTest() {
        cal.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(FrameworkConstants.SYSTEM_DEFAULT_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(1, "jjjjjj");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetOnlyMismatchTest() {
        cal.add(Calendar.DATE, -2);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(1);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateByOffsetTest() {
        cal.add(Calendar.DATE, 1);
        Date expectedDate = cal.getTime();

        Date actualDate = DateUtils.getDateByOffset(1);
        Assert.assertTrue(expectedDate.getTime() <= actualDate.getTime());
    }

    @Test
    public void getDateTimePatternOnlyTest() {
        cal.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternOnlyMismatchTest() {
        cal.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime("HH:mm yyyy-MM-dd");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternAndTimezoneTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternAndTimezoneMismatchTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        cal.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("PST"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT", -3);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetMismatchTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT", -1);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternCalendarTimezoneTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, cal, "GMT");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternCalendarTimezoneMismatchTest() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(cal);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(cal.getTime());

        String actualDate = DateUtils.getDateTime("HH:mm yyyy-MM-dd", cal, "PST");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getFormattedDateTest() {
        Date result = DateUtils.getFormattedDate("2017-12-01 10:45", TEST_DATETIME_PATTERN);
        assertEquals(result, new Date(1512153900000L));
    }

    @Test
    public void getFormattedDateInvalidDateTest() {
        Date result = DateUtils.getFormattedDate("bad data", TEST_DATETIME_PATTERN);
        assertNull(result);
    }

}
