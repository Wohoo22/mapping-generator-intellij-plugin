package com.github.utils;

public class JavaCommandUtils {
    private static final String sp = " ";
    private static final String lb = "\n";
    private static final String eoc = ";";
    private static final String dot = ".";


    public static String generateObjectDeclaration(String qualifiedName, String varName) {
        return qualifiedName + sp + varName + sp + "=" + sp + "new" + sp + qualifiedName + "()" + eoc + lb;
    }

    public static String generateSetterGetter(String objectToGetVarName, String objectToGetFieldName,
                                              String objectToSetVarName, String objectToSetFieldName) {

        return objectToSetVarName + "." + "set" + NameUtils.toUpperCaseFirstChar(objectToSetFieldName) + "("
                + objectToGetVarName + "." + "get" + NameUtils.toUpperCaseFirstChar(objectToGetFieldName) + "()" + ")"
                + eoc + lb
                ;
    }


    public static String generateEnumConverter(String desEnumQualifiedName, String srcObjectToGetVarName, String enumFieldToGetName) {
        return desEnumQualifiedName + "." + "valueOf" + "(" +
                srcObjectToGetVarName + "." + "get" + NameUtils.toUpperCaseFirstChar(enumFieldToGetName) + "()" + "." + "toString()" +
                ")";
    }

    public static String generateEnumConverter(String desEnumQualifiedName, String srcEnumVarName) {
        return desEnumQualifiedName + "." + "valueOf(" + srcEnumVarName + ".toString()" + ")";
    }

    public static String generateSetter(String objectToSetVarName, String fieldToSetName, String valueToSet) {
        return objectToSetVarName + "." + "set" + NameUtils.toUpperCaseFirstChar(fieldToSetName) + "(" + valueToSet + ")" + eoc + lb;
    }

    public static String generateGetter(String objectToGetVarName, String fieldToGetName) {
        return objectToGetVarName + dot + "get" + NameUtils.toUpperCaseFirstChar(fieldToGetName) + "()";
    }

    public static String generateForEachWithoutCloseBrace(String dataTypeQualifiedName, String varName, String listVarName) {
        return "for (" + dataTypeQualifiedName + " " + varName + " : " + listVarName + ") {" + lb;

    }


    public static String generateListDeclaration(String dataTypeName, String varName) {
        return dataTypeName + " " + varName + " = new ArrayList<>()" + eoc + lb;
    }

    public static String generateListAdd(String listToAddVarName, String valueToAdd) {
        return listToAddVarName + "." + "add(" + valueToAdd + ")" + eoc + lb;
    }
}
