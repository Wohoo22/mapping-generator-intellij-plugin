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
        while (innermostDataTypeNode.getChild() != null)
            innermostDataTypeNode = innermostDataTypeNode.getChild();
        if (innermostDataTypeNode.getDataType() == DataTypeNode.DataType.MAP
                || innermostDataTypeNode.getDataType() == DataTypeNode.DataType.OTHERS) {
            result += indent + JavaCommandUtils.generateSetterGetter(parentObjectToGetVarName, fieldToGet.getName(), parentObjectToSetVarName, fieldToSet.getName());
            return result;
        }

        // declare list-to-set
        String crtListToSetVarName = NameUtils.generateUniqueRandomName(fieldToSet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateListDeclaration(fieldToSet.getDataTypeNode().getQualifiedName(), crtListToSetVarName);

        // declare list-to-get
        String crtListToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateDeclarationByGetter(fieldToGet.getDataTypeNode().getQualifiedName(), crtListToGetVarName, parentObjectToGetVarName, fieldToGet.getName());

        result += "\n";

        // generate for-each loop (without close brace)
        String innerLoopObjectToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getChild().getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateForEachWithoutCloseBrace(fieldToGet.getDataTypeNode().getChild().getQualifiedName(), innerLoopObjectToGetVarName, crtListToGetVarName);

        String lastIndent = indent;
        indent = StringUtils.addSpaces(indent, 2);

        // generate inner loop mapping code
        switch (fieldToSet.getDataTypeNode().getChild().getDataType()) {
            case OBJECT:
                InnerLoopObjectMappingCodeGenerator innerLoopObjectMappingCodeGenerator = new InnerLoopObjectMappingCodeGenerator();
                result += indent + innerLoopObjectMappingCodeGenerator.generateMappingCode(
                        crtListToSetVarName, fieldToSet.getDataTypeNode().getChild(), fieldToSet.getChildren(),
                        innerLoopObjectToGetVarName, fieldToGet.getChildren(), indent, usedVariableName
                );
                break;
            case ENUM:
                InnerLoopEnumMappingCodeGenerator innerLoopEnumMappingCodeGenerator = new InnerLoopEnumMappingCodeGenerator();
                result += indent + innerLoopEnumMappingCodeGenerator.generateMappingCode(crtListToSetVarName, innerLoopObjectToGetVarName, fieldToSet.getDataTypeNode().getChild(), indent);
                break;
            case LIST:
                InnerLoopListMappingCodeGenerator innerLoopListMappingCodeGenerator = new InnerLoopListMappingCodeGenerator();
                result += indent + innerLoopListMappingCodeGenerator.generateMappingCode(fieldToSet.getDataTypeNode().getChild(), crtListToSetVarName, fieldToGet.getDataTypeNode().getChild(),
                        innerLoopObjectToGetVarName, fieldToSet.getChildren(), fieldToGet.getChildren(), indent, usedVariableName);
        }

        // close the loop
        result += lastIndent + "}" + "\n";

        result += "\n";

        // set generated list to parent object
        result += indent + JavaCommandUtils.generateSetter(parentObjectToSetVarName, fieldToSet.getName(), crtListToSetVarName);

        result += "\n";

        return result;
    }
}
