#include <iostream>
#include "Token.h"
#include "Scanner.h"
#include "Parser.h"
#include "Scheme.h"
#include "Tuple.h"
#include "DatalogProgram.h"
#include "Interpreter.h"
#include "Relation.h"
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
//    cout << "Success!" << endl;
//    cout << p.data.ToString() ;

    Interpreter interpreter(p.data);
    cout << interpreter.Queries();


    return 0;
}