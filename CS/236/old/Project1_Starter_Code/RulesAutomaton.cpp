#include "RulesAutomaton.h"

void RulesAutomaton::S0(const std::string& input) {
    if (input[index] == 'R') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}//R

void RulesAutomaton::S1(const std::string& input) {
    if (input[index] == 'u') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}//u

void RulesAutomaton::S2(const std::string& input) {
    if (input[index] == 'l') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}//l

void RulesAutomaton::S3(const std::string& input) {
    if (input[index] == 'e') {
        inputRead++;
        index++;
        f(input);
    }
    else {
        Serr();
    }
}//e

void RulesAutomaton::f(const std::string& input) {
    if (input[index] == 's') {
        inputRead++;
    }
    else {
        Serr();
    }
}//s