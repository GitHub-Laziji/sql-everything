package org.laziji.sqleverything.conrtoller;

import org.laziji.sqleverything.bean.Response;
import org.laziji.sqleverything.bean.vo.ApiAddTableVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.service.impl.parser.BaseParser;
import org.laziji.sqleverything.util.ApiUtils;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {


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
