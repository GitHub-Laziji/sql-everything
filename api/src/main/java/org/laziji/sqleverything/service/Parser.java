package org.laziji.sqleverything.service;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;

public interface Parser {

    void parse(ApiAddFileVo params);

}
