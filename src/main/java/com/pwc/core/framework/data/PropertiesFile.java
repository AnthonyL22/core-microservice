package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertiesFile {

    LOG4J_PROPERTIES_FILE("log4j.properties"),
    AUTOMATION_PROPERTIES_FILE("automation.properties"),
    DATABASE_PROPERTIES_FILE("database.properties"),
    GRID_PROPERTIES_FILE("grid.properties");

    public String fileName;

}
