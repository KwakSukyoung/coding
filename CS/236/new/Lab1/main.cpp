#include <iostream>
#include "Token.h"
#include "Scanner.h"

int main() {
    //Part 1 Test
//    Token t = Token(COMMA, ",", 2);
//    std::cout << t.toString() << std::endl;
    //Part 2 Test
//    Scanner s = Scanner(",,");
//    Token t = s.scanToken();
//    std::cout << t.toString() << std::endl;
    //White Space Test
    Scanner s = Scanner(",,", 1);
    Token t = s.scanToken();
    std::cout << t.toString() << std::endl;
    return 0;
}