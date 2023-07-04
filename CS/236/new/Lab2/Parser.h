//
// Created by Sukyoung Kwak on 2/4/23.
//

#ifndef LAB2_PARSER_H
#define LAB2_PARSER_H

#pragma once
#include <string>
#include <sstream>
#include <vector>
#include "Token.h"

class Parser{
private:
    vector<Token> tokens;
public:
    Parser(const vector<Token>& tokens): tokens(tokens) { }

    TokenType tokenType() const{
        return tokens.at(0).getType();
    }
    void advanceToken(){
        tokens.erase(tokens.begin());
    }
    void throwError(){
        throw tokens.at(0);
    }
    void match(TokenType t){
        cout << "match: " << t << endl;
        if (tokenType()==t){
            advanceToken();
        }
        else{
            throwError();
        }
    }
    void idList() {
        //idList -> COMMA ID idList | lambda
        if (tokenType() == COMMA) {
            match(COMMA);
            match(ID);
            idList();
        } else {
            // lambda
        }
    }
    void scheme() {
        //scheme -> ID LEFT_PAREN ID idList RIGHT_PAREN
        match(ID);
        match(LEFT_PAREN);
        match(ID);
        idList();
        match(RIGHT_PAREN);
    }


};

#endif //LAB2_PARSER_H
