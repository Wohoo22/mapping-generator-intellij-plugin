package com.github.action.writer;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public class EditorTextWriter {
    public void write(Editor editor, Project project, String text) {
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        int currentOffSet = primaryCaret.getOffset();

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(currentOffSet, text)
        );
    }
}
