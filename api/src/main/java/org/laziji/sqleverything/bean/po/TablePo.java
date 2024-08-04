package org.laziji.sqleverything.bean.po;

import java.util.List;

public class TablePo {
    private String tableName;
    private List<ColumnPo> columns;

    public TablePo() {
    }

    public TablePo(String tableName, List<ColumnPo> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnPo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnPo> columns) {
        this.columns = columns;
    }
}