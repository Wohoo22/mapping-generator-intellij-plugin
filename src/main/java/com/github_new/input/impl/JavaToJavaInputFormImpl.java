package com.github_new.input.impl;

import com.github_new.input.classnamesuggestion.ClassNameSuggestion;
import com.github_new.input.classnamesuggestion.dropdowntextsuggestion.SuggestionDropDownDecorator;
import com.github_new.input.classnamesuggestion.dropdowntextsuggestion.suggestionclient.impl.TextComponentSuggestionClient;
import com.github_new.input.interfaces.JavaToJavaInputForm;
import com.github_new.input.utils.PsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.GlobalSearchScope;
import com.sun.istack.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JavaToJavaInputFormImpl extends DialogWrapper implements JavaToJavaInputForm {
    private final Project project;
    private final GlobalSearchScope globalSearchScope;

    public JavaToJavaInputFormImpl(Project project, GlobalSearchScope globalSearchScope) {
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
    protected @Nullable
    JComponent createCenterPanel() {

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

    @Override
    public void showAndGetData() {
        this.showAndGet();
    }

    public String getTargetClassQualifiedName() {
        return targetClassField.getText();
    }

    public String getSourceClassVariableName() {
        return sourceObjectVariableNameField.getText();
    }

}
