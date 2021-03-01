package com.github.parser.proto.lexer.enums;

public enum Token {
    // message, enum parts
    ENUM,
    MESSAGE,
    IDENTIFIER,
    OPEN_BRACE,
    CLOSE_BRACE,
    SEMICOLON, // ;
    EQ, // =
    ORDINAL, // 1,2,3,...
    OPEN_PARENTHESIS, // (
    CLOSE_PARENTHESIS, // )

    // data_types
    STRING,
    BOOL,
    DOUBLE,
    FLOAT,
    INT32,
    UINT32,
    SINT32,
    INT64,
    UINT64,
    SINT64,
    REPEATED,
    MAP,

    // else
    LT,
    GT,
    COLON,
}
