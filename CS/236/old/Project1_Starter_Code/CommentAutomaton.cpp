#include "CommentAutomaton.h"

void CommentAutomaton::S0(const std::string& input) {
    if (input[index] == '#') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void CommentAutomaton::S1(const std::string& input) {
    if (input[index] != '|') {
        //line comments
        inputRead++;
        index++;
        S2(input);
    }
    else if (input[index] == '|') {
        //block comments
        inputRead++;
        index++;
        S3(input);
    }
    else{
        return;
    }
}
//line Comment ////////////////////////////////////////////
void CommentAutomaton::S2(const std::string& input) {
    if ((input[index] != '\n')&&(!EndOfFile(input))) {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        return;
    }
}

//block Comment //////////////////////////////////////////
void CommentAutomaton::S3(const std::string& input) {
    if (input[index] != '|') {
        inputRead++;
        index++;
        S3(input);
    }
    else if (input[index] == '|'){
        inputRead++;
        index++;
        S4(input);
    }
    else if(EndOfFile(input)){
        type = TokenType::UNDEFINED;
    }
    else {
        Serr();
    }
}

void CommentAutomaton::S4(const std::string& input){
    if(input[index] != '#'){
        inputRead++;
        index++;
        S3(input);
    }
    else if(EndOfFile(input)){
        type = TokenType::UNDEFINED;
    }
    else if(input[index] =='#'){
        inputRead += 1;
        return;
    }
    else {
        Serr();
    }
}