package com.github2.generator.javatoproto.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.StringUtils;

import java.util.Set;

public class FieldMappingGenerator {
    public String generateMappingCode(ElementNode elementToSet, ElementNode elementToGet, String sourceToGet, String indent,
                                      Set<String> usedVariableName) {

        StringBuilder result = new StringBuilder();
        result.append(indent);

        // if 2 elements are exactly match
        if (DataTypeNodeUtils.qualifiedNameEqual(elementToSet.getDataTypeNode(), elementToGet.getDataTypeNode())) {
            String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateBuilderSetter(elementToSet.getName(), getter));
        }
        // if type == enum
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String enumConversion = JavaCommandUtils.generateEnumConverter(elementToSet.getDataTypeNode().getQualifiedName(), sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateBuilderSetter(elementToSet.getName(), enumConversion));
        }
        // if type == map
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.MAP) {
            String mapToSet = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generatePutAllMap(elementToSet.getName(), mapToSet));
        }
        // if type == others
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateBuilderSetter(elementToSet.getName(), getter));
        }
        // if type == object
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
            String sourceToGetThoseElements = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            String valueToSet = objectMappingGenerator.generateMappingCode(elementToSet.getChildren(), elementToGet.getChildren(),
                    sourceToGetThoseElements, elementToSet.getDataTypeNode().getQualifiedName(), StringUtils.addSpaces(indent, 2), usedVariableName);

            result.append(JavaCommandUtils.generateBuilderSetter(elementToSet.getName(), valueToSet));
        }
        // if type == list
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            ListMappingGenerator listMappingGenerator = new ListMappingGenerator();
            String listSource = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            String valueToSet = listMappingGenerator.generateMappingCode(listSource, elementToSet, elementToGet, indent, usedVariableName);

            result.append(JavaCommandUtils.generateAddAll(elementToSet.getName(), valueToSet));
        }

        return result.toString();
    }
}
