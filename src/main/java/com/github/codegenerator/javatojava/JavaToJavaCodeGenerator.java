package com.github.codegenerator.javatojava;

import com.github.codegenerator.javatojava.subgenerator.FieldMappingCodeGenerator;
import com.github.model.ElementNode;
import com.github.parser.java.JavaParser;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaToJavaCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(JavaToJavaCodeGenerator.class);

    private static final String desObjectVarName = "desObj";
    private static final String srcObjectVarName = "srcObj";

    public String generateMappingCode(String qualifiedSrcFileName, String qualifiedDesFileName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {

        PsiClass srcClass = javaPsiFacade.findClass(qualifiedSrcFileName, globalSearchScope);
        PsiClass desClass = javaPsiFacade.findClass(qualifiedDesFileName, globalSearchScope);

        if (srcClass == null || desClass == null) {
            logger.error("Invalid qualified class name.");
            return null;
        }

        String result = "";

        // build element tree from src_file and des_file
        JavaParser javaParser = new JavaParser();
        List<ElementNode> srcElementTree = javaParser.parse(qualifiedSrcFileName, javaPsiFacade, globalSearchScope);
        List<ElementNode> desElementTree = javaParser.parse(qualifiedDesFileName, javaPsiFacade, globalSearchScope);

        // get Java qualified name
        String srcClassQualifiedName = srcClass.getQualifiedName();
        String desClassQualifiedName = desClass.getQualifiedName();

        // to avoid duplicated variable name generation later
        Set<String> usedVariableName = new HashSet<>();
        usedVariableName.add(desObjectVarName);
        usedVariableName.add(srcClassQualifiedName);

        // declare the des_object
        result += JavaCommandUtils.generateObjectDeclaration(
                desClassQualifiedName,
                desObjectVarName
        );

        // generate setter getter for each field
        FieldMappingCodeGenerator fieldMappingCodeGenerator = new FieldMappingCodeGenerator();
        for (ElementNode fieldToSet : desElementTree) {
            ElementNode fieldToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(fieldToSet, srcElementTree);
            // can't find a suitable field
            if (fieldToGet == null) continue;

            result = result.concat(fieldMappingCodeGenerator.generateMappingCode(desObjectVarName, srcObjectVarName, fieldToSet, fieldToGet, "", usedVariableName));
        }

        return result;
    }


}
