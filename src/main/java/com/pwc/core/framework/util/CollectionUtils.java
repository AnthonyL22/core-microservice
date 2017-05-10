package com.pwc.core.framework.util;

import java.util.List;

public class CollectionUtils {

    /**
     * Get a random sub-list of a given values from a random set in the given <code>List</code> to the end
     *
     * @param listValues base value to get a snippet of
     * @return sub-string of original value
     */
    public static List getRandomSubList(List listValues) {
        int randomEnd = 1 + (int) (Math.random() * listValues.size() - 1);
        return listValues.subList(0, randomEnd);
    }

}
