package com.test.ishop.config;

import java.util.Map;

public class FakeData {
    private Map<String,String> map;
    private String message;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FakeData{" +
                "map=" + map +
                ", message='" + message + '\'' +
                '}';
    }
}
