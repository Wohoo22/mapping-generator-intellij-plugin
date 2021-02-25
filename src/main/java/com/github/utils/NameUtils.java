package com.github.utils;

import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class NameUtils {

    public static String toLowerCaseAllChars(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static String toLowerCaseFirstChar(String name) {
        return name.substring(0, 1).toLowerCase(Locale.ROOT) + name.substring(1);
    }

    public static String toUpperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public static String generateUniqueRandomName(String qualifiedName, Set<String> usedName) {
        if (qualifiedName.length() == 0) return qualifiedName;

        String[] parts = qualifiedName.split("\\.");
        String name = parts[parts.length - 1];
        // to lower case first char
        name = name.substring(0, 1).toLowerCase(Locale.ROOT) + name.substring(1);

        String res = name + new Random().nextInt(10000);
        while (usedName.contains(res))
            res = name + new Random().nextInt(10000);
        usedName.add(res);
        return res;
    }
}
