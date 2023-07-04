#include <iostream>
#include <string>
#include <fstream>
#include <stack>

using namespace std;

int main(int argc, char *argv[]) {
    stack<char> my_stack;
    bool isbalanced = true;
    string arg = argv[1];
    int ind = 0;
    const string OPEN = "([{<";
    const string CLOSE = ")]}>";

    if (arg.length()%2 == 0){
        while ((isbalanced)&&(ind < arg.length())){
            if ((arg.at(ind)=='(')||(arg.at(ind)=='{')||(arg.at(ind)=='[')||(arg.at(ind)=='<')){
                my_stack.push(arg.at(ind));
            }
            else if ((arg.at(ind)==')')||(arg.at(ind)=='}')||(arg.at(ind)==']')||(arg.at(ind)=='>')){
                if(my_stack.empty()){
                    isbalanced = false;
                    break;
                }
                else if(OPEN.find(my_stack.top()) != (CLOSE.find(arg.at(ind)))){
                    isbalanced = false;
                    break;
                }
                else{
                    my_stack.pop();
                }
            }
            ind += 1;
        }
    }
    else{
        isbalanced = false;
    }

    if ((isbalanced == true)&&(my_stack.empty())){
        cout << "true";
    }
    else{
        cout << "false";
    }

    return 0;
}