package com.github.parser.java;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.parser.java.utils.PsiFieldTypeToDataTypeNode;
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

    public List<ElementNode> parse(String qualifiedClassName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {

        List<ElementNode> elementNodes = new ArrayList<>();

        PsiClass psiClass = javaPsiFacade.findClass(qualifiedClassName, globalSearchScope);

        if (psiClass == null) {
            logger.error("Can't find class " + qualifiedClassName);
            return elementNodes;
        }

        // parse super class (if there is any)
        PsiClass[] parentClasses = psiClass.getSupers();
        for (PsiClass parentClass : parentClasses) {
            List<ElementNode> parentElementNodes = parse(parentClass.getQualifiedName(), javaPsiFacade, globalSearchScope);
            elementNodes.addAll(parentElementNodes);
        }

        // parse fields
        PsiField[] psiFields = psiClass.getAllFields();
        for (PsiField field : psiFields) {
            // this is also qualified class name in Java
            String fieldName = field.getName();

            DataTypeNode dataTypeNode = PsiFieldTypeToDataTypeNode.convert(field.getType(), fieldName, javaPsiFacade, globalSearchScope);

            ElementNode elementNode = new ElementNode();
            elementNode.setName(fieldName);
            elementNode.setDataTypeNode(dataTypeNode);

            assert dataTypeNode != null;

            if (dataTypeNode.getDataType() == DataTypeNode.DataType.OBJECT) {
                elementNode.setChildren(parse(dataTypeNode.getQualifiedName(), javaPsiFacade, globalSearchScope));
            } else if (dataTypeNode.getDataType() == DataTypeNode.DataType.LIST) {
                // in case of List<List<...List<Object>...>>
                DataTypeNode innermostDataTypeNode = dataTypeNode.getChild();

                while (innermostDataTypeNode.getDataType() == DataTypeNode.DataType.LIST)
                    innermostDataTypeNode = innermostDataTypeNode.getChild();

                if (innermostDataTypeNode.getDataType() == DataTypeNode.DataType.OBJECT)
                    elementNode.setChildren(parse(innermostDataTypeNode.getQualifiedName(), javaPsiFacade, globalSearchScope));
            }

            elementNodes.add(elementNode);
        }

        return elementNodes;
    }
}
