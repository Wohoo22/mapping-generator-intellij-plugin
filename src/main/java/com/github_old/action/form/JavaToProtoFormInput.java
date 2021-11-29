package com.github_old.action.form;

import com.github_old.action.classnamesuggestion.ClassNameSuggestion;
import com.github_old.action.classnamesuggestion.dropdowntextsuggestion.SuggestionDropDownDecorator;
import com.github_old.action.classnamesuggestion.dropdowntextsuggestion.suggestionclient.impl.TextComponentSuggestionClient;
import com.github_old.utils.PsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JavaToProtoFormInput extends DialogWrapper {
    private final Project project;
    private final GlobalSearchScope globalSearchScope;

    public JavaToProtoFormInput(Project project, GlobalSearchScope globalSearchScope) {
        super(true);
        this.project = project;
        this.globalSearchScope = globalSearchScope;
        init();
        setTitle("Java-to-Proto Pojo Mapping Generator");
    }

    private final JLabel javaQualifiedClassNameLabel = new JLabel("Java class: ");
    private final JLabel javaObjectVariableNameLabel = new JLabel("Source object variable name: ");
    private final JLabel protoFileNameLabel = new JLabel("Proto file: ");
    private final JLabel protoMessageQualifiedNameLabel = new JLabel("Proto message: ");
    private final JLabel protoJavaOuterClassNameLabel = new JLabel("Proto java outer class: ");

    private final JTextField javaQualifiedClassName = new JTextField();
    private final JTextField javaObjectVariableName = new JTextField();
    private final JTextField protoFileName = new JTextField();
    private final JTextField protoMessageQualifiedName = new JTextField();
    private final JTextField protoJavaOuterClassName = new JTextField();

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel jPanel = new JPanel(new GridLayout(5, 2));
        addComponent(jPanel);
        decorateTextFieldsWithClassNameSuggestion();

        return jPanel;
    }

    private void addComponent(JPanel jPanel) {
        jPanel.add(javaQualifiedClassNameLabel);
        jPanel.add(javaQualifiedClassName);
        jPanel.add(javaObjectVariableNameLabel);
        jPanel.add(javaObjectVariableName);
        jPanel.add(protoFileNameLabel);
        jPanel.add(protoFileName);
        jPanel.add(protoMessageQualifiedNameLabel);
        jPanel.add(protoMessageQualifiedName);
        jPanel.add(protoJavaOuterClassNameLabel);
        jPanel.add(protoJavaOuterClassName);
    }

    private void decorateTextFieldsWithClassNameSuggestion() {
        List<String> classNames = PsiUtils.getAllClassNames(project, globalSearchScope);
        ClassNameSuggestion classNameSuggestion = new ClassNameSuggestion(classNames);
        SuggestionDropDownDecorator.decorate(javaQualifiedClassName, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
        SuggestionDropDownDecorator.decorate(protoJavaOuterClassName, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
    }


    public String getJavaQualifiedClassName() {
        return javaQualifiedClassName.getText();
    }

    public String getJavaObjectVariableName() {
        return javaObjectVariableName.getText();
    }

    public String getProtoFileName() {
        return protoFileName.getText();
    }

    public String getProtoMessageQualifiedName() {
        return protoMessageQualifiedName.getText();
    }

    public String getProtoJavaOuterClassName() {
        return protoJavaOuterClassName.getText();
    }
}

