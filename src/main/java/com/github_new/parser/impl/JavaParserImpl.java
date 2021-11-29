package com.github_new.parser.impl;

import com.github_new.domain.DataTypeNode;
import com.github_new.domain.ElementNode;
import com.github_new.parser.interfaces.JavaParser;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaParserImpl implements JavaParser {

    private final JavaPsiFacade javaPsiFacade;
    private final GlobalSearchScope globalSearchScope;

    public JavaParserImpl(JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        this.javaPsiFacade = javaPsiFacade;
        this.globalSearchScope = globalSearchScope;
    }


    private static final Logger logger = LoggerFactory.getLogger(JavaParser.class);


    @Override
    public List<ElementNode> parse(String qualifiedClassName) {
        List<ElementNode> elementNodes = new ArrayList<>();

        PsiClass psiClass = javaPsiFacade.findClass(qualifiedClassName, globalSearchScope);
        if (psiClass == null) {
            logger.error("Can't find class " + qualifiedClassName);
            return elementNodes;
        }

        // parse super class (if there is any)
        PsiClass[] parentClasses = psiClass.getSupers();
        for (PsiClass parentClass : parentClasses) {
            List<ElementNode> parentElementNodes = parse(parentClass.getQualifiedName());
            elementNodes.addAll(parentElementNodes);
        }

        // parse fields
        PsiField[] psiFields = psiClass.getAllFields();
        for (PsiField field : psiFields) {
            // this is also qualified class name in Java
            String fieldName = field.getName();
            DataTypeNode dataTypeNode = psiFieldToDataTypeNode(field.getType(), fieldName, javaPsiFacade, globalSearchScope);

            ElementNode elementNode = new ElementNode();
            elementNode.setName(fieldName);
            elementNode.setDataTypeNode(dataTypeNode);

            assert dataTypeNode != null;

            if (dataTypeNode.getDataType() == DataTypeNode.DataType.OBJECT)
                elementNode.setChildren(this.parse(dataTypeNode.getQualifiedName()));

            else if (dataTypeNode.getDataType() == DataTypeNode.DataType.LIST && dataTypeNode.getChild().getDataType() == DataTypeNode.DataType.OBJECT)
                elementNode.setChildren(this.parse(dataTypeNode.getChild().getQualifiedName()));

            elementNodes.add(elementNode);
        }

        return elementNodes;
    }

    public static DataTypeNode psiFieldToDataTypeNode(PsiType psiType, String fieldName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        String qualifiedName = psiType.getCanonicalText();
        String presentableName = psiType.getPresentableText();

        if (isPrimitive(qualifiedName)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.OTHERS);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        if (isMap(qualifiedName)) {
            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.MAP);
            res.setQualifiedName(qualifiedName);
            res.setPresentableName(presentableName);
            return res;
        }

        if (isList(qualifiedName)) {
            PsiType genericType = PsiUtil.extractIterableTypeParameter(psiType, false);

            if (genericType == null) {
                logger.error("Can't find generic type of field: " + fieldName);
                return null;
            }

            DataTypeNode innerElementDataTypeNode = psiFieldToDataTypeNode(genericType, fieldName, javaPsiFacade, globalSearchScope);

            DataTypeNode res = new DataTypeNode();
            res.setDataType(DataTypeNode.DataType.LIST);
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

    private static boolean isPrimitive(String qualifiedName) {
        String[] types = {"boolean", "char", "int", "double", "float", "byte", "short", "long", "java.lang.String"};
        return Arrays.asList(types).contains(qualifiedName);
    }

    private static boolean isMap(String qualifiedName) {
        String[] types = {"java.util.Map", "java.util.HashMap"};
        for (String type : types)
            if (qualifiedName.startsWith(type)) return true;
        return false;
    }

    private static boolean isList(String qualifiedName) {
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
