package Database;

import java.sql.*;
import java.util.HashMap;

import static java.lang.System.out;

public class MYSQLDatabase implements IDatabase {

    private String url; //
    final String user = "root";
    final String password = "Panasonik!2";

    Connection conn;

    public MYSQLDatabase(String URL) {
        url = URL;
    }

    @Override
    public Boolean connect() {

        if (conn != null) return true;

        try {
            conn = DriverManager.getConnection(url, user, password);

            return true;

        } catch (SQLException e) {
            out.println("Exception connecting to database! " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean addHighscoreData(String username, int score) {

        if (connect() == false) {
            return false;
        }

        if (username.isEmpty() || username.equals(" ")) {
            out.println("Given name was empty when trying to add data to the Database!");
            return false;
        }

        try {
            PreparedStatement pStatement = conn.prepareStatement("INSERT INTO hangmanscores ( Name, Score) VALUES (?,?)");
            pStatement.setString(1, username);
            pStatement.setInt(2, score);

            pStatement.executeUpdate();

            out.println("Added data to database! Name:" + username + ", Score: " + score);
            return true;


        } catch (SQLException e) {
            out.println("Exception adding data to database! " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateUser(String username, int score) {

        if (connect() == false) {
            return false;
        }

        if (username.isEmpty() || username.equals(" ")) {
            out.println("Given name was empty when trying to add data to the Database!");
            return false;
        }

        try {
            PreparedStatement pStatement = conn.prepareStatement("UPDATE hangmanscores SET Score = ? WHERE Name = ?");

            pStatement.setInt(1, score);
            pStatement.setString(2, username);

           pStatement.executeUpdate();

            return true;


        } catch (SQLException e) {
            out.println("Exception adding data to database! " + e.getMessage());
            return false;
        }
    }


    @Override
    public Boolean removeUser(String username) {
        //check DB connection
        if (connect() == false) {
            return false;
        }

        //check for valid input
        if (username.isEmpty() || username.equals(" ")) {
            out.println("Given name was empty when trying to add data to the Database!");
            return false;
        }

        //check for user
        HashMap<String, Integer> user = getUserData(username);
        if (user == null) {
            out.println("User was not found in the database! Cannot delete!");
            //user is no longer in database
            return true;
        }


        PreparedStatement pStatement = null;
        try {
            pStatement = conn.prepareStatement("DELETE FROM hangmanscores WHERE Name = ?");
            pStatement.setString(1, username);
            pStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            out.println("Exception deleting user: '" + username + "' from database! " + e.getMessage());
            return false;
        }
    }

    @Override
    public HashMap<String, Integer> getAllUserData() {
        if (connect() == false) return null;

        try {

            HashMap<String, Integer> result = new HashMap<>();

            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM hangmanscores");
            ResultSet rs = pStatement.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("Name"), rs.getInt("Score"));
            }

            return result;

        } catch (SQLException e) {
            out.println("Exception getting all users from database! " + e.getMessage());
            return null;
        }
    }

    @Override
    public HashMap<String, Integer> getUserData(String username) {
        if (connect() == false) return null;
        try {

            HashMap<String, Integer> result = new HashMap<>();

            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM hangmanscores WHERE Name = ?");
            pStatement.setString(1, username);
            ResultSet rs = pStatement.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("Name"), rs.getInt("Score"));
            }

            if(result.size() == 0 ) return null;

            return result;

        } catch (SQLException e) {
            out.println("Exception getting all users from database! " + e.getMessage());
            return null;
        }
    }

    @Override
    public void disconnect() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            out.println("Exception closing database connection! " + e.getMessage());
        }
    }
}
