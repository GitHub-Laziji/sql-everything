package org.laziji.sqleverything.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.po.TablePo;
import org.laziji.sqleverything.consts.FileType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApiUtils {


    public static void setSid(String sid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("SID", sid);
    }

    public static String getSid() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return Objects.toString(request.getSession().getAttribute("SID"));
    }

    public static DbDto initDb(String name) {
        DbDto db = DbUtils.createDb(name);
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
        return db;
    }

    public static void insertFileData(FilePo file) {
        DbUtils.insertData(getSid(), "__file__", List.of(new JSONObject(ImmutableMap.of(
                "fileName", file.getFileName(),
                "fileType", file.getFileType().name(),
                "alias", file.getAlias(),
                "config", JSON.toJSONString(file.getConfig()),
                "tables", JSON.toJSONString(file.getTables())
        ))));
    }

    public static List<FilePo> queryAllFileData() {
        return DbUtils.query(ApiUtils.getSid(), "select * from __file__").stream().map(o -> {
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

    public static FilePo queryFileData(Long id) {
        return queryAllFileData().stream().filter(o -> Objects.equals(o.getId(), id)).findFirst().orElse(null);
    }

}
