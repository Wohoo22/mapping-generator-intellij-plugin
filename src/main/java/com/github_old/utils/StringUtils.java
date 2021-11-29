package com.github_old.utils;

public class StringUtils {
    public static final String sp = " ";
    public static final String lb = "\n";

    public static String addSpaces(String oldStr, int number) {
        String res = "";
        for (int i = 0; i < number; i++)
            res = res.concat(sp);
        return oldStr + res;
    }

    public static String removeComments(String s) {
        String res = "";

        boolean singleLineCmt = false;
        boolean multipleLineCmt = false;

        for (int i = 0; i < s.length(); i++) {
            if (singleLineCmt && s.charAt(i) == '\n')
                singleLineCmt = false;
            else if (multipleLineCmt && s.charAt(i) == '*' && s.charAt(i + 1) == '/')
                multipleLineCmt = false;
            else if (s.charAt(i) == '/' && s.charAt(i) == '/')
                singleLineCmt = true;
            else if (s.charAt(i) == '/' && s.charAt(i) == '*')
                multipleLineCmt = true;
            else if (!singleLineCmt && !multipleLineCmt)
                res = res.concat(String.valueOf(s.charAt(i)));
        }

        return res;
    }
}
