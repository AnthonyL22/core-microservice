package com.pwc.core.framework.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.reflect.Field;

public class CustomExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    /**
     * Should exclusion skip the field.
     *
     * @param fieldAttributes field attributes to find
     * @return skip flag
     */
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {

        String fieldName = fieldAttributes.getName();
        Class<?> theClass = fieldAttributes.getDeclaringClass();
        return isFieldInSuperclass(theClass, fieldName);
    }

    /**
     * Verify if a given field exists withing a Java class.
     *
     * @param subclass  class to look inside
     * @param fieldName field to find
     * @return flag if field exists
     */
    private boolean isFieldInSuperclass(Class<?> subclass, String fieldName) {

        Class<?> superclass = subclass.getSuperclass();
        Field field;

        while (superclass != null) {
            field = getField(superclass, fieldName);

            if (field != null) {
                return true;
            }
            superclass = superclass.getSuperclass();
        }
        return false;
    }

    /**
     * Get a given field from a class.
     *
     * @param theClass  class to find field within
     * @param fieldName field to get
     * @return Field found
     */
    private Field getField(Class<?> theClass, String fieldName) {

        try {
            return theClass.getDeclaredField(fieldName);
        } catch (Exception e) {
            return null;
        }
    }
}
