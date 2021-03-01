package com.github.parser.proto.lexer.utils;

import com.github.parser.proto.lexer.enums.Token;

public class TokenUtils {
    public static Token charToToken(char c) {
        switch (c) {
            case '{':
                return Token.OPEN_BRACE;
            case '}':
                return Token.CLOSE_BRACE;
            case '=':
                return Token.EQ;
            case ';':
                return Token.SEMICOLON;
            case '(':
                return Token.OPEN_PARENTHESIS;
            case ')':
                return Token.CLOSE_PARENTHESIS;
            case ',':
                return Token.COLON;
            case '<':
                return Token.LT;
            case '>':
                return Token.GT;
        }
        return null;
    }

    public static Token stringToToken(String s) {
        if (s.isBlank() || s.isEmpty()) return null;

        switch (s) {
            case "enum":
                return Token.ENUM;
            case "message":
                return Token.MESSAGE;
            case "string":
                return Token.STRING;
            case "bool":
                return Token.BOOL;
            case "double":
                return Token.DOUBLE;
            case "float":
                return Token.FLOAT;
            case "int32":
                return Token.INT32;
            case "uint32":
                return Token.UINT32;
            case "sint32":
                return Token.SINT32;
            case "int64":
                return Token.INT64;
            case "uint64":
                return Token.UINT64;
            case "sint64":
                return Token.SINT64;
            case "repeated":
                return Token.REPEATED;
            case "map":
                return Token.MAP;
        }

        if (allCharsAreDigit(s))
            return Token.ORDINAL;

        return Token.IDENTIFIER;
    }

    private static boolean allCharsAreDigit(String s) {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }
}
