package org.laziji.sqleverything.bean.vo;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.consts.FileType;

public class ApiAddTableVo {

    private String fileName;
    private FileType fileType;
    private String fileBase64;
    private JSONObject config;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
