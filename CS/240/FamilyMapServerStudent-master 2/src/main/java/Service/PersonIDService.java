package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Result.PersonIDResult;
import java.sql.Connection;


/**
 * class for personID service
 */
public class PersonIDService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;

    /**
     * constructor
     */
    public PersonIDService() throws DataAccessException {
        conn = db.getConnection();
    }
    /**
     * THE MAIN FUNCTION
     */
    public PersonIDResult personID(String authtoken, String personid) {
        try{
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);
            PersonDao myPerson = new PersonDao(conn);

            //1.check if the authtoken exists
            //get the authtoken
            AuthToken mylittleAuthToken = myAuthToken.find(authtoken);
            //check if it's registered
            if (mylittleAuthToken!=null){
                //2.returns Person
                Person person = myPerson.find(personid);
                if(person!=null){
                    if(person.getAssociatedUsername().equals(mylittleAuthToken.getUsername())){
                        db.closeConnection(true);
                        return new PersonIDResult(person.getAssociatedUsername(), person.getPersonID(),
                                person.getFirstName(), person.getLastName(),
                                person.getGender(), person.getFatherID(),
                                person.getMotherID(), person.getSpouseID(),
                                true);
                    }
                }
            }

            //not registered or have no matching password
            db.closeConnection(false);
            return new PersonIDResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new PersonIDResult("Error: [Description of the error]", false);
        }
    }
}