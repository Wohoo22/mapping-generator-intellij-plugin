package com.github_old.parser.proto.messageelementnodesbuilder;


import com.github_old.parser.proto.messageelementnodesbuilder.elementnodesbuilder.BuildElementNodeFromTokens;
import com.github_old.parser.proto.model.MessageNode;

import java.util.List;

public class MessageElementNodesBuilder {
    public void build(List<MessageNode> messageNodes) {

        // build element nodes from tokens
        BuildElementNodeFromTokens buildElementNodeFromTokens = new BuildElementNodeFromTokens();
        for (MessageNode messageNode : messageNodes)
            buildElementNodeFromTokens.build(messageNode, messageNodes);

    }
}
