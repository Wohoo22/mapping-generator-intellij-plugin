package com.github2;

import com.github2.enums.MappingType;
import com.github2.generator.javatojava.JavaToJavaCodeGenerator;
import com.github2.input.JavaToJavaInput;
import com.github2.model.ElementNode;
import com.github2.output.Writer;
import com.github2.parser.java.JavaParser;
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

import java.util.List;

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
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);

        // Step 1: Create Input object
        JavaToJavaInput input = new JavaToJavaInput(project, globalSearchScope);
        // Step 2: Get input
        input.showAndGet();
        // Step 3: Create Parser object
        JavaParser javaParser = JavaParser.builder()
                .javaPsiFacade(javaPsiFacade)
                .globalSearchScope(globalSearchScope)
                .build();
        // Step 4: Parse and get Element Nodes
        List<ElementNode> elementsToSet = javaParser.parse(input.getTargetClassQualifiedName());
        List<ElementNode> elementsToGet = javaParser.parse(input.getSourceClassQualifiedName());
        // Step 5: Create Code Generator object
        JavaToJavaCodeGenerator generator = JavaToJavaCodeGenerator.builder()
                .objectToSetQualifiedName(input.getTargetClassQualifiedName())
                .objectToGetVariableName(input.getSourceClassVariableName())
                .objectToGetQualifiedName(input.getSourceClassQualifiedName())
                .mappingType(input.isTargetClassBuilder() ? MappingType.BUILDER : MappingType.NORMAL)
                .elementsToSet(elementsToSet)
                .elementsToGet(elementsToGet)
                .build();
        // Step 6: Generate mapping code
        String mappingCode = generator.generateMappingCode();
        // Step 7: Write to editor
        Writer writer = Writer.builder()
                .editor(editor)
                .project(project)
                .build();
        writer.write(mappingCode);
    }
}
