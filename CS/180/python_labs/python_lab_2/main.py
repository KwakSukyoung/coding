import json
import string
import argparse
import os

def main(inputString):
    # Write the code to count the number of words here
    # Remember to save the dictionary as a json file named "word-counts.json"
    print(inputString)
    #remove the punctuations
    no_punc = inputString.translate(str.maketrans("","",string.punctuation))
    print(no_punc)

    #separate string into words
    separated = no_punc.split()

    #change separated words into lower cases
    lower = [separated[i].lower() for i in range(len(separated))]

    #find the count of each word uniqueness
    count = {}
    for i in range(len(lower)):
        if lower[i] not in count:
            count[lower[i]] = 1
        else:
            count[lower[i]] += 1

    #change to a json file
    with open('word-counts.json', 'w') as f:
        json.dump(count, f)

    return None

if __name__ == "__main__":
    parser = argparse.ArgumentParser("Word Counter")
    print(parser)
    parser.add_argument("-s","--string",type=str,required=True, help="Sentence to have the number of words counted")
    args = parser.parse_args()
    main(args.string)
