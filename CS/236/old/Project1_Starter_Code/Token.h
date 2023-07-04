#ifndef TOKEN_H
#define TOKEN_H
#include <string>
#include <sstream>
//3. Add '#programa once' to avoid multiple includes of the file
#pragma once

//1. Make a 'TokenType' enum
enum TokenType {
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
    ENDOFFILE

};

//2.Make a 'Token' class
class Token {
private:
    TokenType type;
    std::string description;
    unsigned int lineNumber;

//4. Add a 'Token' class constructor
public:
    Token(TokenType type, std::string description, unsigned int lineNumber):
    type(type), description(description),lineNumber(lineNumber){
        //type(type) = this->type = type;
    };
    //TODO: Can I write like that regardless of the line nubmer?

    std::string token_type_toString(TokenType current_type) {
        std::string out = "";
        switch (current_type) {

            case TokenType::COLON:
                out = "COLON";
                break;
            case TokenType::COLON_DASH:
                out = "COLON_DASH";
                break;
            case TokenType::COMMA:
                out = "COMMA";
                break;
            case TokenType::PERIOD:
                out = "PERIOD";
                break;
            case TokenType::Q_MARK:
                out = "Q_MARK";
                break;
            case TokenType::LEFT_PAREN:
                out = "LEFT_PAREN";
                break;
            case TokenType::RIGHT_PAREN:
                out = "RIGHT_PAREN";
                break;
            case TokenType::MULTIPLY:
                out = "MULTIPLY";
                break;
            case TokenType::ADD:
                out = "ADD";
                break;
            case TokenType::SCHEMES:
                out = "SCHEMES";
                break;
            case TokenType::FACTS:
                out = "FACTS";
                break;
            case TokenType::RULES:
                out = "RULES";
                break;
            case TokenType::QUERIES:
                out = "QUERIES";
                break;
            case TokenType::ID:
                out = "ID";
                break;
            case TokenType::STRING:
                out = "STRING";
                break;
            case TokenType::COMMENT:
                out = "COMMENT";
                break;
            case TokenType::ENDFILE:
                out = "EOF";
                break;
            case TokenType::UNDEFINED:
                out = "UNDEFINED";
                break;
        }
        return out;
    };

    //5. Add a 'toString' function to the 'Token' class
    std::string toString() {
        stringstream out;
        out << "(" << token_type_toString(type) << "," << "\"" << description << "\"" << "," << lineNumber << ")";
        return out.str();
    }

};

#endif //TOKEN_H