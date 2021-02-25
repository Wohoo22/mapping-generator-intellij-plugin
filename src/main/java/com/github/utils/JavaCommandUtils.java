package com.github.utils;

public class JavaCommandUtils {
    private static final String sp = " ";
    private static final String lb = "\n";
    private static final String eoc = ";";


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


    public static String generateEnumConverter(String desEnumQualifiedName, String objectToGetVarName, String fieldToGetName) {
        return desEnumQualifiedName + "." + "valueOf" + "(" +
                objectToGetVarName + "." + "get" + NameUtils.toUpperCaseFirstChar(fieldToGetName) + "()" + "." + "toString()" +
                ")";
    }

    public static String generateSetter(String objectToSetVarName, String fieldToSetName, String valueToSet) {
        return objectToSetVarName + "." + "set" + NameUtils.toUpperCaseFirstChar(fieldToSetName) + "(" + valueToSet + ")" + eoc + lb;
    }

}
