package com.github_old.utils;

public class JavaCommandUtils {

    public static String generateObjectDeclaration(String qualifiedName, String varName) {
        String pattern = "{q_name} {var_name} = new {q_name}(); \n";
        return pattern.replace("{q_name}", qualifiedName).replace("{var_name}", varName);
    }

    public static String openObjectBuilder(String qualifiedName) {
        String pattern = "{q_name}.builder() \n";
        return pattern.replace("{q_name}", qualifiedName);
    }

    public static String openProtoObjectBuilder(String qualifiedName) {
        String pattern = "{q_name}.newBuilder() \n";
        return pattern.replace("{q_name}", qualifiedName);
    }

    public static String closeObjectBuilder() {
        return ".build() \n";
    }

    public static String generateSetterGetter(String objectToGetVarName, String objectToGetFieldName,
                                              String objectToSetVarName, String objectToSetFieldName) {
        String pattern = "{obj_to_set}.set{fld_to_set}({obj_to_get}.get{fld_to_get}()); \n";
        return pattern.replace("{obj_to_set}", objectToSetVarName).replace("{fld_to_set}", NameUtils.toUpperCaseFirstChar(objectToSetFieldName))
                .replace("{obj_to_get}", objectToGetVarName).replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(objectToGetFieldName));
    }


    public static String generateEnumConverter(String destinationEnumQualifiedName, String srcToGet, String fieldToGetName) {
        String pattern = "{des_q_name}.valueOf({obj_to_get}.get{fld_to_get}().toString())";
        return pattern.replace("{des_q_name}", destinationEnumQualifiedName).replace("{obj_to_get}", srcToGet)
                .replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(fieldToGetName));
    }

    public static String generateEnumConverter(String desEnumQualifiedName, String srcEnumVarName) {
        String pattern = "{des_q_name}.valueOf({var_name}.toString())";
        return pattern.replace("{des_q_name}", desEnumQualifiedName).replace("{var_name}", srcEnumVarName);
    }

    public static String generateSetter(String objectToSetVarName, String fieldToSetName, String valueToSet) {
        String pattern = "{obj_to_set}.set{fld_to_set}({val_to_set}); \n";
        return pattern.replace("{obj_to_set}", objectToSetVarName).replace("{fld_to_set}", NameUtils.toUpperCaseFirstChar(fieldToSetName))
                .replace("{val_to_set}", valueToSet);
    }

    public static String generateGetter(String sourceToGet, String fieldToGetName) {
        String pattern = "{obj_to_get}.get{fld_to_get}()";
        return pattern.replace("{obj_to_get}", sourceToGet).replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(fieldToGetName));
    }

    public static String generateForEachWithoutCloseBrace(String dataTypeQualifiedName, String varName, String collectionVarName) {

        String pattern = "for ({data_type} {var_name} : {collection}) { \n";
        return pattern.replace("{data_type}", dataTypeQualifiedName).replace("{var_name}", varName)
                .replace("{collection}", collectionVarName);
    }


    public static String generateListDeclaration(String dataTypeQualifiedName, String varName) {
        String pattern = "{data_type} {var_name} = new java.util.ArrayList<>(); \n";
        return pattern.replace("{data_type}", dataTypeQualifiedName).replace("{var_name}", varName);
    }

    public static String generateListAdd(String listToAddVarName, String valueToAdd) {
        String pattern = "{var_name}.add({val_to_add}); \n";
        return pattern.replace("{var_name}", listToAddVarName).replace("{val_to_add}", valueToAdd);
    }

    public static String generateDeclarationByGetter(String dataType, String varName, String objToGetVarName, String fieldToGet) {
        String pattern = "{data_type} {var_name} = {getter}; \n";
        return pattern.replace("{data_type}", dataType)
                .replace("{var_name}", varName)
                .replace("{getter}", generateGetter(objToGetVarName, fieldToGet));
    }

    public static String closeLoop() {
        return "} \n";
    }

    public static String generateBuilderSetter(String fieldName, String valueToSet) {
        String pattern = ".set{f_name}({value}) \n";
        return pattern.replace("{f_name}", NameUtils.toUpperCaseFirstChar(fieldName))
                .replace("{value}", valueToSet);
    }

    public static String dotField(String fieldName, String value) {
        String pattern = ".{f_name}({value}) \n";
        return pattern.replace("{f_name}", fieldName)
                .replace("{value}", value);
    }

    public static String generateAddAll(String fieldName, String valueToAdd) {
        String pattern = ".addAll{f_name}({value}) \n";
        return pattern.replace("{f_name}", NameUtils.toUpperCaseFirstChar(fieldName))
                .replace("{value}", valueToAdd);
    }

    public static String generatePutAllMap(String mapName, String valueToPut) {
        String pattern = ".putAll{map}({value}) \n";
        return pattern.replace("{map}", NameUtils.toUpperCaseFirstChar(mapName))
                .replace("{value}", valueToPut);
    }

    public static String openStreamMap(String listSource) {
        String pattern = "{list}.stream().map( \n";
        return pattern.replace("{list}", listSource);
    }

    public static String closeStreamMap() {
        return ").collect(java.util.stream.Collectors.toList()) \n";
    }

    public static String generateArrow(String source) {
        String pattern = "{src} -> \n";
        return pattern.replace("{src}", source);
    }

    public static String eoc() {
        return "; \n";
    }

    public static String generateProtoListGetter(String sourceToGet, String fieldToGetName) {
        String pattern = "{obj_to_get}.get{fld_to_get}List()";
        return pattern.replace("{obj_to_get}", sourceToGet)
                .replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(fieldToGetName));
    }

    public static String generateProtoMapGetter(String sourceToGet, String fieldToGetName) {
        String pattern = "{obj_to_get}.get{fld_to_get}Map()";
        return pattern.replace("{obj_to_get}", sourceToGet)
                .replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(fieldToGetName));
    }
}
