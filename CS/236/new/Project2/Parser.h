//
// Created by Sukyoung Kwak on 2/10/23.
//

#ifndef PROJECT2_PARSER_H
#define PROJECT2_PARSER_H

#pragma once
#include <string>
#include <sstream>
#include <vector>
#include "Token.h"
#include "Parameter.h"
#include "DatalogProgram.h"
#include "Predicate.h"
#include <set>
#include "Rule.h"


class Parser{
private:
    vector<Token> tokens;

public:
    Parser(const vector<Token>& tokens): tokens(tokens) { }
    DatalogProgram data;

    TokenType tokenType() const{
        return tokens.at(0).getType();
    }
    void advanceToken(){
        tokens.erase(tokens.begin());
    }
    void throwError(){
        throw tokens.at(0);
    }
    void match(TokenType t){
        if (tokenType()==t){
            advanceToken();
        }
        else{
            throwError();
        }
    }
    void schemeList(){
        if(tokenType()==ID){
            scheme();
            schemeList();
        }
        else{
            //lambda
        }
    }
    void factList(){
        if(tokenType()==ID){
            fact();
            factList();
        }
        else{
            //lambda
        }
    }
    void queryList(){
        if(tokenType()==ID){
            query();
            queryList();
        }
        else{
            //lambda
        }
    }
    void scheme(){
        Predicate pd;
        Parameter pr;
        pd.SetName(tokens.begin()->getDescription());
        data.Schemes.push_back(pd);
        match(ID);
        match(LEFT_PAREN);
        pr.SetParam(tokens.begin()->getDescription());
        data.Schemes.back().params.push_back(pr);
        match(ID);
        idList(false);
        match(RIGHT_PAREN);
    }
    void fact(){
        Predicate pd;
        Parameter pr;
        pd.SetName(tokens.begin()->getDescription());
        data.Facts.push_back(pd);
        match(ID);
        match(LEFT_PAREN);
        pr.SetParam(tokens.begin()->getDescription());
        data.Facts.back().params.push_back(pr);
        data.domain.insert(tokens.begin()->getDescription());
        match(STRING);
        stringList();
        match(RIGHT_PAREN);
        match(PERIOD);
    }
    void rule(){
        headPredicate();
        match(COLON_DASH);
        predicate(false);
        predicateList(false);
        match(PERIOD);
    }
    void headPredicate(){
        Predicate pd;
        Parameter pr;
        Rule r;
        r.Head.SetName(tokens.begin()-> getDescription());
        data.Rules.push_back(r);
        match(ID);
        match(LEFT_PAREN);
        pr.SetParam(tokens.begin()-> getDescription());
        data.Rules.back().Head.params.push_back(pr);
        match(ID);
        idList(true);
        match(RIGHT_PAREN);
    }
    void ruleList(){
        if(tokenType()==ID){
            rule();
            ruleList();
        }
        else{
            //lambda
        }
    }
    void query(){
        predicate(true);
        match(Q_MARK);
    }
    void predicate(bool Queries){
        Predicate pd;
        Parameter pr;

        pd.SetName(tokens.begin()->getDescription());
        if (!Queries){
            data.Rules.back().Body.push_back(pd);
        }
        else{
            data.Queries.push_back(pd);
        }
        match(ID);
        match(LEFT_PAREN);
        if(Queries){
            parameter(false,true);
            parameterList(false,true);
        }
        else{
            parameter(true,false);
            parameterList(true,false);
        }
        match(RIGHT_PAREN);
    }
    void predicateList(bool Queries){
        if(tokenType()==COMMA){
            match(COMMA);
            predicate(Queries);
            predicateList(Queries);
        }
        else{
            //lambda
        }
    }
    void parameterList(bool Body, bool Queries){
        if(tokenType()==COMMA){
            match(COMMA);
            parameter(Body, Queries);
            parameterList(Body, Queries);
        }
        else{
            //lambda
        }

    }
    void stringList(){
        Parameter pr;
        if(tokenType()==COMMA){
            match(COMMA);
            pr.SetParam(tokens.begin()->getDescription());
            data.Facts.back().params.push_back(pr);
            data.domain.insert(tokens.begin()->getDescription());
            match(STRING);
            stringList();
        }
        else{
            //lambda
        }
    }
    void idList(bool Rule) {
        Parameter pr;
        //idList -> COMMA ID idList | lambda
        if (tokenType() == COMMA) {
            match(COMMA);
            pr.SetParam(tokens.begin()->getDescription());
            if(!Rule){
                data.Schemes.back().params.push_back(pr);
            }
            else{
                data.Rules.back().Head.params.push_back(pr);
            }
            match(ID);
            idList(Rule);
        } else {
            // lambda
        }

    }
    void parameter(bool Body, bool Queries){
        Parameter pr;
        pr.SetParam(tokens.begin()->getDescription());
        if(!Body){
            if(Queries){
                data.Queries.back().params.push_back(pr);
            }
            else{
                data.Rules.back().Head.params.push_back(pr);
            }
        }
        else{
            data.Rules.back().Body.back().params.push_back(pr);
        }
        if(tokenType()==STRING){
            match(STRING);
        }
        else{
            match(ID);
        }
    }
    void dataLogProgram(){
        match(SCHEMES);
        match(COLON);
        scheme();
        schemeList();
        match(FACTS);
        match(COLON);
        factList();
        match(RULES);
        match(COLON);
        ruleList();
        match(QUERIES);
        match(COLON);
        query();
        queryList();
        match(ENDOFFILE);
    }

};



#endif //PROJECT2_PARSER_H

