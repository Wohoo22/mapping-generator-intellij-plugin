package com.github.model;

public class DataTypeNode {
    private DataType dataType;
    private DataTypeNode child;
    private String qualifiedName;
    private String presentableName;

    public enum DataType {
        LIST,
        OBJECT,
        MAP,
        ENUM,
        OTHERS;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DataTypeNode getChild() {
        return child;
    }

    public void setChild(DataTypeNode child) {
        this.child = child;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getPresentableName() {
        return presentableName;
    }

    public void setPresentableName(String presentableName) {
        this.presentableName = presentableName;
    }
}
