package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *Tasks for Database
 */
public class Database {
    /**
     * private variables and constructor
     * get the connection and build a constructor with it
     */
    private Connection conn;

    /**
     * OPENCONNECTION: open connection
     */
    public Connection openConnection() throws DataAccessException {
        try {
            // The Structure for this Connection is driver:language:path
            // The path assumes you start in the root of your project unless given a full file path
            final String CONNECTION_URL = "jdbc:sqlite:familydb.db";
            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);
            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    /**
     * GETCONNECTION: get connection
     */
    public Connection getConnection() throws DataAccessException {
        if (conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /**
     * CLOSECONNECTION: open connection
     */
    public void closeConnection(boolean commit){
        try {
            if (commit) {
                // This will commit the changes to the database
                conn.commit();
            } else {
                // If we find out something went wrong, pass a false into closeConnection and this
                // will roll back any changes we made during this connection
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            // If you get here there are probably issues with your code and/or a connection is being left open
            e.printStackTrace();
        }
    }

}
