package org.laziji.sqleverything.service;

import com.alibaba.fastjson2.JSONObject;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.bean.vo.ApiDeleteFileVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.bean.vo.ApiSelectOrNewDbVo;

import java.util.List;

public interface ApiService {

    String getSid();

    void insertFileData(FilePo file);

    List<DbDto> listDbs();

    String selectOrNewDb(ApiSelectOrNewDbVo params);

    List<FilePo> listFiles();

    void addFile(ApiAddFileVo params);

    void deleteTable(ApiDeleteFileVo params);

    List<JSONObject> query(ApiQueryVo params);
}
