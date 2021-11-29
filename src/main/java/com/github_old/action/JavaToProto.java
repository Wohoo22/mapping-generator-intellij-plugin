package com.github_old.action;

import com.github_old.action.form.JavaToProtoFormInput;
import com.github_old.action.writer.EditorTextWriter;
import com.github_old.codegenerator.javatoproto.JavaToProtoCodeGenerator;
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

public class JavaToProto extends AnAction {

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

        JavaToProtoFormInput javaToProtoFormInput = new JavaToProtoFormInput(project, globalSearchScope);
        javaToProtoFormInput.showAndGet();

        JavaToProtoCodeGenerator javaToProtoCodeGenerator = new JavaToProtoCodeGenerator();

        String mappingCode = javaToProtoCodeGenerator.generateMappingCode(javaToProtoFormInput.getJavaQualifiedClassName(),
                javaToProtoFormInput.getJavaObjectVariableName(), javaPsiFacade, globalSearchScope,
                javaToProtoFormInput.getProtoFileName(), javaToProtoFormInput.getProtoMessageQualifiedName(),
                javaToProtoFormInput.getProtoJavaOuterClassName());

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        EditorTextWriter editorTextWriter = new EditorTextWriter();
        editorTextWriter.write(editor, project, mappingCode);
    }
}
