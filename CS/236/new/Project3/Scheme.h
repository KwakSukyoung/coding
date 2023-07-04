//
// Created by Sukyoung Kwak on 3/6/23.
//

#ifndef PROJECT2_SCHEME_H
#define PROJECT2_SCHEME_H

#include <vector>
#include <string>
using namespace std;

class Scheme {

private:

    vector<string> names;

public:

    Scheme(vector<string> names) : names(names) { }

    unsigned long size() const {
        return names.size();
    }

    const string& at(int index) const {
        return names.at(index);
    }

    // TODO: add more delegation functions as needed

};

#endif //PROJECT2_SCHEME_H
