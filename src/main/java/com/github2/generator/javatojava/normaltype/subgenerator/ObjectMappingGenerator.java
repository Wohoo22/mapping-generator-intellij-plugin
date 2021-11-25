package com.github2.generator.javatojava.normaltype.subgenerator;

import com.github2.model.ElementNode;
import com.github2.utils.ElementNodeUtils;
import com.github2.utils.JavaCommandUtils;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public class ObjectMappingGenerator {
    private final List<ElementNode> elementsToSet;
    private final List<ElementNode> elementsToGet;
    private final String objectToSetVarName;
    private final String sourceToGet;
    private final String objectToSetQualifiedName;
    private final String indent;
    private final Set<String> usedVariableName;

    public String generateMappingCode() {

        StringBuilder result = new StringBuilder();

        // declare object to set
        result.append(indent).append(JavaCommandUtils.generateObjectDeclaration(objectToSetQualifiedName, objectToSetVarName));

        // generate mapping for fields
        for (ElementNode elementToSet : elementsToSet) {
            ElementNode elementToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(elementToSet, elementsToGet);
            if (elementToGet == null) continue;
            FieldMappingGenerator fieldMappingGenerator = FieldMappingGenerator.builder()
                    .elementToSet(elementToSet)
                    .elementToGet(elementToGet)
                    .objectToSetVarName(objectToSetVarName)
                    .sourceToGet(sourceToGet)
                    .indent(indent)
                    .usedVariableName(usedVariableName)
                    .build();
            result.append(fieldMappingGenerator.generateMappingCode());
        }

        return result.toString();
    }
}
