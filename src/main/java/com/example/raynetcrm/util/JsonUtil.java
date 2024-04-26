package com.example.raynetcrm.util;

import com.google.gson.Gson;

import java.util.Map;

public class JsonUtil {
    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String toJson(Map<String, Object> map) {
        return new Gson().toJson(map);
    }
}
