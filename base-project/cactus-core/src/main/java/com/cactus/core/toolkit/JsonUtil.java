package com.cactus.core.toolkit;

import com.cactus.core.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * json格式化工具
 * @author lht
 * @date 2021/6/22 2:27 下午
 */
public class JsonUtil {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = createObjectMapper();

    /**
     * 创建一个自定义的JSON ObjectMapper
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    /**
     * 将对象转换为byte数组
     */
    public static byte[] transferToByte(Object val){
        try {
            return DEFAULT_OBJECT_MAPPER.writeValueAsBytes(val);
        } catch (JsonProcessingException e) {
            throw new GlobalException("对象转换为byte数组异常",e);
        }
    }

    /**
     * 将对象转换为JSON字符串
     */
    public static String transferToJson(Object val){
        try {
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            throw new GlobalException("对象转换为json串异常",e);
        }
    }

//    /**
//     * 将对象转换为JSON字符串
//     */
//    public static <T> String transferToJson(T value){
//        StringWriter sw = new StringWriter();
//        JsonGenerator gen = null;
//        try {
//            gen = JSON_FACTORY.createGenerator(sw);
//            DEFAULT_OBJECT_MAPPER.writeValue(gen, value);
//            return sw.toString();
//        } catch (IOException e) {
//            throw new GlobalException("对象转换为json串异常",e);
//        } finally {
//            if (gen != null) {
//                try {
//                    gen.close();
//                } catch (IOException e) {
//                }
//
//            }
//        }
//    }

    /**
     * 将JSON字符串转换为指定对象
     */
    public static <T> T transferToObj(String jsonString, Class<T> valueType) {
        T value = null;
        if(jsonString == null || jsonString.length() == 0) {return value;}
        try {
            value = DEFAULT_OBJECT_MAPPER.readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new GlobalException("json转换对象异常",e);
        }
        return value;
    }

    /**
     * 将JSON字符串转换为指定对象
     */
    public static <T> T transferToObj(String jsonString, TypeReference<T> typeReference) {
        if(jsonString == null || jsonString.length() == 0 || typeReference == null) {
            return null;
        }
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e){
            throw new GlobalException("json转换对象异常",e);
        }

    }

}
