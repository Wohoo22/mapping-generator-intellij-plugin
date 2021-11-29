package com.github_new.parser.interfaces;

import com.github_new.domain.ElementNode;

import java.util.List;

public interface JavaParser {
    public List<ElementNode> parse(String qualifiedClassName);
}
