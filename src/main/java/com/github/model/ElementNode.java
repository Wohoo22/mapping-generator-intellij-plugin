package com.github.model;

import com.github.enums.DataType;

import java.util.List;

public class ElementNode {
    private String name;
    private DataType dataType;
    private List<ElementNode> children;
}
