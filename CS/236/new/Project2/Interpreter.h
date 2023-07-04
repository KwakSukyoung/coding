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

    //3. one of the big projects!
    string ruleEvaluation(){
        //4. make rules and facts objects
        vector<Rule> rules = myDatalogProgram.Rules;
        vector<Predicate> facts = myDatalogProgram.Facts;
        //5. make strinstream which will be the returing values
        stringstream out;

        //6. print Rule Evaluation
        out << "Rule Evaluation \n";

        //7. we are making these values because we need this for loops and later we have to print one of them out
        int numPasses = 0;
        int numTuples = 1;

        //8. we will loop through till there is no difference using numTuples
        // numTuples is the the number of diff I guess
        while(numTuples>0) {
            //9.we keep the numTuples to be 0 every loop so we can see if there is any change every loop
            numTuples = 0;
            //10.now loopt thorugh each rules
            //Then we will also print them out
            for (unsigned int i = 0; i < rules.size(); i++) {
                out <<  rules.at(i).toString() << endl;

                //11.make a relations object
                //actually have no idea what I'm doing here haha\
                // I think this is the evaluate Predicate part
                vector<Relation> relations;
                for (long unsigned int j = 0; j < rules.at(i).Body.size(); ++j) {
                    Predicate myPred = rules.at(i).Body.at(j);
                    relations.push_back(myDatabase.evaluatePredicates(myPred.name, myPred.getNames()));
                }

                //join
                //12. now we are going to join them.
                //You don't have joint function. You are going to make it in relation.
                //Let's go!
                Relation joint = relations.at(0);
                for (long unsigned int j = 1; j < relations.size(); ++j) {
                    joint = joint.join(relations.at(j));
                }

                //20. we try to find the indices of each scheme so we can remove columns that we don't need
                //which is the project part
                vector<int> indices;
                Scheme jointScheme = joint.getScheme();

                for (long unsigned int j = 0; j < rules.at(i).Head.params.size(); ++j) {
                    vector<string>::iterator iter = find(jointScheme.begin(), jointScheme.end(),
                                                         rules.at(i).Head.params.at(j).toString());
                    indices.push_back(distance(jointScheme.begin(), iter));
                }

                joint = joint.project(indices);

                //21. we try to do rename part now
                //we want to change s,n,a,p(example) to capitalized
                vector<string> names;
                for (long unsigned int j = 0; j < joint.getScheme().size(); ++j) {
                    char myChar = toupper(joint.getScheme().at(j)[0]);
                    string ex;
                    ex += myChar;
                    names.push_back(ex);
                }

                //22. we have to make a rename again in relation! Let's go!
                joint = joint.rename(names);

                //26. welcome back! from here, I have no idea what I'm doing haha
                //But it's the process for union step
                string smartPervert = rules.at(i).Head.name;
                Relation sean = myDatabase.helpJake(smartPervert);
                Relation racistJake = sean.uni(joint);
                myDatabase.setRelation(racistJake, smartPervert);
                //27. this is the variable we defined in the beggining of the loop
                //we get the differences here
                numTuples += racistJake.diff(sean).getSize();
                Relation coke = racistJake.diff(sean);
                //28. we print the new added things here
                if(numTuples!=0){
                    out << coke.toString() << endl;
                }
            }
            //29. Then we just add numPasses here
            numPasses++;

        }
        //30 Then we will use the numPasses here
        out << endl;
        out <<"Schemes populated after " + to_string(numPasses)+ " passes through the Rules.";

        return out.str();

    }

    //32.we defind queryEvaluation
    string queryEvaluation(){
        //we get those objects
        vector<Rule> rules = myDatalogProgram.Rules;
        vector<Predicate> facts = myDatalogProgram.Facts;
        //all you are going to do is printing the queries using queries that you defined above
        //long time ago.
        stringstream out;
        out << endl << endl;
        out << "Query Evaluation \n";
        out << Queries();


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
