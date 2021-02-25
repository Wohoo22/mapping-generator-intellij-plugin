package com.github.action;

import com.github.codegenerator.javatojava.JavaToJavaCodeGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class Test extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();

        assert project != null;
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);

        JavaToJavaCodeGenerator javaToJavaCodeGenerator = new JavaToJavaCodeGenerator();
        String result = javaToJavaCodeGenerator.generateMappingCode("Src", "Des",
                javaPsiFacade, globalSearchScope);

        FileWriter outputFile = null;
        try {
            outputFile = new FileWriter("C:\\Users\\ADMIN\\Desktop\\test.txt");
            outputFile.write(result);
            outputFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
