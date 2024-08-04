package org.laziji.sqleverything.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Table;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.TablePo;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DbUtils {

    private static final Pattern dbFileNameReg = Pattern.compile("^((\\d{13})_([a-z0-9]+))__(.+)\\.db$");

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable(String sid, TablePo table) {
        try (Connection conn = getConnection(sid)) {
            String ddl = String.format("create table %s ( %s );",
                    escapeName(table.getTableName()),
                    table.getColumns().stream().map(e -> escapeName(e.getColumnName()) + " " + e.getColumnType()).collect(Collectors.joining(","))
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

    public static List<DbDto> listDbs() {
        checkDbDir();
        List<DbDto> dbs = new ArrayList<>();
        for (File f : Objects.requireNonNull(new File("db").listFiles())) {
            Matcher matcher = dbFileNameReg.matcher(f.getName());
            if (matcher.find()) {
                DbDto db = new DbDto();
                db.setId(matcher.group(1));
                db.setTimestamp(Long.parseLong(matcher.group(2)));
                db.setName(matcher.group(4));
                db.setFileName(f.getName());
                dbs.add(db);
            }
        }
        dbs.sort(Comparator.comparingLong(DbDto::getTimestamp));
        return dbs;
    }

    public static DbDto createDb(String name) {
        checkDbDir();
        DbDto db = new DbDto();
        db.setTimestamp(System.currentTimeMillis());
        db.setName(name);
        db.setId(db.getTimestamp() + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        db.setFileName(db.getId() + "__" + db.getName() + ".db");
        try {
            if (!new File(new File("db"), db.getFileName()).createNewFile()) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return db;
    }

    private static Connection getConnection(String sid) throws SQLException {
        checkDbDir();
        return DriverManager.getConnection(String.format("jdbc:sqlite:db/%s", sid));
    }

    private static void checkDbDir() {
        File dbDir = new File("db");
        if (!dbDir.exists() && !dbDir.mkdir()) {
            throw new RuntimeException();
        }
        if (dbDir.isFile()) {
            throw new RuntimeException();
        }
    }

    private static String escapeName(String name) {
        return String.format("\"%s\"", name.replace("\"", "\"\""));
    }

    private static String escapeValue(Object value) {
        if (value == null) {
            return "null";
        }
        return String.format("'%s'", value.toString().replace("'", "''"));
    }

}
