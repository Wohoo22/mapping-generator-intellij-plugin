package com.github_old.codegenerator.prototojava.normaltype;

import com.github_old.codegenerator.prototojava.normaltype.subgenerator.ObjectMappingGenerator;
import com.github_old.model.ElementNode;
import com.github_old.utils.JavaCommandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalTypeCodeGenerator {
    public String generateMappingCode(List<ElementNode> protoElementTree, List<ElementNode> javaElementTree, String protoObjectVariableName,
                                      String javaQualifiedClassName) {

        StringBuilder result = new StringBuilder();

        Set<String> usedVariableName = new HashSet<>();

        ObjectMappingGenerator objectMappingGenerator = new ObjectMappingGenerator();
        String mappingCode = objectMappingGenerator.generateMappingCode(javaElementTree, protoElementTree, "targetObject",
                protoObjectVariableName, javaQualifiedClassName, "", usedVariableName);
        result.append(mappingCode);

        // EOC
        result.append(JavaCommandUtils.eoc());

        return result.toString();
    }
}
