#ifndef LEXER_H
#define LEXER_H
#include <string>
#include <vector>
#include "Automaton.h"
#include "Token.h"
#include <cctype>

//store a collectino of finite-state automata
//store all the generated tokens
class Lexer
{
private:
    std::vector<Automaton*> automata;
    std::vector<Token*> tokens;
    Token *newToken;

    void CreateAutomata();

    // TODO: add any other private methods here (if needed)

public:
    Lexer();
    ~Lexer();
    void Run(std::string& input);
    //Parallel and Max algorithm
    //Initialize two variables: maximum read value to 0
    // corresponding maximum finite state automaton (=max automaton)
    //to the first finite-state automaton in the collection

    // TODO: add other public methods here
   void print() const;
};

#endif // LEXER_H

