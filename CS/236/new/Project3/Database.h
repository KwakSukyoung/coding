//
// Created by Sukyoung Kwak on 3/18/23.
//

#ifndef PROJECT2_DATABASE_H
#define PROJECT2_DATABASE_H

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"

#pragma once
#include <string>
#include <sstream>
#include <map>
#include <vector>
#include <algorithm>
#include "Relation.h"
using namespace std;

// Created by Sukyoung Kwak on 2/10/23.
//

class Database {
private:
    std::map<std::string, unsigned int> relationVectors;
    std::vector<Relation> allRelations;

public:

    Relation evaluateQueries(const string& names, vector<string> params) {
        Relation curr = allRelations.at(relationVectors[names]);
        map<string, int> myDict;
        vector<string> keys;
        vector<int> values;

        for (unsigned int i = 0; i < params.size(); ++i) {
            auto key = myDict.find(params.at(i));

            if (params.at(i).at(0) == '\'') {
                curr = curr.select(i, params.at(i));
            }
            else if ((params.at(i).at(0) != '\'')&&(key != myDict.end())) {
                curr = curr.select(key->second, i);
            }
            else {
                myDict[params.at(i)] = i;
                keys.push_back(params.at(i));
            }
        }

        values =  updateValues(myDict, values);

        curr = curr.project(values).rename(keys);

        return curr;
    }

    void add(string names, vector<string> params) {
        Relation result(names, params);
        allRelations.push_back(result);
    }

    void setRelationVectors(const string &names, int index) {
        relationVectors[names] = index;
    }

    int getIndex(const string& name) const{
        return relationVectors.at(name);
    }

    void addRelations(int index, Tuple tuple){
        allRelations.at(index).addTuple(tuple);
    }

    vector<int> updateValues(map<string, int> myDict, vector<int> values){
        for (auto key = myDict.begin(); key != myDict.end(); key++) values.push_back(key->second);

        sort(values.begin(), values.end());

        return values;
    }



};

#endif //PROJECT2_DATABASE_H