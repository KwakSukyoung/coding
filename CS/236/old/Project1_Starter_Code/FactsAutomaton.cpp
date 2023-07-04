#include "FactsAutomaton.h"

void FactsAutomaton::S0(const std::string& input) {
    if (input[index] == 'F') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}//F

void FactsAutomaton::S1(const std::string& input) {
    if (input[index] == 'a') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}//a

void FactsAutomaton::S2(const std::string& input) {
    if (input[index] == 'c') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}//c

void FactsAutomaton::S3(const std::string& input) {
    if (input[index] == 't') {
        inputRead++;
        index++;
        f(input);
    }
    else {
        Serr();
    }
}//t

void FactsAutomaton::f(const std::string& input) {
    if (input[index] == 's') {
        inputRead++;
    }
    else {
        Serr();
    }
}//s
