package com.github.action;

import com.github.action.form.prototojava.ProtoToJavaFormInput;
import com.github.action.writer.EditorTextWriter;
import com.github.codegenerator.prototojava.ProtoToJavaCodeGenerator;
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

public class ProtoToJava extends AnAction {

    private static final Logger logger = LoggerFactory.getLogger(ProtoToJava.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            logger.error("Please open a project !");
            return;
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);

        ProtoToJavaFormInput protoToJavaFormInput = new ProtoToJavaFormInput(project, globalSearchScope);
        protoToJavaFormInput.showAndGet();

        MappingType mappingType = MappingType.NORMAL;
        if (protoToJavaFormInput.isTargetClassBuilder())
            mappingType = MappingType.BUILDER;

        ProtoToJavaCodeGenerator protoToJavaCodeGenerator = new ProtoToJavaCodeGenerator();

        String mappingCode = protoToJavaCodeGenerator.generateMappingCode(protoToJavaFormInput.getProtoFileName(),
                protoToJavaFormInput.getProtoMessageQualifiedName(), protoToJavaFormInput.getProtoJavaOuterClassName(),
                protoToJavaFormInput.getProtoObjectVariableName(), protoToJavaFormInput.getJavaQualifiedClassName(),
                javaPsiFacade, globalSearchScope, mappingType);

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        EditorTextWriter editorTextWriter = new EditorTextWriter();
        editorTextWriter.write(editor, project, mappingCode);
    }
}
