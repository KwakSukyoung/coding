cmake_minimum_required(VERSION 3.16)
project(Project1_Starter_Code)

set(CMAKE_CXX_STANDARD 17)

file(GLOB SOURCES *.cpp *.h)

add_executable(Project1_Starter_Code ${SOURCES})