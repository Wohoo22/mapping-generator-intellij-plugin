package com.github.parser.java;

import com.github.model.ElementNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

public interface JavaParser {
    List<ElementNode> parse(String qualifiedClassName, JavaPsiFacade javaPsiFacade, GlobalSearchScope globalSearchScope);
}
