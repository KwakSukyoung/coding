#include <iostream>
#include "Token.h"
#include "Scanner.h"
#include <string>
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

    Scanner s = Scanner(input);//read the input
    Token t = s.scanToken();
    cout << t.toString() << endl;
    cout << "Total Tokens = " << s.getTotalTokenNum() << endl;

    return 0;
}