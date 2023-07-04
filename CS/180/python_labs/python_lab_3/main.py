import argparse
import os
import math

def main(number: int) -> int:
    # Write the code to sum up cubed numbers here.
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    #list of n cubs
    n_cubes = [i**3 for i in range(number+1)]

    #change to string to see if the first digit is even
    stri = [str(i**3) for i in range(number+1)]

    #get the first digits
    k =[int(stri[i][0]) for i in range(len(stri))]

    #filter the index
    T = [i for i in range(len(k)) if (k[i]%2 ==0)]

    #add all the even digit numbers
    total = sum([n_cubes[t] for t in T])
    print("cube("+ str(number) +") = " +str(total))

    return None

if __name__ == "__main__":
    parser = argparse.ArgumentParser("Cube Counter")
    parser.add_argument("--n", type=int, required=True, help="Input a number to sum the cube counts")
    arguments = parser.parse_args()
    main(arguments.n)