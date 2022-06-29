package Hangman;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class GameLoop {

    InputValidator inputValidator = new InputValidator();
    UserManager userManager = new UserManager();

    ArrayList<String> wrongLetters = new ArrayList<String>();
    ArrayList<String> rightLetters = new ArrayList<String>();

    List<String> HangManArt;
    String word = "";

    int currentTry = 0;
    int currentScore;

    //Grabs a new word and resets the game to starting settings
    public void startGame() {
        try {

            List<String> result = Files.readAllLines(Paths.get("E:\\Dev\\Genspark\\Project7 Hangman Remastered\\Hangman2.0\\src\\main\\resources\\WordsToGuess.text"));
            HangManArt = Files.readAllLines(Paths.get("E:\\Dev\\Genspark\\Project7 Hangman Remastered\\Hangman2.0\\src\\main\\resources\\HangmanArt.text"));


            word = result.get(generateNewRandomNumberFromRange(0, result.size()));
            out.println("H A N G M A N");

            if (userManager.hasUsername() == false) {
                out.println("Please enter your name!");
                userManager.inputPlayerName();
            }


            //reset variables
            currentTry = 0;
            wrongLetters.clear();
            rightLetters.clear();

            printHangMan();

            loop();
        } catch (Exception e) {
            out.println("The program has encountered an Exception: " + e.getMessage());
        }
    }

    //Game loop
    private void loop() {
        boolean isPlaying = true;
        boolean replay = false;
        do {
            displayGuessedLetters();
            playRound();
            GameState state = checkHasFinishedGame();
            if (state != GameState.playing) {
                replay = endOfGame(state);
                if (replay && state == GameState.lost)
                    currentScore = 0;
                isPlaying = false;
            }

        }
        while (isPlaying);

        if (replay) startGame();
    }

    // lets the player guess and evaluates said guess
    private void playRound() {
        out.println("Guess a Letter!");
        String guess = inputValidator.GetValidCharacterInput(new Scanner(System.in)).toString();

        if (wrongLetters.contains(guess) || rightLetters.contains(guess)) {
            out.println("You already guessed that letter! Try again!");
            return;
        }

        if (word.contains(guess)) {

            out.println(guess + " was correct!");
            rightLetters.add(guess);
            currentScore++;
            if (checkHasFinishedGame() == GameState.playing) {
                printHangMan();
                out.println("Your word so far:");
                out.println(getCurrentWordWithGuesses());
            }
        } else {

            wrongLetters.add(guess);
            out.println("Your guess was wrong.");
            currentTry++;

            if (checkHasFinishedGame() == GameState.playing) {
                printHangMan();
                out.println(getCurrentWordWithGuesses());
            }
        }
    }

    private void printHangMan() {
        if (currentTry >= HangManArt.size()) {
            out.println("The man has HUNG!");
            return;
        }
        String man = HangManArt.get(currentTry);
        man = man.replace("\\n", System.getProperty("line.separator"));
        out.println(man);
    }

    private String getCurrentWordWithGuesses() {
        //convert word to list
        List<String> wordList = Arrays.stream(word.split("")).toList();
        //stream over list and check if rightLetters contains current char
        //print it out if yes and print a space if no
        return wordList.stream()
                .reduce("", (acc, element) -> {
                    if (rightLetters.contains(element))
                        return acc + " " + element;

                    return acc + " _ ";
                });
    }

    private void displayGuessedLetters() {
        if (wrongLetters.size() > 0) {
            out.println("Guessed Letters:");
            out.println(wrongLetters.stream().reduce("", (acc, element) -> acc + element + " , "));
        }
    }

    private GameState checkHasFinishedGame() {
        String guessedWord = getCurrentWordWithGuesses();

        if (currentTry >= HangManArt.size()) return GameState.lost;

        if (guessedWord.contains("_"))
            return GameState.playing;
        else
            return GameState.won;
    }

    private boolean endOfGame(GameState state) {

        if (state == GameState.won) {
            out.println("You have guessed the word: " + word);
            out.println("Your score is: " + currentScore + " continue playing to increase it!");
        } else if (state == GameState.lost) {
            out.println("You didn't manage to guess the word...");
            out.println("The word was: " + word);
            //add score to file and reset it since the player lost
            out.println("Your score was: " + currentScore);
        }

        int highscore = userManager.getHighestScore();
        if (currentScore > highscore) {
            out.println("You achieved the new highscore with a score of: " + currentScore);
        } else {
            out.println("You did not achieve a new highscore." +
                    " The current highscore is : " + highscore + "." +
                    " You achieved: " + currentScore);
        }
        userManager.addHighscore(currentScore);

        out.println("Would you like to play again?");
        out.println("Yes or No ");

        String input = inputValidator.GetValidStringInput(new Scanner(System.in), new String[]{"Yes", "No", "yes", "no"});

        if (input.equals("yes") || input.equals("Yes")) {
            return true;
        }

        return false;
    }

    private int generateNewRandomNumberFromRange(int minNumber, int maxNumber) {
        return (int) ((Math.random() * (maxNumber - minNumber)) + minNumber);
    }
}
