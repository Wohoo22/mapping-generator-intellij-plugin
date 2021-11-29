package com.github_old.parser.proto.utils;

import com.github_old.parser.proto.model.MessageNode;

import java.util.ArrayList;
import java.util.List;

public class MessageNodeUtils {
    public static MessageNode findMessageNodeByPresentableName(List<MessageNode> rootMessageNodes, MessageNode searchScope, String name) {
        if (searchScope == null) {
            for (MessageNode rootMsgNode : rootMessageNodes)
                if (rootMsgNode.getPresentableName().equals(name))
                    return rootMsgNode;
            return null;
        }

        for (MessageNode messageNode : searchScope.getChildren())
            if (messageNode.getPresentableName().equals(name))
                return messageNode;

        return findMessageNodeByPresentableName(rootMessageNodes, searchScope.getParent(), name);
    }

    public static String findQualifiedName(MessageNode messageNode, String javaOuterClassQualifiedName) {
        List<String> names = new ArrayList<>();

        MessageNode tmp = messageNode;
        while (tmp != null) {
            names.add(tmp.getPresentableName());
            tmp = tmp.getParent();
        }

        names.add(javaOuterClassQualifiedName);

        String res = "";
        for (int i = names.size() - 1; i >= 0; i--)
            if (i == 0)
                res = res.concat(names.get(i));
            else
                res = res.concat(names.get(i) + ".");

        return res;
    }

    public static MessageNode findMessageNodeByPresentableAndQualifiedName(List<MessageNode> rootMessageNodes, MessageNode searchScope, String presentableName, String qualifiedName) {
        MessageNode messageNode;

        messageNode = findMessageNodeByPresentableName(rootMessageNodes, searchScope, presentableName);
        if (messageNode != null) return messageNode;

        messageNode = findMessageNodeByQualifiedName(rootMessageNodes, qualifiedName);
        return messageNode;
    }

    public static MessageNode findMessageNodeByQualifiedName(List<MessageNode> rootMessageNodes, String qualifiedName) {
        if (rootMessageNodes == null) return null;
        for (MessageNode rootMessageNode : rootMessageNodes) {
            if (rootMessageNode.getQualifiedName().equals(qualifiedName))
                return rootMessageNode;
            MessageNode child = findMessageNodeByQualifiedName(rootMessageNode.getChildren(), qualifiedName);
            if (child != null) return child;
        }
        return null;
    }
}
