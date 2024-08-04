package org.laziji.sqleverything.bean.po;

import org.laziji.sqleverything.consts.ColumnType;

public class ColumnPo {
    private String columnName;
    private ColumnType columnType;

    public ColumnPo() {
    }

    public ColumnPo(String columnName, ColumnType columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }
}