package com.github_old.parser.proto.messagetreebuilder.utils;

import com.github_old.parser.proto.model.MessageNode;

import java.util.ArrayList;
import java.util.List;

public class MessageNodeUtils {
    public static String findQualifiedName(MessageNode messageNode) {
        StringBuilder name = new StringBuilder();
        MessageNode tmpNode = messageNode;

        List<String> names = new ArrayList<>();

        while (tmpNode != null) {
            if (tmpNode.getPresentableName() == null) return "";
            names.add(tmpNode.getPresentableName());
            tmpNode = tmpNode.getParent();
        }

        for (int i = names.size() - 1; i >= 0; i--) {
            if (i == 0)
                name.append(names.get(i));
            else
                name.append(names.get(i)).append(".");
        }

        return name.toString();
    }

    public static String extractPresentableName(String s) {
        if (!s.contains("."))
            return s;
        String[] p = s.split("\\.");
        return p[p.length - 1];
    }
}
