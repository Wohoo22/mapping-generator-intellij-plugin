package com.github.utils;

public class JavaCommandUtils {

    public static String generateObjectDeclaration(String qualifiedName, String varName) {
        String pattern = "{q_name} {var_name} = new {q_name}(); \n";
        return pattern.replace("{q_name}", qualifiedName).replace("{var_name}", varName);
    }

    public static String generateSetterGetter(String objectToGetVarName, String objectToGetFieldName,
                                              String objectToSetVarName, String objectToSetFieldName) {
        String pattern = "{obj_to_set}.set{fld_to_set}({obj_to_get}.get{fld_to_get}()); \n";
        return pattern.replace("{obj_to_set}", objectToSetVarName).replace("{fld_to_set}", NameUtils.toUpperCaseFirstChar(objectToSetFieldName))
                .replace("{obj_to_get}", objectToGetVarName).replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(objectToGetFieldName));
    }


    public static String generateEnumConverter(String desEnumQualifiedName, String srcObjectToGetVarName, String enumFieldToGetName) {
        String pattern = "{des_q_name}.valueOf({obj_to_get}.get{fld_to_get}().toString())";
        return pattern.replace("{des_q_name}", desEnumQualifiedName).replace("{obj_to_get}", srcObjectToGetVarName)
                .replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(enumFieldToGetName));
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

    public static String generateGetter(String objectToGetVarName, String fieldToGetName) {
        String pattern = "{obj_to_get}.get{fld_to_get}()";
        return pattern.replace("{obj_to_get}", objectToGetVarName).replace("{fld_to_get}", NameUtils.toUpperCaseFirstChar(fieldToGetName));
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
}
