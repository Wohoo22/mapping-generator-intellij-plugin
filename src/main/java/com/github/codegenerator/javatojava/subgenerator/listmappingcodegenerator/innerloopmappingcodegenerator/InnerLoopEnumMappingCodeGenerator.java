package com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator;

import com.github.model.DataTypeNode;
import com.github.utils.JavaCommandUtils;

public class InnerLoopEnumMappingCodeGenerator {
    public String generateMappingCode(String parentListToAddVarName, String enumToGetVarName, DataTypeNode enumToSetDataTypeNode,
                                      String indent) {

        return indent + JavaCommandUtils.generateListAdd(parentListToAddVarName, JavaCommandUtils.generateEnumConverter(enumToSetDataTypeNode.getQualifiedName(), enumToGetVarName));
    }
}
