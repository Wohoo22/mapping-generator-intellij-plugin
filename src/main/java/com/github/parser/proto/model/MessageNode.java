package com.github.parser.proto.model;

import com.github.model.ElementNode;

import java.util.ArrayList;
import java.util.List;

public class MessageNode {
    private String presentableName;
    private String qualifiedName;
    private MessageNode parent;
    private boolean isEnum;
    private List<MessageNode> children;
    private TokenStorage tokenStorage;
    private List<ElementNode> elementNodes;

    public MessageNode() {
        children = new ArrayList<>();
        isEnum = false;
        elementNodes = new ArrayList<>();
        tokenStorage = new TokenStorage();
    }

    public List<ElementNode> getElementNodes() {
        return elementNodes;
    }

    public void setElementNodes(List<ElementNode> elementNodes) {
        this.elementNodes = elementNodes;
    }

    public TokenStorage getTokenStorage() {
        return tokenStorage;
    }

    public void setTokenStorage(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    public String getPresentableName() {
        return presentableName;
    }

    public void setPresentableName(String presentableName) {
        this.presentableName = presentableName;
    }


    public MessageNode getParent() {
        return parent;
    }

    public void setParent(MessageNode parent) {
        this.parent = parent;
    }

    public List<MessageNode> getChildren() {
        return children;
    }

    public void setChildren(List<MessageNode> children) {
        this.children = children;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
}
