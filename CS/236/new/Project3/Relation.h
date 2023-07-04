//
// Created by Sukyoung Kwak on 3/6/23.
//

#ifndef PROJECT2_RELATION_H
#define PROJECT2_RELATION_H

#include <set>
#include <utility>

class Relation {

private:

    string name;
    Scheme scheme;
    set<Tuple> tuples;

public:

    Relation(string  name, Scheme  scheme)
            : name(std::move(name)), scheme(std::move(scheme)) { }

    void addTuple(const Tuple& tuple) {
        tuples.insert(tuple);
    }

    string toString() const {
        stringstream out;
        // fix the code to print "name=value" pairs
        for (auto& tuple: tuples)
            out << tuple.toString(scheme) << endl;
        return out.str();
    }
    Relation select(int index, const string& value) const {
        Relation result(name, scheme);
        // add tuples to the result if they meet the condition
        for (auto& tuple: tuples){
            if (tuple.at(index) == value){
                result.addTuple(tuple);
            }
        }
        return result;
    }

    Relation select(int index1, int index2) const {
        Relation result(name, scheme);
        // add tuples to the result if they meet the condition
        for (auto& tuple: tuples){
            if (tuple.at(index1) == tuple.at(index2)) { result.addTuple(tuple); }
        }
        return result;
    }

    Relation project(vector<int> indices) {
        vector<string> temp;
        for(auto index: indices){
            temp.push_back(scheme.at(index));
        }

        Scheme newScheme(temp);
        Relation result(name, newScheme);

        for (auto &tuple: tuples) {
            vector<string> newTuple;
            for (auto index: indices)newTuple.push_back(tuple.at(index));
            result.addTuple(newTuple);
        }
        return result;
    }

    Relation rename(vector<string> names) {
        Scheme newScheme(names);
        Relation result(name, newScheme);

        for (auto &tuple: tuples) result.addTuple(tuple);
        return result;
    }

    const Scheme &getScheme() const {
        return scheme;
    }

    const set<Tuple> &getTuples() const {
        return tuples;
    }


};

#endif //PROJECT2_RELATION_H
