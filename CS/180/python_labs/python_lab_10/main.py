import argparse
import numpy as np

def main(documentsTxt):
    # Write the code to compute the One Hot Encodings for various "documents"
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    lines = documentsTxt.splitlines()
    I = len(lines)
    compare = [lines[i].lower().split(' ') for i in range(I)]

    words = documentsTxt.split()
    unique = list(set([word.lower() for word in words]))
    unique.sort()
    J = len(unique)

    mat = np.zeros((I, J))
    for i in range(I):
        for j in range(len(compare[i])):
            mat[i][unique.index(compare[i][j])] += 1

    print("# Features:")
    print(mat)



    return None

if __name__ == "__main__":
    parser = argparse.ArgumentParser("One Hot Encoder")
    parser.add_argument("--fpath", type=str, help="Name of the txt file to be read in")
    args = parser.parse_args()
    main(open(args.fpath).read())