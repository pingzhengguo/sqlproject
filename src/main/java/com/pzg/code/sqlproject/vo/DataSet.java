package com.pzg.code.sqlproject.vo;


import java.util.List;

/**
 * @description 数据集类，对应t_dataset表
 */
public class DataSet {

    private Integer id;
    private String name;
    //别名
    private String alias;
    private Integer pid;
    private String dataSourceId;
    private Boolean isUploadFile;
    private String uploadId;
    private String type;
    private String deleteFlag;
    private Integer dataType;
    private char isPublic;

    @Deprecated
    private String filePath;
    /**
     * 上传文件的绝对路径，为了传递给分析平台获取文件使用
     */
    private String absoluteFilePath;


    /**
     * 在当前文件夹中的顺序
     */
    private Integer sequence;


    private List<DataSet> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Boolean getUploadFile() {
        return isUploadFile;
    }

    public void setUploadFile(Boolean uploadFile) {
        isUploadFile = uploadFile;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataSet> getChildren() {
        return children;
    }

    public void setChildren(List<DataSet> children) {
        this.children = children;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Deprecated
    public String getFilePath() {
        return filePath;
    }

    @Deprecated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    public void setAbsoluteFilePath(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
    }


    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public char getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(char isPublic) {
        this.isPublic = isPublic;
    }


}
