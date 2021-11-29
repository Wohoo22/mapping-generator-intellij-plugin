package com.github_old.utils;

import com.github_old.model.ElementNode;

import java.util.List;

public class ElementNodeUtils {
    public static ElementNode findElementWithEqualNameAndDataType(ElementNode elementNode, List<ElementNode> searchList) {
        for (ElementNode res : searchList)
            if (res.getName().equals(elementNode.getName())
                    && DataTypeNodeUtils.dataTypeEqual(res.getDataTypeNode(), elementNode.getDataTypeNode()))
                return res;
        return null;
    }
}
