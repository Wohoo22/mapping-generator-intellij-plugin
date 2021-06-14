package com.github.codegenerator.javatojava.buildertype.subgenerator;

import com.github.model.ElementNode;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public class ObjectMappingGenerator {
    private final List<ElementNode> elementsToSet;
    private final List<ElementNode> elementsToGet;
    private final String sourceToGet;
    private final String objectToSetQualifiedName;
    private final String indent;
    private final Set<String> usedVariableName;

    public String generateMappingCode() {
        StringBuilder result = new StringBuilder();

        // open the builder
        result.append(indent);
        result.append(JavaCommandUtils.openObjectBuilder(objectToSetQualifiedName));

        // generate mapping for fields
        for (ElementNode elementToSet : elementsToSet) {
            ElementNode elementToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(elementToSet, elementsToGet);
            if (elementToGet == null)
                continue;
            result.append(FieldMappingGenerator.generateMappingCode(FieldMappingGenerator.Args.builder()
                    .elementToSet(elementToSet)
                    .elementToGet(elementToGet)
                    .sourceToGet(sourceToGet)
                    .indent(indent)
                    .usedVariableName(usedVariableName)
                    .build()));
        }

        // close the builder
        result.append(indent);
        result.append(JavaCommandUtils.closeObjectBuilder());

        return result.toString();
    }
}
