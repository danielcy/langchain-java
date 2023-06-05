package com.topopixel.library.langchain.java.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

    public static Map<String, Object> getParams(Object obj) {
        Map<String, Object> result = new HashMap<>();
        try {
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                result.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException e) {
            // throw exception
        }
        return result;
    }
}
