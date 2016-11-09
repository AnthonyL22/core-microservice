package com.pwc.core.framework.controller;

import com.pwc.core.framework.service.DatabaseEventService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseControllerTest {

    @Mock
    Connection mockConnection;

    @Mock
    DataSource mockDataSource;

    @Mock
    DatabaseController mockDatabaseController;

    @Mock
    AbstractApplicationContext mockAbstractApplicationContext;

    @Mock
    DatabaseEventService mockDatabaseEventService;

    @Mock
    JdbcTemplate mockJdbcTemplate;

    @Before
    public void setUp() throws SQLException {
        mockDatabaseController = new DatabaseController();
        when(mockJdbcTemplate.getDataSource()).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockAbstractApplicationContext.getBean("jdbcTemplate")).thenReturn(mockJdbcTemplate);
        when(mockDatabaseEventService.getDatabaseServiceConnection()).thenReturn(mockConnection);
    }

    @Test
    public void establishDatabaseConnectionTest() throws SQLException {
        mockDatabaseController.establishDatabaseConnection(mockAbstractApplicationContext);
        Assert.assertEquals(mockConnection, mockJdbcTemplate.getDataSource().getConnection());
        Assert.assertEquals(mockDataSource, mockJdbcTemplate.getDataSource());
    }

    @Test
    public void closeDatabaseConnectionTest() throws SQLException {
        Assert.assertFalse(mockConnection.isClosed());
        when(mockConnection.isClosed()).thenReturn(true);
        mockDatabaseController.setDatabaseEventService(mockDatabaseEventService);
        mockDatabaseController.closeDatabaseConnection();
        Assert.assertTrue(mockConnection.isClosed());
    }

    @Test(expected = AssertionError.class)
    public void failedCloseDatabaseConnectionTest() throws SQLException {
        mockDatabaseController.setDatabaseEventService(mockDatabaseEventService);
        doThrow(new SQLException("Unable to establish connection")).when(mockConnection).close();
        mockDatabaseController.closeDatabaseConnection();
    }

}
