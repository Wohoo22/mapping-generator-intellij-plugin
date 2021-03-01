package com.github.parser.proto.messageelementnodesbuilder.elementnodesbuilder;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.messageelementnodesbuilder.elementnodesbuilder.utils.TokenUtils;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;

import java.util.ArrayList;
import java.util.List;

public class BuildElementNodeFromTokens {
    public void build(MessageNode messageNode, List<MessageNode> rootMessageNodes) {
        if (messageNode.isEnum()) return;

        List<ElementNode> elementNodes = new ArrayList<>();

        TokenStorage tokenStorage = messageNode.getTokenStorage();

        while (!tokenStorage.outOfToken()) {
            Token currentToken = tokenStorage.getCurrentToken();
            String currentTokenValue = tokenStorage.getCurrentTokenValue();
            tokenStorage.next();

            ElementNode elementNode = new ElementNode();
            DataTypeNode dataTypeNode = TokenUtils.tokenToDataTypeNode(currentToken, currentTokenValue, rootMessageNodes, messageNode, tokenStorage);

            if (dataTypeNode.getDataType() == DataTypeNode.DataType.LIST) {
                Token innerToken = tokenStorage.getCurrentToken();
                String innerTokenValue = tokenStorage.getCurrentTokenValue();
                tokenStorage.next();
                dataTypeNode.setChild(TokenUtils.tokenToDataTypeNode(innerToken, innerTokenValue, rootMessageNodes, messageNode, tokenStorage));
            }

            String elementName = tokenStorage.getCurrentTokenValueAndMoveNext();
            elementNode.setName(elementName);
            elementNode.setDataTypeNode(dataTypeNode);

            elementNodes.add(elementNode);

            // pass the semicolon
            while (tokenStorage.getCurrentToken() != Token.SEMICOLON)
                tokenStorage.next();
            tokenStorage.next();
        }

        for (MessageNode childMsgNode : messageNode.getChildren())
            this.build(childMsgNode, rootMessageNodes);

        messageNode.setElementNodes(elementNodes);
    }
}
