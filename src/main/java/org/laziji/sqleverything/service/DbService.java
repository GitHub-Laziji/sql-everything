package org.laziji.sqleverything.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DbService {

    void createTable(String sid, String tableName, List<String> columnNames);

    void insertData(String sid, String tableName, List<JSONObject> data);

    void dropTable(String sid, String tableName);

    List<JSONObject> query(String sid, String sql);
}
