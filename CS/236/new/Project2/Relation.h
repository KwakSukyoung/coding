//
// Created by Sukyoung Kwak on 3/6/23.
//

#ifndef PROJECT2_RELATION_H
#define PROJECT2_RELATION_H

#include <set>
#include <utility>

class Relation {

private:

    string name;
    Scheme scheme;
    set<Tuple> tuples;

public:
    Relation() {}

    Relation(string  name, Scheme  scheme)
            : name(name), scheme(scheme) { }


    void addTuple(const Tuple& tuple) {
        tuples.insert(tuple);
    }

    string toString() const {
        stringstream out;
        // fix the code to print "name=value" pairs
        for (auto& tuple: tuples)
            out << tuple.toString(scheme);
        return out.str();
    }
    Relation select(int index, const string& value) const {
        Relation result(name, scheme);
        // add tuples to the result if they meet the condition
        for (auto& tuple: tuples){
            if (tuple.at(index) == value){
                result.addTuple(tuple);
            }
        }
        return result;
    }

    Relation select(int index1, int index2) const {
        Relation result(name, scheme);
        // add tuples to the result if they meet the condition
        for (auto& tuple: tuples){
            if (tuple.at(index1) == tuple.at(index2)) { result.addTuple(tuple); }
        }
        return result;
    }

    Relation project(vector<int> indices) {
        vector<string> temp;
        for(auto index: indices){
            temp.push_back(scheme.at(index));
        }

        Scheme newScheme(temp);
        Relation result(name, newScheme);

        for (auto &tuple: tuples) {
            vector<string> newTuple;
            for (auto index: indices)newTuple.push_back(tuple.at(index));
            result.addTuple(newTuple);
        }
        return result;
    }
    //23. we define rename
    Relation rename(vector<string> names) {
        //24. obviously make the object here
        Scheme newScheme(names);
        Relation result(name, newScheme);

        //25. you chnage the name with the inputs
        for (auto &tuple: tuples) result.addTuple(tuple);
        return result;
    }

    const Scheme &getScheme() const {
        return scheme;
    }

    const set<Tuple> &getTuples() const {
        return tuples;
    }

    static bool joinable(const Scheme& leftScheme, const Scheme& rightScheme,
                         const Tuple& leftTuple, const Tuple& rightTuple) {
        // add code to test whether the tuples are joinable
        bool canJoin = true;
        for(unsigned int i =0; i< leftScheme.size();++i){
            for(unsigned int j =0; j< rightScheme.size();++j){
                if((leftScheme.at(i)==rightScheme.at(j))&&(leftTuple.at(i)!= rightTuple.at(j))){
                    canJoin = false;
                }
            }
        }

        return canJoin;

    }
    //13. we define the function here.
    Relation join(const Relation& right) {
        const Relation& left = *this;
        Relation result;
        //14. make a join scheme outside of the function
        result.scheme = joinSchemes(left.scheme, right.scheme);

        //15.loopt through each tuples
        for (const Tuple& leftTuple : left.tuples) {
            for (const Tuple& rightTuple : right.tuples) {
                //16. check if it's joinable
                if(joinable(left.scheme, right.scheme, leftTuple, rightTuple)){
                    //17. we join the tuples which you should define again.
                    Tuple myTuple = joinTuples(leftTuple, rightTuple, left.scheme, right.scheme);
                    result.addTuple(myTuple);
                }
            }
        }


        return result;
    }
    //15.we make joinSchemes
    Scheme joinSchemes(Scheme leftScheme, Scheme rightScheme){
        Scheme myScheme = leftScheme;
        //16. we check if there is a same scheme
        //Then we remove the duplicated ones
        for(long unsigned int i =0; i < leftScheme.size();++i){
            for(long unsigned int j =0; j < rightScheme.size();++j){
                if(leftScheme.at(i)==rightScheme.at(j)){
                    rightScheme.erase(rightScheme.begin() +j);
                }
            }
        }
        //Then for the rest of the left overs, you add them together
        for(long unsigned int i =0; i < rightScheme.size();++i){
            myScheme.push_back(rightScheme.at(i));
        }

        return myScheme;

    }

    //18. we make joinTuples
    Tuple joinTuples(Tuple leftTuple, Tuple rightTuple, Scheme leftScheme, Scheme righScheme){
        //19. you are doing the sample thing almost we joinScheme.
        Tuple myTuple = leftTuple;
        for(long unsigned int i =0; i < leftTuple.size();++i){
            for(long unsigned int j =0; j < rightTuple.size();++j){
                if(leftScheme.at(i)==righScheme.at(j)){
                    rightTuple.erase(rightTuple.begin() +j);
                    righScheme.erase(righScheme.begin() +j);
                }
            }
        }

        for(long unsigned int i =0; i < rightTuple.size();++i){
            myTuple.push_back(rightTuple.at(i));
        }

        return myTuple;
    }

    Relation uni(const Relation& justOne){
        const Relation left = *this;
        Relation result("",scheme);

        set<Tuple> leTu = left.getTuples();
        set<Tuple> riTu = justOne.getTuples();

        for(auto tuple: leTu){
            result.addTuple(tuple);
        }

        for(auto tuple: riTu){
            result.addTuple(tuple);
        }
        return result;
    }

    Relation diff(const Relation& right){
        const Relation left = *this;
        Relation new_rel("",scheme);

        set<Tuple> riTu = right.tuples;
        set<Tuple> leTu = left.tuples;

        for(auto tuple: leTu){
            if(riTu.find(tuple) == riTu.end()){
                new_rel.addTuple(tuple);
            }
        }

        return new_rel;
    }

    int getSize(){
        return tuples.size();
    }
};

#endif //PROJECT2_RELATION_H
