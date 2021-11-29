package com.github_old.parser.proto.elementtreebuilder;

import com.github_old.model.DataTypeNode;
import com.github_old.model.ElementNode;
import com.github_old.parser.proto.model.MessageNode;
import com.github_old.parser.proto.utils.MessageNodeUtils;
import com.github_old.utils.QualifiedNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElementTreeBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ElementTreeBuilder.class);

    public List<ElementNode> build(List<MessageNode> rootMessageNodes, String messageName, String javaOuterClassQualifiedName) {

        MessageNode desiredMessageNode = MessageNodeUtils.findMessageNodeByPresentableName(rootMessageNodes, null, messageName);

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

                MessageNode childMsgNode = MessageNodeUtils.findMessageNodeByPresentableAndQualifiedName(rootMessageNodes, messageNode,
                        elementNode.getDataTypeNode().getPresentableName(), elementNode.getDataTypeNode().getQualifiedName());

                if (childMsgNode == null) continue;

                // append outer class to qualified name
                String javaQualifiedName = QualifiedNameUtils.appendToBegin(javaOuterClassQualifiedName, elementNode.getDataTypeNode().getQualifiedName());
                elementNode.getDataTypeNode().setQualifiedName(javaQualifiedName);

                // build child element nodes
                buildElementNodes(childMsgNode, rootMessageNodes, javaOuterClassQualifiedName);
                elementNode.setChildren(childMsgNode.getElementNodes());
            }
            // array
            else if (elementNode.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST
                    && elementNode.getDataTypeNode().getChild().getDataType() == DataTypeNode.DataType.OBJECT) {

                MessageNode childMsgNode = MessageNodeUtils.findMessageNodeByPresentableAndQualifiedName(rootMessageNodes, messageNode,
                        elementNode.getDataTypeNode().getChild().getPresentableName(), elementNode.getDataTypeNode().getChild().getQualifiedName());

                if (childMsgNode == null) continue;

                // append outer class to qualified name
                String javaQualifiedName = QualifiedNameUtils.appendToBegin(javaOuterClassQualifiedName, elementNode.getDataTypeNode().getChild().getQualifiedName());
                elementNode.getDataTypeNode().getChild().setQualifiedName(javaQualifiedName);

                // build child element nodes
                buildElementNodes(childMsgNode, rootMessageNodes, javaOuterClassQualifiedName);
                elementNode.setChildren(childMsgNode.getElementNodes());
            }
        }
    }


}
