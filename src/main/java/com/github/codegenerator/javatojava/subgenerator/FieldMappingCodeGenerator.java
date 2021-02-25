package com.github.codegenerator.javatojava.subgenerator;

import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;

public class FieldMappingCodeGenerator {

    public String generateMappingCode(String objectToSetVarName, String objectToGetVarName,
                                      ElementNode fieldToSet, ElementNode fieldToGet, String result,
                                      String indent) {

        if (DataTypeNodeUtils.equalDataTypeAndQualifiedName(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode())) {
            result += indent + JavaCommandUtils.generateSetterGetter(objectToGetVarName, fieldToGet.getName(), objectToSetVarName, fieldToSet.getName());
        }

        return result;
    }

}
