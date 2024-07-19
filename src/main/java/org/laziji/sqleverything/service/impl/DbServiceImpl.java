package org.laziji.sqleverything.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.service.DbService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DbServiceImpl implements DbService {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable(String sid, String tableName, List<String> columnNames) {
        try (Connection conn = getConnection(sid)) {
            conn.createStatement().execute(
                    String.format("create table %s ( %s );",
                            escapeName(tableName),
                            columnNames.stream().map(c -> escapeName(c) + " text").collect(Collectors.joining(","))
                    )
            );
        } catch (Exception e) {

        }
    }

    @Override
    public void insertData(String sid, String tableName, List<JSONObject> data) {

    }

    @Override
    public void dropTable(String sid, String tableName) {

    }

    @Override
    public List<JSONObject> query(String sid, String sql) {
        return List.of();
    }

    private Connection getConnection(String sid) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memdb1?mode=memory&cache=shared");
    }

    private String escapeName(String name) {
        return "`" + name.replace("`", "``") + "`";
    }
    private String escapeValue(Object value) {
        return value.toString();
    }
}
