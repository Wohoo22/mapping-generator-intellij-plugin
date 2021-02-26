package com.github.action.form;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FormInput extends DialogWrapper {

    public FormInput() {
        super(true);
        init();
        setTitle("Mapping Generator");
    }

    private JTextField desFileQualifiedNameField;
    private JTextField srcFileQualifiedNameField;

    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gridBag = new GridBag();

        gridBag.setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultInsets(JBUI.insets(0, 0, 5, 5))
        ;

        JTextField desFileQualifiedNameField = new JFormattedTextField();
        JTextField srcFileQualifiedNameField = new JFormattedTextField();

        JPanel jPanel = new JPanel(new GridBagLayout());

        jPanel.add(new Label("Destination file qualified name"));
        jPanel.add(desFileQualifiedNameField);

        jPanel.add(new Label("Source file qualified name"));
        jPanel.add(srcFileQualifiedNameField);

        this.desFileQualifiedNameField = desFileQualifiedNameField;
        this.srcFileQualifiedNameField = srcFileQualifiedNameField;

        return jPanel;
    }

    public String getDestinationFileQualifiedName() {
        return desFileQualifiedNameField.getText();
    }

    public String getSourceFileQualifiedName() {
        return srcFileQualifiedNameField.getText();
    }
}
