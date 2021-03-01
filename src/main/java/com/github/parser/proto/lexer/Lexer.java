package com.github.parser.proto.lexer;

import com.github.parser.proto.lexer.enums.Token;
import com.github.parser.proto.lexer.model.Reader;
import com.github.parser.proto.lexer.utils.TokenUtils;
import com.github.parser.proto.model.TokenStorage;
import com.github.utils.StringUtils;

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
            String nextWord = reader.parseNextWord();

            if (nextWord.isEmpty()) {
                reader.nextChar();
                continue;
            }

            Token token = TokenUtils.stringToToken(nextWord);

            if (token == null) continue;

            tokenStorage.add(token, nextWord);
        }

        return tokenStorage;
    }
}
