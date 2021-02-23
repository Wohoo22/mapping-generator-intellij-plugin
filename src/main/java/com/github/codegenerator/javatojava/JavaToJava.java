package com.github.codegenerator.javatojava;

import com.github.model.ElementNode;
import com.github.parser.java.JavaParser;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

public class JavaToJava {
    public static String generateMapper(String qualifiedSrcFileName, String qualifiedDesFileName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope) {

        List<ElementNode> srcElementTree = JavaParser.parse(qualifiedSrcFileName, javaPsiFacade, globalSearchScope);
        List<ElementNode> desElementTree = JavaParser.parse(qualifiedDesFileName, javaPsiFacade, globalSearchScope);

        return "";
    }
}
