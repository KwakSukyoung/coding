//
// Created by Sukyoung Kwak on 1/18/23.
//
#ifndef UNTITLED1_SCANNER_H
#define UNTITLED1_SCANNER_H

#pragma once
#include <string>
#include "Token.h"
#include <cctype>
#include <iostream>
using namespace std;

class Scanner {
private:
    std::string input;
    //maybe more variables are inquired
    int temp_line;
    int totalToken;

public:
    Scanner(const string &input) : input(input){
        // if input size is zero, line is 0
        temp_line = 1;
        totalToken = 1;
    }

    Token scanToken() {
        while(!input.empty()){
            //whitespace
            if (isspace(input.at(0))){
                if (input.at(0)=='\n'){
                    temp_line++;
                }
                input = input.substr(1);
                continue;
            }
            //COMMA
            if (input.at(0) == ',') {
                Token t = Token( COMMA, ",", temp_line);
                cout << t.toString() << endl;
                increaseTokenNum();
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //$
            if (input.at(0) == '$') {
                Token t = Token( UNDEFINED, "$", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //DIGIT
            if (isdigit(input.at(0))) {
                Token t = Token( UNDEFINED, input.substr(0,1), temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }

            //$
            if (input.at(0) == '!') {
                Token t = Token( UNDEFINED, "!", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //$
            if (input.at(0) == '-') {
                Token t = Token( UNDEFINED, "-", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //%
            if (input.at(0) == '%') {
                Token t = Token( UNDEFINED, "%", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //&
            if (input.at(0) == '&') {
                Token t = Token( UNDEFINED, "&", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //^
            if (input.at(0) == '^') {
                Token t = Token( UNDEFINED, "^", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);
                }
                else{
                    continue;
                }
            }
            //@
            if (input.at(0) == '@') {
                Token t = Token( UNDEFINED, "@", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //PERIOD
            if (input.at(0) == '.') {
                Token t = Token( PERIOD, ".", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //Q_MARK
            if (input.at(0) == '?') {
                Token t = Token( Q_MARK, "?", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //LEFT_PAREN
            if (input.at(0) == '(') {
                Token t = Token( LEFT_PAREN, "(", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //RIGHT_PAREN
            if (input.at(0) == ')') {
                Token t = Token( RIGHT_PAREN, ")", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //COLON and COLON DASH
            if (input.at(0) == ':') {
                //COLON DASH
                if (input.at(1)=='-'){
                    Token t = Token( COLON_DASH, ":-", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(2);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }

                }
                    //COLON
                else{
                    Token t = Token( COLON, ":", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(1);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }

                }
            }
            //MULTIPLY
            if (input.at(0) == '*') {
                Token t = Token( MULTIPLY, "*", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }

            }
            //ADD
            if (input.at(0) == '+') {
                Token t = Token( ADD, "+", temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }
            //SCHEMES
            if (input.length()>= 7){
                if ((input.substr(0,7) == "Schemes")&&(!isalnum(input.at(7)))) {
                    Token t = Token( SCHEMES, "Schemes", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(7);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }
                }
            }
            //FACTS
            if (input.length()>= 5) {
                if ((input.substr(0,5) == "Facts")&&(!isalpha(input.at(5)))&&(!isdigit(input.at(5)))) {
                    Token t = Token( FACTS, "Facts", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(5);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }
                }
            }
            //RULES
            if (input.length()>= 5) {
                if ((input.substr(0,5) == "Rules")&&(!isalpha(input.at(5)))&&(!isdigit(input.at(5)))) {
                    Token t = Token( RULES, "Rules", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(5);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }
                }
            }
            //QUERIES
            if (input.length()>= 7) {
                if ((input.substr(0,7) == "Queries")&&(!isalpha(input.at(7)))&&(!isdigit(input.at(7)))) {
                    Token t = Token( QUERIES, "Queries", temp_line);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(7);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }
                }
            }

            int remove_character_number = 0;
            //ID
            string input_store = input;
            if (isalpha(input.at(0))) {
                remove_character_number++;
                input_store = input_store.substr(1);
                while ((isdigit(input_store.at(0))) || (isalpha(input_store.at(0)))) {
                    remove_character_number++;
                    input_store = input_store.substr(1);
                }
                Token t = Token( ID, input.substr(0,remove_character_number), temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(remove_character_number);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }            }
            //STRING
            int tempLineString = temp_line;
            if (input.at(0)=='\''){
                remove_character_number++;
                input_store = input_store.substr(1);
                while(!input_store.empty()&&(input_store.at(0)!='\'')){
                    if (input_store.at(0)=='\n'){
                        temp_line++;
                    }
                    remove_character_number++;
                    input_store = input_store.substr(1);
                }
                if (input_store.empty()){
                    Token t = Token(UNDEFINED, input.substr(0,remove_character_number), tempLineString);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(remove_character_number);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);
                    }
                    else{
                        continue;
                    }
                }
                else{
                    Token t = Token(STRING, input.substr(0,remove_character_number+1), tempLineString);
                    increaseTokenNum();
                    cout << t.toString() << endl;
                    input = input.substr(remove_character_number+1);
                    if (input.empty()){
                        return Token(ENDOFFILE, "", temp_line);

                    }
                    else{
                        continue;
                    }
                }
            }
            //COMMENT
            input_store = input;
            if (input.at(0)=='#'){
                input_store =  input_store.substr(1);
                while((!input_store.empty())&&(input_store.at(0)!='\n')){
                    remove_character_number++;
                    input_store = input_store.substr(1);
                }
                Token t = Token(COMMENT, input.substr(0, remove_character_number+1), temp_line);
                increaseTokenNum();
                cout << t.toString() << endl;
                input = input.substr(remove_character_number+1);
                if (input.empty()){
                    return Token(ENDOFFILE, "", temp_line);

                }
                else{
                    continue;
                }
            }

        }

        //no more input string
        return Token(ENDOFFILE, "", temp_line);
    }

    void increaseTokenNum() {
        totalToken++;
    }

    std::string getTotalTokenNum(){
        std::stringstream out;
        out << totalToken;
        return out.str();
    }


};


#endif //UNTITLED1_SCANNER_H
