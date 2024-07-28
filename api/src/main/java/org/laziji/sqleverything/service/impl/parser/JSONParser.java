package org.laziji.sqleverything.service.impl.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
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
        ApiUtils.insertFileData(fileName, getFileType(), List.of(tableName));
        String data = new String(Base64.getDecoder().decode(fileBase64));
        JSONArray jsonDataOri = JSON.parseArray(data);
        List<JSONObject> jsonData = new ArrayList<>();
        for (int i = 0; i < jsonDataOri.size(); i++) {
            jsonData.add(jsonDataOri.getJSONObject(i));
        }
        Set<String> columns = new HashSet<>();
        for (JSONObject row : jsonData) {
            columns.addAll(row.keySet());
        }
        DbUtils.createTable(ApiUtils.getSid(), tableName, ImmutableList.copyOf(columns));
        DbUtils.insertData(ApiUtils.getSid(), tableName, jsonData);
    }

    @Override
    protected FileType getFileType() {
        return FileType.JSON;
    }
}
