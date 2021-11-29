package com.github_new.generator.ports;

import com.github_new.domain.ElementNode;
import lombok.Builder;

import java.util.List;

public class JavaToJavaGeneratorPort {
    @Builder
    public static class GenerateInput {
        public final String objectToSetQualifiedName;
        public final String objectToGetVariableName;
        public final String objectToGetQualifiedName;
        public final List<ElementNode> elementsToSet;
        public final List<ElementNode> elementsToGet;
        public final MappingType mappingType;

        public enum MappingType {
            BUILDER, NORMAL
        }
    }
}
