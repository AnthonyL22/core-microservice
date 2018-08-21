package com.pwc.core.framework.util;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectUtilsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void isEmptyTest() {

        String element = "abc";
        Assert.assertFalse(ObjectUtils.isEmpty(element));

    }

    @Test
    public void isEmptyNullTest() {

        Object element = null;
        Assert.assertTrue(ObjectUtils.isEmpty(element));

    }

    @Test
    public void isEmptyIntegerTest() {

        int element = 1234;
        Assert.assertFalse(ObjectUtils.isEmpty(element));

    }

    @Test
    public void isNotEmptyTest() {

        String element = "abc";
        Assert.assertTrue(ObjectUtils.isNotEmpty(element));

    }

    @Test
    public void isNotEmptyNullTest() {

        Object element = null;
        Assert.assertFalse(ObjectUtils.isNotEmpty(element));

    }

    @Test
    public void isNotEmptyIntegerTest() {

        int element = 1234;
        Assert.assertTrue(ObjectUtils.isNotEmpty(element));

    }

    @Test
    public void isNullTest() {

        Assert.assertFalse(ObjectUtils.isNotEmpty(null));

    }

    @Test
    public void isNotNullTest() {

        Assert.assertTrue(ObjectUtils.isEmpty(null));

    }

    @Test
    public void isNotNullExceptionTest() {

        Assert.assertFalse(ObjectUtils.isEmpty(new Exception()));

    }

}
