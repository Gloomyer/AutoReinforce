package com.gloomyer.auto.config;

import java.util.HashMap;

public class ApkUrlConfig {

    private static ApkUrlConfig ins = new ApkUrlConfig();

    public static ApkUrlConfig get() {
        return ins;
    }

    private HashMap<String, String> urls = new HashMap<>();

    public synchronized void put(String key, boolean sign, String url) {
        urls.put(key + (sign ? "加固包" : "普通包"), url);
    }

    public HashMap<String, String> getMap() {
        return urls;
    }

    @Override
    public String toString() {
        return urls.toString();
    }
}
