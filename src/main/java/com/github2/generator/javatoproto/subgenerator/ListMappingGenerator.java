package com.github2.generator.javatoproto.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;

import java.util.Set;

public class ListMappingGenerator {
    public String generateMappingCode(String listSource, ElementNode elementToSet, ElementNode elementToGet,
                                      String indent, Set<String> usedVariableName) {

        StringBuilder result = new StringBuilder();

        // open stream map
        result.append(indent).append(JavaCommandUtils.openStreamMap(listSource));

        ElementNode childElementToSet = new ElementNode();
        childElementToSet.setName(elementToSet.getName());
        childElementToSet.setDataTypeNode(elementToSet.getDataTypeNode().getChild());
        childElementToSet.setChildren(elementToSet.getChildren());

        ElementNode childElementToGet = new ElementNode();
        childElementToGet.setName(elementToGet.getName());
        childElementToGet.setDataTypeNode(elementToGet.getDataTypeNode().getChild());
        childElementToGet.setChildren(elementToGet.getChildren());

        String innerStreamSourceToGet = NameUtils.generateUniqueRandomName(elementToGet.getName(), usedVariableName);

        // generate arrow
        result.append(indent).append(JavaCommandUtils.generateArrow(innerStreamSourceToGet));

        // generate mapping
        String fieldMapping = generateInnerListMappingCode(childElementToSet, childElementToGet, innerStreamSourceToGet, StringUtils.addSpaces(indent, 2), usedVariableName);
        result.append(indent).append(fieldMapping);

        // close stream map
        result.append(indent).append(JavaCommandUtils.closeStreamMap());

        return result.toString();
    }

    private String generateInnerListMappingCode(ElementNode elementToSet, ElementNode elementToGet, String innerStreamSourceToGet, String indent, Set<String> usedVariableName) {
        StringBuilder result = new StringBuilder();
        // if type == enum
        if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String enumConversion = JavaCommandUtils.generateEnumConverter(elementToSet.getDataTypeNode().getQualifiedName(), innerStreamSourceToGet);
            result.append(indent).append(enumConversion);
        }
        // if type == others
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            result.append(indent).append(innerStreamSourceToGet);
        }
        // if type == object
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
            String code = objectMappingGenerator.generateMappingCode(elementToSet.getChildren(), elementToGet.getChildren(),
                    innerStreamSourceToGet, elementToSet.getDataTypeNode().getQualifiedName(), StringUtils.addSpaces(indent, 2), usedVariableName);
            result.append(indent).append(code);
        }
        return result.toString();
    }
}
