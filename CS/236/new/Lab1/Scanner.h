//
// Created by Sukyoung Kwak on 1/18/23.
//
#ifndef UNTITLED1_SCANNER_H
#define UNTITLED1_SCANNER_H

#pragma once
#include <string>
#include "Token.h"
#include <cctype>

class Scanner {
private:
    std::string input;
    //maybe more variables are inquired
    TokenType temp_type;
    std::string temp_value;
    int temp_line;

public:
    Scanner(const std::string &input, int tempLine, TokenType type, const std::string &value) : input(input),
                                                                                                temp_type(DEFAULT),
                                                                                                temp_value("&&&"),
                                                                                                temp_line(1){}

    Token scanToken() {
        //skipping whitespace
        //if the new line, change the lineNumber
        //if just a space, keep the lineNumber
        while (input.size() >0) {
            //check if it's whitespace
            if (isspace(input.at(0))){
                if (input.at(0) == '\n'){
                    //lineNumber += 1;
                    temp_line++;
                }
                input = input.substr(1);
                continue;
            }
            else{
                break;
            }
        }
        //check for COMMA
        for (int i = 0; i < input.length(); ++i){
            if (input.at(i) == ','){
                input = input.substr(0,i)+input.substr(i+1);
                return Token(COMMA, ",", temp_line);
            }
        }

    }


};


#endif //UNTITLED1_SCANNER_H
