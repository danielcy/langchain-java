package com.topopixel.library.langchain.java.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;

public class SnakeJsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    static {
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.getFactory().disable(JsonFactory.Feature.INTERN_FIELD_NAMES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
        }
        return null;
    }

    public static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
        }
        return null;
    }

    public static String toPrettyJson(Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
        }
        return null;
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T fromJson(InputStream stream, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(stream, clazz);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T fromJson(byte[] json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T fromJson(byte[] json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(object, clazz);
    }

    public static <T> T convert(Object object, TypeReference<T> type) {
        return OBJECT_MAPPER.convertValue(object, type);
    }
}
