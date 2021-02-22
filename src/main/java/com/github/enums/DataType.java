package com.github.enums;

public enum DataType {
    ARRAY,
    OBJECT,
    MAP,
    ENUM,
    OTHERS;

    private DataType arrayElementDataType;

    public DataType getArrayElementDataType() {
        return arrayElementDataType;
    }

    public void setArrayElementDataType(DataType arrayElementDataType) {
        this.arrayElementDataType = arrayElementDataType;
    }
}
