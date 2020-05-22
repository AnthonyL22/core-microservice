package com.pwc.core.framework.controller;

import com.pwc.core.framework.service.DatabaseEventService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

import static com.pwc.assertion.AssertService.assertFail;

@Component
public class DatabaseController {

    private DatabaseEventService databaseEventService;
    private JdbcTemplate jdbcTemplate;
    private MongoTemplate mongoTemplate;

    public DatabaseController() {
    }

    /**
     * Establish database connection in service layer from Spring.
     *
     * @param ctx Application context
     */
    public void establishDatabaseConnection(AbstractApplicationContext ctx) {
        if (jdbcTemplate == null) {
            jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        }
        if (databaseEventService == null) {
            databaseEventService = new DatabaseEventService(jdbcTemplate);
        }
    }

    /**
     * Establish Mongo database connection in service layer from Spring.
     *
     * @param ctx Application context
     */
    public void establishMongoDatabaseConnection(AbstractApplicationContext ctx) {
        if (jdbcTemplate == null) {
            mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
        }
        if (databaseEventService == null) {
            databaseEventService = new DatabaseEventService(mongoTemplate);
        }
    }

    /**
     * Close the current database connection in the service layer.
     */
    public void closeDatabaseConnection() {
        try {
            databaseEventService.getDatabaseServiceConnection().close();
        } catch (final SQLException e) {
            assertFail("FAILED to close database connection due to exception=%s", e.getMessage());
        }
    }

    public DatabaseEventService getDatabaseEventService() {
        return databaseEventService;
    }

    public void setDatabaseEventService(DatabaseEventService databaseEventService) {
        this.databaseEventService = databaseEventService;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
