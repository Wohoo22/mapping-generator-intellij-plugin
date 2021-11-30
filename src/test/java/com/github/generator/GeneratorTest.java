package com.github.generator;

import com.github.FileUtils;
import com.github_new.domain.ElementNode;
import com.github_new.generator.impl.JavaToJavaGeneratorImpl;
import com.github_new.generator.interfaces.JavaToJavaGenerator;
import com.github_new.generator.ports.JavaToJavaGeneratorPort;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.util.List;

public class GeneratorTest {

    public static class NodesWrapper {
        public List<ElementNode> nodes;
    }

    @Test
    public void runTests() throws Exception {
        int total = 2;
        for (int i = 2; i <= total; i++) {
            BufferedReader input = FileUtils.readBuffer("/generator/input" + i + ".txt");

            String objectToSetQualifiedName = input.readLine();
            String objectToGetVariableName = input.readLine();
            String objectToGetQualifiedName = input.readLine();
            String elementToSetJson = FileUtils.nextJson(input);
            String elementToGetJson = FileUtils.nextJson(input);
            List<ElementNode> elementToSet = new Gson().fromJson(elementToSetJson, NodesWrapper.class).nodes;
            List<ElementNode> elementToGet = new Gson().fromJson(elementToGetJson, NodesWrapper.class).nodes;

            JavaToJavaGenerator javaToJavaGenerator = new JavaToJavaGeneratorImpl();
            String actualBuilder = javaToJavaGenerator.generate(JavaToJavaGeneratorPort.GenerateInput.builder()
                    .objectToSetQualifiedName(objectToSetQualifiedName)
                    .objectToGetVariableName(objectToGetVariableName)
                    .objectToGetQualifiedName(objectToGetQualifiedName)
                    .mappingType(JavaToJavaGeneratorPort.GenerateInput.MappingType.NORMAL)
                    .elementsToSet(elementToSet)
                    .elementsToGet(elementToGet)
                    .build());
            String actualNormal = javaToJavaGenerator.generate(JavaToJavaGeneratorPort.GenerateInput.builder()
                    .objectToSetQualifiedName(objectToSetQualifiedName)
                    .objectToGetVariableName(objectToGetVariableName)
                    .objectToGetQualifiedName(objectToGetQualifiedName)
                    .mappingType(JavaToJavaGeneratorPort.GenerateInput.MappingType.BUILDER)
                    .elementsToSet(elementToSet)
                    .elementsToGet(elementToGet)
                    .build());
            String actual = actualBuilder + "\n\n############\n\n" + actualNormal;

            String expect = FileUtils.read("/generator/output" + i + ".txt");
            try {
                Assertions.assertEquals(actual, expect);
            } catch (Error error) {
//                FileUtils.writeFile("/generator/output" + i + ".txt", actual);
                System.out.println("Test " + i + " fail.");
                throw error;
            }
        }
    }

}
