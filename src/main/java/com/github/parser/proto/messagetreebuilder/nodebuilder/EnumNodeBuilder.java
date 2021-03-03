package com.github.parser.proto.messagetreebuilder.nodebuilder;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.messagetreebuilder.utils.MessageNodeUtils;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;

public class EnumNodeBuilder {
    // start with token IDENTIFIER (enum name)
    public MessageNode build(TokenStorage tokenStorage, MessageNode parent) {
        MessageNode enumNode = new MessageNode();

        // set parent
        enumNode.setParent(parent);

        // parse name
        String rawName = tokenStorage.getCurrentTokenValueAndMoveNext();

        String presentableName = MessageNodeUtils.extractPresentableName(rawName);
        enumNode.setPresentableName(presentableName);

        String qualifiedName = MessageNodeUtils.findQualifiedName(enumNode);
        enumNode.setQualifiedName(qualifiedName);

        // is enum = true
        enumNode.setEnum(true);

        // skip the open brace
        tokenStorage.next();

        // pass the close brace
        while (tokenStorage.getCurrentToken() != Token.CLOSE_BRACE)
            tokenStorage.next();
        tokenStorage.next();

        return enumNode;
    }
}
