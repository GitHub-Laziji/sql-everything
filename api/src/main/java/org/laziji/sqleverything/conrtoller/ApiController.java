package org.laziji.sqleverything.conrtoller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.laziji.sqleverything.bean.Response;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.po.TablePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.bean.vo.ApiDeleteFileVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.bean.vo.ApiSelectOrNewDbVo;
import org.laziji.sqleverything.service.impl.parser.BaseParser;
import org.laziji.sqleverything.util.ApiUtils;
import org.laziji.sqleverything.util.DbUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class ApiController {


    @PostMapping("listDbs")
    public Response<List<DbDto>> listDbs() {
        return Response.success(DbUtils.listDbs());
    }

    @PostMapping("selectOrNewDb")
    public Response<String> selectOrNewDb(@RequestBody ApiSelectOrNewDbVo params) {
        if (StringUtils.isNotBlank(params.getId())) {
            for (DbDto db : DbUtils.listDbs()) {
                if (db.getId().equals(params.getId())) {
                    ApiUtils.setSid(db.getFileName());
                    return Response.success(params.getId());
                }
            }
            return Response.error("DB不存在");
        }
        DbDto db = ApiUtils.initDb(params.getName());
        ApiUtils.setSid(db.getFileName());
        return Response.success(db.getId());
    }

    @RequestMapping("listFiles")
    public Response<List<FilePo>> listFiles() {
        return Response.success(ApiUtils.queryAllFileData());
    }

    @RequestMapping("addFile")
    public Response<Object> addFile(@RequestBody ApiAddFileVo params) {
        BaseParser.getInstance(params.getFileType()).parse(params);
        return Response.success();
    }

    @RequestMapping("deleteFile")
    public Response<Object> deleteTable(@RequestBody ApiDeleteFileVo params) {
        for (TablePo table : ApiUtils.queryFileData(params.getId()).getTables()) {
            DbUtils.dropTable(ApiUtils.getSid(), table.getTableName());
        }
        return Response.success();
    }

    @RequestMapping("query")
    public Response<List<JSONObject>> query(@RequestBody ApiQueryVo params) {
        return Response.success(DbUtils.query(ApiUtils.getSid(), params.getSql()));
    }
}
