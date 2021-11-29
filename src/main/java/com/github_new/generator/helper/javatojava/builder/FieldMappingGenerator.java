package com.github_new.generator.helper.javatojava.builder;

import com.github_new.domain.DataTypeNode;
import com.github_new.domain.ElementNode;
import com.github_new.generator.helper.utils.DataTypeNodeUtils;
import com.github_new.generator.helper.utils.JavaCommandUtils;
import com.github_new.generator.helper.utils.StringUtils;
import lombok.Builder;

import java.util.Set;

public class FieldMappingGenerator {
    @Builder
    public static class Args {
        private final ElementNode elementToSet;
        private final ElementNode elementToGet;
        private final String sourceToGet;
        private final String indent;
        private final Set<String> usedVariableName;
    }

    public static String generateMappingCode(Args args) {


        StringBuilder result = new StringBuilder();
        result.append(args.indent);

        // if 2 elements are exactly match
        if (DataTypeNodeUtils.qualifiedNameEqual(
                args.elementToSet.getDataTypeNode(),
                args.elementToGet.getDataTypeNode()
        )) {
            result.append(forMatchingElements(args));
        }
        // if type == enum
        else if (args.elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            result.append(forEnum(args));
        }
        // if type == map
        else if (args.elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.MAP) {
            result.append(forMap(args));
        }
        // if type == others
        else if (args.elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OTHERS) {
            result.append(forOthers(args));
        }
        // if type == object
        else if (args.elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            result.append(forObject(args));
        }
        // if type == list
        else if (args.elementToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            result.append(forList(args));
        }

        return result.toString();
    }

    private static String forMatchingElements(Args args) {
        String getter = JavaCommandUtils.generateGetter(args.sourceToGet, args.elementToGet.getName());
        return JavaCommandUtils.dotField(args.elementToSet.getName(), getter);
    }

    private static String forEnum(Args args) {
        String enumConversion = JavaCommandUtils.generateEnumConverter(
                args.elementToSet.getDataTypeNode().getQualifiedName(),
                args.sourceToGet,
                args.elementToGet.getName());
        return JavaCommandUtils.dotField(args.elementToSet.getName(), enumConversion);
    }

    private static String forMap(Args args) {
        String mapToSet = JavaCommandUtils.generateGetter(args.sourceToGet, args.elementToGet.getName());
        return JavaCommandUtils.dotField(args.elementToSet.getName(), mapToSet);
    }

    private static String forOthers(Args args) {
        String getter = JavaCommandUtils.generateGetter(args.sourceToGet, args.elementToGet.getName());
        return JavaCommandUtils.dotField(args.elementToSet.getName(), getter);
    }

    private static String forObject(Args args) {
        String sourceToGetThoseElements = JavaCommandUtils.generateGetter(args.sourceToGet, args.elementToGet.getName());
        ObjectMappingGenerator objectMappingGenerator = ObjectMappingGenerator
                .builder()
                .elementsToSet(args.elementToSet.getChildren())
                .elementsToGet(args.elementToGet.getChildren())
                .sourceToGet(sourceToGetThoseElements)
                .objectToSetQualifiedName(args.elementToSet.getDataTypeNode().getQualifiedName())
                .indent(StringUtils.addSpaces(args.indent, 2))
                .usedVariableName(args.usedVariableName)
                .build();
        String valueToSet = objectMappingGenerator.generateMappingCode();
        return JavaCommandUtils.dotField(args.elementToSet.getName(), valueToSet);
    }

    private static String forList(Args args) {
        String listSource = JavaCommandUtils.generateGetter(args.sourceToGet, args.elementToGet.getName());
        ListMappingGenerator listMappingGenerator = ListMappingGenerator.builder()
                .listSource(listSource)
                .elementToSet(args.elementToSet)
                .elementToGet(args.elementToGet)
                .indent(args.indent)
                .usedVariableName(args.usedVariableName)
                .build();
        String valueToSet = listMappingGenerator.generateMappingCode();
        return JavaCommandUtils.dotField(args.elementToSet.getName(), valueToSet);
    }
}