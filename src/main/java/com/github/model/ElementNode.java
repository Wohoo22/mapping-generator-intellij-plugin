package com.github.model;

import com.github.enums.DataType;

import java.util.List;

public class ElementNode {
    private String name;
    private DataType dataType;
    private List<ElementNode> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public List<ElementNode> getChildren() {
        return children;
    }

    public void setChildren(List<ElementNode> children) {
        this.children = children;
    }
}
