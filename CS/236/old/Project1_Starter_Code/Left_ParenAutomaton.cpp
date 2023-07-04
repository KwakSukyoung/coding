#include "Left_ParenAutomaton.h"

void Left_ParenAutomaton::S0(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == '(') {
        inputRead = 1;
    }
    else {
        Serr();
    }
}