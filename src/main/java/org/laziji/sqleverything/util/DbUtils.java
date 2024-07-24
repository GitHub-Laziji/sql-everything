package org.laziji.sqleverything.util;

import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
            conn.createStatement().execute(
                    String.format("create table %s ( %s );",
                            escapeName(tableName),
                            columnNames.stream().map(c -> escapeName(c) + " text").collect(Collectors.joining(","))
                    )
            );
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
                                values.stream().map(c -> "'" + c + "'").collect(Collectors.joining(","))
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
        return List.of();
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
        return "`" + name.replace("`", "``") + "`";
    }

    private static String escapeValue(Object value) {
        return value.toString();
    }

}
