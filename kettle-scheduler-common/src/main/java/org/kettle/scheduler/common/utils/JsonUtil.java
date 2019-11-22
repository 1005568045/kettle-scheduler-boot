package org.kettle.scheduler.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.exceptions.MyMessageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson序列化工具
 *
 * @author lyf
 */
@Slf4j
public class JsonUtil {

    public final static ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将Object转换为Json字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            String msg = "将Object转换为Json字符串异常";
            log.error(msg, e);
            throw new MyMessageException(msg);
        }
    }

    /**
     * 将json格式字符串转换为Map对象
     */
    public static Map<String, String> parseMap(String jsonStr) {
        try {
            return MAPPER.readValue(jsonStr, new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException e) {
            String msg = "将json格式字符串转换为Map对象异常";
            log.error(msg, e);
            throw new MyMessageException(msg);
        }
    }

    /**
     * 将JSON串解析为Bean对象
     */
    public static <T> T parseBean(String jsonStr, Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            String msg = "将JSON串解析为Bean对象异常";
            log.error(msg, e);
            throw new MyMessageException(msg);
        }
    }

    /**
     * 将Json串转换为 List
     */
    public static <T> List<T> parseList(String jsonStr, Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonStr, new TypeReference<ArrayList<T>>() {
            });
        } catch (IOException e) {
            String msg = "将JSON串解析为List对象异常";
            log.error(msg, e);
            throw new MyMessageException(msg);
        }
    }
}
