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
    vector<Parameter> getParams(){
        return params;
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
        for (auto &string: params)paramStrings.push_back(string.toString());

        return paramStrings;
    }
};

#endif //PROJECT2_PREDICATE_H
