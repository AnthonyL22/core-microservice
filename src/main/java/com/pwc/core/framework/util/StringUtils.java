package com.pwc.core.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Convert a CLOB object to a <code>String</code> representation.
     *
     * @param clobObject java.sql Clob object
     * @return A string representation of Clob
     */
    public static String clobToString(Clob clobObject) {

        String clobAsString = "";
        try {
            InputStream asciiStream = clobObject.getAsciiStream();
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(asciiStream, stringWriter);
            clobAsString = stringWriter.toString();
        } catch (Exception e) {
            return clobAsString;
        }
        return clobAsString;
    }

    /**
     * toJSON - Helper method to convert domain objects into JSON.
     *
     * @param domainObject domain object
     * @param jsonFeatures JSON configuration features
     * @return JSON representation of the Domain object
     */
    public static String toJSON(Object domainObject, List<ConfigFeature> jsonFeatures) {

        String json = null;
        try {
            ObjectMapper om = new ObjectMapper();
            if (jsonFeatures != null) {
                for (ConfigFeature feature : jsonFeatures) {
                    om.configure((MapperFeature) feature, true);
                }
            }
            ObjectWriter ow = om.writer();
            json = ow.writeValueAsString(domainObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * objectFromJSON - Helper method to convert JSON string to domainObject.
     *
     * @param clazz      Class of the resulting object
     * @param jsonString JSON String to be converted
     * @return An object converted from json
     */
    public static Object objectFromJSON(Class clazz, String jsonString) {

        Object retVal = null;

        try {
            ObjectReader or = new ObjectMapper().reader(clazz);
            retVal = or.readValue(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Retrieves a matching part of a given <code>String</code> based on regex pattern and a group.
     *
     * @param regexToMatch regex to match against
     * @param searchable   initial given String
     * @param group        group to match
     * @return matching part of a given String
     */
    public static String getMatchFromRegex(final String regexToMatch, final String searchable, final int group) {

        Pattern pattern = Pattern.compile(regexToMatch);
        Matcher matcher = pattern.matcher(searchable);
        matcher.find();
        String matchedString = matcher.group(group);
        if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(matchedString, "amp;")) {
            matchedString = org.apache.commons.lang3.StringUtils.replace(matchedString, "amp;", "");
            matchedString = org.apache.commons.lang3.StringUtils.replace(matchedString, "\\", "");
        }
        if (!org.apache.commons.lang3.StringUtils.containsIgnoreCase(matchedString, "https")) {
            matchedString = org.apache.commons.lang3.StringUtils.replace(matchedString, "http", "https");
        }
        return matchedString;
    }

}
