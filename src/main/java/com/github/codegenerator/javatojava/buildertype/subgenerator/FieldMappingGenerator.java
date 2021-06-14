package com.github.codegenerator.javatojava.buildertype.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

@Builder
public class FieldMappingGenerator {
    private final ElementNode elementToSet;
    private final ElementNode elementToGet;
    private final String sourceToGet;
    private final String indent;
    private final Set<String> usedVariableName;
    private final Set<String> referredQualifiedName;
    private final Set<String> usedPresentableName;

    public String generateMappingCode() {

        StringBuilder result = new StringBuilder();
        result.append(indent);

        // if 2 elements are exactly match
        if (DataTypeNodeUtils.qualifiedNameEqual(
                elementToSet.getDataTypeNode(),
                elementToGet.getDataTypeNode()
        )) {
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
            result.append(forObject());
        }
        // if type == list
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            result.append(forList());
        }

        return result.toString();
    }

    private String forMatchingElements() {
        String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        return JavaCommandUtils.dotField(elementToSet.getName(), getter);
    }

    private String forEnum() {
        String setterGenerationName = NameUtils.getGenerationNameAndMark(
                elementToSet.getDataTypeNode().getPresentableName(),
                elementToSet.getDataTypeNode().getQualifiedName(),
                usedPresentableName,
                referredQualifiedName
        );
        String enumConversion = JavaCommandUtils.generateEnumConverter(
                setterGenerationName,
                sourceToGet,
                elementToGet.getName());
        return JavaCommandUtils.dotField(elementToSet.getName(), enumConversion);
    }

    private String forMap() {
        String mapToSet = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        return JavaCommandUtils.dotField(elementToSet.getName(), mapToSet);
    }

    private String forOthers() {
        String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        return JavaCommandUtils.dotField(elementToSet.getName(), getter);
    }

    private String forObject() {
        String sourceToGetThoseElements = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator
                .builder()
                .elementsToSet(elementToSet.getChildren())
                .elementsToGet(elementToGet.getChildren())
                .sourceToGet(sourceToGetThoseElements)
                .objectToSetQualifiedName(elementToSet.getDataTypeNode().getQualifiedName())
                .objectToSetPresentableName(elementToSet.getDataTypeNode().getPresentableName())
                .indent(StringUtils.addSpaces(indent, 2))
                .usedVariableName(usedVariableName)
                .referredQualifiedName(referredQualifiedName)
                .usedPresentableName(usedPresentableName)
                .build();
        String valueToSet = objectMappingGenerator.generateMappingCode();
        return JavaCommandUtils.dotField(elementToSet.getName(), valueToSet);
    }

    private String forList() {
        String listSource = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
        ListMappingGenerator listMappingGenerator = ListMappingGenerator.builder()
                .listSource(listSource)
                .elementToSet(elementToSet)
                .elementToGet(elementToGet)
                .indent(indent)
                .usedVariableName(usedVariableName)
                .referredQualifiedName(referredQualifiedName)
                .usedPresentableName(usedPresentableName)
                .build();
        String valueToSet = listMappingGenerator.generateMappingCode();
        return JavaCommandUtils.dotField(elementToSet.getName(), valueToSet);
    }
}
