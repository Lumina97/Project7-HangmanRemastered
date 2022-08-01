package Database;

import java.util.HashMap;

public interface IDatabase {
    Boolean connect();
    Boolean addHighscoreData(String username, int score);
    Boolean removeUser(String username);
    Boolean updateUser(String username, int score);
    HashMap<String,Integer> getAllUserData();
    HashMap<String,Integer> getUserData(String username);
    void disconnect();
}
