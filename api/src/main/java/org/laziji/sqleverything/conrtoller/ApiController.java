package org.laziji.sqleverything.conrtoller;

import com.alibaba.fastjson2.JSONObject;
import org.laziji.sqleverything.bean.Response;
import org.laziji.sqleverything.bean.dto.DbDto;
import org.laziji.sqleverything.bean.po.FilePo;
import org.laziji.sqleverything.bean.vo.ApiAddFileVo;
import org.laziji.sqleverything.bean.vo.ApiDeleteFileVo;
import org.laziji.sqleverything.bean.vo.ApiQueryVo;
import org.laziji.sqleverything.bean.vo.ApiSelectOrNewDbVo;
import org.laziji.sqleverything.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    private ApiService apiService;


    @PostMapping("listDbs")
    public Response<List<DbDto>> listDbs() {
        return Response.success(apiService.listDbs());
    }

    @PostMapping("selectOrNewDb")
    public Response<String> selectOrNewDb(@RequestBody ApiSelectOrNewDbVo params) {
        try {
            return Response.success(apiService.selectOrNewDb(params));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping("listFiles")
    public Response<List<FilePo>> listFiles() {
        return Response.success(apiService.listFiles());
    }

    @RequestMapping("addFile")
    public Response<Object> addFile(@RequestBody ApiAddFileVo params) {
        apiService.addFile(params);
        return Response.success();
    }

    @RequestMapping("deleteFile")
    public Response<Object> deleteTable(@RequestBody ApiDeleteFileVo params) {
        apiService.deleteTable(params);
        return Response.success();
    }

    @RequestMapping("query")
    public Response<List<JSONObject>> query(@RequestBody ApiQueryVo params) {
        return Response.success(apiService.query(params));
    }
}
