package com.github.parser.proto.elementtreebuilder;

import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.parser.proto.model.MessageNode;
import com.github.parser.proto.utils.MessageNodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElementTreeBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ElementTreeBuilder.class);

    public List<ElementNode> build(List<MessageNode> rootMessageNodes, String messageName, String javaOuterClassQualifiedName) {

        MessageNode desiredMessageNode = MessageNodeUtils.findMessageNodeByName(rootMessageNodes, null, messageName);

        if (desiredMessageNode == null) {
            logger.error("\n Can't find message: " + messageName + "\n");
            return null;
        } else if (desiredMessageNode.isEnum()) {
            logger.error("\n Message " + messageName + " is an enum \n");
            return null;
        }

        buildElementNodes(desiredMessageNode, rootMessageNodes, javaOuterClassQualifiedName);

        return desiredMessageNode.getElementNodes();
    }

    private static void buildElementNodes(MessageNode messageNode, List<MessageNode> rootMessageNodes, String javaOuterClassQualifiedName) {
        if (messageNode == null || messageNode.isEnum()) return;

        for (ElementNode elementNode : messageNode.getElementNodes()) {

            // object
            if (elementNode.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
                MessageNode childMsgNode = MessageNodeUtils.findMessageNodeByName(rootMessageNodes, messageNode,
                        elementNode.getDataTypeNode().getQualifiedName());

                // get qualified name
                String qualifiedName = MessageNodeUtils.findQualifiedName(childMsgNode, javaOuterClassQualifiedName);
                elementNode.getDataTypeNode().setQualifiedName(qualifiedName);

                // build child element nodes
                buildElementNodes(childMsgNode, rootMessageNodes, javaOuterClassQualifiedName);
                assert childMsgNode != null;
                elementNode.setChildren(childMsgNode.getElementNodes());
            }
            // array
            else if (elementNode.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST
                    && elementNode.getDataTypeNode().getChild().getDataType() == DataTypeNode.DataType.OBJECT) {

                MessageNode childMsgNode = MessageNodeUtils.findMessageNodeByName(rootMessageNodes, messageNode,
                        elementNode.getDataTypeNode().getChild().getQualifiedName());

                // get qualified name
                String qualifiedName = MessageNodeUtils.findQualifiedName(childMsgNode, javaOuterClassQualifiedName);
                elementNode.getDataTypeNode().getChild().setQualifiedName(qualifiedName);

                // build child element nodes
                buildElementNodes(childMsgNode, rootMessageNodes, javaOuterClassQualifiedName);
                assert childMsgNode != null;
                elementNode.setChildren(childMsgNode.getElementNodes());
            }
            // enum
            else if (elementNode.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
                // get qualified name
                String qualifiedName = MessageNodeUtils.findQualifiedName(messageNode, javaOuterClassQualifiedName);
                elementNode.getDataTypeNode().setQualifiedName(qualifiedName);
            }
        }
    }


}
