package com.github2.utils;

public class QualifiedNameUtils {
    public static String refine(String s) {
        switch (s) {
            case "string":
                return "java.lang.String";
            case "bool":
                return "boolean";
            case "int32":
            case "uint32":
            case "sint32":
                return "int";
            case "int64":
            case "uint64":
            case "sint64":
                return "long";
        }
        return s;
    }

    public static String appendToBegin(String value, String name) {
        return value + "." + name;
    }
}
