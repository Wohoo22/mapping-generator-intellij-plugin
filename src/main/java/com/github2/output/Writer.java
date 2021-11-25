package com.github2.output;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import lombok.Builder;

@Builder
public class Writer {
    private final Editor editor;
    private final Project project;
    public void write(String text) {
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        int currentOffSet = primaryCaret.getOffset();

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(currentOffSet, text)
        );
    }
}
