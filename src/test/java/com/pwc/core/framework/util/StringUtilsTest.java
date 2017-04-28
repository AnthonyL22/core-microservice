package com.pwc.core.framework.util;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.pwc.core.framework.FrameworkConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StringUtilsTest {

    @Test
    public void clobToStringNullTest() {
        String clobToStringConversion = StringUtils.clobToString(null);
        Assert.assertEquals(clobToStringConversion, "");
    }

    @Test
    public void toJSONTest() {
        String result = StringUtils.toJSON(FrameworkConstants.class, null);
        Assert.assertEquals(result, "\"com.pwc.core.framework.FrameworkConstants\"");
    }

    @Test
    public void toJSONWithFeaturesTest() {
        List<ConfigFeature> jsonFeatures = new ArrayList<>();
        jsonFeatures.add(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        String result = StringUtils.toJSON(FrameworkConstants.class, jsonFeatures);
        Assert.assertEquals(result, "\"com.pwc.core.framework.FrameworkConstants\"");
    }

    @Test
    public void objectFromJSONTest() {
        Car myTestCar = new Car();
        myTestCar.setColor("red");
        myTestCar.setNumberOfTires(4);
        String carJson = StringUtils.toJSON(myTestCar, null);
        Car result = (Car) StringUtils.objectFromJSON(Car.class, carJson);
        Assert.assertEquals(result.getColor(), "red");
        Assert.assertEquals(result.getNumberOfTires(), 4);
    }

}
