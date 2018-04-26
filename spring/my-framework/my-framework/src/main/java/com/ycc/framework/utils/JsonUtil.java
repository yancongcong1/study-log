package com.ycc.framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * As a transporter between json and Object.
 *
 * created by ycc at 2018\4\24 0024
 */
public class JsonUtil {
    private static final Logger logger = Logger.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String getString(T object) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("获取json字符串时出错");
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T getObject(String json, Class<T> type) {
        T obj;
        try {
            obj = OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            logger.error("获取json类时出错");
            throw new RuntimeException(e);
        }
        return obj;
    }
}
