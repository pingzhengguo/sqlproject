package com.pzg.code.sqlproject.vo;

public class DBTableColumn {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列的数据类型
     */
    private String dataType;
    /**
     * 注释
     */
    private String comments;

    public DBTableColumn() {
    }

    public DBTableColumn(String columnName, String dataType, String comments) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.comments = comments;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "DBTableColumn{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
