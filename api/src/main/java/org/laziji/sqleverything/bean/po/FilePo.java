package org.laziji.sqleverything.bean.po;

import com.alibaba.fastjson.JSONObject;
import org.laziji.sqleverything.consts.FileType;

import java.util.List;

public class FilePo {

    private Long id;
    private String fileName;
    private FileType fileType;
    private String alias;
    private JSONObject config;
    private List<TablePo> tables;

    public FilePo() {
    }

    public FilePo(String fileName, FileType fileType, String alias, JSONObject config, List<TablePo> tables) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.alias = alias;
        this.config = config;
        this.tables = tables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public List<TablePo> getTables() {
        return tables;
    }

    public void setTables(List<TablePo> tables) {
        this.tables = tables;
    }
}
