package com.github_new.output.impl;

import com.github_new.output.interfaces.Writer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public class WriterImpl implements Writer {
    private final Editor editor;
    private final Project project;

    public WriterImpl(Editor editor, Project project) {
        this.editor = editor;
        this.project = project;
    }

    @Override
    public void write(String data) {
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        int currentOffSet = primaryCaret.getOffset();

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(currentOffSet, data)
        );
    }
}
