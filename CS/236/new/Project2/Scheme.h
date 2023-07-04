//
// Created by Sukyoung Kwak on 3/6/23.
//

#ifndef PROJECT2_SCHEME_H
#define PROJECT2_SCHEME_H

#include <vector>
#include <string>
using namespace std;

class Scheme: public vector<string> {

public:
    Scheme() {}
    Scheme(vector<string> names) : vector<string>(names) { }

    // TODO: add more delegation functions as needed
    string toString() const{
        stringstream out;
        for(unsigned int i =0; i < this->size();++i){
            out << this->at(i) << " ";
        }
        return out.str();
    }

};

#endif //PROJECT2_SCHEME_H
