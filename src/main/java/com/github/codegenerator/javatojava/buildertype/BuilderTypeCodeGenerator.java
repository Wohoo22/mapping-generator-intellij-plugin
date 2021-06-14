package com.github.codegenerator.javatojava.buildertype;

import com.github.codegenerator.javatojava.buildertype.subgenerator.ObjectMappingGenerator;
import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
public class BuilderTypeCodeGenerator {
    private final List<ElementNode> elementsToSet;
    private final List<ElementNode> elementsToGet;
    private final String objectToGetVariableName;
    private final String objectToSetQualifiedName;
    private final Set<String> referredQualifiedName;

    public String generateMappingCode() {
        StringBuilder result = new StringBuilder();
        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                .elementsToSet(elementsToSet)
                .elementsToGet(elementsToGet)
                .sourceToGet(objectToGetVariableName)
                .objectToSetQualifiedName(objectToSetQualifiedName)
                .objectToSetPresentableName(objectToSetQualifiedName)
                .indent("")
                .usedVariableName(usedVariableName)
                .referredQualifiedName(referredQualifiedName)
                .build();
        String mappingCode = objectMappingGenerator.generateMappingCode();
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}
