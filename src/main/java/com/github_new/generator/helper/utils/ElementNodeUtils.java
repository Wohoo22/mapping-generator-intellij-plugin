package com.github_new.generator.helper.utils;

import com.github_new.domain.ElementNode;

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
