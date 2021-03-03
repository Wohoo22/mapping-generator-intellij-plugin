package com.github.parser.proto.messagetreebuilder.nodebuilder;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.messagetreebuilder.utils.MessageNodeUtils;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;

public class MessageNodeBuilder {
    // start with token IDENTIFIER (class name)
    public MessageNode build(TokenStorage tokenStorage, MessageNode parent) {
        MessageNode msgNode = new MessageNode();

        // set parent
        msgNode.setParent(parent);

        // get message name
        String rawMsgName = tokenStorage.getCurrentTokenValueAndMoveNext();

        String presentableName = MessageNodeUtils.extractPresentableName(rawMsgName);
        msgNode.setPresentableName(presentableName);

        String qualifiedName = MessageNodeUtils.findQualifiedName(msgNode);
        msgNode.setQualifiedName(qualifiedName);

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

        return msgNode;
    }
}
