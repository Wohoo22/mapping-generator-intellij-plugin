package com.github.utils;

import java.util.Locale;

public class NameUtils {
    public static String getPresentableName(String qualifiedName) {
        return qualifiedName.split("\\.")[0];
    }

    public static String toLowerCaseAllChars(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static String toLowerCaseFirstChar(String name) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (i == 0) {
                res.append(String.valueOf(name.charAt(i)).toLowerCase(Locale.ROOT));
            } else {
                res.append(name.charAt(i));
            }
        }
        return res.toString();
    }
}
