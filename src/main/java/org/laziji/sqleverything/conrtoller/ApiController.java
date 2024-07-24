package org.laziji.sqleverything.conrtoller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.laziji.sqleverything.bean.Response;
import org.laziji.sqleverything.bean.vo.ApiAddTableVo;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class ApiController {


    @PostMapping("listDbs")
    public Response listDbs() {
        return Response.success(new File("./db").list());
    }

    @PostMapping("selectOrNewDb")
    public Response selectOrNewDb(@RequestBody ApiSelectOrNewDbVo params) {
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
                "create table __record__(" +
                        "id INTEGER AUTOINCREMENT," +
                        "fileName TEXT," +
                        "tables TEXT," +
                        ");");
        return Response.success(id);
    }

    @RequestMapping("listTables")
    public Response listTables() {
        return Response.success(DbUtils.query(ApiUtils.getSid(),"select * from __record__"));
    }

    @RequestMapping("addTable")
    public Response addTable(@RequestBody ApiAddTableVo params) {
        BaseParser.getInstance(params.getFileType()).parse(params.getFileName(), params.getFileBase64(), params.getConfig());
        return Response.success();
    }

//    @RequestMapping("deleteTable")
//    public Response deleteTable() {
//        return Response.success();
//    }

    @RequestMapping("query")
    public Response query(@RequestBody ApiQueryVo params) {
        return Response.success(DbUtils.query(ApiUtils.getSid(), params.getSql()));
    }
}
