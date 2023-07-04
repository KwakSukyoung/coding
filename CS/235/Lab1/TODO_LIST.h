#ifndef TODO_LIST_H
#define TODO_LIST_H

#include "TodoListInterface-1.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

class TodoList: public TodoListInterface {
public:
    vector <string> tasks;
    TodoList() {//Constructor
        ifstream infile ("TODOList.txt");
        string line;
        if (infile.is_open()){
            while (getline (infile, line)){
                // cout << line << "\n";
                tasks.push_back(line);
            }
            infile.close();
        }
    }
    ~TodoList() {//Destructor
        ofstream outfile;
        outfile.open("TODOList.txt", ofstream::out | ofstream::trunc);
        for(int i = 0; i < tasks.size(); i++){
            outfile << tasks[i] << endl;
        }
        outfile.close();
    }

    /*
    *   Adds an item to the todo list with the data specified by the string "_duedate" and the task specified by "_task"
    */
    void add(string _duedate, string _task){
        bool val = false;
        string list[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for(int i = 0; i < 7; ++i){
            if (_duedate == list[i]){
                val = true;
                break;
            }
        }
        if (val){
            cout << "In add" <<_duedate <<" "<< endl;
            tasks.push_back(_duedate+" "+_task);
        }
        else{
            cout << "Not a valid date. It has to be in the form of \"Sunday\"";
        }

    };

    /*
    *   Removes an item from the todo list with the specified task name
    *
    *   Returns 1 if it removes an item, 0 otherwise
    */
    int remove(string _task){
        int i =0;
        bool erased = false;
        while (i < tasks.size()){
            if (tasks[i].find(_task)!= string::npos){
                tasks.erase(tasks.begin()+i);
                erased = true;
            }
            else{
                i++;
            }
        }
        // for(int i = m-1;i >= 0; --i) {
        //   cout <<"in for loops" << endl;
        //     if (tasks[i].find(_task) != string::npos) {
        //         cout << "if works" << endl;
        //         tasks.erase(tasks.begin()+i);
        //         erased = true;
        //     }
        // }

        if (erased == false){
            return 0;
        }
        else{
            return 1;
        }
    };

    /*
    *   Prints out the full todo list to the console
    */
    void printTodoList(){
        for(int i=0;i<tasks.size();++i) {
            cout << tasks[i] << " ";
        }
    };

    /*
    *   Prints out all items of a todo list with a particular due date (specified by _duedate)
    */
    void printDaysTasks(string _date){
        bool val = false;
        bool isItThere = false;
        string list[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for(int i = 0; i < 7; ++i){
            if (_date == list[i]){
                val = true;
                break;
            }
        }

        if (val){
            cout << "In print the tasks of " <<_date <<" "<< endl;
            for (int i = 0; i < tasks.size(); ++i){
                if (tasks[i].find(_date)!= string::npos){
                    int index = tasks[i].find(" ") +1;
                    cout << tasks[i].substr(index, tasks[i].size()-1) << endl;
                    isItThere = true;
                }
            }
        }
        else{
            cout << "Not a valid date. It has to be in the form of \"Sunday\"";
        }
        if (!isItThere){
            cout << "We don't have anything to do on that day.";
        }
    };
};

#endif