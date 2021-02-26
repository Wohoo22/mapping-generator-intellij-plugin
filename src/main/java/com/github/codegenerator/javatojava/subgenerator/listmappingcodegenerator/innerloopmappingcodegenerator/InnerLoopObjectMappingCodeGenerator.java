package com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.innerloopmappingcodegenerator;

import com.github.codegenerator.javatojava.subgenerator.FieldMappingCodeGenerator;
import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;

import java.util.List;
import java.util.Set;

public class InnerLoopObjectMappingCodeGenerator {
    public String generateMappingCode(String parentListToAddVarName, DataTypeNode objectToSetDataTypeNode, List<ElementNode> objectToSetFields,
                                      DataTypeNode objectToGetDataTypeNode, String objectToGetVarName, List<ElementNode> objectToGetFields,
                                      String indent, Set<String> usedVariableName) {
        String result = "";

        // declare object-to-set
        String crtObjectToSetVarName = NameUtils.generateUniqueRandomName(objectToSetDataTypeNode.getPresentableName(), usedVariableName);
        result += indent + JavaCommandUtils.generateObjectDeclaration(objectToSetDataTypeNode.getQualifiedName(), crtObjectToSetVarName);

        // generate mapping code for object fields
        FieldMappingCodeGenerator fieldMappingCodeGenerator = new FieldMappingCodeGenerator();
        for (ElementNode childFieldToSet : objectToSetFields) {
            ElementNode childFieldToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(childFieldToSet, objectToGetFields);
            // can't find a suitable field
            if (childFieldToGet == null) continue;

            result = result.concat(fieldMappingCodeGenerator.generateMappingCode(crtObjectToSetVarName, objectToGetVarName,
                    childFieldToSet, childFieldToGet, indent, usedVariableName));
        }

        result += "\n";

        // add mapped object to parent list
        result += indent + JavaCommandUtils.generateListAdd(parentListToAddVarName, crtObjectToSetVarName);

        return result;
    }
}
