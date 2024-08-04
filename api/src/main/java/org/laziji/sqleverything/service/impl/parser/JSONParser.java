package org.laziji.sqleverything.service.impl.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.laziji.sqleverything.bean.po.ColumnPo;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.po.TablePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.consts.ColumnType;
import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.util.ApiUtils;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JSONParser extends BaseParser {


    @Override
    public void parse(ApiAddFileVo params) {
        String data = new String(Base64.getDecoder().decode(params.getFileBase64()));
        JSONArray jsonDataOri = JSON.parseArray(data);
        List<JSONObject> jsonData = new ArrayList<>();
        for (int i = 0; i < jsonDataOri.size(); i++) {
            jsonData.add(jsonDataOri.getJSONObject(i));
        }
        Map<String, ColumnPo> columns = new HashMap<>();
        for (JSONObject row : jsonData) {
            row.keySet().forEach(k -> columns.put(k, new ColumnPo(k, ColumnType.TEXT)));
        }
        TablePo table = new TablePo(params.getAlias(), new ArrayList<>(columns.values()));

        ApiUtils.insertFileData(new FilePo(params.getFileName(), getFileType(), params.getAlias(), params.getConfig(), List.of(table)));
        DbUtils.createTable(ApiUtils.getSid(), table);
        DbUtils.insertData(ApiUtils.getSid(), params.getAlias(), jsonData);
    }

    @Override
    protected FileType getFileType() {
        return FileType.JSON;
    }
}
