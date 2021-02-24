package com.github.codegenerator.javatojava;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.parser.java.JavaParser;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaToJava {
    private static final Logger logger = LoggerFactory.getLogger(JavaToJava.class);

    public static String generateMappingCode(String qualifiedSrcFileName, String qualifiedDesFileName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {

        PsiClass srcClass = javaPsiFacade.findClass(qualifiedSrcFileName, globalSearchScope);
        PsiClass desClass = javaPsiFacade.findClass(qualifiedDesFileName, globalSearchScope);

        if (srcClass == null || desClass == null) {
            logger.error("Invalid qualified class name.");
            return null;
        }

        String result = "";

        // build element tree from src_file and des_file
        List<ElementNode> srcElementTree = JavaParser.parse(qualifiedSrcFileName, javaPsiFacade, globalSearchScope);
        List<ElementNode> desElementTree = JavaParser.parse(qualifiedDesFileName, javaPsiFacade, globalSearchScope);

        // get Java qualified name
        String srcClassQualifiedName = srcClass.getQualifiedName();
        String desClassQualifiedName = desClass.getQualifiedName();

        Set<String> usedVariableName = new HashSet<>();

        return result;
    }


    private static String generateMappingForObject(String objectToSetQualifiedName, String objectToGetQualififedName,
                                                   String objectToSetVarName, String objectToGetVarName,
                                                   List<ElementNode> objectToSetFields, List<ElementNode> objectToGetFields,
                                                   Set<String> usedVariableName, String result, String indent) {
        // add name to set to avoid duplicated name in the future
        usedVariableName.add(objectToGetVarName);
        usedVariableName.add(objectToSetVarName);

        // declare object to set
        result += JavaCommandUtils.generateObjectDeclaration(objectToSetQualifiedName, objectToSetVarName);

        // for each fields from object to set
        for (ElementNode fieldToSet : objectToSetFields) {
            ElementNode fieldToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(fieldToSet, objectToGetFields);
            if (fieldToGet == null) continue;

            // both nodes have data_type = OBJECT and qualified names of 2 reference objects are not equal
            if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT
                    && !fieldToSet.getDataTypeNode().getQualifiedName().equals(fieldToGet.getDataTypeNode().getQualifiedName())) {

                String newObjectToSetVarName = NameUtils.generateUniqueRandomName(fieldToSet.getDataTypeNode().getQualifiedName(), usedVariableName);
                String newObjectToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getQualifiedName(), usedVariableName);

                result = generateMappingForObject(fieldToSet.getDataTypeNode().getQualifiedName(), fieldToGet.getDataTypeNode().getQualifiedName(), newObjectToSetVarName, newObjectToGetVarName,
                        fieldToSet.getChildren(), fieldToGet.getChildren(), usedVariableName, result, indent);

                result += JavaCommandUtils.generateSetter(objectToSetVarName, fieldToSet.getName(), newObjectToSetVarName);
            }
            // both nodes have data_type = ARRAY
        }
        return null;
    }

    private static String generateMappingArray(String name) {
        return "";
    }
}
