package com.github.codegenerator.prototojava.normaltype.subgenerator;

import com.github.model.ElementNode;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;

import java.util.List;
import java.util.Set;

public class ObjectMappingGenerator {
    public String generateMappingCode(List<ElementNode> elementsToSet, List<ElementNode> elementsToGet, String objectToSetVarName,
                                      String sourceToGet, String objectToSetQualifiedName, String indent, Set<String> usedVariableName) {

        StringBuilder result = new StringBuilder();

        // declare object to set
        result.append(indent).append(JavaCommandUtils.generateObjectDeclaration(objectToSetQualifiedName, objectToSetVarName));

        // generate mapping for fields
        FieldMappingGenerator fieldMappingGenerator = new FieldMappingGenerator();
        for (ElementNode elementToSet : elementsToSet) {
            ElementNode elementToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(elementToSet, elementsToGet);
            if (elementToGet == null) continue;
            result.append(fieldMappingGenerator.generateMappingCode(elementToSet, elementToGet, objectToSetVarName, sourceToGet, indent, usedVariableName));
        }

        return result.toString();
    }
}
