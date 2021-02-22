package com.github.enums;

public enum DataType {
    ARRAY,
    OBJECT,
    MAP,
    ENUM,
    OTHERS;

    private DataType innerElementDataType;
    private String qualifiedName;

    public DataType getInnerElementDataType() {
        return innerElementDataType;
    }

    public void setInnerElementDataType(DataType innerElementDataType) {
        this.innerElementDataType = innerElementDataType;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
}
