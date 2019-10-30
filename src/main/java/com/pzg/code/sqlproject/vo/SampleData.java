package com.pzg.code.sqlproject.vo;

import java.util.List;
import java.util.Map;

public class SampleData {

    private List<DBTableColumn> tableColumns;
    private List<Map<String, Object>> sampleData;
    private DataSet dataSet;

    public List<DBTableColumn> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<DBTableColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public List<Map<String, Object>> getSampleData() {
        return sampleData;
    }

    public void setSampleData(List<Map<String, Object>> sampleData) {
        this.sampleData = sampleData;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String toString() {
        return "SampleData{" +
                "tableColumns=" + tableColumns +
                ", sampleData=" + sampleData +
                ", dataSet=" + dataSet +
                '}';
    }
}
