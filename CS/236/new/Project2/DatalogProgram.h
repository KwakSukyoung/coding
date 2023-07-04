//
// Created by Sukyoung Kwak on 2/11/23.
//

#ifndef PROJECT2_DATALOGPROGRAM_H
#define PROJECT2_DATALOGPROGRAM_H

#pragma once
#include <string>
#include <sstream>
#include <vector>
#include "Token.h"
#include "Parser.h"
#include "Predicate.h"
#include "Rule.h"
#include <set>

using namespace std;

class Parser;

class DatalogProgram{
private:


public:
    DatalogProgram() {}

    vector<Predicate> Schemes;
    vector<Predicate> Facts;
    vector<Predicate> Queries;
    vector<Rule> Rules;
    set<string> domain;
    vector<Token> tokens;
    Parser p(Token);

    DatalogProgram(const vector<Token>& tokens, const Parser p(Token) ):
    tokens(tokens) { }

    string ToString(){
        stringstream out;
        out << "Schemes(" << Schemes.size() << "):\n";
        for (long unsigned int i = 0; i< Schemes.size();i++) {
            out<< "  " << Schemes.at(i).toString() << "\n";
        }
        out << "Facts(" << Facts.size() << "):\n";
        for (long unsigned int i = 0; i< Facts.size();i++) {
            out<< "  " << Facts.at(i).toString() << ".\n";
        }
        out << "Rules(" << Rules.size() << "):\n";
        for (long unsigned int i = 0; i< Rules.size();i++) {
            out<< "  " << Rules.at(i).toString() << "\n";
        }
        out << "Queries(" << Queries.size() << "):\n";
        for (long unsigned int i = 0; i< Queries.size();i++) {
            out<< "  " << Queries.at(i).toString() << "?\n";
        }
        set<string>::iterator iter;
        out << "Domain(" << domain.size() << "):\n";
        for (iter = domain.begin(); iter != domain.end(); iter++) {
            out << "  " << *iter << "\n";
        }
        return out.str();
    }

};



#endif //PROJECT2_DATALOGPROGRAM_H


