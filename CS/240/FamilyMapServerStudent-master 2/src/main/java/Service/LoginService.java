package Service;

import DataAccess.*;
import Help.randomNumber;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.sql.Connection;
import java.util.Objects;

/**
 * class for Login Service
 */
public class LoginService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;
    private randomNumber random = new randomNumber();

    /**
     * constructor
     */
    public LoginService() throws DataAccessException {
        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     * @return
     * @throws DataAccessException
     */
    public LoginResult login(LoginRequest loginRequest) {
        try{
            UserDao myUser = new UserDao(conn);
            AuthTokenDao myAuth = new AuthTokenDao(conn);

            //1.log the user in
            //- check if the user is registered
            //- check the password
            User mylittleUser = myUser.find(loginRequest.getUsername());
            //check if it's registered
            if (mylittleUser!=null){
                //check if it has the same password
                if(Objects.equals(loginRequest.getPassword(), mylittleUser.getPassword())){
                    //create the random authtoken
                    String authToken  = random.randomNumber();
                    myAuth.insert(new AuthToken(authToken, loginRequest.getUsername()));
                    //2.returns an authtoken
                    db.closeConnection(true);
                    return new LoginResult(authToken, loginRequest.getUsername(),
                            mylittleUser.getPersonID(), true);
                }
            }
            //not registered or have no matching password
            db.closeConnection(false);
            return new LoginResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new LoginResult("Error: [Description of the error]", false);
        }
    }

}