package Service;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

import java.sql.Connection;

/**
 * class for loadService
 */
public class LoadService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;


    /**
     * constructor
     */
    public LoadService() throws DataAccessException {
        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     */
    public LoadResult load(LoadRequest loadRequest) {
        try{
            EventDao myEvent = new EventDao(conn);
            UserDao myUser = new UserDao(conn);
            PersonDao myPerson = new PersonDao(conn);
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);

            //1.clear
            myEvent.clear();
            myPerson.clear();
            myUser.clear();
            myAuthToken.clear();

            //2.load
            for(Event event: loadRequest.getEvents()){
                myEvent.insert(event);
            }
            for(User user: loadRequest.getUsers()){
                myUser.insert(user);
            }
            for(Person person: loadRequest.getPersons()){
                myPerson.insert(person);
            }

            db.closeConnection(true);

            return new LoadResult("Successfully added "+ loadRequest.getUsers().length+" users," +
                                          " " + loadRequest.getPersons().length+ " persons, and " +
                                          loadRequest.getEvents().length +" events to the database.",true);
        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new LoadResult("Error: [Description of the error]", false);
        }
    }

}

