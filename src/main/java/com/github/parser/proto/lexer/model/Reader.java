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

    public String parseNextWord() {
        skipWhiteSpaces();

        String res = "";

        while (!end() && Character.isLetterOrDigit(data.charAt(index))) {
            res = res.concat(String.valueOf(data.charAt(index)));
            index++;
        }

        return res;
    }
}
