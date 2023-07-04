#include "ID_Automaton.h"
#include <iostream>

void ID_Automaton::S0(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if ((input.substr(0,7) == "Queries")&&(!isalpha(input[index+7]))){
        Serr();
    }
    else if ((input.substr(0,7) == "Schemes")&&(!isalpha(input[index+7]))){
        Serr();
    }
    else if ((input.substr(0,5) == "Facts")&&(!isalpha(input[index+5]))){
        Serr();
    }
    else if ((input.substr(0,5) == "Rules")&&(!isalpha(input[index+5]))){
        Serr();
    }
    else if (isalpha(input[index])) {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}//letters

void ID_Automaton::S1(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if ((isdigit(input[index]))||(isalpha(input[index]))) {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        return;
    }
}//zero