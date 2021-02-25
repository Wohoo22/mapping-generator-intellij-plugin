package com.github.utils;

import com.github.model.DataTypeNode;

public class DataTypeNodeUtils {
    public static boolean qualifiedNameEqual(DataTypeNode dataTypeNode1, DataTypeNode dataTypeNode2) {
        return dataTypeNode1.getQualifiedName().equals(dataTypeNode2.getQualifiedName());
    }

    public static boolean dataTypeEqual(DataTypeNode dataTypeNode1, DataTypeNode dataTypeNode2) {
        return true;
    }
}
