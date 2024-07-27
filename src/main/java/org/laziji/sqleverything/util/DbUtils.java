package org.laziji.sqleverything.util;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbUtils {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable(String sid, String tableName, List<String> columnNames) {
        try (Connection conn = getConnection(sid)) {
            String ddl = String.format("create table %s ( %s );",
                    escapeName(tableName),
                    columnNames.stream().map(c -> escapeName(c) + " text").collect(Collectors.joining(","))
            );
            conn.createStatement().execute(ddl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertData(String sid, String tableName, List<JSONObject> data) {
        try (Connection conn = getConnection(sid)) {
            for (JSONObject row : data) {
                List<String> columns = new ArrayList<>();
                List<Object> values = new ArrayList<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    columns.add(entry.getKey());
                    values.add(entry.getValue());
                }
                conn.createStatement().execute(
                        String.format("insert into %s ( %s ) values ( %s );",
                                escapeName(tableName),
                                columns.stream().map(DbUtils::escapeName).collect(Collectors.joining(",")),
                                values.stream().map(DbUtils::escapeValue).collect(Collectors.joining(","))
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dropTable(String sid, String tableName) {
        try (Connection conn = getConnection(sid)) {
            conn.createStatement().execute(
                    String.format("drop table %s;", escapeName(tableName))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<JSONObject> query(String sid, String sql) {
        try (Connection conn = getConnection(sid)) {
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<JSONObject> result = new ArrayList<>();
            while (resultSet.next()) {
                JSONObject row = new JSONObject();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                result.add(row);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void execute(String sid, String sql) {
        try (Connection conn = getConnection(sid)) {
            conn.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String sid) throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:db/%s", sid));
    }

    private static String escapeName(String name) {
        return name;
    }

    private static String escapeValue(Object value) {
        return "'" + value.toString() + "'";
    }

}
