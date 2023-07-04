#include "QS.h"
#include <iostream>
#include <string>
#include <list>
#include <bits/stdc++.h>
using namespace std;

bool QS::createArray(int capacity){
	if (my_array != NULL){
		delete[] my_array;
		cip = 0;
	}
	if (capacity > 0){
		my_array = new int[capacity];
		cap = capacity;
		return true;
	}
	else{
		return false;
	}
}

bool QS::addToArray(int value){
	if (cip == cap){
		return false;
	}
	my_array[cip] = value;
	cip += 1;
	return true;
}

string QS::getArray() const{
	string str = "";
	if (my_array == NULL){
		return str;
	}
	else {
		for (int i = 0; i < cip; ++i){
			if (i == cip-1){
				str += to_string(my_array[i]);
				return str;
			}
			else{
				str += to_string(my_array[i]);
				str += ",";
			}
		} 
		return str;
	}
}

int QS::getSize() const{
	return cip;
}

void QS::clear(){
	my_array = NULL;
	cip =0;
}

int QS::medianOfThree(int left, int right) {
	int middle;
	int a,b,c;
	int foo[3];

	cout << "median" << endl;
	cout << getArray() << endl;
	cout << left << endl;
	cout << right << endl;
	
	if ((cip == 0)||(left <0)||(right >= cip)||(left >= right)){
		return -1;
	}
	
	middle = (left+right)/2;
	a = my_array[left];
	b = my_array[middle];
	c = my_array[right];

	foo[0] = a;
	foo[1] = b;
	foo[2] = c;
	sort(foo, foo + 3);

	if (middle != 0) {
		my_array[left] = foo[0];
		my_array[middle] = foo[1];
		my_array[right] = foo[2];
	}
	else{
		my_array[left] = foo[0];
		my_array[right] = foo[2];
	}

	cout << getArray() << endl;

	return middle;

}

int QS::partition(int left, int right, int pivotIndex){
	
	if ((my_array == NULL)||(left <0)||(right >= cip)||(left >= right)||(pivotIndex < left)||(pivotIndex >right)){
	// first integer is not less than the second integer
		return -1;
	}
	
	int temp = my_array[pivotIndex];
	// Initialize up = firstand down = last-1
	int up = left+1;
	int down = right;

	// Swap table[pivot] with table[first]
	my_array[pivotIndex] = my_array[left];
	my_array[left] = temp;
	// While up < down:
	// cout << "before while" << endl;
	// cout << left << right << pivotIndex << endl;
	do{
		// Increment up until table[up] > table[pivot] or up == last-1
		while ((my_array[up] <= my_array[left])&&(up < right)){
			up +=1;
		}
		// Decrement down until table[down] <= table[pivot] or down == first
		while ((my_array[down] > my_array[left])&&(down > left)){
			down -=1;
		}
		// If up < down
		if (up<down){
			// Swap table[up] and table[down]
			temp = my_array[up];
			my_array[up] = my_array[down];
			my_array[down] = temp;
		}
	}
	while (up < down);
	// Swap table[first] and table[down]
	temp = my_array[left];
	my_array[left] = my_array[down];
	my_array[down] = temp;
	// Return down
	// cout << "before return" << endl;

	// cout << "partition" << endl;
	// cout << getArray() << endl;
	return down;
	
}

void QS::sortAll(){
	int a = 0;
	int b = cip-1;
	if (cip > 0){
		my_sort(a,b);
	}
}