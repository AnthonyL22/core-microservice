package com.pwc.core.framework.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CollectionUtilsTest {

    private static final List<String> CITYS_LIST = new ArrayList<>(
            Arrays.asList("Flagler", "San Diego", "Auckland", "Hatsfield Beach", "Matakana", "Joshua Tree"));

    @Test
    public void getRandomSubListTest() {
        List result = CollectionUtils.getRandomSubList(CITYS_LIST);
        assertTrue(result.size() > 0);
    }

}
