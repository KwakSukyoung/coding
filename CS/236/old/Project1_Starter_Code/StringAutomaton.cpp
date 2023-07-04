#include "StringAutomaton.h"
#include "Lexer.h"

void StringAutomaton::S0(const std::string& input) {
    if (input[index] == '\'') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}//receive a single quote

void StringAutomaton::S1(const std::string& input) {
    int quote_num = 0;
    if (EndOfFile(input) ){
        Serr();
    }
    else if (input[index]=='\''){
        //when there's another single quote
        quote_num = 0;
        int i = 0;
        //check how many single quotes we have after the first one
        while(input[index+i]== '\''){
            quote_num += 1;
            i += 1;
        }
        inputRead+= quote_num;
        index += quote_num;
        //odd number of quotes
        if ((quote_num%2)==1){
            return;
        }
        else{
            S1(input);
        }

    }
    else{
        if (input[index] == '\n'){
            newLines ++;
        }
        inputRead++;
        index ++;
        S1(input);
    }

}//receive any input till you get a single quote


