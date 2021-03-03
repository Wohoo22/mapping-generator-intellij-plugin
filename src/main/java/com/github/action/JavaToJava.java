package com.github.action;

import com.github.action.form.javatojava.JavaToJavaFormInput;
import com.github.action.writer.EditorTextWriter;
import com.github.codegenerator.javatojava.JavaToJavaCodeGenerator;
import com.github.enums.MappingType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        JavaToJavaCodeGenerator javaToJavaCodeGenerator = new JavaToJavaCodeGenerator();
        MappingType mappingType = MappingType.NORMAL;
        if (javaToJavaFormInput.isTargetClassBuilder())
            mappingType = MappingType.BUILDER;

        String mappingCode = javaToJavaCodeGenerator.generateMappingCode(javaToJavaFormInput.getTargetClassQualifiedName(), javaToJavaFormInput.getSourceClassVariableName(),
                javaToJavaFormInput.getSourceClassQualifiedName(), javaPsiFacade, globalSearchScope, mappingType);

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        EditorTextWriter editorTextWriter = new EditorTextWriter();
        editorTextWriter.write(editor, project, mappingCode);
    }
}

