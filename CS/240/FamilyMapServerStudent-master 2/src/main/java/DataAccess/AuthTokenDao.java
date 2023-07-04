package DataAccess;
import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DataAccess for authToken
 */
public class AuthTokenDao {

    /**
     * private variables and constructor
     * get the connection and build a constructor with it
     */
    private Connection conn;

    /**
     * constructor
     */
    public AuthTokenDao(Connection conn) {this.conn = conn;}

    /**
     * INSERT: We insert authToken information here
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthToken (authtoken, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getAuthtoken());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * FIND: We find authToken information here
     */
    public AuthToken find(String authtoken) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return authToken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * CLEAR: clear all the authToken information
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtoken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }


}
