package hangman;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.*;
import java.io.FileNotFoundException;

public class EvilHangman {

    public static void main(String[] args) throws GuessAlreadyMadeException {
        //Save inputs///////////////////////////////////////////////////////////////////
        EvilHangmanGame evil = new EvilHangmanGame();//saving variables
        String dictionary_path = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        //Start the game////////////////////////////////////////////////////////////////
        File myObj = new File(dictionary_path);
        try {
            evil.startGame(myObj, wordLength);//set possible words, current status, word length
            while(numGuesses!= 0){

                //You have # guesses left
                if (numGuesses==1){
                    System.out.println("you have 1 guess left");
                }
                else{
                    System.out.println("you have "+ numGuesses +" guesses left");
                }

                //Used letters:
                System.out.print("Used Letters: ");
//            System.out.println(evil.getGuessedLetters());
                if (!evil.getGuessedLetters().isEmpty()){
                    for (Character letter : evil.getGuessedLetters())
                        System.out.print(letter.charValue() + " ");
                }
                System.out.println("\n");

                //Word:-----
                String currentW = evil.getCurrentStatus();
                System.out.print("Word: "+currentW+"\n");

                //Enter guess:
                Scanner myReader;//ab cd ef
                myReader = new Scanner(System.in);
                System.out.print("Enter guess: ");
                String line = myReader.nextLine().toLowerCase();
                while(line.isEmpty()){
                    System.out.print("Wrong input:\n"+
                            "type again");
                    line = myReader.nextLine().toLowerCase();
                }
                Character letter = line.charAt(0);
                while(!Character.isAlphabetic(letter)){
                    System.out.print("Wrong input:\n"+
                            "type again");
                    line = myReader.nextLine().toLowerCase();
                    letter = line.charAt(0);
                }

                //got the right letter
//                System.out.println(letter);//remoive
                String oldKey = evil.getCurrentStatus();
                //check if it's redundant
                while(true){
                    try{
                        Set<String > recentWords = evil.makeGuess(letter);
                        break;
                    }catch (GuessAlreadyMadeException e){
                        System.out.println("You made the guess already"+
                                "Enter guess: ");

                        line = myReader.nextLine().toLowerCase();
                        while(line.isEmpty()){
                            System.out.print("Wrong input:\n"+
                                    "type again");
                            line = myReader.nextLine().toLowerCase();
                        }
                        letter = line.charAt(0);
                        while(!Character.isAlphabetic(letter)){
                            System.out.print("Wrong input:\n"+
                                    "type again");
                            line = myReader.nextLine().toLowerCase();
                            letter = line.charAt(0);
                        }
                    }
                }
                System.out.println(evil.getCurrentStatus());
                if (Objects.equals(oldKey, evil.getCurrentStatus())){
                    System.out.println("Sorry, there are no "+ letter +"'s\n");
                }
                else{
//                    System.out.println(evil.getNewKey());
//                    System.out.println(evil.getCurrentStatus());
                    System.out.println("Yes, there is " + evil.getNumChanged() + " "+ letter+"\n");
                    numGuesses ++;
                }
                if (!evil.getCurrentStatus().contains("-")){
                    System.out.println("You Win!");
                    break;
                }
                numGuesses --;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (EmptyDictionaryException e) {
            System.out.println("You have no input.");
        }


        //loop the game///////////////////////////////////////////////////////////////
        if (evil.getCurrentStatus().contains("-")){
            System.out.println("You lose!");
            System.out.println("The word was: "+evil.getFirstPossibleWords());
        }
    }

}
