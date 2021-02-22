package com.github.parser.java.impl;

import com.github.enums.DataType;
import com.github.model.ElementNode;
import com.github.parser.java.JavaParser;
import com.github.parser.java.utils.PsiFieldTypeToDataType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JavaParserImpl implements JavaParser {

    private static final Logger logger = LoggerFactory.getLogger(JavaParserImpl.class);

    @Override
    public List<ElementNode> parse(String qualifiedClassName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
        List<ElementNode> elementTree = new ArrayList<>();

        PsiClass psiClass = javaPsiFacade.findClass(qualifiedClassName, globalSearchScope);

        if (psiClass == null) {
            logger.error("Can't find class " + qualifiedClassName);
            return elementTree;
        }

        PsiField[] psiFields = psiClass.getAllFields();

        for (PsiField field : psiFields) {
            // this is also qualified class name in Java
            String fieldName = field.getName();

            DataType dataType = PsiFieldTypeToDataType.convert(field.getType());

            ElementNode elementNode = new ElementNode();
            elementNode.setName(fieldName);
            elementNode.setDataType(dataType);

            if (dataType == DataType.OBJECT) {
                elementNode.setChildren(this.parse(dataType.getQualifiedName(), javaPsiFacade, globalSearchScope));
            } else if (dataType == DataType.ARRAY) {
                // in case of List<List<...List<Object>...>>
                DataType innermostDataType = dataType.getInnerElementDataType();

                while (innermostDataType.getInnerElementDataType() == DataType.ARRAY)
                    innermostDataType = innermostDataType.getInnerElementDataType();

                if (innermostDataType == DataType.OBJECT)
                    elementNode.setChildren(this.parse(innermostDataType.getQualifiedName(), javaPsiFacade, globalSearchScope));
            }
        }

        return elementTree;
    }
}
