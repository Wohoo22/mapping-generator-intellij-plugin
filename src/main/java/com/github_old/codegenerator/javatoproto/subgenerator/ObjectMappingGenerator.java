package com.github_old.codegenerator.javatoproto.subgenerator;

import com.github_old.model.ElementNode;
import com.github_old.utils.ElementNodeUtils;
import com.github_old.utils.JavaCommandUtils;

import java.util.List;
import java.util.Set;

public class ObjectMappingGenerator {
    public String generateMappingCode(List<ElementNode> elementsToSet, List<ElementNode> elementsToGet,
                                      String sourceToGet, String objectToSetQualifiedName, String indent, Set<String> usedVariableName) {
        StringBuilder result = new StringBuilder();

        // open the builder
        result.append(indent);
        result.append(JavaCommandUtils.openProtoObjectBuilder(objectToSetQualifiedName));

        // generate mapping for fields
        FieldMappingGenerator fieldMappingGenerator = new FieldMappingGenerator();
        for (ElementNode elementToSet : elementsToSet) {
            ElementNode elementToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(elementToSet, elementsToGet);
            if (elementToGet == null) continue;
            result.append(fieldMappingGenerator.generateMappingCode(elementToSet, elementToGet, sourceToGet, indent, usedVariableName));
        }

        // close the builder
        result.append(indent);
        result.append(JavaCommandUtils.closeObjectBuilder());

        return result.toString();
    }
}
