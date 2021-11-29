package com.github_old.codegenerator.prototojava.normaltype.subgenerator;

import com.github_old.model.DataTypeNode;
import com.github_old.model.ElementNode;
import com.github_old.utils.DataTypeNodeUtils;
import com.github_old.utils.JavaCommandUtils;
import com.github_old.utils.NameUtils;
import com.github_old.utils.StringUtils;

import java.util.Set;

public class FieldMappingGenerator {
    public String generateMappingCode(ElementNode elementToSet, ElementNode elementToGet, String objectToSetVarName,
                                      String sourceToGet, String indent,
                                      Set<String> usedVariableName) {


        StringBuilder result = new StringBuilder();
        result.append(indent);

        // if 2 elements are exactly match
        if (DataTypeNodeUtils.qualifiedNameEqual(elementToSet.getDataTypeNode(), elementToGet.getDataTypeNode())) {
            String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), getter));
        }
        // if type == enum
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String enumConversion = JavaCommandUtils.generateEnumConverter(elementToSet.getDataTypeNode().getQualifiedName(), sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), enumConversion));
        }
        // if type == map
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.MAP) {
            String mapToSet = JavaCommandUtils.generateProtoMapGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), mapToSet));
        }
        // if type == others
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            String getter = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());
            result.append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), getter));
        }
        // if type == object
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            result.append("\n");
            String childObjectToSetVarName = NameUtils.generateUniqueRandomName(elementToSet.getDataTypeNode().getPresentableName(), usedVariableName);
            String sourceToGetThoseElements = JavaCommandUtils.generateGetter(sourceToGet, elementToGet.getName());

            ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
            String mappingCode = objectMappingGenerator.generateMappingCode(elementToSet.getChildren(), elementToGet.getChildren(), childObjectToSetVarName,
                    sourceToGetThoseElements, elementToSet.getDataTypeNode().getQualifiedName(), StringUtils.addSpaces(indent, 2), usedVariableName);

            result.append(mappingCode)
                    .append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), childObjectToSetVarName));
            result.append("\n");
        }
        // if type == list
        else if (elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            result.append("\n");
            String listToGetSource = JavaCommandUtils.generateProtoListGetter(sourceToGet, elementToGet.getName());
            String listToAddVarName = NameUtils.generateUniqueRandomName(elementToSet.getDataTypeNode().getPresentableName(), usedVariableName);

            ListMappingGenerator listMappingGenerator = new ListMappingGenerator();
            String mappingCode = listMappingGenerator.generateMappingCode(listToGetSource, listToAddVarName, elementToSet, elementToGet,
                    StringUtils.addSpaces(indent, 2), usedVariableName);

            result.append(mappingCode)
                    .append(JavaCommandUtils.generateSetter(objectToSetVarName, elementToSet.getName(), listToAddVarName));
            result.append("\n");
        }

        return result.toString();

    }
}
