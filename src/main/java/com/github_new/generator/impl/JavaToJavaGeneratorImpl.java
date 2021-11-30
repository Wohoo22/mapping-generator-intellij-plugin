package com.github_new.generator.impl;

import com.github_new.domain.GlobalCounter;
import com.github_new.generator.helper.utils.JavaCommandUtils;
import com.github_new.generator.interfaces.JavaToJavaGenerator;
import com.github_new.generator.ports.JavaToJavaGeneratorPort;

import java.util.HashSet;
import java.util.Set;

public class JavaToJavaGeneratorImpl implements JavaToJavaGenerator {
    @Override
    public String generate(JavaToJavaGeneratorPort.GenerateInput input) {
        GlobalCounter.reset();
        if (input.mappingType == JavaToJavaGeneratorPort.GenerateInput.MappingType.NORMAL) {
            return forNormal(input);
        }
        return forBuilder(input);
    }

    public String forNormal(JavaToJavaGeneratorPort.GenerateInput input) {
        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        com.github_new.generator.helper.javatojava.normal.ObjectMappingGenerator objectMappingGenerator = com.github_new.generator.helper.javatojava.normal.ObjectMappingGenerator.builder()
                .elementsToSet(input.elementsToSet)
                .elementsToGet(input.elementsToGet)
                .objectToSetVarName("resultObject")
                .sourceToGet(input.objectToGetVariableName)
                .objectToSetQualifiedName(input.objectToSetQualifiedName)
                .indent("")
                .usedVariableName(usedVariableName)
                .build();
        String mappingCode = objectMappingGenerator.generateMappingCode();
        result.append(mappingCode);

        return result.toString();
    }

    public String forBuilder(JavaToJavaGeneratorPort.GenerateInput input) {
        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        com.github_new.generator.helper.javatojava.builder.ObjectMappingGenerator objectMappingGenerator = com.github_new.generator.helper.javatojava.builder.ObjectMappingGenerator.builder()
                .elementsToSet(input.elementsToSet)
                .elementsToGet(input.elementsToGet)
                .sourceToGet(input.objectToGetVariableName)
                .objectToSetQualifiedName(input.objectToSetQualifiedName)
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
