package com.github.action;

import com.github.model.ElementNode;
import com.github.parser.proto.ProtoParser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateMappingCode extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

//        FormInput formInput = new FormInput();
//        formInput.showAndGet();
//
//        String desFileQualifiedName = formInput.getDestinationFileQualifiedName();
//        String srcFileQualifiedName = formInput.getSourceFileQualifiedName();
//
//        Project project = e.getProject();
//
//        assert project != null;
//        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
//        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
//
//        JavaToJavaCodeGenerator javaToJavaCodeGenerator = new JavaToJavaCodeGenerator();
//        String resultString = javaToJavaCodeGenerator.generateMappingCode(srcFileQualifiedName, desFileQualifiedName,
//                javaPsiFacade, globalSearchScope);
//
//
//        FileEditorManager manager = FileEditorManager.getInstance(project);
//        final Editor editor = manager.getSelectedTextEditor();
//        assert editor != null;
//        int cursorOffset = editor.getCaretModel().getOffset();
//        Document crtDocument = editor.getDocument();
//
//        WriteCommand writeCommand = new WriteCommand(project, resultString, crtDocument, cursorOffset);
//
//        writeCommand.execute();

        ProtoParser protoParser = new ProtoParser();
        List<ElementNode> tree = protoParser.parse("D:\\Work\\work-space\\nexttech\\core-grpc-proto\\co-item\\item.proto",
                "Item");

        int z = 0;
    }
}
