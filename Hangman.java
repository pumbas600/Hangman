package com.company;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class Hangman
{

    /*******************
     * Custom Methods: *
     *******************/

    //I'm lazy so, instead of writing System.out.println, I can just use print instead.
    private static <E> void print(E x)
    {
        System.out.println(x);
    }

    public static void main(String[] args)
    {

        /************************************************************
         * Selecting a random word from the 3000 words in words.txt *
         ************************************************************/

        //Initialising a Scanner object, which allows us to read user input from the console.
        Scanner reader = new Scanner(System.in);

        print("enter file path of txt document: E.G: D:\\words.txt");
        String filePath = reader.nextLine();

        String randomWord = "";
        FileInputStream fileName;
        int rn = (int) (Math.random() * 3000); //Selecting a random word between 0 and 3000.
        try
        {
            //"D:\\Eclipse\\Workspace\\Hangman\\words.txt"
            fileName = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileName));
            for (int i = 0; i < rn; i++)
            {
                br.readLine(); //ignores all the lines before we reach the line we selected.
            }
            randomWord = br.readLine(); //collects the random word.
            br.close();

        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
        if (randomWord.indexOf('-') != -1)
        { //If the random word contains a '-', replace it with a '_'.
            randomWord = randomWord.replace('-', '_');
        }
        randomWord = randomWord.toLowerCase(); //change the random word to lower case.
        // (I just copy pasted the words, some have capitals)

        /****************************************************
         * Initialising the necessary variables for Hangman *
         ****************************************************/



        //Creating a constant list of acceptable values (All others will be rejected).
        final String ACCEPTABLE_VALUES = "abcdefghijklmnopqrstuvwxyz_";

        ArrayList<String> wrong = new ArrayList<>();
        //Setting alphabet to the acceptable values (Alphabet will be used to determine if that
        //letters already been guessed later on).
        String notGuessedLetters = ACCEPTABLE_VALUES;

        //Creating all the different states of the hangman (Its a mess I know).
        String[] hangman = {"", "==========", "|\n|\n|\n|\n|\n==========", "_______\n|\n|\n|\n|\n|\n==========",
                "_______\n|     |\n|\n|\n|\n|\n==========", "_______\n|     |\n|     O\n|\n|\n|\n==========",
                "_______\n|     |\n|     O\n|     |\n|\n|\n==========", "_______\n|     |\n|     O\n|    /|\n|\n|\n==========",
                "_______\n|     |\n|     O\n|    /|\\ \n|\n|\n==========", "_______\n|     |\n|     O\n|    /|\\ \n|    /\n|\n==========",
                "_______\n|     |\n|     O\n|    /|\\ \n|    / \\ \n|\n=========="
        };

        //Creates string without any duplicate letters
        StringBuilder unknownLetters = new StringBuilder();
        for(String letter : randomWord.split(""))
        {
            if(unknownLetters.indexOf(letter) == -1) unknownLetters.append(letter);
        }
        //String unknownLetters = unknownLettersBuilder.toString();

        //Setting up basic variables:
        int stage = 0; //What hangman stage will it start at (0 is nothing for the first guess).
        int guesses = 0; //Initialising a variable to keep track of the guesses.

        String initialOutput = new String(new char[randomWord.length()]).replace('\0', '-');
        StringBuilder output = new StringBuilder(initialOutput);
        while (true)
        {

            print("");
            print(hangman[stage]); //Displays the hangman at your current 'stage'.
            print("Incorrect letters: " + wrong); //Displays incorrect guessed letters.
            print(output); //Displays the word with all the found letters revealed.


            String input = reader.nextLine(); //Get next guess
            guesses++;
            input = input.toLowerCase();
            if (input.length() != 1)
            {
                print("Please enter only a SINGLE letter!");
                //Continues to next loop iteration and get a new guess.

            }
            else if (notGuessedLetters.contains(input))
            { //If the guessed letter hasn't been used before.
                char letter = input.charAt(0);
                notGuessedLetters = notGuessedLetters.replace(letter, '-'); //Set the letter to '-' to indicate its been used.
                if (!randomWord.contains(input))
                { //If the guessed letters not in the word:
                    wrong.add(input); //add the letter to the wrong ArrayList.
                    stage++;          //increase the hangman stage.
                    if (stage == hangman.length - 1)
                    { //If the hangman has been hung, then game over.
                        print("");
                        print(hangman[hangman.length - 1]);
                        print("You failed to guess the word: " + randomWord + ". Better luck next time!");
                        break; //Stop the infinite loop.
                    }

                }
                else
                { //If the guessed letter IS in the word
                    unknownLetters.deleteCharAt(unknownLetters.toString().indexOf(letter)); //remove the letter from original letters and check its got some left.
                    int index = randomWord.indexOf(letter);
                    while (index >= 0)
                    {
                        output.setCharAt(index, letter);
                        index = randomWord.indexOf(letter, index + 1);
                    }
                    if (unknownLetters.length() == 0)
                    { //if there are none left, you guessed the word.
                        print("You guessed the word: " + randomWord + " in " + guesses + " guesses!");
                        print("With " + (hangman.length - 1 - stage) + " incorrect guesses left!");
                        break; //stop the infinite loop.
                    }
                }
            }
            else
            { //If the guessed letter isn't in the alphabet:
                if (!ACCEPTABLE_VALUES.contains(input)) //checks its an acceptable value.
                    print("Please only enter letters!");
                else
                    print("You have already guessed that character, please try again!");

                //continue to next loop iteration and get a new guess.
            }

        }

        reader.close(); //Stop the communication stream with the console.
        System.exit(0); //Stop the program.
    }

}

