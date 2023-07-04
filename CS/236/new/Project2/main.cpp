#include <iostream>
#include "Token.h"
#include "Scanner.h"
#include "Parser.h"
#include "Scheme.h"
#include "Tuple.h"
#include "DatalogProgram.h"
#include "Interpreter.h"
#include "Relation.h"
#include "Rule.h"
#include <string>
#include <vector>
#include <fstream>
using namespace std;




int main(int argc, char** argv) {

    //opening an input stream
    string filename = argv[1];
    ifstream myFile(filename);
    string myString;
    string input = "";

    //getting the inputs
    if (myFile.is_open()) {
        string line;
        while (getline(myFile,line)) {
            input += line +"\n";
        }
        myFile.close();

    }
    else{
        cout << "Not working"<< endl;
    }

//    //Project1

    Scanner s = Scanner(input);
    Token t = s.scanToken();
    vector<Token> totos = s.getTokens();

    totos.push_back(t);

    if(totos.at(totos.size()-2).getType()==ENDOFFILE){
        totos.pop_back();
    }

//    for(int i =0; i < totos.size();++i){
//        cout << totos.at(i).toString() << endl;
//
//    }

    //Project2
    Parser p = Parser(totos);

    try{
        p.dataLogProgram();

    }
    catch (Token t){
        cout << "Failure!" << endl;
        cout << "  " << t.toString() << "\n";
        return 0;
    }

    //1.make interpreter object
    Interpreter interpreter(p.data);
    //2.call ruleEvaluation which you prob don't have.
    //so, go to interpreter and make one!
    cout << interpreter.ruleEvaluation();
    //31.call queryEvaluation whcih you don't have either
    //Let's go back to interpreter
    cout << interpreter.queryEvaluation();

    return 0;
}