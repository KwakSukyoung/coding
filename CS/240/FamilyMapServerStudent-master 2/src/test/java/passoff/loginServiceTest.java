package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import Service.LoginService;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class loginServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * loginRequest: Request variable for the test
     */
    private LoginRequest loginRequest;
    /**
     * badRequest: Request variable for the test
     */
    private LoginRequest badRequest;
    /**
     * registerRequest: Request variable for the test
     */
    private RegisterRequest registerRequest;

    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException, FileNotFoundException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        //create request objects
        loginRequest = new LoginRequest("Seany", "asdf");
        badRequest = new LoginRequest("Jakey", "asdf");
        registerRequest = new RegisterRequest("Seany","asdf","sean@babo", "Sean", "Galacher", "m" );
        //open the connection
        Connection conn = db.getConnection();
        UserDao myUser = new UserDao(conn);
        AuthTokenDao myAuth = new AuthTokenDao(conn);
        myUser.clear();
        myAuth.clear();

    }
    /**
     * Wrapping step for the test
     */
    @AfterEach
    public void tearDown() {
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }
    /**
     * pass case for the loginService test
     */
    @Test
    public void loginServicePass() throws DataAccessException {
        db.closeConnection(true);
        // use the service to register
        RegisterService service= new RegisterService();
        RegisterResult result = service.register(registerRequest);
        //then login
        LoginService Service = new LoginService();
        LoginResult Result = Service.login(loginRequest);
        //open the connection
        Connection conn = db.getConnection();
        //check if it's the same thing or not
        assertEquals(registerRequest.getUsername(), Result.getUsername());
        //check if the message is printed correctly
        assertTrue(Result.isSuccess());

    }

    /**
     * fail case for the loginService test
     */
    @Test
    public void loginServiceFail() throws DataAccessException {
        // use the service to register
        RegisterService service= new RegisterService();
        RegisterResult result = service.register(registerRequest);
        //then login
        LoginService Service = new LoginService();
        LoginResult Result = Service.login(badRequest);
        Connection conn = db.getConnection();
        //check if it's the same thing or not
        assertNull(Result.getUsername());
        assertNull(Result.getPersonID());
        //check if the message is printed correctly
        assertFalse(Result.isSuccess());
    }
}
