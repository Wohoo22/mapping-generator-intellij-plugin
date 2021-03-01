package com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator;

import com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator.InnerLoopEnumMappingCodeGenerator;
import com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator.InnerLoopListMappingCodeGenerator;
import com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator.InnerLoopObjectMappingCodeGenerator;
import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;

import java.util.Set;

public class ListMappingCodeGenerator {
    public String generateMappingCode(String parentObjectToSetVarName, ElementNode fieldToSet,
                                      String parentObjectToGetVarName, ElementNode fieldToGet,
                                      Set<String> usedVariableName, String indent) {

        String result = "";

        // 2 data types are not equal -> return
        if (fieldToSet == null || fieldToGet == null)
            return result;
        if (!DataTypeNodeUtils.dataTypeEqual(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode()))
            return result;


        // if innermost data-type = MAP or OTHERS, generate getter setter right away and return
        DataTypeNode innermostDataTypeNode = fieldToSet.getDataTypeNode();
        while (innermostDataTypeNode.getChild() != null) innermostDataTypeNode = innermostDataTypeNode.getChild();
        if (innermostDataTypeNode.getDataType() == DataTypeNode.DataType.MAP || innermostDataTypeNode.getDataType() == DataTypeNode.DataType.OTHERS) {
            result += indent + JavaCommandUtils.generateSetterGetter(parentObjectToGetVarName, fieldToGet.getName(), parentObjectToSetVarName, fieldToSet.getName());
            return result;
        }

        // declare list-to-add-data
        String crtListToAddVarName = NameUtils.generateUniqueRandomName(fieldToSet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateListDeclaration(fieldToSet.getDataTypeNode().getQualifiedName(), crtListToAddVarName);

        // declare list-to-get-data-from
        String crtListToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateDeclarationByGetter(fieldToGet.getDataTypeNode().getQualifiedName(), crtListToGetVarName, parentObjectToGetVarName, fieldToGet.getName());

        result += "\n";

        // generate for-each loop (without close brace)
        String innerLoopObjectToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getChild().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateForEachWithoutCloseBrace(fieldToGet.getDataTypeNode().getChild().getQualifiedName(), innerLoopObjectToGetVarName, crtListToGetVarName);

        String lastIndent = indent;
        indent = StringUtils.addSpaces(indent, 2);

        InnerLoopObjectMappingCodeGenerator innerLoopObjectMappingCodeGenerator = new InnerLoopObjectMappingCodeGenerator();
        InnerLoopEnumMappingCodeGenerator innerLoopEnumMappingCodeGenerator = new InnerLoopEnumMappingCodeGenerator();
        InnerLoopListMappingCodeGenerator innerLoopListMappingCodeGenerator = new InnerLoopListMappingCodeGenerator();

        // generate inner loop mapping code
        switch (fieldToSet.getDataTypeNode().getChild().getDataType()) {
            case OBJECT:
                result += indent + innerLoopObjectMappingCodeGenerator.generateMappingCode(
                        crtListToAddVarName, fieldToSet.getDataTypeNode().getChild(), fieldToSet.getChildren(),
                        innerLoopObjectToGetVarName, fieldToGet.getChildren(), indent, usedVariableName
                );
                break;
            case ENUM:
                result += indent + innerLoopEnumMappingCodeGenerator.generateMappingCode(crtListToAddVarName, innerLoopObjectToGetVarName, fieldToSet.getDataTypeNode().getChild(), indent);
                break;
            case LIST:
                result += indent + innerLoopListMappingCodeGenerator.generateMappingCode(fieldToSet.getDataTypeNode().getChild(), crtListToAddVarName, fieldToGet.getDataTypeNode().getChild(),
                        innerLoopObjectToGetVarName, fieldToSet.getChildren(), fieldToGet.getChildren(), indent, usedVariableName);
        }

        // close the loop
        result += lastIndent + "}" + "\n";

        result += "\n";

        // set generated list to parent object
        result += indent + JavaCommandUtils.generateSetter(parentObjectToSetVarName, fieldToSet.getName(), crtListToAddVarName);

        return result;
    }
}
