package com.github.parser.java;

import com.github.enums.DataType;
import com.github.model.ElementNode;
import com.github.parser.java.utils.PsiFieldTypeToDataType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JavaParser {

    private static final Logger logger = LoggerFactory.getLogger(JavaParser.class);

    public static List<ElementNode> parse(String qualifiedClassName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {
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

            DataType dataType = PsiFieldTypeToDataType.convert(field.getType(), fieldName, javaPsiFacade, globalSearchScope);

            ElementNode elementNode = new ElementNode();
            elementNode.setName(fieldName);
            elementNode.setDataType(dataType);

            if (dataType == DataType.OBJECT) {
                elementNode.setChildren(parse(dataType.getQualifiedName(), javaPsiFacade, globalSearchScope));
            } else if (dataType == DataType.ARRAY) {
                // in case of List<List<...List<Object>...>>
                DataType innermostDataType = dataType.getInnerElementDataType();

                while (innermostDataType.getInnerElementDataType() == DataType.ARRAY)
                    innermostDataType = innermostDataType.getInnerElementDataType();

                if (innermostDataType == DataType.OBJECT)
                    elementNode.setChildren(parse(innermostDataType.getQualifiedName(), javaPsiFacade, globalSearchScope));
            }

            elementTree.add(elementNode);
        }

        return elementTree;
    }
}
