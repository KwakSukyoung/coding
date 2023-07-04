package Service;

import DataAccess.*;
import Help.FamilyGenerator;
import Help.randomNumber;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Model.Event;
import Request.RegisterRequest;
import Result.RegisterResult;

import java.sql.Connection;
import java.util.Objects;


/**
 * class for RegisterService
 */
public class RegisterService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();
    private randomNumber random = new randomNumber();

    /**
     * constructor
     */
    /**
     * constructor
     */
    public RegisterService() throws DataAccessException {
        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     */
    public RegisterResult register(RegisterRequest registerRequest) {
        try{
            UserDao myUser = new UserDao(conn);
            AuthTokenDao myAuth = new AuthTokenDao(conn);
            EventDao myEvent = new EventDao(conn);
            PersonDao myPerson = new PersonDao(conn);

            String saveAuth = random.randomNumber();

            if (myUser.find(registerRequest.getUsername())!=null){
                //the user already exists
                db.closeConnection(false);
                return new RegisterResult("Error: Username already taken by another user", false);
            }


            //1.creates a new user
            User user = new User(registerRequest.getUsername(), registerRequest.getPassword(),
                                 registerRequest.getEmail(), registerRequest.getFirstName(),
                                 registerRequest.getLastName(), registerRequest.getGender(),
                                 random.randomNumber());
            myUser.insert(user);

            // creates the new user's authtoken
            AuthToken auth = new AuthToken(saveAuth, registerRequest.getUsername());
            myAuth.insert(auth);

            //2.Generates 4 generations of ancestor data for the new user
            //  (just like the /fill endpoint if called with a generations value
            //  of 4 and this new user's username as parameters)
            FamilyGenerator myFamily = new FamilyGenerator();
            myFamily.generatePerson(user, 4);
            for(Person person: myFamily.getPeople()){
                myPerson.insert(person);
            }
            for(Event event: myFamily.getEvents()){
                myEvent.insert(event);
            }

            //3.log the user in
            //- check if the user is registered
            //- check the password
            User mylittleUser = myUser.find(registerRequest.getUsername());
            //check if it's registered
            if (mylittleUser!=null){
                //check if it has the same password
                if(Objects.equals(registerRequest.getPassword(), mylittleUser.getPassword())){
                    //2.returns an authtoken
                    db.closeConnection(true);
                    return new RegisterResult(saveAuth, registerRequest.getUsername(),
                            mylittleUser.getPersonID(), true);
                }
            }
            else{
                db.closeConnection(false);
                return new RegisterResult("Error: [Description of the error]", false);
            }
            //not registered or have no matching password
            db.closeConnection(false);
            return new RegisterResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult("Error: [Description of the error]", false);
        }
    }

}