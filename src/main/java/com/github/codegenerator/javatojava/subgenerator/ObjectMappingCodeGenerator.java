package com.github.codegenerator.javatojava.subgenerator;

import com.github.model.ElementNode;
import com.github.utils.ElementNodeUtils;
import com.github.utils.JavaCommandUtils;
import com.github.utils.NameUtils;

import java.util.Set;

public class ObjectMappingCodeGenerator {
    public String generateMappingCode(String parentObjectToSetVarName, ElementNode fieldToSet,
                                      String parentObjectToGetVarName, ElementNode fieldToGet,
                                      Set<String> usedVariableName, String indent) {
        String result = "";

        // declare object-to-set
        String crtObjectToSetVarName = NameUtils.generateUniqueRandomName(fieldToSet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += JavaCommandUtils.generateObjectDeclaration(fieldToSet.getDataTypeNode().getQualifiedName(), crtObjectToSetVarName);

        // declare object-to-get
        String crtObjectToGetVarName = NameUtils.generateUniqueRandomName(fieldToGet.getDataTypeNode().getPresentableName(), usedVariableName);
        result += fieldToGet.getDataTypeNode().getQualifiedName() + " " + crtObjectToGetVarName + " = "
                + JavaCommandUtils.generateGetter(parentObjectToGetVarName, fieldToGet.getName()) + ";" + "\n";

        // generate mapping code for object fields
        FieldMappingCodeGenerator fieldMappingCodeGenerator = new FieldMappingCodeGenerator();
        for (ElementNode childFieldToSet : fieldToSet.getChildren()) {
            ElementNode childFieldToGet = ElementNodeUtils.findElementWithEqualNameAndDataType(childFieldToSet, fieldToGet.getChildren());
            // can't find a suitable field
            if (childFieldToGet == null) continue;

            result = result.concat(fieldMappingCodeGenerator.generateMappingCode(crtObjectToSetVarName, crtObjectToGetVarName,
                    childFieldToSet, childFieldToGet, indent, usedVariableName));
        }

        result += "\n";

        // set generated object to parent object
        result += JavaCommandUtils.generateSetter(parentObjectToSetVarName, fieldToSet.getName(), crtObjectToSetVarName);

        return result;
    }
}
