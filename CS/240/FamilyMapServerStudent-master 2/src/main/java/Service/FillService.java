package Service;

import DataAccess.*;
import Help.FamilyGenerator;
import Model.Event;
import Model.Person;
import Model.User;
import Result.FillResult;
import java.sql.Connection;


/**
 * class for FillService
 */
public class FillService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;

    /**
     * constructor
     */
    public FillService() throws DataAccessException {
        conn = db.getConnection();
    }
    /**
     * THE MAIN FUNCTION
     */
    public FillResult fill(String username, int num_gen) throws DataAccessException {
        try{
            UserDao myUser = new UserDao(conn);
            EventDao myEvent = new EventDao(conn);
            PersonDao myPerson = new PersonDao(conn);
            FamilyGenerator myFamily = new FamilyGenerator();

            //1. check if the user is already registered with server.
            User user = myUser.find(username);
            if(user!=null){
                //2. delete the associated database
                myEvent.clearWithName(username);
                myPerson.clearWithName(username);

                //3. populates the servers database with generated data for specified username
                myFamily.generatePerson(user, num_gen);

                for(Person person: myFamily.getPeople()){
                    myPerson.insert(person);
                }
                for(Event event: myFamily.getEvents()){
                    myEvent.insert(event);
                }

            }else{
                db.closeConnection(true);
                return new FillResult("Error: [Description of the error]", false);
            }

            db.closeConnection(true);

            return new FillResult("Successfully added "+ myFamily.getPeople().size()+ " persons and " + myFamily.getEvents().size() +" events to the database.",true);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new FillResult("Error: [Description of the error]", false);
        }
    }

}