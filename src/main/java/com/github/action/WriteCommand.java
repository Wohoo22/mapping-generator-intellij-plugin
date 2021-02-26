package com.github.action;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class WriteCommand extends WriteCommandAction {

    private final String dataToWrite;
    private final Document document;
    private final int cursorOffset;

    public WriteCommand(Project project, String dataToWrite, Document document, int cursorOffset) {
        super(project);
        this.dataToWrite = dataToWrite;
        this.document = document;
        this.cursorOffset = cursorOffset;
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        document.insertString(cursorOffset, dataToWrite);
    }
}
