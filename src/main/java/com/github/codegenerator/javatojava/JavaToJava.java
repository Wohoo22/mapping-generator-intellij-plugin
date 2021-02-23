package com.github.codegenerator.javatojava;

import com.github.model.ElementNode;
import com.github.parser.java.JavaParser;
import com.github.utils.NameUtils;
import com.github.utils.StringUtils;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

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

        List<ElementNode> srcElementTree = JavaParser.parse(qualifiedSrcFileName, javaPsiFacade, globalSearchScope);
        List<ElementNode> desElementTree = JavaParser.parse(qualifiedDesFileName, javaPsiFacade, globalSearchScope);

        String presentableSrcClassName = NameUtils.getPresentableName(Objects.requireNonNull(srcClass.getQualifiedName()));
        String presentableDesClassName = NameUtils.getPresentableName(Objects.requireNonNull(desClass.getQualifiedName()));

        // ObjectDes objectDes = new ObjectDes();
        String desObjectName = NameUtils.toLowerCaseFirstChar(presentableDesClassName);
        result += presentableDesClassName + StringUtils.sp + desObjectName + StringUtils.sp +
                "=" + StringUtils.sp + "new" + StringUtils.sp + presentableDesClassName + "()" + ";" + StringUtils.lb;


        return result;
    }
}
