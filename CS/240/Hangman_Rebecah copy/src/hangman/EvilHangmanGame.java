package hangman;

import javax.swing.*;
import javax.swing.text.Style;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.Arrays;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     *	<p>
     *	This method should set up everything required to play the game,
     *	but should not actually play the game. (ie. There should not be
     *	a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     * @throws IOException if the dictionary does not exist or an error occurs when reading it.
     * @throws EmptyDictionaryException if the dictionary does not contain any words.
     */
    //variables
    private Set<String> possibleWords = new HashSet<String>();
    private StringBuilder currentStatus = new StringBuilder();
    private int WordLength;
    private SortedSet<Character> allGuessedLetters = new TreeSet<Character>();
    private String newKey = new String();
    private Integer numChanged;


    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        //get the all possible words
        Scanner myReader;
        myReader = new Scanner(dictionary);
        possibleWords.clear();
        while (myReader.hasNext()){
            String word = myReader.next().replace(",", "");
            if(word.length() == wordLength){
                possibleWords.add(word);
            }
        }
        if (possibleWords.isEmpty()){
            throw new EmptyDictionaryException();
        }
        //set the defaults
        for(int i = 0; i< wordLength;++i){
            currentStatus.append("-");
        }
        WordLength = wordLength;
        allGuessedLetters.clear();

    }
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return check_to_be_evil(Character.toLowerCase(guess));
    }
    @Override
    public SortedSet<Character> getGuessedLetters() {
        /**
         * Returns the set of previously guessed letters, in alphabetical order.
         *
         * @return the previously guessed letters.
         */
        return allGuessedLetters;
    }
    public String getCurrentStatus(){
        return currentStatus.toString();
    }
    public  Set<String> check_to_be_evil(char guess) throws GuessAlreadyMadeException {
        String finalKey = "";
        if (allGuessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }

        //Parse////////////////////////////////////////////////////////////////////
        HashMap<String,Set<String>> matchingGroups = Parse_Groups(guess);

        //find the largest group//////////////////////////////////////////////////
        HashMap<String,Set<String>> filteredGroups = findLargestGroup(matchingGroups);;
//        System.out.println("Hello"+filteredGroups);
        filteredGroups = findLeastLetters(filteredGroups);;

        possibleWords.clear();
        //check first if it has less words
        //check now if it's the most right or not
        finalKey = checkTheMostRight(filteredGroups);
//        System.out.println("works"+finalKey);
        newKey = finalKey.toString();
        possibleWords = filteredGroups.get(finalKey);
        allGuessedLetters.add(guess);
        return possibleWords;

    }
    public HashMap<String,Set<String>> Parse_Groups(char guess) {
        HashMap<String, Set<String>> returningGroups = new HashMap<String, Set<String>>();
        for (String currentWord : possibleWords) {
            StringBuilder groupKeys = new StringBuilder();
            char[] words_to_character = currentWord.toCharArray();
            //get the keys
            for (int i = 0; i < words_to_character.length; ++i) {
                if (words_to_character[i] == guess) {
                    groupKeys.append(guess);
                } else groupKeys.append("-");
            }
            //if we already have the key
            if (returningGroups.containsKey(groupKeys.toString())) {
                Set<String> newSetToAdd = returningGroups.get(groupKeys.toString());
                newSetToAdd.add(currentWord);
            } else {//if we don't have the key
                Set<String> newSetToAdd = new HashSet<String>();
                newSetToAdd.add(currentWord);
                returningGroups.put(groupKeys.toString(), newSetToAdd);
            }
        }
        return returningGroups;
    }
    public HashMap<String,Set<String>> findLargestGroup(HashMap<String, Set<String>> groups){
        int group_size = 0;
        HashMap<String,Set<String>> returningGroups = new HashMap<String,Set<String>>();
        //for each group
        for(Map.Entry<String, Set<String>> value: groups.entrySet()){
            if(value.getValue().size() >= group_size){
                if (value.getValue().size()!=group_size){
                    returningGroups.clear();
                    group_size = value.getValue().size();
                }
                returningGroups.put(value.getKey(), value.getValue());
            }
        }
        return returningGroups;
    }
    public HashMap<String,Set<String>> findLeastLetters(HashMap<String, Set<String>> groups){
        int MostDashes = 0;
        HashMap<String,Set<String>> returningGroups = new HashMap<String,Set<String>>();
        //for each group
        for(Map.Entry<String, Set<String>> value: groups.entrySet()){
            int numDahses = 0;
            for(int i = 0;i<WordLength;++i){
                if (value.getKey().charAt(i) == '-'){
                    numDahses++;
                }
            }
            if (numDahses >= MostDashes){
                if (numDahses>MostDashes){
                    returningGroups.clear();
                }
                MostDashes = numDahses;
                returningGroups.put(value.getKey(), value.getValue());
            }
        }
        return returningGroups;
    }
    public String checkTheMostRight(HashMap<String, Set<String>> manyGroups){
        //check the arrangement
        Integer number;
        Integer maxToCompare = -1;
        String maxKey = "";

        for (Map.Entry<String, Set<String>> set : manyGroups.entrySet()) {
            number = keyToNumber(set.getKey());
            if (number > maxToCompare){
                maxToCompare = number;
                maxKey = set.getKey();
            }
        }

        //get the final
        takeWinningKey(maxKey);
        return maxKey;
    }
    private Integer keyToNumber(String keys){
        Integer changedNumber = 0;
        Integer increaseNumber = 1;
        for(int i= 0; i< keys.length(); ++i){
            if(keys.charAt(i)!='-'){
                changedNumber += increaseNumber;
            }
            increaseNumber *=2;
        }
        return changedNumber;
    }
    private void takeWinningKey(String finalKey){
        numChanged = 0;
        for(int i = 0; i< finalKey.length(); ++i){
            if(finalKey.charAt(i)!= '-'){
                currentStatus.setCharAt(i,finalKey.charAt(i));
                numChanged ++;
            }
        }
    }
    public String getNewKey(){
        return newKey;
    }
    public String getFirstPossibleWords(){
        String returningWord = "";
        for (String words : possibleWords) {
            returningWord += words;
            break;
        }
        return returningWord;

    }
    public Integer getNumChanged(){
        return numChanged;
    }
}
