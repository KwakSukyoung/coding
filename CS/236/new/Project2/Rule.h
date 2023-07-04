//
// Created by Sukyoung Kwak on 3/20/23.
//

#ifndef PROJECT2_RULE_H
#define PROJECT2_RULE_H


#include "Predicate.h"

class Rule{
public:
    Predicate Head;
    vector<Predicate> Body;
    Rule() {}

    string toString(){
        string a = Head.toString();
        a += " :- ";
        a += Body.at(0).toString();
        for (long unsigned int i = 1; i<Body.size(); ++i){
            a += ","+ Body.at(i).toString();
        }
        return a+ ".";
    }

};

#endif //PROJECT2_RULE_H
