package com.pwc.core.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pwc.logging.service.LoggerService.LOG;

public class CollectionUtils {

    /**
     * Get a random sub-list of a given values from a random set in the given <code>List</code> to the end
     *
     * @param listValues base list to randomize
     * @return random List of original values
     */
    public static List getRandomSubList(List listValues) {

        try {
            Random random = new Random();
            int randomBegin = 1 + (int) (random.nextDouble() * listValues.size() - 1);
            if ((listValues.subList(0, randomBegin)).isEmpty()) {
                return listValues;
            } else {
                return listValues.subList(0, randomBegin);
            }
        } catch (Exception e) {
            LOG(true, "Unable to get random list due to reason='%s'", e);
            return listValues;
        }
    }

    /**
     * Get a random sub-set of a given values from a random set in the given <code>Set</code> to the end
     *
     * @param setValues base set to randomize
     * @return random Set of original values
     */
    public static Set getRandomSet(Set setValues) {

        try {
            Random random = new Random();
            int randomBegin = 1 + (int) (random.nextDouble() * setValues.size() - 1);
            return (Set) setValues.stream().limit(randomBegin).collect(Collectors.toSet());
        } catch (Exception e) {
            LOG(true, "Unable to get random set due to reason='%s'", e);
            return setValues;
        }
    }

    /**
     * Remove items from the list with a list of blacklist items.
     *
     * @param sourceList    source list to remove items from
     * @param itemsToRemove items to remove from sourceList by their value
     * @return cleaned list based on blacklisted items
     */
    public static List removeBlacklistedItems(List sourceList, final List<String> itemsToRemove) {

        List cleanList = new ArrayList();
        IntStream.range(0, sourceList.size()).forEach(index -> {
            itemsToRemove.forEach(blacklistedItem -> {
                if (!StringUtils.equalsIgnoreCase(String.valueOf(sourceList.get(index)), blacklistedItem)) {
                    cleanList.add(sourceList.get(index));
                }
            });
        });
        return cleanList;
    }

}
