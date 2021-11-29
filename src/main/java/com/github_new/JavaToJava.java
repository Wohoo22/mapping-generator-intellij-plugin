package com.github_new;

import com.github_new.domain.ElementNode;
import com.github_new.generator.impl.JavaToJavaGeneratorImpl;
import com.github_new.generator.interfaces.JavaToJavaGenerator;
import com.github_new.generator.ports.JavaToJavaGeneratorPort;
import com.github_new.input.impl.JavaToJavaInputFormImpl;
import com.github_new.input.interfaces.JavaToJavaInputForm;
import com.github_new.output.impl.WriterImpl;
import com.github_new.output.interfaces.Writer;
import com.github_new.parser.impl.JavaParserImpl;
import com.github_new.parser.interfaces.JavaParser;
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
        JavaToJavaInputForm input = new JavaToJavaInputFormImpl(project, globalSearchScope);
        // Step 2: Get input
        input.showAndGetData();
        // Step 3: Create Parser object
        JavaParser javaParser = new JavaParserImpl(javaPsiFacade, globalSearchScope);
        // Step 4: Parse and get Element Nodes
        List<ElementNode> elementsToSet = javaParser.parse(input.getTargetClassQualifiedName());
        List<ElementNode> elementsToGet = javaParser.parse(input.getSourceClassQualifiedName());
        // Step 5: Create Code Generator object
        JavaToJavaGenerator generator = new JavaToJavaGeneratorImpl();
        // Step 6: Generate mapping code
        String mappingCode = generator.generate(JavaToJavaGeneratorPort.GenerateInput.builder()
                .objectToSetQualifiedName(input.getTargetClassQualifiedName())
                .objectToGetVariableName(input.getSourceClassVariableName())
                .objectToGetQualifiedName(input.getSourceClassQualifiedName())
                .mappingType(input.isTargetClassBuilder() ?
                        JavaToJavaGeneratorPort.GenerateInput.MappingType.BUILDER :
                        JavaToJavaGeneratorPort.GenerateInput.MappingType.NORMAL)
                .elementsToSet(elementsToSet)
                .elementsToGet(elementsToGet)
                .build());
        // Step 7: Write to editor
        Writer writer = new WriterImpl(editor, project);
        writer.write(mappingCode);
    }
}
