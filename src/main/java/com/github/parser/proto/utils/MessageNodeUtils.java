package com.github.parser.proto.utils;

import com.github.parser.proto.model.MessageNode;

import java.util.ArrayList;
import java.util.List;

public class MessageNodeUtils {
    public static MessageNode findMessageNodeByName(List<MessageNode> rootMessageNodes, MessageNode searchScope, String name) {
        if (searchScope == null) {
            for (MessageNode rootMsgNode : rootMessageNodes)
                if (rootMsgNode.getName().equals(name))
                    return rootMsgNode;
            return null;
        }

        for (MessageNode messageNode : searchScope.getChildren())
            if (messageNode.getName().equals(name))
                return messageNode;

        return findMessageNodeByName(rootMessageNodes, searchScope.getParent(), name);
    }

    public static String findQualifiedName(MessageNode messageNode, String javaOuterClassQualifiedName) {
        List<String> names = new ArrayList<>();

        MessageNode tmp = messageNode;
        while (tmp != null) {
            names.add(tmp.getName());
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
}
