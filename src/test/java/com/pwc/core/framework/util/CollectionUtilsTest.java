package com.pwc.core.framework.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CollectionUtilsTest {

    private static final List<String> BEST_PLACES_ON_EARTH_LIST = new ArrayList<>(Arrays.asList("Flagler", "Hossegor", "Auckland", "Hatsfield Beach", "Matakana", "Gisborne"));

    private static final Set<String> HOMES_SET = new HashSet<>(Arrays.asList("Flagler", "San Diego", "Paris", "Napa", "Fontainebleau"));

    @Test
    public void getRandomSubListTest() {
        List result = CollectionUtils.getRandomSubList(BEST_PLACES_ON_EARTH_LIST);
        assertTrue(result.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void getRandomSubEmptyListTest() {
        List result = CollectionUtils.getRandomSubList(null);
        assertFalse(result.size() > 0);
    }

    @Test
    public void getRandomSetTest() {
        Set<String> result = CollectionUtils.getRandomSet(HOMES_SET);
        assertTrue(result.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void getRandomSetEmptySetTest() {
        Set result = CollectionUtils.getRandomSet(null);
        assertFalse(result.size() > 0);
    }

    @Test
    public void removeBlacklistedItemsTest() {
        List result = CollectionUtils.removeBlacklistedItems(BEST_PLACES_ON_EARTH_LIST, Arrays.asList("Flagler"));
        assertEquals(BEST_PLACES_ON_EARTH_LIST.size() - 1, result.size());
    }

}
