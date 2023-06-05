package com.topopixel.library.langchain.java.utils;

import java.util.Map;
import java.util.Optional;

public class LangChainUtils {

    public static String getFromDictOrEnv(Map<String, Object> data, String key, String envKey, Optional<String> defaultValue) {
        if (data.containsKey(key)) {
            return String.valueOf(data.get(key));
        } else {
            return getFromEnv(key, envKey, defaultValue);
        }
    }

    public static String getFromEnv(String key, String envKey, Optional<String> defaultValue) {
        Map<String, String> envMap = System.getenv();
        if (envMap.containsKey(envKey)) {
            return envMap.get(envKey);
        }
        if (defaultValue.isPresent()) {
            return defaultValue.get();
        }

        //TODO: throw exception
        return null;
    }
}
