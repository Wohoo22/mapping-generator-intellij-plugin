package com.github.parser.proto;

import com.github.model.ElementNode;
import com.github.parser.proto.elementtreebuilder.ElementTreeBuilder;
import com.github.parser.proto.lexer.Lexer;
import com.github.parser.proto.messageelementnodesbuilder.MessageElementNodesBuilder;
import com.github.parser.proto.messagetreebuilder.MessageTreeBuilder;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.model.TokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ProtoParser {

    private static final Logger logger = LoggerFactory.getLogger(ProtoParser.class);

    public List<ElementNode> parse(String fileName, String desiredMessageName) {

        // open file
        String data;
        try {
            data = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("\n Can't open file: " + fileName + "\n");
            return null;
        }

        // create lexer
        Lexer lexer = new Lexer();
        TokenStorage tokenStorage = lexer.lex(data);

        // create message tree
        MessageTreeBuilder messageTreeBuilder = new MessageTreeBuilder();
        List<MessageNode> rootMessageNodes = messageTreeBuilder.build(data, tokenStorage);

        // build message element nodes
        MessageElementNodesBuilder messageElementNodesBuilder = new MessageElementNodesBuilder();
        messageElementNodesBuilder.build(rootMessageNodes);

        // build element tree
        ElementTreeBuilder elementTreeBuilder = new ElementTreeBuilder();
        List<ElementNode> elementTree = elementTreeBuilder.build(rootMessageNodes, desiredMessageName);

        return elementTree;
    }
}
