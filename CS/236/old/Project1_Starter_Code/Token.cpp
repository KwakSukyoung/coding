#include "Token.h"

#include <iostream>

Token::Token(TokenType type, std::string description, int line) {
    this->type = type;
    this->description = description;
    this->lineNumber = line;
    // TODO: initialize all member variables

}

void Token::print() {
    std::cout << "(" << token_type_toString(type) <<  "," << "\"" << description << "\"" << ","  << lineNumber << ")\n";
}