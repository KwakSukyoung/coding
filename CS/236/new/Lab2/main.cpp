#include <iostream>
#include "Token.h"
#include "Scanner.h"
#include "Parser.h"
#include <string>
#include <fstream>
using namespace std;

int main(int argc, char** argv) {
    //opening an input stream
//    string filename = argv[1];
//    ifstream myFile(filename);
//    string myString;
//    string input = "";
//
//    //getting the inputs
//    if (myFile.is_open()) {
//        string line;
//        while (getline(myFile,line)) {
//            input += line +"\n";
//        }
//        myFile.close();
//
//    }
//    else{
//        cout << "Not working"<< endl;
//    }

//    //Project1
//    Scanner s = Scanner(input);//read the input
//    Token t = s.scanToken();
//    cout << t.toString() << endl;
//    cout << "Total Tokens = " << s.getTotalTokenNum() << endl;

    //Project2
    vector<Token> tokens = {
            Token(ID,"Ned",2),
            //Token(LEFT_PAREN,"(",2),
            Token(ID,"Ted",2),
            Token(COMMA,",",2),
            Token(ID,"Zed",2),
            Token(RIGHT_PAREN,")",2),
    };

    try{
        Parser p = Parser(tokens);
        p.scheme();
    }
    catch (Token t){
        cout << "Failure" << endl;
        cout << "  " << t.toString();
    }
    return 0;
}