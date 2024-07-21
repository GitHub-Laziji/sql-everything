package org.laziji.sqleverything.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.service.DbService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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
            e.printStackTrace();
        }
    }

    @Override
    public void insertData(String sid, String tableName, List<JSONObject> data) {
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
                                columns.stream().map(this::escapeName).collect(Collectors.joining(",")),
                                values.stream().map(c -> "'" + c + "'").collect(Collectors.joining(","))
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropTable(String sid, String tableName) {
        try (Connection conn = getConnection(sid)) {
            conn.createStatement().execute(
                    String.format("drop table %s;", escapeName(tableName))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
