#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#pragma once
#include "LinkedListInterface.h"
#include <iostream>
#include <sstream>
#include <string>

using namespace std;

template <class T> class LinkedList : public LinkedListInterface<T> {
private:
  struct Node {
    T data;
    Node *next;
    Node(const T &the_data) : data(the_data), next(nullptr) {}
    Node(const T &the_data, Node *next_val) : data(the_data), next(next_val) {}
  };
  Node *head;
  Node *tail;
  Node *temp;
  int num_items = 0;

public:
  LinkedList(void) { this->head = nullptr; };
  ~LinkedList(void) { clear(); };

  /*
    insertHead

    A node with the given value should be inserted at the beginning of the list.

    Do not allow duplicate values in the list.
    */
  void insertHead(T value) {
    cout << "insertHead, " << value << endl;
    if (num_items == 0) {
      // cout << "Here?1" << endl;
      head = new Node(value);
      head->next = nullptr;
      // cout << toString() << endl;
      num_items += 1;
      return;
    }

    for (Node *cur = head; cur != nullptr; cur = cur->next) {
      if (cur->data == value) {
        // cout << "Here?3" << endl;
        return;
      }
    }

    // cout << "Here?4" << endl;
    Node *old_head = head;
    head = new Node(value);
    head->next = old_head;
    num_items += 1;
    // cout << num_items << endl;
    // cout << toString() << endl;
    return;
  };

  /*
  insertTail

  A node with the given value should be inserted at the end of the list.

  Do not allow duplicate values in the list.
  */
  void insertTail(T value) {
    cout << "Inserting value on the tail." << endl;
    // cout << "Here?0" << endl;
    if (num_items == 0) {
      // cout << "Here?1" << endl;
      head = new Node(value);
      num_items += 1;
      return;
    }

    for (Node *cur = head; cur != nullptr; cur = cur->next) {
      // cout << "here?2" << endl;
      if (cur->data == value) {
        return;
      }
      if (cur->next == nullptr) {
        // cout << "here?3" << endl;
        temp = new Node(value);
        cur->next = temp;
        temp->next = nullptr;
        num_items += 1;
        return;
      }
    }
  };

  /*
  insertAfter

  A node with the given value should be inserted immediately after the
  node whose value is equal to insertionNode.

  A node should only be added if the node whose value is equal to
  insertionNode is in the list. Do not allow duplicate values in the list.
  */
  void insertAfter(T value, T insertionNode) {
    cout << "Inserting value on the after." << endl;
    if (num_items == 0) {
      return;
    }

    for (Node *cur = head; cur != NULL; cur = cur->next) {
      if (cur->data == value) {
        return;
      }
    }

    for (Node *cur = head; cur != NULL; cur = cur->next) {
      if (cur->data == insertionNode) {
        temp = new Node(value);
        cout << toString() << endl;
        // cout << "here???" << endl;
        if (cur->next == nullptr) {
          temp->next = nullptr;
          cur->next = temp;
        } else {
          temp->next = cur->next;
          cur->next = temp;
        }
        cur->next = temp;
        num_items += 1;
        toString();
        return;
      }
    }
    cout << num_items << endl;
  };

  /*
  remove

  The node with the given value should be removed from the list.

  The list may or may not include a node with the given value.
  */
  void remove(T value) {
    cout << "remove " << value << endl;
    // int i = 0;
    if (num_items == 0) {
      return;
    }
    for (Node *cur = head; cur->next != nullptr; cur = cur->next) {
      if (head->data == value) {
        head = cur->next;
        num_items -= 1;
        return;
      }
      // cout << i << endl;
      if (cur->next->data == value) {
        // cout << toString() << endl;
        // cout << i << endl;
        cur->next = cur->next->next;
        num_items -= 1;
        return;
      }
      if ((cur->next == nullptr) && (cur->data != value)) {
        return;
      }
      // i += 1;
    }
    return;
  };

  /*
  clear

  Remove all nodes from the list.
  */
  void clear() { ////////////////////////////////////////////////
    while (head != nullptr) {
      // cout << toString() << endl;
      Node *temp = head;
      head = head->next;
      num_items = 0;
      delete temp;
    }
  };

  /*
  at

  Returns the value of the node at the given index. The list begins at
  index 0.

  If the given index is out of range of the list, throw an out of range
  exception.
  */
  T at(int index) {
    if ((num_items == 0) || (index >= num_items) || (index < 0)) {
      throw out_of_range("out of range.");
    } else {
      Node *cur = head;
      for (int i = 0; i <= index; ++i) {
        if (i == index) {
          return cur->data;
        }
        cur = cur->next;
      }
    }
  };

  /*
  size

  Returns the number of nodes in the list.
  */
  int size() { return num_items; };

  /*
  toString

  Returns a string representation of the list, with the value of each node
  listed in order (Starting from the head) and separated by a single space There
  should be no trailing space at the end of the string

  For example, a LinkedList containing the value 1, 2, 3, 4, and 5 should return
  "1 2 3 4 5"
  */
  string toString() {
    stringstream ss;
    for (Node *cur = head; cur != nullptr; cur = cur->next) {
      if (cur->next == nullptr) {
        ss << cur->data;
      } else {
        ss << cur->data << " ";
      }
    };
    return (ss.str());
    return "";
  };
};

#endif
