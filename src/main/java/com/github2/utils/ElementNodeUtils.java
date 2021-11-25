package com.github2.utils;

import com.github2.model.ElementNode;

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
