package com.github.codegenerator.javatojava.buildertype;

import com.github.codegenerator.javatojava.buildertype.subgenerator.ObjectMappingGenerator;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuilderTypeCodeGenerator {
    public String generateMappingCode(List<ElementNode> elementsToGet, List<ElementNode> elementsToSet, String objectToGetVariableName,
                                      String objectToSetQualifiedName) {
        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
        String mappingCode = objectMappingGenerator.generateMappingCode(elementsToSet, elementsToGet,
                objectToGetVariableName, objectToSetQualifiedName, "", usedVariableName);
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}
