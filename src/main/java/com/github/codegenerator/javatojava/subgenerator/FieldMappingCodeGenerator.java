package com.github.codegenerator.javatojava.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;

public class FieldMappingCodeGenerator {

    public String generateMappingCode(String objectToSetVarName, String objectToGetVarName,
                                      ElementNode fieldToSet, ElementNode fieldToGet, String result,
                                      String indent) {

        // 2 data types are not equal -> return
        if (!DataTypeNodeUtils.dataTypeEqual(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode()))
            return result;

        // if 2 data types are equal in type and qualified_name -> can be set directly
        if (DataTypeNodeUtils.qualifiedNameEqual(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode())) {
            result += indent + JavaCommandUtils.generateSetterGetter(objectToGetVarName, fieldToGet.getName(), objectToSetVarName, fieldToSet.getName());
        }
        // if type == ENUM
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String valueToSet = JavaCommandUtils.generateEnumConverter(fieldToSet.getDataTypeNode().getQualifiedName(), objectToGetVarName, fieldToGet.getName());
            result += JavaCommandUtils.generateSetter(objectToSetVarName, fieldToSet.getName(), valueToSet);
        }
        // type == OBJECT
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {

        }
        // type == ARRAY
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ARRAY) {

        }
        // else (may be MAP or OTHERS)
        else {
            result += indent + JavaCommandUtils.generateSetterGetter(objectToGetVarName, fieldToGet.getName(), objectToSetVarName, fieldToSet.getName());
        }

        return result;
    }

}