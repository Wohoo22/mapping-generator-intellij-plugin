package com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;

import java.util.List;
import java.util.Set;

public class InnerLoopListMappingCodeGenerator {
    public String generateMappingCode(DataTypeNode listToAddDataTypeNode, String parentListToAddVarName,
                                      DataTypeNode listToGetDataTypeNode, String parentListToGetVarName,
                                      // use if child data type = OBJECT
                                      List<ElementNode> childFieldsToSet, List<ElementNode> childFieldsToGet,
                                      String indent, Set<String> usedVariableName) {

        String result = "";

        // some pre-condition
        if (listToAddDataTypeNode == null || listToGetDataTypeNode == null || childFieldsToSet == null || childFieldsToGet == null)
            return result;
        if (listToAddDataTypeNode.getDataType() != DataTypeNode.DataType.LIST || listToGetDataTypeNode.getDataType() != DataTypeNode.DataType.LIST)
            return result;
        if (listToAddDataTypeNode.getChild() == null || listToGetDataTypeNode.getChild() == null)
            return result;


        // declare list-to-add
        String crtListToAddVarName = NameUtils.generateUniqueRandomName(listToAddDataTypeNode.getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateListDeclaration(listToAddDataTypeNode.getQualifiedName(), crtListToAddVarName);

        // generate for-each loop (without close brace)
        String innerLoopObjectToGetVarName = NameUtils.generateUniqueRandomName(listToAddDataTypeNode.getChild().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateForEachWithoutCloseBrace(listToGetDataTypeNode.getChild().getQualifiedName(), innerLoopObjectToGetVarName, parentListToGetVarName);

        String lastIndent = indent;
        indent = StringUtils.addSpaces(indent, 2);

        // generate inner loop mapping code
        switch (listToAddDataTypeNode.getChild().getDataType()) {
            case OBJECT:
                InnerLoopObjectMappingCodeGenerator innerLoopObjectMappingCodeGenerator = new InnerLoopObjectMappingCodeGenerator();
                result += indent + innerLoopObjectMappingCodeGenerator.generateMappingCode(crtListToAddVarName, listToAddDataTypeNode.getChild(), childFieldsToSet,
                        innerLoopObjectToGetVarName, childFieldsToGet, indent, usedVariableName);
                break;
            case ENUM:
                InnerLoopEnumMappingCodeGenerator innerLoopEnumMappingCodeGenerator = new InnerLoopEnumMappingCodeGenerator();
                result += indent + innerLoopEnumMappingCodeGenerator.generateMappingCode(crtListToAddVarName, innerLoopObjectToGetVarName, listToAddDataTypeNode.getChild(), indent);
                break;
            case LIST:
                result += indent + generateMappingCode(listToAddDataTypeNode.getChild(), crtListToAddVarName, listToGetDataTypeNode.getChild(),
                        innerLoopObjectToGetVarName, childFieldsToSet, childFieldsToGet, indent, usedVariableName);
        }

        // close the loop
        result += lastIndent + "}" + "\n";

        // add generated list to parent list
        result += indent + JavaCommandUtils.generateListAdd(parentListToAddVarName, crtListToAddVarName);

        return result;
    }
}
