package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ElementNode {
    private String name;
    private DataTypeNode dataTypeNode;
    private List<ElementNode> children;

    public ElementNode() {
        children = new ArrayList<>();
    }
}
