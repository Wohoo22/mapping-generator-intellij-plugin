package com.github.codegenerator.javatojava.buildertype.subgenerator;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

@Builder
public class ListMappingGenerator {
    private final String listSource;
    private final ElementNode elementToSet;
    private final ElementNode elementToGet;
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
        String innerListMappingCode = generateInnerListMappingCode(innerStreamSourceToGet, StringUtils.addSpaces(indent, 2));
        result.append(indent).append(innerListMappingCode);

        // close stream map
        result.append(indent).append(JavaCommandUtils.closeStreamMap());

        return result.toString();
    }

    private String generateInnerListMappingCode(String innerStreamSourceToGet, String indent) {
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

            ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                    .elementsToSet(elementToSet.getChildren())
                    .elementsToGet(elementToGet.getChildren())
                    .sourceToGet(innerStreamSourceToGet)
                    .objectToSetQualifiedName(elementToSet.getDataTypeNode().getQualifiedName())
                    .indent(StringUtils.addSpaces(indent, 2))
                    .usedVariableName(usedVariableName)
                    .build();

            String mappingCode = objectMappingGenerator.generateMappingCode();
            result.append(indent).append(mappingCode);
        }
        return result.toString();
    }
}
