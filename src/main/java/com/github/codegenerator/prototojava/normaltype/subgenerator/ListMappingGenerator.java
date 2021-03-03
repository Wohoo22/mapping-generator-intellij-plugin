package com.github.codegenerator.prototojava.normaltype.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.QualifiedNameUtils;
import com.github.utils.StringUtils;

import java.util.Set;

public class ListMappingGenerator {
    public String generateMappingCode(String listToGetSource, String listToAddVarName, ElementNode elementToSet, ElementNode elementToGet,
                                      String indent, Set<String> usedVariableName) {

        StringBuilder result = new StringBuilder();

        // list to add declaration
        result.append(indent).append(JavaCommandUtils.generateListDeclaration(elementToSet.getDataTypeNode().getQualifiedName(), listToAddVarName));

        // open for loop
        String innerLoopSourceToGet = NameUtils.generateUniqueRandomName(elementToGet.getDataTypeNode().getChild().getPresentableName(), usedVariableName);
        String refinedElementToGetDataTypeNodeQualifiedName = QualifiedNameUtils.refine(elementToGet.getDataTypeNode().getChild().getQualifiedName());
        result.append(indent).append(JavaCommandUtils.generateForEachWithoutCloseBrace(refinedElementToGetDataTypeNodeQualifiedName, innerLoopSourceToGet, listToGetSource));

        // value to add to list
        StringBuilder valueToAdd = new StringBuilder();

        DataTypeNode childElementToSetDataTypeNode = elementToSet.getDataTypeNode().getChild();
        switch (childElementToSetDataTypeNode.getDataType()) {
            case ENUM:
                valueToAdd.append(JavaCommandUtils.generateEnumConverter(childElementToSetDataTypeNode.getQualifiedName(), innerLoopSourceToGet));
                break;
            case OTHERS:
                valueToAdd.append(innerLoopSourceToGet);
                break;
            case OBJECT:
                String childObjectToSetVarName = NameUtils.generateUniqueRandomName(childElementToSetDataTypeNode.getPresentableName(), usedVariableName);

                ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
                String mappingCode = objectMappingGenerator.generateMappingCode(elementToSet.getChildren(), elementToGet.getChildren(), childObjectToSetVarName,
                        innerLoopSourceToGet, childElementToSetDataTypeNode.getQualifiedName(), StringUtils.addSpaces(indent, 2), usedVariableName);

                result.append(indent).append(mappingCode);
                valueToAdd.append(childObjectToSetVarName);
        }

        // add to list
        result.append(JavaCommandUtils.generateListAdd(listToAddVarName, valueToAdd.toString()));

        // close for loop
        result.append(indent).append(JavaCommandUtils.closeLoop());

        return result.toString();
    }

}

