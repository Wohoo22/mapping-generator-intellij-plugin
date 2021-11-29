package com.github_old.parser.java;

import com.github_old.model.DataTypeNode;
import com.github_old.model.ElementNode;
import com.github_old.parser.java.utils.PsiFieldTypeToDataTypeNode;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Builder
public class JavaParser {

    private static final Logger logger = LoggerFactory.getLogger(JavaParser.class);

    private final JavaPsiFacade javaPsiFacade;
    private final GlobalSearchScope globalSearchScope;

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
            DataTypeNode dataTypeNode = PsiFieldTypeToDataTypeNode.convert(field.getType(), fieldName, javaPsiFacade, globalSearchScope);

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
}
