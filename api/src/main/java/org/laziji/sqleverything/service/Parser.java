package org.laziji.sqleverything.service;

import com.alibaba.fastjson.JSONObject;

public interface Parser {

    void parse(String fileName, String fileBase64, JSONObject config);

}
