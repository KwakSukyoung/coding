cmake_minimum_required(VERSION 3.16)
project(Project2)

set(CMAKE_CXX_STANDARD 14)

add_executable(Project2 main.cpp Tuple.h Scheme.h Relation.h Database.h Interpreter.h Parameter.h Predicate.h Rule.h)
