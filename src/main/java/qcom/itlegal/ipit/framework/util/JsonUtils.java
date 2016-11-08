package qcom.itlegal.ipit.framework.util;


import com.jayway.restassured.path.json.JsonPath;
import org.json.simple.JSONArray;

import java.util.*;

public class JsonUtils {

    /**
     * Create new <code>JsonPath</code> from JSON String representation
     *
     * @param json String of JSON
     * @return Hydrated JsonPath
     */
    public static JsonPath parseJson(final String json) {
        JsonPath jsonPath = new JsonPath(json);
        return jsonPath;
    }

    /**
     * Create List representation of JSON
     *
     * @param json String of JSON
     * @return List representation of JSON
     */
    public static List<HashMap> getJsonList(final String json) {
        JsonPath jsonPath = new JsonPath(json);
        return jsonPath.getList("");
    }

    /**
     * Create String representation of JSON from a Collection type
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

}
