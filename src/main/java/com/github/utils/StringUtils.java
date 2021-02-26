package com.github.utils;

public class StringUtils {
    public static final String sp = " ";
    public static final String lb = "\n";

    public static String addSpaces(String oldStr, int number) {
        String res = "";
        for (int i = 0; i < number; i++)
            res = res.concat(sp);
        return oldStr + res;
    }
}
