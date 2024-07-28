package org.laziji.sqleverything.conrtoller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.laziji.sqleverything.bean.Response;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.bean.vo.ApiSelectOrNewDbVo;
import org.laziji.sqleverything.service.impl.parser.BaseParser;
import org.laziji.sqleverything.util.ApiUtils;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api")
public class ApiController {


    @PostMapping("listDbs")
    public Response<List<Map<String, Object>>> listDbs() {
        Pattern reg = Pattern.compile("^([a-z0-9]+)-(.+)\\.db$");
        List<Map<String, Object>> dbs = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File("./db").listFiles())) {
            String fileName = file.getName();
            Matcher matcher = reg.matcher(fileName);
            if (matcher.find()) {
                dbs.add(ImmutableMap.of(
                        "id", matcher.group(1),
                        "name", matcher.group(2)
                ));
            }
        }
        return Response.success(dbs);
    }

    @PostMapping("selectOrNewDb")
    public Response<String> selectOrNewDb(@RequestBody ApiSelectOrNewDbVo params) {
        if (StringUtils.isNotBlank(params.getId())) {
            for (File f : Objects.requireNonNull(new File("db").listFiles())) {
                if (f.getName().startsWith(params.getId())) {
                    ApiUtils.setSid(f.getName());
                    return Response.success(params.getId());
                }
            }
            return Response.error("DB不存在");
        }
        String id = UUID.randomUUID().toString().replace("-", "");
        ApiUtils.setSid(id + "-" + params.getName() + ".db");
        DbUtils.execute(ApiUtils.getSid(),
                """
                        create table __file__(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        fileName TEXT,
                        fileType TEXT,
                        tables TEXT
                        )
                        """);
        return Response.success(id);
    }

    @RequestMapping("listFiles")
    public Response<List<JSONObject>> listFiles() {
        List<JSONObject> files = DbUtils.query(ApiUtils.getSid(), "select * from __file__");
        files.forEach(f -> f.put("tables", JSON.parseArray(f.getString("tables"))));
        return Response.success(files);
    }

    @RequestMapping("addFile")
    public Response<Object> addFile(@RequestBody ApiAddFileVo params) {
        BaseParser.getInstance(params.getFileType()).parse(params.getFileName(), params.getFileBase64(), params.getConfig());
        return Response.success();
    }

    @RequestMapping("deleteFile")
    public Response<Object> deleteTable() {
        return Response.success();
    }

    @RequestMapping("query")
    public Response<List<JSONObject>> query(@RequestBody ApiQueryVo params) {
        return Response.success(DbUtils.query(ApiUtils.getSid(), params.getSql()));
    }
}
