package com.github.action.form;

import com.github.action.classnamesuggestion.ClassNameSuggestion;
import com.github.action.classnamesuggestion.dropdowntextsuggestion.SuggestionDropDownDecorator;
import com.github.action.classnamesuggestion.dropdowntextsuggestion.suggestionclient.impl.TextComponentSuggestionClient;
import com.github.utils.PsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JavaToJavaFormInput extends DialogWrapper {
    private final Project project;
    private final GlobalSearchScope globalSearchScope;

    public JavaToJavaFormInput(Project project, GlobalSearchScope globalSearchScope) {
        super(true);
        this.project = project;
        this.globalSearchScope = globalSearchScope;
        init();
        setTitle("Java-to-Java Pojo Mapping Generator");
    }

    private final JLabel sourceClassLabel = new JLabel("Source class name:");
    private final JLabel sourceObjectVariableNameLabel = new JLabel("Source object variable name:");
    private final JLabel targetClassLabel = new JLabel("Target class name:");

    private final JTextField sourceClassField = new JTextField();
    private final JTextField sourceObjectVariableNameField = new JTextField();
    private final JTextField targetClassField = new JTextField();

    private final JCheckBox targetClassIsBuilderType = new JCheckBox("Builder type");

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel jPanel = new JPanel(new GridLayout(4, 2));
        addComponent(jPanel);
        decorateTextFieldsWithClassNameSuggestion();

        return jPanel;
    }

    private void addComponent(JPanel jPanel) {
        jPanel.add(sourceClassLabel);
        jPanel.add(sourceClassField);
        jPanel.add(sourceObjectVariableNameLabel);
        jPanel.add(sourceObjectVariableNameField);
        jPanel.add(targetClassLabel);
        jPanel.add(targetClassField);
        jPanel.add(targetClassIsBuilderType);
    }

    private void decorateTextFieldsWithClassNameSuggestion() {
        List<String> classNames = PsiUtils.getAllClassNames(project, globalSearchScope);
        ClassNameSuggestion classNameSuggestion = new ClassNameSuggestion(classNames);
        SuggestionDropDownDecorator.decorate(sourceClassField, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
        SuggestionDropDownDecorator.decorate(targetClassField, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
    }

    public boolean isTargetClassBuilder() {
        return targetClassIsBuilderType.isSelected();
    }

    public String getSourceClassQualifiedName() {
        return sourceClassField.getText();
    }

    public String getTargetClassQualifiedName() {
        return targetClassField.getText();
    }

    public String getSourceClassVariableName() {
        return sourceObjectVariableNameField.getText();
    }

}
