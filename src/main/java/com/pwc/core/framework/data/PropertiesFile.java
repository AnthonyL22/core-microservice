package com.pwc.core.framework.data;


public enum PropertiesFile {

    LOG4J_PROPERTIES_FILE("log4j.properties"),
    AUTOMATION_PROPERTIES_FILE("automation.properties"),
    DATABASE_PROPERTIES_FILE("database.properties"),
    GRID_PROPERTIES_FILE("grid.properties");

    public String fileName;

    PropertiesFile(String name) {
        fileName = name;
    }

}
