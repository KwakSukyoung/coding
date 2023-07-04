//
// Created by Sukyoung Kwak on 3/18/23.
//

#ifndef PROJECT2_INTERPRETER_H
#define PROJECT2_INTERPRETER_H

#include "DatalogProgram.h"
#include "Database.h"

using namespace std;

class Interpreter {
private:
    DatalogProgram myDatalogProgram;
    Database myDatabase;
public:
    Interpreter(const DatalogProgram &myDatalogProgram) : myDatalogProgram(myDatalogProgram) {
        updateDatabaseSchemes();
        updateDatabaseFacts();
    }

    string Queries() {
        vector<Predicate> queries = myDatalogProgram.Queries;
        stringstream out;

        for (unsigned int i = 0; i < queries.size(); i++) {
            string names = queries.at(i).name;
            vector<string> params = queries.at(i).getNames();

            Relation myRelation = myDatabase.evaluateQueries(names,params);

            out << queryString(queries, i, myRelation);

        }
        return out.str();
    }

    string queryString(vector<Predicate> queries, int index, Relation myRelation){
        string str = queries.at(index).toString() + "? ";
        if (myRelation.getTuples().size() == 0) {
            str += "No \n";
        }
        else if ((myRelation.getTuples().size() != 0)&&(myRelation.getScheme().size() <= 0)){
            str += "Yes(" + to_string(myRelation.getTuples().size()) + ") \n";
        }
        else {
            str += "Yes(" + to_string(myRelation.getTuples().size()) + ") \n"+ myRelation.toString();
        }
        return str;
    }

    void updateDatabaseSchemes(){
        for (unsigned int i = 0; i < myDatalogProgram.Schemes.size();++i) {
            string names = myDatalogProgram.Schemes.at(i).name;
            myDatabase.add(names, myDatalogProgram.Schemes.at(i).getNames());
            myDatabase.setRelationVectors(names, i);
        }
    }

    void updateDatabaseFacts(){
        for (unsigned int i = 0; i < myDatalogProgram.Facts.size();++i) {
            string names = myDatalogProgram.Facts.at(i).name;
            Tuple tuple(myDatalogProgram.Facts.at(i).getNames());
            myDatabase.addRelations(myDatabase.getIndex(names), tuple);
        }
    }
};

#endif //PROJECT2_INTERPRETER_H
