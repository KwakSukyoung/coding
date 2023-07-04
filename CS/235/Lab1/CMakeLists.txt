cmake_minimum_required(VERSION 3.21)
project(Lab1)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Lab1
        main.cpp
        TODO_LIST.h
        TodoListInterface-1.h)
