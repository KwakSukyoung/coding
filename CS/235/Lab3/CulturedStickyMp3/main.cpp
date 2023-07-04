#include <iostream>
#include <vector>
#include <set>
#include <sstream>
#include <fstream>
#include <map>
#include <list>

using namespace std;

// vector<string> part2(string filename){
// 	ifstream in(filename);
// 	string next_line;
// 	vector<string> returning;
	
// 	while (getline(in, next_line)){
// 		istringstream iss(next_line);
// 		string token;
// 		while (iss >> token){
// 			string nopunct = "";
// 			for(auto &c : token){
// 				if (isalpha(c)){
// 					nopunct += c;
// 				}
// 			}
// 			returning.push_back(nopunct);
// 		}
// 	}

// 	return returning;
// }

int main(int argc, char *argv[]){
	vector<string> tokens;
	set <string> unique;
	string next_line;
	string filename = argv[1];
	ifstream in(filename);

	while (getline(in, next_line)){
		istringstream iss(next_line);
		string token;
		while (iss >> token){
			string nopunct = "";
			for(auto &c : token){
				if (isalpha(c)){
					nopunct += c;
				}
			}
			tokens.push_back(nopunct);
			unique.insert(nopunct);
		}
	}

	cout << "Number of words " << tokens.size() << endl;
	cout << "Number of unique words " << unique.size() << endl;

	ofstream setfile(filename+"_set.txt");
	ofstream vectorfile(filename+"_vector.txt");
	ofstream mapfile(filename+"_map.txt");

	//part1
	for (set<string>::iterator it=unique.begin(); it!= unique.end(); ++it){
		setfile << ' ' << *it;
	}

	//part2
	for (vector<string>::iterator it=tokens.begin(); it <= tokens.end()-2; ++it){
		vectorfile << ' ' << *it;
	}

	//part3
  map<list<string>, vector<string> > wordmap;
  list<string> state;
	const int M = 3;

  for (int i = 0; i < M; i++) {
    state.push_back("");
  }
                        
  for (vector<string>::iterator it=tokens.begin(); it!=tokens.end(); it++) {
    wordmap[state].push_back(*it);
    state.push_back(*it);
    state.pop_front();
  }

	// for (map<list<string>, vector<string>>::iterator it=wordmap.begin(); it!=wordmap.end(); it++){
	// 	mapfile << it->first<<", "<<it->second<< endl;
	// }
	srand(time(NULL)); // this line initializes the random number generated
                   // so you dont get the same thing every time
	state.clear();
  for (int i = 0; i < M; i++) {
    state.push_back("");
  }
  for (int i = 0; i < 100; i++) {
    int ind = rand() % wordmap[state].size();
    cout << wordmap[state][ind]<<" ";
    state.push_back(wordmap[state][ind]);
    state.pop_front();
  }

    return 0;
}