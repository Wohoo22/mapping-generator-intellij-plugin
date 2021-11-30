package com.github_old.codegenerator.javatojava.normaltype;

import com.github_old.codegenerator.javatojava.normaltype.subgenerator.ObjectMappingGenerator;
import com.github_old.model.ElementNode;
import com.github_old.utils.JavaCommandUtils;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
public class NormalTypeCodeGenerator {
    private final List<ElementNode> elementsToGet;
    private final List<ElementNode> elementsToSet;
    private final String objectToGetVariableName;
    private final String objectToSetQualifiedName;

    public String generateMappingCode() {
        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator.builder()
                .elementsToSet(elementsToSet)
                .elementsToGet(elementsToGet)
                .objectToSetVarName("targetObject")
                .sourceToGet(objectToGetVariableName)
                .objectToSetQualifiedName(objectToSetQualifiedName)
                .indent("")
                .usedVariableName(usedVariableName)
                .build();
        String mappingCode = objectMappingGenerator.generateMappingCode();
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}