package com.github2.generator.javatojava.normaltype.subgenerator;

import com.github2.model.DataTypeNode;
import com.github2.model.ElementNode;
import com.github2.utils.JavaCommandUtils;
import com.github2.utils.NameUtils;
import com.github2.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

@Builder
public class ListMappingGenerator {
    private final String listToGetSource;
    private final String listToAddVarName;
    private final ElementNode elementToSet;
    private final ElementNode elementToGet;
    private final String indent;
    private final Set<String> usedVariableName;

    public String generateMappingCode() {

        StringBuilder result = new StringBuilder();

        // list to add declaration
        result.append(indent).append(JavaCommandUtils.generateListDeclaration(elementToSet.getDataTypeNode().getQualifiedName(), listToAddVarName));

        // open for loop
        String innerLoopSourceToGet = NameUtils.generateUniqueRandomName(elementToGet.getDataTypeNode().getChild().getPresentableName(), usedVariableName);
        result.append(indent).append(JavaCommandUtils.generateForEachWithoutCloseBrace(elementToGet.getDataTypeNode().getChild().getQualifiedName(), innerLoopSourceToGet, listToGetSource));

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
                String mappingCode = forChildObject(childElementToSetDataTypeNode, innerLoopSourceToGet, childObjectToSetVarName);
                result.append(indent).append(mappingCode);
                valueToAdd.append(childObjectToSetVarName);
                break;
        }

        // add to list
        result.append(JavaCommandUtils.generateListAdd(listToAddVarName, valueToAdd.toString()));

        // close for loop
        result.append(indent).append(JavaCommandUtils.closeLoop());

        return result.toString();
    }

    private String forChildObject(DataTypeNode childElementToSetDataTypeNode, String innerLoopSourceToGet, String childObjectToSetVarName) {

        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                .elementsToSet(elementToSet.getChildren())
                .elementsToGet(elementToGet.getChildren())
                .objectToSetVarName(childObjectToSetVarName)
                .sourceToGet(innerLoopSourceToGet)
                .objectToSetQualifiedName(childElementToSetDataTypeNode.getQualifiedName())
                .indent(StringUtils.addSpaces(indent, 2))
                .usedVariableName(usedVariableName)
                .build();

        return objectMappingGenerator.generateMappingCode();
    }
}

