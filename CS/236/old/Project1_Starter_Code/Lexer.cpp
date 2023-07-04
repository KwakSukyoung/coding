#include "Lexer.h"
#include "ColonAutomaton.h"
#include "ColonDashAutomaton.h"
#include "AddAutomaton.h"
#include "CommaAutomaton.h"
#include "CommentAutomaton.h"
#include "FactsAutomaton.h"
#include "ID_Automaton.h"
#include "Left_ParenAutomaton.h"
#include "MultiplyAutomaton.h"
#include "PeriodAutomaton.h"
#include "Q_MarkAutomaton.h"
#include "QueriesAutomaton.h"
#include "Right_ParenAutomaton.h"
#include "RulesAutomaton.h"
#include "SchemesAutomaton.h"
#include "StringAutomaton.h"
#include "Automaton.h"
#include "UndefinedAutomaton.h"
#include <string>
#include <iostream>
#include <cctype>

Lexer::Lexer() {
    CreateAutomata();
}

Lexer::~Lexer() {
    automata.clear();
    tokens.clear();

    // TODO: need to clean up the memory in `automata` and `tokens`
}
//Check end of file in the beginning
void Lexer::CreateAutomata() {
    automata.push_back(new ColonAutomaton());
    automata.push_back(new ColonDashAutomaton());
    automata.push_back(new AddAutomaton());
    automata.push_back(new CommaAutomaton());
    automata.push_back(new CommentAutomaton());
    automata.push_back(new FactsAutomaton());
    automata.push_back(new ID_Automaton());
    automata.push_back(new Left_ParenAutomaton());
    automata.push_back(new MultiplyAutomaton());
    automata.push_back(new PeriodAutomaton());
    automata.push_back(new Q_MarkAutomaton());
    automata.push_back(new QueriesAutomaton());
    automata.push_back(new Right_ParenAutomaton());
    automata.push_back(new RulesAutomaton());
    automata.push_back(new SchemesAutomaton());
    automata.push_back(new StringAutomaton());
    automata.push_back(new UndefinedAutomaton());
    //Add the other needed automata here
}

void Lexer::Run(std::string& input) {
    // TODO: convert this pseudo-code with the algorithm into actual C++ code
    int lineNumber = 1;
    int maxRead = 0;
//    Automaton *maxAutomaton;
    // While there are more characters to tokenize
    while (input.size() > 0) {
        if (isspace(input[0])){
            if (input[0] == '\n'){
                lineNumber += 1;
            }
            input = input.substr(1);
            continue;
        }

        maxRead = 0;
        Automaton *maxAutomaton = automata[0];

        // TODO: you need to handle whitespace in between toke
        // "PARALLEL"
        //   Each automaton runs with the same input;
        // For each automaton in automata
        for(int i = 0; i < (int)automata.size()-1; i++){
            //inputRead = atuomaton.Start(input)
            int inputRead = automata[i]->Start(input);
            //if (inputRead > maxRead):
            if (inputRead > maxRead) {
                //set maxRead to inputRead
                maxRead = inputRead;
                //set maxAutomaton to automaton
                maxAutomaton = automata[i];
            }
        }
        // Here is the "Max" part of the algorithm
        if (maxRead > 0) {
            newToken = maxAutomaton->CreateToken(input.substr(0, maxRead), lineNumber);
            lineNumber += maxAutomaton->NewLinesRead();
            tokens.push_back(newToken);
        }
        // No automaton accepted input
        // Create single character undefined token
        // needs to be modified after the undefined automaton
        else{
            int inputRead = automata[15]->Start(input);
            maxRead = inputRead;
            newToken = new Token(TokenType::UNDEFINED, input.substr(0, maxRead), lineNumber);
            tokens.push_back(newToken);
            maxRead = 1;
//            newToken = new Token(TokenType::UNDEFINED, input.substr(0, maxRead), lineNumber);
//            tokens.push_back(newToken);
        }
        // Update `input` by removing characters read to create Token
        input = input.substr(maxRead);
    }
    newToken = new Token(TokenType::ENDFILE, input.substr(0, maxRead), lineNumber);
    tokens.push_back(newToken);
}

void Lexer::print() const{
    for (auto token : tokens){
        token -> print();
    }
    std::cout<< "Total Tokens = " << tokens.size();
}
