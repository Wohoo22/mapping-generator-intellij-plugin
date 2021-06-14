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
    private ElementNode elementToSet;
    private ElementNode elementToGet;
    private final String indent;
    private final Set<String> usedVariableName;
    private final Set<String> referredQualifiedName;
    private final Set<String> usedPresentableName;

    public String generateMappingCode() {

        StringBuilder result = new StringBuilder();

        // open stream map
        result.append(indent).append(JavaCommandUtils.openStreamMap(listSource));


        ElementNode childElementToSet = new ElementNode(
                elementToSet.getName(),
                elementToSet.getDataTypeNode().getChild(),
                elementToSet.getChildren()
        );


        ElementNode childElementToGet = new ElementNode(
                elementToGet.getName(),
                elementToGet.getDataTypeNode().getChild(),
                elementToGet.getChildren()
        );


        elementToSet = childElementToSet;
        elementToGet = childElementToGet;

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
            String setterGenerationName = NameUtils.getGenerationNameAndMark(
                    elementToSet.getDataTypeNode().getPresentableName(),
                    elementToSet.getDataTypeNode().getQualifiedName(),
                    usedPresentableName,
                    referredQualifiedName
            );
            String enumConversion = JavaCommandUtils.generateEnumConverter(setterGenerationName, innerStreamSourceToGet);
            result.append(indent).append(enumConversion);
        }
        // if type == others
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            result.append(indent).append(innerStreamSourceToGet);
        }
        // if type == object
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            result.append(indent).append(forInnerListObject(innerStreamSourceToGet));
        }
        return result.toString();
    }

    public String forInnerListObject(String innerStreamSourceToGet) {
        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                .elementsToSet(elementToSet.getChildren())
                .elementsToGet(elementToGet.getChildren())
                .sourceToGet(innerStreamSourceToGet)
                .objectToSetQualifiedName(elementToSet.getDataTypeNode().getQualifiedName())
                .objectToSetPresentableName(elementToSet.getDataTypeNode().getPresentableName())
                .indent(StringUtils.addSpaces(indent, 2))
                .usedVariableName(usedVariableName)
                .referredQualifiedName(referredQualifiedName)
                .usedPresentableName(usedPresentableName)
                .build();

        return objectMappingGenerator.generateMappingCode();
    }
}
