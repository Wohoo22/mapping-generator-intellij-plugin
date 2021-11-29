package com.github_old.parser.proto.model;

import com.github_old.parser.proto.lexer.enums.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenStorage {
    private final List<Token> tokens;
    private final List<String> tokenValues;

    public void add(Token token, String value) {
        if (token == null) return;
        this.tokens.add(token);
        this.tokenValues.add(value);
    }

    public TokenStorage() {
        tokens = new ArrayList<>();
        tokenValues = new ArrayList<>();
        tokenId = 0;
    }

    private int tokenId;

    public boolean outOfToken() {
        return tokenId >= tokens.size();
    }

    public void next() {
        tokenId++;
    }

    public Token getCurrentToken() {
        return tokens.get(tokenId);
    }

    public String getCurrentTokenValue() {
        return tokenValues.get(tokenId);
    }

    public Token getCurrentTokenAndMoveNext() {
        Token res = tokens.get(tokenId);
        tokenId++;
        return res;
    }

    public String getCurrentTokenValueAndMoveNext() {
        String res = tokenValues.get(tokenId);
        tokenId++;
        return res;
    }
}
