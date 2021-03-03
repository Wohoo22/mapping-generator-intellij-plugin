package com.github.parser.proto.messageelementnodesbuilder.elementnodesbuilder.utils;

import com.github.model.DataTypeNode;
import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;
import com.github.parser.proto.utils.MessageNodeUtils;

import java.util.List;

public class TokenUtils {
    public static DataTypeNode tokenToDataTypeNode(Token token, String tokenValue,
                                                   List<MessageNode> rootMessageNodes, MessageNode searchScope,
                                                   TokenStorage tokenStorage) {
        DataTypeNode dataTypeNode = new DataTypeNode();
        dataTypeNode.setPresentableName(tokenValue);
        dataTypeNode.setQualifiedName(tokenValue);

        switch (token) {
            case STRING:
            case BOOL:
            case DOUBLE:
            case FLOAT:
            case INT32:
            case UINT32:
            case SINT32:
            case INT64:
            case UINT64:
            case SINT64:
                dataTypeNode.setDataType(DataTypeNode.DataType.OTHERS);
                break;
            case REPEATED:
                dataTypeNode.setDataType(DataTypeNode.DataType.LIST);
                break;
            case MAP:
                dataTypeNode.setDataType(DataTypeNode.DataType.MAP);
                // skip the >
                while (tokenStorage.getCurrentToken() != Token.GT)
                    tokenStorage.next();
                tokenStorage.next();
                break;
            // 2 cases left are ENUM and OBJECT, we need to search the relevant Message Node to find the Data Type
            default:
                MessageNode messageNode = MessageNodeUtils.findMessageNodeByPresentableAndQualifiedName(rootMessageNodes, searchScope, tokenValue, tokenValue);
                if (messageNode != null && messageNode.isEnum())
                    dataTypeNode.setDataType(DataTypeNode.DataType.ENUM);
                else
                    dataTypeNode.setDataType(DataTypeNode.DataType.OBJECT);

                dataTypeNode.setQualifiedName(messageNode == null ? tokenValue : messageNode.getQualifiedName());
                dataTypeNode.setPresentableName(messageNode == null ? tokenValue : messageNode.getPresentableName());
        }

        return dataTypeNode;
    }

}
