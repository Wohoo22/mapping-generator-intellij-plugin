package com.github.parser.proto.lexer.model;

public class Reader {
    private final String data;
    private int index;


    private final char EOF = '\0';

    public Reader(String data) {
        data = data.concat(String.valueOf(EOF));
        this.data = data;
        index = 0;
    }

    public boolean end() {
        return data.charAt(index) == EOF;
    }

    public void skipWhiteSpaces() {
        while (!end() && data.charAt(index) == ' ')
            index++;
    }

    public char currentChar() {
        return data.charAt(index);
    }

    public void nextChar() {
        index++;
    }

    public String parseNextKeyWord() {
        skipWhiteSpaces();

        String res = "";

        while (!end() && charInKeyWord(data.charAt(index))) {
            res = res.concat(String.valueOf(data.charAt(index)));
            index++;
        }

        return res;
    }

    private boolean charInKeyWord(char c) {
        if (Character.isLetterOrDigit(c))
            return true;
        switch (c) {
            case ' ':
            case '{':
            case '}':
            case '=':
            case ';':
            case '(':
            case ')':
            case ',':
            case '<':
            case '>':
                return false;
        }
        return true;
    }
}
