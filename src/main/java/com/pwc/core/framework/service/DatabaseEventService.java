package com.pwc.core.framework.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import static com.pwc.logging.service.LoggerService.LOG;

public class DatabaseEventService {

    private Connection databaseServiceConnection;
    private DB mongoDatabaseServiceConnection;

    public DatabaseEventService(JdbcTemplate jdbcTemplate) {
        try {
            if (databaseServiceConnection == null) {
                databaseServiceConnection = jdbcTemplate.getDataSource().getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseEventService(MongoTemplate mongoTemplate) {
        try {
            if (mongoDatabaseServiceConnection == null) {
                mongoDatabaseServiceConnection = mongoTemplate.getDb();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute a parameter query that takes an array of parameters.
     *
     * @param sqlTemplateQuery   SQL query to run, with ? placeholders for parameters
     * @param valuesToSubstitute Array of parameters to replace in query
     * @return returns a <code>PreparedStatement</code> to then be processed by calling DatabaseController method
     */
    private PreparedStatement executeQuery(final String sqlTemplateQuery, final Object[] valuesToSubstitute) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseServiceConnection.prepareStatement(sqlTemplateQuery);
            preparedStatement = substituteQueryValues(preparedStatement, sqlTemplateQuery, valuesToSubstitute);
            preparedStatement.executeQuery();
        } catch (final SQLException e) {
            e.printStackTrace();
            LOG("Error executing QUERY", e);
        }
        return preparedStatement;
    }

    /**
     * Execute a MongoDB query for collections.
     *
     * @param collectionName MongoDB collection name
     * @param query          MongoDB Query
     * @return DBCursor database result cursor
     */
    public DBCursor executeQuery(final String collectionName, final BasicDBObject query) {
        DBCursor resultSet = null;
        try {
            resultSet = mongoDatabaseServiceConnection.getCollection(collectionName).find(query);
        } catch (final Exception e) {
            e.printStackTrace();
            LOG("Error executing QUERY", e);
        }
        return resultSet;
    }

    /**
     * Execute a standard query and returns a single <code>String</code> result value.
     *
     * @param sqlTemplateQuery SQL query to run, with no parameter placeholders
     * @return returns a single <code>ResultSet</code>
     */
    public ResultSet executeQuery(final String sqlTemplateQuery) {
        ResultSet resultSet = null;
        try {
            final PreparedStatement preparedStatement = databaseServiceConnection.prepareStatement(sqlTemplateQuery);
            resultSet = preparedStatement.executeQuery();
        } catch (final Exception e) {
            e.printStackTrace();
            LOG("Error executing QUERY", e);
        }
        return resultSet;
    }

    /**
     * Execute a parameter query that takes an array of parameters and returns a single
     * <code>String</code> result value.
     *
     * @param sqlTemplateQuery   SQL query to run, with ? placeholders for parameters
     * @param valuesToSubstitute Array of parameters to replace in query
     * @return returns a single <code>String</code> result value
     */
    public Object executeParameterQuery(final String sqlTemplateQuery, final Object[] valuesToSubstitute) {
        try {
            if (StringUtils.startsWith(sqlTemplateQuery, "update") || StringUtils.startsWith(sqlTemplateQuery, "delete") || StringUtils.startsWith(sqlTemplateQuery, "insert")) {
                return executeParameterUpdate(sqlTemplateQuery, valuesToSubstitute);
            } else {
                PreparedStatement preparedStatement = executeQuery(sqlTemplateQuery, valuesToSubstitute);
                ResultSet resultSet = preparedStatement.getResultSet();
                ResultSetMetaData rsmd = resultSet.getMetaData();

                List rowsOfData = new ArrayList();
                List columnsOfData = null;

                while (resultSet.next()) {
                    int numCols = rsmd.getColumnCount();
                    if (numCols > 1) {
                        columnsOfData = new ArrayList();
                        for (int i = 1; i <= numCols; i++) {
                            columnsOfData.add(resultSet.getObject(i));
                        }
                        rowsOfData.add(columnsOfData);
                    } else {
                        rowsOfData.add(resultSet.getObject(numCols));
                    }
                }

                if (rowsOfData.size() == 1 && columnsOfData == null) {
                    return rowsOfData.get(0);
                } else {
                    return rowsOfData;
                }

            }
        } catch (final NullPointerException e) {
            return "";
        } catch (final Exception e) {
            e.printStackTrace();
            LOG("Error executing QUERY", e);
        }
        return "";
    }

    /**
     * Execute a parameter query that returns List of Maps.
     *
     * @param sqlTemplateQuery SQL query to run, with ? placeholders for parameters
     * @param includeColumns   include columns flag
     * @return returns a Object of Maps
     */
    public Object executeParameterQueryMap(final String sqlTemplateQuery, final boolean includeColumns) {

        List<Map> rows = new ArrayList<>();
        try {
            if (!StringUtils.startsWith(sqlTemplateQuery, "update") || !StringUtils.startsWith(sqlTemplateQuery, "delete") || !StringUtils.startsWith(sqlTemplateQuery, "insert") && includeColumns) {
                ResultSet resultSet = executeQuery(sqlTemplateQuery);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                while (resultSet.next()) {
                    int numCols = rsmd.getColumnCount();
                    Map row = new HashMap<>(numCols);
                    for (int i = 1; i <= numCols; ++i) {
                        row.put(rsmd.getColumnName(i), resultSet.getObject(i));
                    }
                    rows.add(row);
                }
                return rows;

            } else {
                return executeQuery(sqlTemplateQuery);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            LOG(true, "Error executing QUERY exception=%s", e.getCause());
        }
        return rows;
    }

    /**
     * Executes a parameter SQL update statement given. Normally used for UPDATE, INSERT or DELETE actions.
     *
     * @param sqlTemplateQuery   parameter query to run
     * @param valuesToSubstitute Array of parameters to replace in query
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or
     *      (2) 0 for SQL statements that return nothing or (-1) if an error occurred
     */
    public int executeParameterUpdate(final String sqlTemplateQuery, final Object[] valuesToSubstitute) {
        try {

            PreparedStatement preparedStatement = databaseServiceConnection.prepareStatement(sqlTemplateQuery);
            preparedStatement = substituteQueryValues(preparedStatement, sqlTemplateQuery, valuesToSubstitute);
            return preparedStatement.executeUpdate();
        } catch (final Exception e) {
            e.printStackTrace();
            LOG("Error executing UPDATE", e);
        }
        return -1;
    }

    /**
     * Executes the SQL update statement given. Normally used for UPDATE or INSERT actions without
     * parameters to be substituted.
     *
     * @param sqlTemplateQuery standard query to run
     */
    public void executeUpdate(final String sqlTemplateQuery) {
        try {
            final PreparedStatement preparedStatement = databaseServiceConnection.prepareStatement(sqlTemplateQuery);
            preparedStatement.executeUpdate();
        } catch (final Exception e) {
            e.printStackTrace();
            LOG("Error executing UPDATE", e);
        }
    }

    /**
     * Substitute all ? query arguments.
     *
     * @param preparedStatement  SQL statement
     * @param valuesToSubstitute values to substitute in the query
     * @throws SQLException prepared statement SQL exception
     */
    private PreparedStatement substituteQueryValues(PreparedStatement preparedStatement, final String sqlTemplateQuery, final Object[] valuesToSubstitute) throws SQLException {

        if (valuesToSubstitute != null) {
            for (int i = 0; i < valuesToSubstitute.length; i++) {
                if (valuesToSubstitute[i] instanceof String) {
                    if (StringUtils.contains(sqlTemplateQuery, "like")) {
                        preparedStatement.setString(i + 1, "%" + valuesToSubstitute[i].toString() + "%");
                    } else if (StringUtils.containsIgnoreCase(valuesToSubstitute[i].toString(), "null")) {
                        int sqlType = Integer.parseInt(StringUtils.substringAfter(valuesToSubstitute[i].toString(), ":"));
                        preparedStatement.setNull(i + 1, sqlType);
                    } else {
                        preparedStatement.setString(i + 1, valuesToSubstitute[i].toString());
                    }
                } else if (valuesToSubstitute[i] instanceof java.sql.Timestamp) {
                    final java.sql.Timestamp date = (java.sql.Timestamp) valuesToSubstitute[i];
                    Calendar utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    preparedStatement.setTimestamp(i + 1, date, utcCal);
                } else if (valuesToSubstitute[i] instanceof Date) {
                    Calendar utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    java.sql.Date dateParam = new java.sql.Date(((Date) valuesToSubstitute[i]).getTime());
                    preparedStatement.setDate(i + 1, dateParam, utcCal);
                } else if (valuesToSubstitute[i] instanceof UUID) {
                    preparedStatement.setString(i + 1, valuesToSubstitute[i].toString());
                } else if (valuesToSubstitute[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, Integer.parseInt(valuesToSubstitute[i].toString()));
                } else if (valuesToSubstitute[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, Long.parseLong(valuesToSubstitute[i].toString()));
                } else if (valuesToSubstitute[i] instanceof Double) {
                    preparedStatement.setDouble(i + 1, Double.parseDouble(valuesToSubstitute[i].toString()));
                } else if (valuesToSubstitute[i] instanceof Boolean) {
                    preparedStatement.setBoolean(i + 1, Boolean.valueOf(valuesToSubstitute[i].toString()));
                } else if (valuesToSubstitute[i] instanceof BigDecimal) {
                    preparedStatement.setBigDecimal(i + 1, (BigDecimal) valuesToSubstitute[i]);
                }
            }
        }
        return preparedStatement;
    }

    /**
     * Process a SQL <code>ResultSet</code> object into a list or single object.
     *
     * @param resultSet SQL ResultSet
     * @return processed Result set
     */
    public Object processResultSetOutput(ResultSet resultSet) {

        try {
            List results = new ArrayList();
            Object[] record = null;
            ArrayList list;
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            while (resultSet.next()) {
                record = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    record[i - 1] = resultSet.getObject(i);
                }
                list = new ArrayList(Arrays.asList(record));
                results.add(list);
            }

            if (record != null) {
                if (record.length > 1 || results.size() > 0) {
                    return results;
                } else {
                    return record[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG(true, "Failed to process ResultSet due to exception = %s", e.getMessage());
        }
        return null;
    }

    public Connection getDatabaseServiceConnection() {
        return databaseServiceConnection;
    }

    public void setMongoDatabaseServiceConnection(DB mongoDatabaseServiceConnection) {
        this.mongoDatabaseServiceConnection = mongoDatabaseServiceConnection;
    }

}

