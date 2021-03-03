package com.github.codegenerator.javatojava.normaltype;

import com.github.codegenerator.javatojava.normaltype.subgenerator.ObjectMappingGenerator;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalTypeCodeGenerator {
    public String generateMappingCode(List<ElementNode> elementsToGet, List<ElementNode> elementsToSet, String objectToGetVariableName,
                                      String objectToSetQualifiedName) {
        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
        String mappingCode = objectMappingGenerator.generateMappingCode(elementsToSet, elementsToGet, "targetObject",
                objectToGetVariableName, objectToSetQualifiedName, "", usedVariableName);
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}
