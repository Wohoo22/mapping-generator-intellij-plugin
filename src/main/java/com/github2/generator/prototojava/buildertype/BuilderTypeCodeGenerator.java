package com.github2.generator.prototojava.buildertype;

import com.github.model.ElementNode;
import com.github.utils.JavaCommandUtils;
import com.github2.generator.prototojava.buildertype.subgenerator.ObjectMappingGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuilderTypeCodeGenerator {
    public String generateMappingCode(List<ElementNode> protoElementTree, List<ElementNode> javaElementTree, String protoObjectVariableName,
                                      String javaQualifiedClassName) {

        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
        String mappingCode = objectMappingGenerator.generateMappingCode(javaElementTree, protoElementTree, protoObjectVariableName,
                javaQualifiedClassName, "", usedVariableName);
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}
