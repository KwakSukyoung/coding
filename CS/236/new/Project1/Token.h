//
// Created by Sukyoung Kwak on 1/17/23.
//
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"

#ifndef UNTITLED1_TOKEN_H
#define UNTITLED1_TOKEN_H

#pragma once
#include <string>
#include <sstream>

enum TokenType{
    COMMA,
    PERIOD,
    Q_MARK,
    LEFT_PAREN,
    RIGHT_PAREN,
    COLON,
    COLON_DASH,
    MULTIPLY,
    ADD,
    SCHEMES,
    FACTS,
    RULES,
    QUERIES,
    ID,
    STRING,
    COMMENT,
    UNDEFINED,
    ENDOFFILE,
};

class Token {
private:
    TokenType type;
    std::string description;
    unsigned int lineNumber;

public:
    Token(TokenType type, std::string description, unsigned int lineNumber):
    type(type), description(description), lineNumber(lineNumber) {
        //type(type) = this-> type = type;
    }

    std::string typeName(TokenType type) const {
        std::string out = "";
        switch (type) {
            case COMMA:
                out = "COMMA";
                break;
            case PERIOD:
                out = "PERIOD";
                break;
            case Q_MARK:
                out = "Q_MARK";
                break;
            case LEFT_PAREN:
                out = "LEFT_PAREN";
                break;
            case RIGHT_PAREN:
                out = "RIGHT_PAREN";
                break;
            case COLON:
                out = "COLON";
                break;
            case COLON_DASH:
                out = "COLON_DASH";
                break;
            case MULTIPLY:
                out = "MULTIPLY";
                break;
            case ADD:
                out = "ADD";
                break;
            case SCHEMES:
                out = "SCHEMES";
                break;
            case FACTS:
                out = "FACTS";
                break;
            case RULES:
                out = "RULES";
                break;
            case QUERIES:
                out = "QUERIES";
                break;
            case ID:
                out = "ID";
                break;
            case STRING:
                out = "STRING";
                break;
            case COMMENT:
                out = "COMMENT";
                break;
            case UNDEFINED:
                out = "UNDEFINED";
                break;
            case ENDOFFILE:
                out = "EOF";
                break;

        }
        return out;
    };

    std::string toString() const{
        std::stringstream out;
        out << "(" << typeName(type)  << "," << "\"" << description << "\"" <<"," << lineNumber << ")";
        return out.str();
    }

};

#endif //UNTITLED1_TOKEN_H