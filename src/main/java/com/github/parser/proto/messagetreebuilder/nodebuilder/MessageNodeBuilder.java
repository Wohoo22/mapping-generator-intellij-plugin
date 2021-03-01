package com.github.parser.proto.messagetreebuilder.nodebuilder;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.model.TokenStorage;
import com.github.parser.proto.model.MessageNode;

public class MessageNodeBuilder {
    // start with token IDENTIFIER (class name)
    public MessageNode build(TokenStorage tokenStorage, MessageNode parent) {
        MessageNode msgNode = new MessageNode();

        // get message name
        String msgName = tokenStorage.getCurrentTokenValueAndMoveNext();
        msgNode.setName(msgName);

        // pass the open brace
        tokenStorage.next();

        EnumNodeBuilder enumNodeBuilder = new EnumNodeBuilder();

        while (tokenStorage.getCurrentToken() != Token.CLOSE_BRACE) {
            Token currentToken = tokenStorage.getCurrentToken();
            String currentTokenValue = tokenStorage.getCurrentTokenValue();
            tokenStorage.next();

            // message
            if (currentToken == Token.MESSAGE) {
                MessageNode childMsgNode = this.build(tokenStorage, msgNode);
                msgNode.getChildren().add(childMsgNode);
            }
            // enum
            else if (currentToken == Token.ENUM) {
                MessageNode enumNode = enumNodeBuilder.build(tokenStorage, msgNode);
                msgNode.getChildren().add(enumNode);
            }
            // element token
            else {
                msgNode.getTokenStorage().add(currentToken, currentTokenValue);
            }
        }

        // pass the close brace
        tokenStorage.next();

        // set parent
        msgNode.setParent(parent);

        return msgNode;
    }
}
