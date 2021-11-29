package com.github_new.generator.interfaces;

import com.github_new.generator.ports.JavaToJavaGeneratorPort;

public interface JavaToJavaGenerator {
    public String generate(JavaToJavaGeneratorPort.GenerateInput input);
}
