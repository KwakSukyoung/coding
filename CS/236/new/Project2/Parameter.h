//
// Created by Sukyoung Kwak on 3/20/23.
//

#ifndef PROJECT2_PARAMETER_H
#define PROJECT2_PARAMETER_H

#include <string>
using namespace std;

class Parameter{
private:
    string name;
public:
    void SetParam(string param){
        name = param;
    }
    string toString(){
        return name;
    }
};
#endif //PROJECT2_PARAMETER_H
