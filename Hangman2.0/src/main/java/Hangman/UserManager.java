package Hangman;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.load;
import static java.lang.System.out;

public class UserManager {

    //highscore file setup
    //name
    //highscore for the name above


    String userName;
    final String path = "E:\\Dev\\Genspark\\Project7 Hangman Remastered\\Hangman2.0\\src\\main\\resources\\HighScores.text";

    InputValidator inputValidator = new InputValidator();
    List<String> highScores = new ArrayList<String>();

    public void inputPlayerName() {
        userName = inputValidator.GetValidStringInput(new Scanner(System.in));
    }

    private boolean loadScores() {
        if (highScores.size() > 0) return true;

        try {
            highScores = Files.readAllLines(Paths.get(path));
            if (highScores.size() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            out.println("There was an error loading the scores!");
            out.println(e.getMessage());
            out.println(e.getCause());
            return false;
        }
    }

    public void addHighscore(int newScore) {
        if (userName.isEmpty()) return;

        if (loadScores() && highScores.contains(userName)) {

            int scoreIndex = highScores.indexOf(userName) + 1;
            int highscore = Integer.parseInt(highScores.get(scoreIndex));
            if (highscore < newScore)
                highScores.set(scoreIndex, String.valueOf(newScore));
        }
        //first time writing to the highscore list or username does not exist
        else {
            if (highScores == null) highScores = new ArrayList<>();
            highScores.add(userName);
            highScores.add(String.valueOf(newScore));
        }
        writeScoresToFile();
    }

    private boolean writeScoresToFile() {

        try {
            String fileData = highScores.stream().reduce("", (acc, element) -> acc + element + System.getProperty("line.separator"));
            FileWriter writer = new FileWriter(path);
            writer.write(fileData);
            writer.close();
            return true;
        } catch (Exception e) {
            out.println("There was an error writing scores to file!");
            out.println(e.getMessage());
            return false;
        }
    }

    public int getHighestScore() {
        try {
            if (loadScores()) {
                List<String> scores = IntStream
                        .range(0, highScores.size())
                        .filter(i -> i % 2 != 0)
                        .mapToObj(i -> highScores.get(i))
                        .collect(Collectors.toList());

                //sort the scores
                scores = scores.stream().sorted(Comparator.reverseOrder()).toList();
                return Integer.parseInt(scores.get(0));
            }
        } catch (Exception e) {
            out.println("There was an error getting scores!");
            out.println(e.getMessage());
            return -100;
        }
        return -100;
    }

    public boolean hasUsername() {
        if (userName == null || userName.isEmpty())
            return false;

        return true;
    }
}