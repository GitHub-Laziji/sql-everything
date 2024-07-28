package org.laziji.sqleverything.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import org.laziji.sqleverything.consts.FileType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ApiUtils {


    public static void setSid(String sid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("SID", sid);
    }

    public static String getSid() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return Objects.toString(request.getSession().getAttribute("SID"));
    }

    public static void insertFileData(String fileName, FileType fileType, List<String> tables) {
        DbUtils.insertData(getSid(), "__file__", List.of(new JSONObject(ImmutableMap.of(
                "fileName", fileName,
                "fileType", fileType.name(),
                "tables", JSON.toJSONString(tables)
        ))));
    }
}
