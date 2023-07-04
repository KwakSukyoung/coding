//
// Created by Sukyoung Kwak on 3/6/23.
//

#ifndef PROJECT2_TUPLE_H
#define PROJECT2_TUPLE_H

#include <utility>
#include <vector>
#include <sstream>
#include "Scheme.h"

using namespace std;

class Tuple {

private:

    vector<string> values;


public:

    Tuple(vector<string> values) : values(std::move(values)) { }

    unsigned long size() const {
        return values.size();
    }
    const string& at(int index) const {
        return values.at(index);
    }
    bool operator<(const Tuple t) const {
        return values < t.values;
    }

    // TODO: add more delegation functions as needed

    string toString(const Scheme& scheme) const {
        const Tuple& tuple = *this;
        stringstream out;
        // fix the code to print "name=value" pairs
        for (unsigned int i = 0; i< scheme.size();++i){
            if (i < scheme.size()-1){
                out << scheme.at(i);
                out << "=";
                out << tuple.at(i);
                out << ", ";
            }
            else{
                out << scheme.at(i);
                out << "=";
                out << tuple.at(i);
            }
        }
        out << endl;
        return out.str();
    }


};

#endif //PROJECT2_TUPLE_H

