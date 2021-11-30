package com.github_new.generator.helper.javatojava.normal;

import com.github_new.domain.DataTypeNode;
import com.github_new.domain.ElementNode;
import com.github_new.generator.helper.utils.DataTypeNodeUtils;
import com.github_new.generator.helper.utils.JavaCommandUtils;
import com.github_new.generator.helper.utils.NameUtils;
import com.github_new.generator.helper.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

@Builder
public class FieldMappingGenerator {
    private final ElementNode elementToSet;
    private final ElementNode elementToGet;
    private final String objectToSetVarName;
    private final String sourceToGet;
    private final String indent;
    private final Set<String> usedVariableName;

    public String generateMappingCode() {


        StringBuilder result = new StringBuilder();
        result.append(indent);

        // if 2 elements are exactly match
        if (elementToSet.getDataTypeNode().getDataType() != DataTypeNode.DataType.LIST
                && DataTypeNodeUtils.qualifiedNameEqual(elementToSet.getDataTypeNode(), elementToGet.getDataTypeNode())) {
            result.append(forMatchingElements());
        }
        // if type == enum
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            result.append(forEnum());
        }
        // if type == map
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.MAP) {
            result.append(forMap());
        }
        // if type == others
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            result.append(forOthers());
        }
        // if type == object
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            result.append("\n");
            result.append(forObject());
            result.append("\n");
        }
        // if type == list
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            result.append("\n");
            result.append(forList());
            result.append("\n");
        }

        return result.toString();

    }

    private String forMatchingElements() {
        StringBuilder result = new StringBuilder();
        String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), getter));
        return result.toString();
    }

    private String forEnum() {
        StringBuilder result = new StringBuilder();
        String enumConversion = JavaCommandUtils.generateEnumConverter(elementToSet.getDataTypeNode().getQualifiedName(), sourceToGet, elementToGet.getName());
        result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), enumConversion));
        return result.toString();
    }

    private String forMap() {
        StringBuilder result = new StringBuilder();
        String mapToSet = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), mapToSet));
        return result.toString();
    }

    private String forOthers() {
        StringBuilder result = new StringBuilder();
        String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), getter));
        return result.toString();
    }

    private String forObject() {
        StringBuilder result = new StringBuilder();

        String childObjectToSetVarName = NameUtils.generateUniqueRandomName(elementToSet.getDataTypeNode().getPresentableName(), usedVariableName);
        String sourceToGetThoseElements = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());

        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                .elementsToSet(elementToSet.getChildren())
                .elementsToGet(elementToGet.getChildren())
                .objectToSetVarName(childObjectToSetVarName)
                .sourceToGet(sourceToGetThoseElements)
                .objectToSetQualifiedName(elementToSet.getDataTypeNode().getQualifiedName())
                .indent(StringUtils.addSpaces(indent, 2))
                .usedVariableName(usedVariableName)
                .build();
        String mappingCode = objectMappingGenerator.generateMappingCode();

        result.append(mappingCode)
                .append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), childObjectToSetVarName));

        return result.toString();
    }

    private String forList() {
        StringBuilder result = new StringBuilder();

        String listToGetSource = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        String listToAddVarName = NameUtils.generateUniqueRandomName(elementToSet.getDataTypeNode().getPresentableName(), usedVariableName);

        ListMappingGenerator listMappingGenerator = ListMappingGenerator.builder()
                .listToGetSource(listToGetSource)
                .listToAddVarName(listToAddVarName)
                .elementToSet(elementToSet)
                .elementToGet(elementToGet)
                .indent(StringUtils.addSpaces(indent, 2))
                .usedVariableName(usedVariableName)
                .build();
        String mappingCode = listMappingGenerator.generateMappingCode();

        result.append(mappingCode)
                .append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), listToAddVarName));

        return result.toString();
    }
}