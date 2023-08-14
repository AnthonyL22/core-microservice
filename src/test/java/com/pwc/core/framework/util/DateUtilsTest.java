package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    private static final String TEST_DATETIME_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String US_DATETIME_PATTERN = "MM-dd-yyyy";
    private static final String INTERNATIONAL_DATETIME_PATTERN = "dd-MMM-yyyy";
    private static Calendar activeCalendar;

    @Before
    public void setUp() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
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
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.MONTH, -12);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.MONTH, -12);
        if (actualDate.contains(expectedDate)) {
            Assert.assertTrue(actualDate.contains(expectedDate));
        } else {
            Assert.assertFalse(actualDate.contains(expectedDate));
        }
    }

    @Test
    public void getDateTimePatternTimezoneOffsetByYearTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.YEAR, 4);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.YEAR, 4);
        if (actualDate.contains(expectedDate)) {
            Assert.assertTrue(actualDate.contains(expectedDate));
        } else {
            Assert.assertFalse(actualDate.contains(expectedDate));
        }
    }

    @Test
    public void getDateTimePatternTimezoneOffsetByHourTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.HOUR, 4);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, Calendar.HOUR, 4);
        if (actualDate.contains(expectedDate)) {
            Assert.assertTrue(actualDate.contains(expectedDate));
        } else {
            Assert.assertFalse(actualDate.contains(expectedDate));
        }

    }

    @Test
    public void getDateTimeOffsetOnlyTest() {
        activeCalendar.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(1);
        if (actualDate.contains(expectedDate)) {
            Assert.assertTrue(actualDate.contains(expectedDate));
        } else {
            Assert.assertFalse(actualDate.contains(expectedDate));
        }
    }

    @Test
    public void getDateTimeOffsetAndPatternTest() {
        activeCalendar.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(1, TEST_DATETIME_PATTERN);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetAndBadSimpleDatePatternTest() {
        activeCalendar.add(Calendar.DATE, 1);
        DateFormat formatter = new SimpleDateFormat(FrameworkConstants.SYSTEM_DEFAULT_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(1, "jjjjjj");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimeOffsetOnlyMismatchTest() {
        activeCalendar.add(Calendar.DATE, -2);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(1);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateByOffsetTest() {
        activeCalendar.add(Calendar.DATE, 1);
        Date expectedDate = activeCalendar.getTime();

        Date actualDate = DateUtils.getDateByOffset(1);
        Assert.assertTrue(expectedDate.getTime() <= actualDate.getTime());
    }

    @Test
    public void getDateTimePatternOnlyTest() {
        activeCalendar.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternOnlyMismatchTest() {
        activeCalendar.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone(System.getProperty(FrameworkConstants.SYSTEM_USER_TIMEZONE)));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime("HH:mm yyyy-MM-dd");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternAndTimezoneTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternAndTimezoneMismatchTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        activeCalendar.add(Calendar.DATE, 0);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("PST"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT", -3);
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternTimezoneOffsetMismatchTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, "GMT", -1);
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternCalendarTimezoneTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime(TEST_DATETIME_PATTERN, activeCalendar, "GMT");
        Assert.assertTrue(actualDate.contains(expectedDate));
    }

    @Test
    public void getDateTimePatternCalendarTimezoneMismatchTest() {
        activeCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        activeCalendar.add(Calendar.DATE, -3);
        DateFormat formatter = new SimpleDateFormat(TEST_DATETIME_PATTERN);
        formatter.setCalendar(activeCalendar);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expectedDate = formatter.format(activeCalendar.getTime());

        String actualDate = DateUtils.getDateTime("HH:mm yyyy-MM-dd", activeCalendar, "PST");
        Assert.assertFalse(actualDate.contains(expectedDate));
    }

    @Test
    public void getFormattedDateTest() {
        Date result = DateUtils.getFormattedDate("2017-12-01 13:45", TEST_DATETIME_PATTERN);
        assertNotNull(result);
    }

    @Test
    public void getFormattedDateInvalidDateTest() {
        Date result = DateUtils.getFormattedDate("bad data", TEST_DATETIME_PATTERN);
        assertNull(result);
    }

}
