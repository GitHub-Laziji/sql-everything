package org.laziji.sqleverything.service.impl.parser;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.consts.FileType;
import org.springframework.stereotype.Service;

@Service
public class JSONParser extends BaseParser {


    @Override
    public void parse(String fileName, String fileBase64, JSONObject config) {

    }

    @Override
    protected FileType getFileType() {
        return FileType.JSON;
    }
}
