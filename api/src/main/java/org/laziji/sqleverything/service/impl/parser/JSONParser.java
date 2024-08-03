package org.laziji.sqleverything.service.impl.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.util.ApiUtils;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JSONParser extends BaseParser {


    @Override
    public void parse(String fileName, String fileBase64, JSONObject config) {
        String tableName = config.getString("alias");
        String data = new String(Base64.getDecoder().decode(fileBase64));
        JSONArray jsonDataOri = JSON.parseArray(data);
        List<JSONObject> jsonData = new ArrayList<>();
        for (int i = 0; i < jsonDataOri.size(); i++) {
            jsonData.add(jsonDataOri.getJSONObject(i));
        }
        Map<String, String> columns = new HashMap<>();
        for (JSONObject row : jsonData) {
            row.keySet().forEach(k -> columns.put(k, "TEXT"));
        }
        ApiUtils.insertFileData(fileName, getFileType(), config, ImmutableMap.of(tableName, columns));
        DbUtils.createTable(ApiUtils.getSid(), tableName, columns);
        DbUtils.insertData(ApiUtils.getSid(), tableName, jsonData);
    }

    @Override
    protected FileType getFileType() {
        return FileType.JSON;
    }
}
