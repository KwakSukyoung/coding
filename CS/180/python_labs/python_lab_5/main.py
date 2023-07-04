import argparse

def main(inputString):
    # Write the code to reverse the string here
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.
    # print(inputString)
    letters = [inputString[i] for i in range(len(inputString))]
    opp_letters = [letters[len(letters)-1-i] for i in range(len(letters))]
    output = ""
    output = output.join(opp_letters)
    print(output)
    return None

if __name__ == "__main__":
    argumentParser = argparse.ArgumentParser("String Reverser")
    argumentParser.add_argument("--string", type=str, help="Input a string to reverse")
    parsed = argumentParser.parse_args()
    main(parsed.string)