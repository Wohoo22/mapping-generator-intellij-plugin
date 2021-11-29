package com.github_old.parser.proto.lexer;

import com.github_old.parser.proto.lexer.enums.Token;
import com.github_old.parser.proto.lexer.model.Reader;
import com.github_old.parser.proto.lexer.utils.TokenUtils;
import com.github_old.parser.proto.model.TokenStorage;
import com.github_old.utils.StringUtils;

public class Lexer {
    public TokenStorage lex(String data) {
        // replace comments, /n
        data = StringUtils.removeComments(data);
        data = data.replace('\n', ' ').replace('\r', ' ');

        TokenStorage tokenStorage = new TokenStorage();
        Reader reader = new Reader(data);

        while (!reader.end()) {
            // skip all white space
            reader.skipWhiteSpaces();

            if (reader.end()) break;

            // parse current char
            char currentChar = reader.currentChar();
            Token currentCharToToken = TokenUtils.charToToken(currentChar);
            if (currentCharToToken != null) {
                tokenStorage.add(currentCharToToken, String.valueOf(currentChar));
                reader.nextChar();
                continue;
            }

            // parse next word
            String nextWord = reader.parseNextKeyWord();

            if (nextWord.isEmpty()) {
                reader.nextChar();
                continue;
            }

            Token token = TokenUtils.stringToToken(nextWord);
            if (token != null) tokenStorage.add(token, nextWord);
        }

        return tokenStorage;
    }
}
