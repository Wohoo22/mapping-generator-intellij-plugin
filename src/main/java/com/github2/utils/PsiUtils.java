package com.github2.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsiUtils {
    public static List<String> getAllClassNames(Project project, GlobalSearchScope scope) {
        List<String> res = new ArrayList<>();

        JavaFullClassNameIndex javaFullClassNameIndex = JavaFullClassNameIndex.getInstance();

        Collection<Integer> keyIds = javaFullClassNameIndex.getAllKeys(project);
        for (int keyId : keyIds) {
            Collection<PsiClass> psiClasses = javaFullClassNameIndex.get(keyId, project, scope);
            for (PsiClass psiClass : psiClasses)
                res.add(psiClass.getQualifiedName());
        }

        return res;
    }
}
