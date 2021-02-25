package com.github.utils;

import com.github.model.DataTypeNode;

public class DataTypeNodeUtils {
    public static boolean equalDataTypeAndQualifiedName(DataTypeNode dataTypeNode1, DataTypeNode dataTypeNode2) {
        return dataTypeNode1.getQualifiedName().equals(dataTypeNode2.getQualifiedName()) &&
                dataTypeNode1.getDataType() == dataTypeNode2.getDataType();
    }
}
