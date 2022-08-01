package Database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class MYSQLDatabaseTest {
    MYSQLDatabase database;


    @BeforeEach
    void setUp() {
        String URL = "jdbc:mysql://localhost:3306/eriknivalatestdb?autoReconnect=true&useSSL=false";
        database = new MYSQLDatabase(URL);
    }

    @AfterEach
    void tearDown() {
        String name = "Peter Parker";

        Boolean remove = database.removeUser(name);
        database = null;
    }

    @Test
    void addHighscoreData() {
        String name = "Peter Parker";
        Integer score = 90000;

        Boolean insert = database.addHighscoreData(name, score);
        HashMap<String, Integer> user = database.getUserData(name);
        Boolean check = user != null && user.containsKey(name);


        assertEquals(true, insert && check, "Add score data failed");

    }

    @Test
    void getAllUserData() {

    }

    @Test
    void getUserData() {
        String name = "Peter Parker";
        Integer score = 90000;

        Boolean insert = database.addHighscoreData(name, score);
        HashMap<String, Integer> user = database.getUserData(name);
        Boolean check = user != null && user.containsKey(name);

        assertEquals(true, insert && check, "Add score data successful");
    }

    @Test
    void removeUser() {
        String name = "Peter Parker";

        Boolean remove = database.removeUser(name);
        if (remove == false) out.println("Failed to remove user!");
        HashMap<String, Integer> user = database.getUserData(name);
        Boolean check = user == null || user.size() == 0;


        assertEquals(true, remove && check, "remove user failed");
    }

    @Test
    void updateUser() {

        String name = "Peter Parker";
        Integer score = 90000;

        Boolean insert = database.addHighscoreData(name, score);
        HashMap<String, Integer> userPreUpdate = database.getUserData(name);
        if (userPreUpdate == null || userPreUpdate.size() == 0)
            out.println("User insert failed!");

        score = 1;

        Boolean update = database.updateUser(name, score);
        HashMap<String, Integer> userPostUpdate = database.getUserData(name);
        if (userPostUpdate == null || userPostUpdate.size() == 0)
            out.println("User update failed!");

        out.println("Name: " + name + " has the score: " + userPostUpdate.get(name));

        assertEquals(1, userPostUpdate.get(name), "User score update failed!");
    }
}