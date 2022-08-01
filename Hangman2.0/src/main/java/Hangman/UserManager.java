package Hangman;

import java.util.*;
import java.util.stream.Collectors;

import Database.MYSQLDatabase;

import static java.util.stream.Collectors.*;
import static java.lang.System.out;
import static java.util.Map.Entry.*;


public class UserManager {
    private String userName;
    private final String DBURL = "jdbc:mysql://localhost:3306/eriknivaladb?autoReconnect=true&useSSL=false";

    private MYSQLDatabase database;
    private InputValidator inputValidator = new InputValidator();

    public UserManager() {
        database = new MYSQLDatabase(DBURL);
    }

    public void inputPlayerName() {
        userName = inputValidator.GetValidStringInput(new Scanner(System.in));
    }

    public void addHighscore(int newScore) {
        if (userName.isEmpty()) return;

        HashMap<String, Integer> user = database.getUserData(userName);
        if (user != null && user.size() != 0) {
            if (user.get(userName) >= newScore) {
                out.println("Trying to update user with a lower score then he had before!");
                return;
            }

            out.println("Updating user: " + userName + " with new score: " + newScore);
            database.updateUser(userName, newScore);
            return;
        }

        out.println("User does not exist in database! Adding new user and score!");
        database.addHighscoreData(userName, newScore);
    }

    public int getHighestScore() {
        try {
            HashMap<String, Integer> users = database.getAllUserData();

            List<Integer> values = users.entrySet().stream()
                    .sorted(comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new)).values().stream().sorted(Comparator.reverseOrder()).toList();


            return values.get(0);

        } catch (Exception e) {
            out.println("There was an error getting scores!");
            out.println(e.getMessage());
            return -100;
        }
    }

    public boolean hasUsername() {
        if (userName == null || userName.isEmpty())
            return false;

        return true;
    }
}