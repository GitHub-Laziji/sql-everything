package org.laziji.sqleverything.service.impl.parser;

import com.alibaba.fastjson2.JSONObject;
import com.opencsv.CSVReader;
import org.laziji.sqleverything.bean.po.ColumnPo;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.po.TablePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.consts.ColumnType;
import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.service.ApiService;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class CsvParserService extends BaseParserService {

    @Autowired
    private ApiService apiService;

    @Override
    public void parse(ApiAddFileVo params) {
        Map<String, ColumnPo> columns = new HashMap<>();
        List<JSONObject> jsonData = new ArrayList<>();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(params.getFileBase64()));
             InputStreamReader ir = new InputStreamReader(bis);
             CSVReader reader = new CSVReader(ir)
        ) {
            String[] nextLine;
            int r = 0;
            String[] columnArr = {};
            while ((nextLine = reader.readNext()) != null) {
                if (r == 0) {
                    columnArr = nextLine;
                    for (String k : nextLine) {
                        columns.put(k, new ColumnPo(k, ColumnType.TEXT));
                    }
                } else {
                    JSONObject row = new JSONObject();
                    for (int i = 0; i < nextLine.length && i < columnArr.length; i++) {
                        row.put(columnArr[i], nextLine[i]);
                    }
                    jsonData.add(row);
                }
                r++;
            }

        } catch (Exception e) {
            throw new RuntimeException("解析失败");
        }
        TablePo table = new TablePo(params.getAlias(), new ArrayList<>(columns.values()));
        apiService.insertFileData(new FilePo(params.getFileName(), getFileType(), params.getAlias(), params.getConfig(), List.of(table)));
        DbUtils.createTable(apiService.getSid(), table);
        DbUtils.insertData(apiService.getSid(), params.getAlias(), jsonData);
    }

    @Override
    protected FileType getFileType() {
        return FileType.CSV;
    }
}
