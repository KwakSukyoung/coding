//
// Created by Sukyoung Kwak on 3/20/23.
//

#ifndef PROJECT2_PREDICATE_H
#define PROJECT2_PREDICATE_H
#include <string>
#include "Parameter.h"
#include <vector>

using namespace std;

class Predicate{

public:
    string name;
    vector<Parameter> params;

    void SetName(string theName){
        name = theName;
    }

    string toString(){
        string a = name + "("+params.at(0).toString();
        for (long unsigned int i = 1; i<params.size(); ++i){
            a += ","+ params.at(i).toString();
        }
        return a+ ")";
    }

    vector<string> getNames() {
        vector<string> paramStrings;
        for(long unsigned int i = 0; i < params.size();++i){
            paramStrings.push_back(params.at(i).toString());
        }

        return paramStrings;
    }
};

#endif //PROJECT2_PREDICATE_H
