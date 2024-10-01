package com.pwc.core.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.pwc.logging.service.LoggerService.LOG;

public class JsonUtils {

    /**
     * Create new <code>JsonPath</code> from JSON String representation.
     *
     * @param json String of JSON
     * @return Hydrated JsonPath
     */
    public static JsonPath parseJson(final String json) {

        JsonPath jsonPath = new JsonPath(json);
        return jsonPath;
    }

    /**
     * Create List representation of JSON.
     *
     * @param json String of JSON
     * @return List representation of JSON
     */
    public static List<HashMap> getJsonList(final String json) {

        JsonPath jsonPath = new JsonPath(json);
        return jsonPath.getList("");
    }

    /**
     * Create String representation of JSON from a Collection type.
     *
     * @param obj List or Set collection object
     * @return String of JSON
     */
    public static String getJSONString(Object obj) {

        String jsonString;
        if (obj instanceof Set) {
            List testableEndpoints = new ArrayList();
            testableEndpoints.addAll((HashSet) obj);
            jsonString = JSONArray.toJSONString(testableEndpoints);
        } else if (obj instanceof List) {
            jsonString = JSONArray.toJSONString(((ArrayList) obj));
        } else {
            jsonString = JSONArray.toJSONString(new ArrayList(Arrays.asList(obj)));
        }
        return jsonString;
    }

    /**
     * Check if Object is valid JSON.
     *
     * @param jsonInString JSON to check
     * @return valid JSON flag
     */
    public static boolean isJSONValid(Object jsonInString) {

        try {
            String json = jsonInString.toString();
            if (!json.contains(":")) {
                return false;
            } else {
                new JsonParser().parse(json);
                return true;
            }
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

    /**
     * Convert a parameter map into valid query parameters for a REST call.
     *
     * @param pathParameter source map to be converted
     * @return query parameter <code>String</code>
     */
    public static String convertMapToQueryParameters(Map<String, String> pathParameter) {

        String decoratedQueryUrl;
        StringBuilder pathParameterUrl = new StringBuilder();
        pathParameter.forEach((key, value) -> {
            pathParameterUrl.append(key);
            pathParameterUrl.append("=");
            pathParameterUrl.append(value);
            pathParameterUrl.append("&");
        });

        decoratedQueryUrl = org.apache.commons.lang3.StringUtils.removeEnd(pathParameterUrl.toString(), "?");
        decoratedQueryUrl = org.apache.commons.lang3.StringUtils.removeEnd(decoratedQueryUrl, "&");
        return decoratedQueryUrl;
    }

    /**
     * Convert a parameter map into valid query parameters for a REST call.
     *
     * @param pathParameter source map to be converted
     * @return query parameter <code>String</code>
     */
    public static String convertMapToQueryParametersEnding(Map<String, String> pathParameter) {

        String decoratedQueryUrl;
        StringBuilder pathParameterUrl = new StringBuilder("?");
        pathParameter.forEach((key, value) -> {
            pathParameterUrl.append(key);
            pathParameterUrl.append("=");
            pathParameterUrl.append(value);
            pathParameterUrl.append("&");
        });

        decoratedQueryUrl = StringUtils.removeEnd(pathParameterUrl.toString(), "&");
        return decoratedQueryUrl;
    }

    /**
     * Convert a HashMap representation of any object to the defined destination type.
     *
     * @param sourceObject    source payload that can be a HashMap or JSONObject
     * @param destinationType destination object type
     * @return hydrated destination object
     */
    public static Object convertJSONToObject(JSONObject sourceObject, Class<?> destinationType) {

        GsonBuilder builder = new GsonBuilder();
        builder.addDeserializationExclusionStrategy(new CustomExclusionStrategy());
        builder.addSerializationExclusionStrategy(new CustomExclusionStrategy());
        Gson gson = builder.create();
        return gson.fromJson(sourceObject.toString(), destinationType);
    }

    /**
     * Convert a HashMap representation of any object to the defined destination type.
     *
     * @param sourceMap       source HashMap
     * @param destinationType destination object type
     * @return hydrated destination object
     */
    public static Object convertMapToObject(HashMap sourceMap, Class<?> destinationType) {

        JSONObject payload = new JSONObject(sourceMap);
        GsonBuilder builder = new GsonBuilder();
        builder.addDeserializationExclusionStrategy(new CustomExclusionStrategy());
        builder.addSerializationExclusionStrategy(new CustomExclusionStrategy());
        Gson gson = builder.create();
        return gson.fromJson(payload.toString(), destinationType);
    }

    /**
     * Convert a POJO Java object to JSON.
     *
     * @param sourceObject source Java object to convert to JSON (POJO)
     * @return hydrated destination object
     */
    public static JSONObject convertObjectToJson(Object sourceObject) {

        JSONObject hydratedJson = null;
        ObjectMapper source = new ObjectMapper();
        try {
            String jsonStr = source.writeValueAsString(sourceObject);
            hydratedJson = new JSONObject(jsonStr);
        } catch (Exception e) {
            LOG(true, "Failed to convert POJO to JSON due to exception=%s", e.getMessage());
        }
        return hydratedJson;
    }

}
