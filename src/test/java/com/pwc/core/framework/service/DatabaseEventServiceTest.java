package com.pwc.core.framework.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseEventServiceTest {

    private static final String SELECT_STATEMENT = "select name from model_recommendation where id=?";
    private static final String SELECT_LIKE_STATEMENT = "select name from model_recommendation where name like ?";
    private static final String SELECT_GENERIC_STATEMENT = "select name from model_recommendation where name field = ?";
    private static final String UPDATE_STATEMENT = "update patent_analysis set resubmit_to_model=? where id=?";

    @Mock
    JdbcTemplate mockJdbcTemplate;

    @Mock
    DataSource mockDataSource;

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockPreparedStatement;

    @Mock
    ResultSet mockResultSet;

    @Mock
    ResultSetMetaData mockResultSetMetaData;

    private DatabaseEventService databaseEventService;
    private DB mockDB;
    private DBCursor mockDBCursor;
    private DBCollection mockDBCollection;

    @Before
    public void setUp() throws SQLException {

        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(5);
        Mockito.when(mockResultSetMetaData.getColumnLabel(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnLabel(2)).thenReturn("patent_name");
        Mockito.when(mockResultSetMetaData.getColumnLabel(3)).thenReturn("inventor");
        Mockito.when(mockResultSetMetaData.getColumnLabel(4)).thenReturn("location");
        Mockito.when(mockResultSetMetaData.getColumnLabel(5)).thenReturn("address");

        Mockito.when(mockResultSet.getInt("id")).thenReturn(1);
        Mockito.when(mockResultSet.getString("patent_name")).thenReturn("foobar");
        Mockito.when(mockResultSet.getString("inventor")).thenReturn("Anthony");
        Mockito.when(mockResultSet.getString("location")).thenReturn("San Diego");
        Mockito.when(mockResultSet.getString("address")).thenReturn("2 Oak Street");
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(mockResultSet.getObject(1)).thenReturn(1);
        Mockito.when(mockResultSet.getObject(2)).thenReturn("foobar");
        Mockito.when(mockResultSet.getObject(3)).thenReturn("Anthony");
        Mockito.when(mockResultSet.getObject(4)).thenReturn("San Diego");
        Mockito.when(mockResultSet.getObject(5)).thenReturn("2 Oak Street");

        when(mockJdbcTemplate.getDataSource()).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        databaseEventService = new DatabaseEventService(mockJdbcTemplate);

        mockDB = mock(DB.class);
        mockDBCursor = mock(DBCursor.class);
        mockDBCollection = mock(DBCollection.class);
    }

    @Test
    public void executeParameterQueryTimestampTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        Timestamp timestamp = new Timestamp(10000);
        Object result = databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {timestamp});
        Assert.assertEquals("", result);
    }

    @Test
    public void executeParameterQueryDateTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        Date date = new Date(1000);
        Object result = databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {date});
        Assert.assertEquals("", result);
    }

    @Test
    public void executeParameterQueryUUIDTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        UUID uuid = UUID.fromString("1-22-333-44-555");
        Object result = databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {uuid});
        Assert.assertEquals("", result);
    }

    @Test
    public void executeParameterQueryIntegerTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getResultSet()).thenReturn(mockResultSet);
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Integer intVersion = new Integer("123");
        List results = (List) databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {intVersion});
        Assert.assertEquals(1, results.size());
        for (Object result : results) {
            List columns = (List) result;
            Assert.assertEquals(5, columns.size());
            Assert.assertEquals(1, columns.get(0));
            Assert.assertEquals("foobar", columns.get(1));
            Assert.assertEquals("Anthony", columns.get(2));
            Assert.assertEquals("San Diego", columns.get(3));
            Assert.assertEquals("2 Oak Street", columns.get(4));
        }
    }

    @Test
    public void executeParameterQueryDoubleTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        Double doubleVersion = new Double("123");
        Object result = databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {doubleVersion});
        Assert.assertEquals("", result);
    }

    @Test
    public void executeParameterQueryBigDecimalTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_GENERIC_STATEMENT)).thenReturn(mockPreparedStatement);
        BigDecimal bd = new BigDecimal("123");
        Object result = databaseEventService.executeParameterQuery(SELECT_GENERIC_STATEMENT, new Object[] {bd});
        Assert.assertEquals("", result);
    }

    @Test
    public void executeQueryMongoTest() throws SQLException {
        BasicDBObject query = new BasicDBObject("docket", "1234");
        when(mockDB.getCollection("Patents")).thenReturn(mockDBCollection);
        when(mockDBCollection.find(query)).thenReturn(mockDBCursor);
        databaseEventService.setMongoDatabaseServiceConnection(mockDB);
        DBCursor result = databaseEventService.executeQuery("Patents", query);
        Assert.assertNotNull(result);
    }

    @Test
    public void executeQueryMongoExceptionTest() throws SQLException {
        DBCursor result = databaseEventService.executeQuery("Patents", null);
        Assert.assertNull(result);
    }

    @Test
    public void executeQuerySQLTemplateBadSQLTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_STATEMENT)).thenReturn(mockPreparedStatement);
        Mockito.doThrow(new NullPointerException()).when(mockPreparedStatement).executeQuery();
        ResultSet resultSet = databaseEventService.executeQuery(SELECT_STATEMENT);
        Assert.assertNull(resultSet);
    }

    @Test
    public void executeParameterQueryResultSetTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_STATEMENT)).thenReturn(mockPreparedStatement);
        Mockito.doReturn(mockResultSet).when(mockPreparedStatement).executeQuery();
        ResultSet resultSet = databaseEventService.executeQuery(SELECT_STATEMENT);
        Assert.assertEquals(1, resultSet.getInt("id"));
        Assert.assertEquals("foobar", resultSet.getString("patent_name"));
    }

    @Test
    public void emptyResultSetExecuteParameterQueryLikeTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_LIKE_STATEMENT)).thenReturn(mockPreparedStatement);
        Object result = databaseEventService.executeParameterQuery(SELECT_LIKE_STATEMENT, new Object[] {"Anthony"});
        Assert.assertEquals("", result);
    }

    @Test
    public void emptyResultSetExecuteParameterQueryObjectExceptionTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_STATEMENT)).thenThrow(new NullPointerException());
        Object result = databaseEventService.executeParameterQuery(SELECT_STATEMENT, new Object[] {1L, "Anthony"});
        Assert.assertEquals("", result);
    }

    @Test
    public void emptyResultSetExecuteParameterQueryObjectIndexExceptionTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_STATEMENT)).thenThrow(new IndexOutOfBoundsException());
        Object result = databaseEventService.executeParameterQuery(SELECT_STATEMENT, new Object[] {1L, "Anthony"});
        Assert.assertEquals("", result);
    }

    @Test
    public void emptyResultSetExecuteParameterQueryObjectTest() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_STATEMENT)).thenReturn(mockPreparedStatement);
        Object result = databaseEventService.executeParameterQuery(SELECT_STATEMENT, new Object[] {1L, "Anthony"});
        Assert.assertEquals("", result);
    }

    @Test
    public void getDatabaseServiceConnectionTest() {
        Connection connection = databaseEventService.getDatabaseServiceConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void executeUpdateTest() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_STATEMENT)).thenReturn(mockPreparedStatement);
        Mockito.doReturn(1).when(mockPreparedStatement).executeUpdate();
        databaseEventService.executeUpdate(UPDATE_STATEMENT);
    }

    @Test
    public void executeUpdateSqlReturnNothingTest() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_STATEMENT)).thenReturn(mockPreparedStatement);
        Mockito.doReturn(0).when(mockPreparedStatement).executeUpdate();
        databaseEventService.executeUpdate(UPDATE_STATEMENT);
    }

    @Test
    public void executeUpdateNullPreparedStatementTest() throws SQLException {
        databaseEventService.executeUpdate(UPDATE_STATEMENT);
    }

    @Test
    public void executeParameterUpdateResultSetTest() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_STATEMENT)).thenReturn(mockPreparedStatement);
        Object result = databaseEventService.executeParameterUpdate(UPDATE_STATEMENT, new Object[] {true, 1L});
        Assert.assertEquals(0, result);
    }

    @Test
    public void executeParameterUpdateBadSqlResultSetTest() throws SQLException {
        String badSQL = "update patent_analysis set where id=?";
        when(mockConnection.prepareStatement(badSQL)).thenReturn(mockPreparedStatement);
        Object result = databaseEventService.executeParameterUpdate(badSQL, new Object[] {true, 1L});
        Assert.assertEquals(0, result);
    }

    @Test
    public void executeParameterUpdateNullPreparedStatementTest() throws SQLException {
        Object result = databaseEventService.executeParameterUpdate(UPDATE_STATEMENT, new Object[] {true, 1L});
        Assert.assertEquals(-1, result);
    }

    @Test
    public void processResultSetOutputTest() {
        List results = (List) databaseEventService.processResultSetOutput(mockResultSet);
        Assert.assertEquals(1, results.size());
        for (Object result : results) {
            List columns = (List) result;
            Assert.assertEquals(5, columns.size());
            Assert.assertEquals(1, columns.get(0));
            Assert.assertEquals("foobar", columns.get(1));
            Assert.assertEquals("Anthony", columns.get(2));
            Assert.assertEquals("San Diego", columns.get(3));
            Assert.assertEquals("2 Oak Street", columns.get(4));
        }
    }


}
