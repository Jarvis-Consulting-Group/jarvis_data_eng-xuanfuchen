package ca.jrvs.apps.twitter.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUtil {

    /**
     * Convert a Java object to JSON string
     * @param object input object
     * @param prettyJson
     * @param includeNullValues
     * @return JSON String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
            throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        if(!includeNullValues){
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if(prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return m.writeValueAsString(object);
    }

    /**
     * Parse JSON string to a corresponding object
     * @param json JSON string
     * @param clazz Object class
     * @return Object
     * @param <T> Object Type
     * @throws IOException
     */
    public static <T> T toObjectFromJson(String json, Class clazz)
            throws IOException {
        ObjectMapper m = new ObjectMapper();
        return (T) m.readValue(json, clazz);
    }


}
