package com.test.ishop.config;

import java.util.Map;

public class FakeData {
    private Map<Long,String> map;
    private String message;

    public FakeData() {
    }

    public Map<Long, String> getMap() {
        return map;
    }

    public void setMap(Map<Long, String> map) {
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
