package com.github_new.generator.helper.utils;

import com.github_new.domain.DataTypeNode;

public class DataTypeNodeUtils {
    public static boolean qualifiedNameEqual(DataTypeNode dataTypeNode1, DataTypeNode dataTypeNode2) {
        return dataTypeNode1.getQualifiedName().equals(dataTypeNode2.getQualifiedName());
    }

    public static boolean dataTypeEqual(DataTypeNode dataTypeNode1, DataTypeNode dataTypeNode2) {
        if (dataTypeNode2.getDataType() != dataTypeNode1.getDataType()) return false;

        DataTypeNode crtDataTypeNode1 = dataTypeNode1;
        DataTypeNode crtDataTypeNode2 = dataTypeNode2;

        while (crtDataTypeNode1.getChild() != null
                && crtDataTypeNode2.getChild() != null) {
            crtDataTypeNode1 = crtDataTypeNode1.getChild();
            crtDataTypeNode2 = crtDataTypeNode2.getChild();
            if (crtDataTypeNode1.getDataType() != crtDataTypeNode2.getDataType())
                return false;
        }

        return true;
    }
}
