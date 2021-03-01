package com.github.parser.proto.utils;

import com.github.parser.proto.model.MessageNode;

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
}
