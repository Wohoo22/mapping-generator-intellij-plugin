package com.github.parser.proto.messagetreebuilder.nodebuilder;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.model.TokenStorage;
import com.github.parser.proto.model.MessageNode;

public class EnumNodeBuilder {
    // start with token IDENTIFIER (enum name)
    public MessageNode build(TokenStorage tokenStorage, MessageNode parent) {
        MessageNode enumNode = new MessageNode();

        // parse name
        String name = tokenStorage.getCurrentTokenValueAndMoveNext();

        enumNode.setName(name);
        enumNode.setEnum(true);
        enumNode.setParent(parent);

        // skip the open brace
        tokenStorage.next();

        // pass the close brace
        while (tokenStorage.getCurrentToken() != Token.CLOSE_BRACE)
            tokenStorage.next();
        tokenStorage.next();

        return enumNode;
    }
}
