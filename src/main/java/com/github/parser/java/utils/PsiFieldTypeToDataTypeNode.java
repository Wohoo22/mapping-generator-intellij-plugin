package com.github.parser.java.utils;

import com.github.model.DataTypeNode;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public class PsiFieldTypeToDataTypeNode {

    private static final Logger logger = LoggerFactory.getLogger(PsiFieldTypeToDataTypeNode.class);

    public static DataTypeNode convert(PsiType psiType, String fieldName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        String qualifiedName = psiType.getCanonicalText();
        String presentableName = psiType.getPresentableText();

        if (isPrimitiveType(qualifiedName)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.OTHERS);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        if (isMapType(qualifiedName)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.MAP);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        if (isArray(qualifiedName)) {
            PsiType genericType = PsiUtil.extractIterableTypeParameter(psiType, false);

            if (genericType == null) {
                logger.error("Can't find generic type of field: " + fieldName);
                return null;
            }

            DataTypeNode innerElementDataTypeNode = convert(genericType, fieldName, javaPsiFacade, globalSearchScope);

            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.ARRAY);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            res.setChild(innerElementDataTypeNode);
            return res;
        }

        if (isEnum(qualifiedName, javaPsiFacade, globalSearchScope)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.ENUM);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        if (isObject(qualifiedName, javaPsiFacade, globalSearchScope)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.OBJECT);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        DataTypeNode res = new DataTypeNode();
        res.setDataType(DataTypeNode.DataType.OTHERS);
        res.setQualifiedName(qualifiedName);
        res.setPresentableName(presentableName);
        return res;
    }

    private static boolean isPrimitiveType(String qualifiedName) {
        String[] types = {"boolean", "char", "int", "double", "float", "byte", "short", "long", "java.lang.String"};
        return Arrays.asList(types).contains(qualifiedName);
    }

    private static boolean isMapType(String qualifiedName) {
        String[] types = {"java.util.Map", "java.util.HashMap"};
        for (String type : types)
            if (qualifiedName.startsWith(type)) return true;
        return false;
    }

    private static boolean isArray(String qualifiedName) {
        String[] types = {"java.util.List", "java.util.ArrayList"};
        for (String type : types)
            if (qualifiedName.startsWith(type)) return true;
        return false;
    }

    private static boolean isEnum(String qualifiedName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        PsiClass psiClass = javaPsiFacade.findClass(qualifiedName, globalSearchScope);
        if (psiClass == null) return false;
        return psiClass.isEnum();
    }

    private static boolean isObject(String qualifiedName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        PsiClass psiClass = javaPsiFacade.findClass(qualifiedName, globalSearchScope);
        return psiClass != null;
    }

}
