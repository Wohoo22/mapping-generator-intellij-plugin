package com.github.action;

import com.github.action.form.FormInput;
import com.github.codegenerator.javatojava.JavaToJavaCodeGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class GenerateMappingCode extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        FormInput formInput = new FormInput();
        formInput.showAndGet();

        String desFileQualifiedName = formInput.getDestinationFileQualifiedName();
        String srcFileQualifiedName = formInput.getSourceFileQualifiedName();

        Project project = e.getProject();

        assert project != null;
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);

        JavaToJavaCodeGenerator javaToJavaCodeGenerator = new JavaToJavaCodeGenerator();
        String resultString = javaToJavaCodeGenerator.generateMappingCode(srcFileQualifiedName, desFileQualifiedName,
                javaPsiFacade, globalSearchScope);


        FileEditorManager manager = FileEditorManager.getInstance(project);
        final Editor editor = manager.getSelectedTextEditor();
        assert editor != null;
        int cursorOffset = editor.getCaretModel().getOffset();
        Document crtDocument = editor.getDocument();

        WriteCommand writeCommand = new WriteCommand(project, resultString, crtDocument, cursorOffset);

        writeCommand.execute();
    }
}
