package com.github.codegenerator.javatojava.subgenerator;

import com.github.codegenerator.javatojava.subgenerator.listmappingcodegenerator.ListMappingCodeGenerator;
import com.github.model.DataTypeNode;
import com.github.model.ElementNode;
import com.github.utils.DataTypeNodeUtils;
import com.github.utils.JavaCommandUtils;

import java.util.Set;

public class FieldMappingCodeGenerator {

    public String generateMappingCode(String objectToSetVarName, String objectToGetVarName,
                                      ElementNode fieldToSet, ElementNode fieldToGet, String indent, Set<String> usedVariableName) {

        String result = "";

        // 2 data types are not equal -> return
        if (fieldToGet == null || fieldToSet == null)
            return result;
        if (!DataTypeNodeUtils.dataTypeEqual(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode()))
            return result;

        // if 2 data types are equal in type and qualified_name -> can be set directly
        if (DataTypeNodeUtils.qualifiedNameEqual(fieldToSet.getDataTypeNode(), fieldToGet.getDataTypeNode())) {
            result += indent + JavaCommandUtils.generateSetterGetter(objectToGetVarName, fieldToGet.getName(), objectToSetVarName, fieldToSet.getName());
        }
        // if type == ENUM
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.ENUM) {
            String valueToSet = JavaCommandUtils.generateEnumConverter(fieldToSet.getDataTypeNode().getQualifiedName(), objectToGetVarName, fieldToGet.getName());
            result += indent + JavaCommandUtils.generateSetter(objectToSetVarName, fieldToSet.getName(), valueToSet);
        }
        // type == OBJECT
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.OBJECT) {
            ObjectMappingCodeGenerator objectMappingCodeGenerator = new ObjectMappingCodeGenerator();
            result += "\n";
            result += indent + objectMappingCodeGenerator.generateMappingCode(objectToSetVarName, fieldToSet, objectToGetVarName, fieldToGet,
                    usedVariableName, indent);
        }
        // type == ARRAY
        else if (fieldToSet.getDataTypeNode().getDataType() == DataTypeNode.DataType.LIST) {
            ListMappingCodeGenerator listMappingCodeGenerator = new ListMappingCodeGenerator();
            result += "\n";
            result += indent + listMappingCodeGenerator.generateMappingCode(objectToSetVarName, fieldToSet, objectToGetVarName, fieldToGet,
                    usedVariableName, indent);
        }
        // else (may be MAP or OTHERS)
        else {
            result += indent + JavaCommandUtils.generateSetterGetter(objectToGetVarName, fieldToGet.getName(), objectToSetVarName, fieldToSet.getName());
        }

        return result;
    }

}
