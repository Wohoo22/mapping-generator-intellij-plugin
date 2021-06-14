package com.github.action.writer;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;
import lombok.Builder;

import java.util.Set;

@Builder
public class EditorTextWriter {
    private final Editor editor;
    private final Project project;
    private final String text;
    private final PsiJavaFile openingPsiFile;
    private final Set<String> referredQualifiedName;
    private final JavaPsiFacade javaPsiFacade;
    private final GlobalSearchScope globalSearchScope;

    public void write() {
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        int currentOffSet = primaryCaret.getOffset();
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        manager.commitDocument(document);

        WriteCommandAction.runWriteCommandAction(project, () -> {
                    document.insertString(currentOffSet, text);
                    for (String name : referredQualifiedName) {
                        PsiClass psiClass = javaPsiFacade.findClass(name, globalSearchScope);
                        if (psiClass != null) {
                            manager.commitDocument(document);
                            openingPsiFile.importClass(psiClass);
                        }
                    }
                }
        );
    }
}
