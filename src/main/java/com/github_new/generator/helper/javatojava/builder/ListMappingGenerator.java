package com.github_new.generator.helper.javatojava.builder;

import com.github_new.domain.DataTypeNode;
import com.github_new.domain.ElementNode;
import com.github_new.generator.helper.utils.JavaCommandUtils;
import com.github_new.generator.helper.utils.NameUtils;
import com.github_new.generator.helper.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

@Builder
public class ListMappingGenerator {
    private final String listSource;
    private ElementNode elementToSet;
    private ElementNode elementToGet;
    private final String indent;
    private final Set<String> usedVariableName;

    public String generateMappingCode() {

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
        String innerListMappingCode = generateInnerListMappingCode(
                innerStreamSourceToGet, StringUtils.addSpaces(indent, 2),
                childElementToSet, childElementToGet);
        result.append(indent).append(innerListMappingCode);

        // close stream map
        result.append(indent).append(JavaCommandUtils.closeStreamMap());

        return result.toString();
    }

    private String generateInnerListMappingCode(String innerStreamSourceToGet, String indent,
                                                ElementNode childElementToSet, ElementNode childElementToGet) {
        StringBuilder result = new StringBuilder();
        // if type == enum
        if (childElementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String enumConversion = JavaCommandUtils.generateEnumConverter(childElementToSet.getDataTypeNode().getQualifiedName(), innerStreamSourceToGet);
            result.append(indent).append(enumConversion);
        }
        // if type == others
        else if (childElementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            result.append(indent).append(innerStreamSourceToGet);
        }
        // if type == object
        else if (childElementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {

            ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                    .elementsToSet(childElementToSet.getChildren())
                    .elementsToGet(childElementToGet.getChildren())
                    .sourceToGet(innerStreamSourceToGet)
                    .objectToSetQualifiedName(childElementToSet.getDataTypeNode().getQualifiedName())
                    .indent(StringUtils.addSpaces(indent, 2))
                    .usedVariableName(usedVariableName)
                    .build();

            String mappingCode = objectMappingGenerator.generateMappingCode();
            result.append(indent).append(mappingCode);
        }
        return result.toString();
    }
}
