package com.github.utils;

import com.github.model.ElementNode;

import java.util.List;

public class ElementNodeUtils {
    public static ElementNode findElementWithEqualNameAndDataType(ElementNode elementNode, List<ElementNode> searchList) {
        for (ElementNode res : searchList)
            if (res.getName().equals(elementNode.getName())
                    && res.getDataTypeNode().getDataType() == elementNode.getDataTypeNode().getDataType())
                return res;
        return null;
    }
}
