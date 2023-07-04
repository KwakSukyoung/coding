package Service;

import DataAccess.*;
import Result.ClearResult;

import java.sql.Connection;

/**
 * class for ClearService
 */
public class ClearService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;

    /**
     * constructor
     */
    public ClearService() throws DataAccessException {
        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     */
    public ClearResult clear() throws DataAccessException {
        try{
            //get the connections to the database
            EventDao myEvent = new EventDao(conn);
            UserDao myUser = new UserDao(conn);
            PersonDao myPerson = new PersonDao(conn);
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);

            //clear all the data
            myEvent.clear();
            myPerson.clear();
            myUser.clear();
            myAuthToken.clear();

            db.closeConnection(true);

            return new ClearResult("clear succeeded",true);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new ClearResult("Error: internal server error", false);

        }
    }
}