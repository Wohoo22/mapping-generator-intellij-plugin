package com.github2.model;

import java.util.ArrayList;
import java.util.List;

public class ElementNode {
    private String name;
    private DataTypeNode dataTypeNode;
    private List<ElementNode> children;

    public ElementNode() {
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataTypeNode getDataTypeNode() {
        return dataTypeNode;
    }

    public void setDataTypeNode(DataTypeNode dataTypeNode) {
        this.dataTypeNode = dataTypeNode;
    }

    public List<ElementNode> getChildren() {
        return children;
    }

    public void setChildren(List<ElementNode> children) {
        this.children = children;
    }
}
