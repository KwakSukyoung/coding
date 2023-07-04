import argparse as ap

def main(array):
    # Write the compute the variance and the mean of a given list of numbers
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.
    total_sum = sum([array[i] for i in range(len(array))])
    print("mean = ", total_sum/len(array))
    print("var = ", sum([(array[i]-total_sum/len(array))**2 for i in range(len(array))])/len(array))
    return None

if __name__ == "__main__":
    argParse = ap.ArgumentParser("Variance and Mean Calculator")
    argParse.add_argument("--array", nargs="+", type=int, help="Input a list of numbers to compute the variance and mean of")
    parsedArgs = argParse.parse_args()
    main(parsedArgs.array)