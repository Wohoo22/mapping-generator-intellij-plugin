package com.github.action.form.prototojava;

import com.github.action.classnamesuggestion.ClassNameSuggestion;
import com.github.action.classnamesuggestion.dropdowntextsuggestion.SuggestionDropDownDecorator;
import com.github.action.classnamesuggestion.dropdowntextsuggestion.suggestionclient.impl.TextComponentSuggestionClient;
import com.github.utils.PsiUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProtoToJavaFormInput extends DialogWrapper {
    private final Project project;
    private final GlobalSearchScope globalSearchScope;

    public ProtoToJavaFormInput(Project project, GlobalSearchScope globalSearchScope) {
        super(true);
        this.project = project;
        this.globalSearchScope = globalSearchScope;
        init();
    }

    private final JLabel protoFileNameLabel = new JLabel("Proto file: ");
    private final JLabel protoMessageQualifiedNameLabel = new JLabel("Proto message: ");
    private final JLabel protoJavaOuterClassNameLabel = new JLabel("Proto java outer class: ");
    private final JLabel protoObjectVariableNameLabel = new JLabel("Source object variable name: ");
    private final JLabel javaQualifiedClassNameLabel = new JLabel("Java class: ");

    private final JTextField protoFileName = new JTextField();
    private final JTextField protoMessageQualifiedName = new JTextField();
    private final JTextField protoJavaOuterClassName = new JTextField();
    private final JTextField protoObjectVariableName = new JTextField();
    private final JTextField javaQualifiedClassName = new JTextField();

    private final JCheckBox targetClassIsBuilderType = new JCheckBox("Builder type");

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel jPanel = new JPanel(new GridLayout(6, 2));
        addComponent(jPanel);
        decorateTextFieldsWithClassNameSuggestion();

        return jPanel;
    }

    private void addComponent(JPanel jPanel) {
        jPanel.add(protoFileNameLabel);
        jPanel.add(protoFileName);
        jPanel.add(protoMessageQualifiedNameLabel);
        jPanel.add(protoMessageQualifiedName);
        jPanel.add(protoJavaOuterClassNameLabel);
        jPanel.add(protoJavaOuterClassName);
        jPanel.add(protoObjectVariableNameLabel);
        jPanel.add(protoObjectVariableName);
        jPanel.add(javaQualifiedClassNameLabel);
        jPanel.add(javaQualifiedClassName);
        jPanel.add(targetClassIsBuilderType);
    }

    private void decorateTextFieldsWithClassNameSuggestion() {
        List<String> classNames = PsiUtil.getAllClassNames(project, globalSearchScope);
        ClassNameSuggestion classNameSuggestion = new ClassNameSuggestion(classNames);
        SuggestionDropDownDecorator.decorate(javaQualifiedClassName, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
        SuggestionDropDownDecorator.decorate(protoJavaOuterClassName, new TextComponentSuggestionClient(classNameSuggestion::getSuggestions));
    }

    public boolean isTargetClassBuilder() {
        return targetClassIsBuilderType.isSelected();
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

    public String getProtoObjectVariableName() {
        return protoObjectVariableName.getText();
    }

    public String getJavaQualifiedClassName() {
        return javaQualifiedClassName.getText();
    }
}
