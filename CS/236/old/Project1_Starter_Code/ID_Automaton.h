#ifndef ID_AUTOMATON_H
#define ID_AUTOMATON_H

#include "Automaton.h"

class ID_Automaton : public Automaton
{
private:
    void S1(const std::string& input);
    void f(const std::string& input);

public:
    ID_Automaton() : Automaton(TokenType::ID) {}  // Call the base constructor

    void S0(const std::string& input);
};

#endif // ID_AUTOMATON_H


