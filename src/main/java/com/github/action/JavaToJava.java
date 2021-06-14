package com.github.action;

import com.github.action.form.JavaToJavaFormInput;
import com.github.action.writer.EditorTextWriter;
import com.github.codegenerator.javatojava.JavaToJavaCodeGenerator;
import com.github.enums.MappingType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class JavaToJava extends AnAction {

    private static final Logger logger = LoggerFactory.getLogger(JavaToJava.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            logger.error("Please open a project !");
            return;
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);

        JavaToJavaFormInput javaToJavaFormInput = new JavaToJavaFormInput(project, globalSearchScope);
        javaToJavaFormInput.showAndGet();

        MappingType mappingType = MappingType.NORMAL;
        if (javaToJavaFormInput.isTargetClassBuilder())
            mappingType = MappingType.BUILDER;

        PsiJavaFile openingPsiFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
        Set<String> referredQualifiedName = new HashSet<>();
        JavaToJavaCodeGenerator javaToJavaCodeGenerator = JavaToJavaCodeGenerator.builder()
                .objectToSetQualifiedName(javaToJavaFormInput.getTargetClassQualifiedName())
                .objectToGetVariableName(javaToJavaFormInput.getSourceClassVariableName())
                .objectToGetQualifiedName(javaToJavaFormInput.getSourceClassQualifiedName())
                .mappingType(mappingType)
                .javaPsiFacade(javaPsiFacade)
                .globalSearchScope(globalSearchScope)
                .referredQualifiedName(referredQualifiedName)
                .build();

        String mappingCode = javaToJavaCodeGenerator.generateMappingCode();

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        EditorTextWriter editorTextWriter = EditorTextWriter.builder()
                .editor(editor)
                .project(project)
                .text(mappingCode)
                .openingPsiFile(openingPsiFile)
                .referredQualifiedName(referredQualifiedName)
                .javaPsiFacade(javaPsiFacade)
                .globalSearchScope(globalSearchScope)
                .build();
        editorTextWriter.write();
    }
}

