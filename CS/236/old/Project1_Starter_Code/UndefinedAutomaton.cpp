#include "UndefinedAutomaton.h"
#include <iostream>

void UndefinedAutomaton::S0(const std::string& input) {
    if (EndOfFile(input)){
        Serr();
    }
    else{
        //false string automaton
        if (input[index] == '\''){
            inputRead++;
            index++;
            S1(input);
        }
        //needs to make a comment automaton also
        else{
            Serr();
        }
    }
}//letters

void UndefinedAutomaton::S1(const std::string& input){
    if (EndOfFile(input)) {
        return;
    }
    else{
        inputRead++;
        index++;
        S1(input);
    }
}