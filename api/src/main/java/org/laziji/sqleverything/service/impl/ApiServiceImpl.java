package org.laziji.sqleverything.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.po.TablePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.bean.vo.ApiDeleteFileVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.bean.vo.ApiSelectOrNewDbVo;
import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.service.ApiService;
import org.laziji.sqleverything.service.impl.parser.BaseParserService;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl implements ApiService {

    @Override
    public String getSid() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return Objects.toString(request.getSession().getAttribute("SID"));
    }

    @Override
    public void insertFileData(FilePo file) {
        DbUtils.insertData(getSid(), "__file__", List.of(new JSONObject(ImmutableMap.of(
                "fileName", file.getFileName(),
                "fileType", file.getFileType().name(),
                "alias", file.getAlias(),
                "config", JSON.toJSONString(file.getConfig()),
                "tables", JSON.toJSONString(file.getTables())
        ))));
    }


    @Override
    public List<DbDto> listDbs() {
        return DbUtils.listDbs();
    }

    @Override
    public String selectOrNewDb(ApiSelectOrNewDbVo params) {
        if (StringUtils.isNotBlank(params.getId())) {
            for (DbDto db : DbUtils.listDbs()) {
                if (db.getId().equals(params.getId())) {
                    setSid(db.getFileName());

                    return params.getId();
                }
            }
            throw new RuntimeException("DB不存在");
        }
        DbDto db = DbUtils.createDb(params.getName());
        DbUtils.execute(db.getFileName(),
                """
                        create table __file__(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        fileName TEXT,
                        fileType TEXT,
                        alias TEXT,
                        config TEXT,
                        tables TEXT
                        )
                        """);
        setSid(db.getFileName());
        return db.getId();
    }

    @Override
    public List<FilePo> listFiles() {
        return queryAllFileData();
    }

    @Override
    public void addFile(ApiAddFileVo params) {
        BaseParserService.getInstance(params.getFileType()).parse(params);
    }

    @Override
    public void deleteTable(ApiDeleteFileVo params) {
        for (TablePo table : queryFileData(params.getId()).getTables()) {
            DbUtils.dropTable(getSid(), table.getTableName());
        }
    }

    @Override
    public List<JSONObject> query(ApiQueryVo params) {
        return DbUtils.query(getSid(), params.getSql());
    }

    private void setSid(String sid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("SID", sid);
    }

    private List<FilePo> queryAllFileData() {
        return DbUtils.query(getSid(), "select * from __file__").stream().map(o -> {
            FilePo file = new FilePo();
            file.setId(o.getLong("id"));
            file.setFileName(o.getString("fileName"));
            file.setFileType(FileType.valueOf(o.getString("fileType")));
            file.setAlias(o.getString("alias"));
            file.setConfig(JSON.parseObject(o.getString("config")));
            file.setTables(JSON.parseArray(o.getString("tables"), TablePo.class));
            return file;
        }).collect(Collectors.toList());
    }

    private FilePo queryFileData(Long id) {
        return queryAllFileData().stream().filter(o -> Objects.equals(o.getId(), id)).findFirst().orElse(null);
    }
}
