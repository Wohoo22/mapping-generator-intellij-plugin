package com.github_new.generator.helper.utils;

import com.github_new.domain.GlobalCounter;

import java.util.Locale;
import java.util.Set;

public class NameUtils {
    public static String toLowerCaseFirstChar(String name) {
        return name.substring(0, 1).toLowerCase(Locale.ROOT) + name.substring(1);
    }

    public static String toUpperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public static String generateUniqueRandomName(String name, Set<String> usedName) {
        // delete all chars that are not letter
        String cleanedName = "";
        for (int i = 0; i < name.length(); i++)
            if (Character.isLetter(name.charAt(i)))
                cleanedName = cleanedName.concat(String.valueOf(name.charAt(i)));

        cleanedName = toLowerCaseFirstChar(cleanedName);

        String res = cleanedName;
        while (usedName.contains(res)) {
            res = cleanedName.concat(GlobalCounter.get() + "");
            GlobalCounter.inc();
        }

        usedName.add(res);
        return res;
    }

    public static boolean isQualifiedName(String s) {
        return s.contains(".");
    }
}