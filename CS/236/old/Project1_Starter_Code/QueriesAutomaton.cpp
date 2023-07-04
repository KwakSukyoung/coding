#include "QueriesAutomaton.h"
#include <iostream>

void QueriesAutomaton::S0(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'Q') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}//Q

void QueriesAutomaton::S1(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'u') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}//u

void QueriesAutomaton::S2(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'e') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}//e

void QueriesAutomaton::S3(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'r') {
        inputRead++;
        index++;
        S4(input);
    }
    else {
        Serr();
    }
}//r

void QueriesAutomaton::S4(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'i') {
        inputRead++;
        index++;
        S5(input);
    }
    else {
        Serr();
    }
}//i

void QueriesAutomaton::S5(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 'e') {
        inputRead++;
        index++;
        f(input);
    }
    else {
        Serr();
    }
}//e

void QueriesAutomaton::f(const std::string& input) {
    if (EndOfFile(input)){
        return;
    }
    else if (input[index] == 's') {
        inputRead++;
    }
    else {
        Serr();
    }
}//s