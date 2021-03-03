package com.github.parser.proto.messagetreebuilder;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.messagetreebuilder.nodebuilder.EnumNodeBuilder;
import com.github.parser.proto.messagetreebuilder.nodebuilder.MessageNodeBuilder;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;

import java.util.ArrayList;
import java.util.List;

public class MessageTreeBuilder {
    public List<MessageNode> build(TokenStorage tokenStorage) {

        List<MessageNode> messageNodes = new ArrayList<>();

        MessageNodeBuilder messageNodeBuilder = new MessageNodeBuilder();
        EnumNodeBuilder enumNodeBuilder = new EnumNodeBuilder();

        while (!tokenStorage.outOfToken()) {
            Token currentToken = tokenStorage.getCurrentToken();
            tokenStorage.next();

            // message
            if (currentToken == Token.MESSAGE) {
                MessageNode msgNode = messageNodeBuilder.build(tokenStorage, null);
                messageNodes.add(msgNode);
            }
            // enum
            else if (currentToken == Token.ENUM) {
                MessageNode enumNode = enumNodeBuilder.build(tokenStorage, null);
                messageNodes.add(enumNode);
            }
        }

        return messageNodes;
    }
}
