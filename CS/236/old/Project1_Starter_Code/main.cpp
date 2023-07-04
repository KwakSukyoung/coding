#include "Lexer.h"
#include <fstream>
#include <string>
#include <iostream>
#include <string>
using namespace std;

int main(int argc, char** argv) {
    //opening an input stream
    ifstream myFile;
    string myString;
    myFile.open(argv[1]);
    string input = "";

    if (myFile) {
        string line;
        while (getline(myFile,line)) {
            input = input + line;
            input = input + "\n";
        }
    }

    myFile.close();
    Lexer* lexer = new Lexer();
    lexer->Run(input);
    lexer->print();
    return 0;
}

//Verify command-line arguments
//Instantiate an instance of the Lexer class
//Pass the input to Lexer and let it run
//Main will need to get the Tokens from the Lexer
//and print them out in a specified format
//perform any clean up

//delete lexer;