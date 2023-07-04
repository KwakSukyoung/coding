package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Result.PersonResult;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * class for PersonService
 */
public class PersonService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;


    /**
     * constructor
     */
    public PersonService() throws DataAccessException {
        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     */
    public PersonResult person(String authtoken) {
        try{
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);
            PersonDao myPerson = new PersonDao(conn);

            //1.check if the authtoken exists
            //get the authtoken
            AuthToken mylittleAuthToken = myAuthToken.find(authtoken);
            //check if it's registered
            if (mylittleAuthToken!=null){
                //get the persons
                ArrayList<Person> persons = myPerson.findForPersons(mylittleAuthToken.getUsername());
                //2.returns person
                db.closeConnection(true);
                return new PersonResult(persons, true);
            }
            //not registered or have no matching password
            db.closeConnection(false);
            return new PersonResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new PersonResult("Error: [Description of the error]", false);
        }
    }
}
