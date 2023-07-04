#ifndef UNDEFINEDAUTOMATON_H
#define UNDEFINEDAUTOMATON_H

#include "Automaton.h"

class UndefinedAutomaton : public Automaton
{
private:
    void S1(const std::string& input);
public:
    UndefinedAutomaton() : Automaton(TokenType::UNDEFINED) {}  // Call the base constructor

    void S0(const std::string& input);
};

#endif // UNDEFINEDAUTOMATON_H


