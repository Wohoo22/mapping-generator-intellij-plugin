package com.github_new.input.interfaces;

public interface JavaToJavaInputForm {
    public void showAndGetData();

    public String getTargetClassQualifiedName();

    public String getSourceClassQualifiedName();

    public String getSourceClassVariableName();

    public boolean isTargetClassBuilder();
}
