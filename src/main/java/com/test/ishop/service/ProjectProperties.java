package com.test.ishop.service;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProjectProperties {
    private static final Logger LOGGER = Logger.getLogger(ProjectProperties.class);
    private Properties properties;
    private String propertiesFileName;

    public ProjectProperties() {
    }

    public ProjectProperties(Properties properties, String propertiesFileName) {
        this.properties = properties;
        this.propertiesFileName = propertiesFileName;

        try {
            this.properties.load(getClass().getResourceAsStream("/"+propertiesFileName));
        } catch (IOException e) {
            LOGGER.error("Can't open properties file "+propertiesFileName);
        }


    }

    public String getValue(String name) {
        return  properties.getProperty(name);
    }

    public Map<String,String> getAll() {
        Map<String,String> map = new HashMap<>();

        for(Object o: Collections.list(properties.propertyNames())) {
            String name = (String)o;
            map.put(name,properties.getProperty(name));
        }

        return map;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPropertiesFileName() {
        return propertiesFileName;
    }

    public void setPropertiesFileName(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }
}
