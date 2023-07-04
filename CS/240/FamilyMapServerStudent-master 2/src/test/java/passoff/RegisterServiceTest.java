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
public class RegisterServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestUser: user variable for the test
     */
    private User bestUser;
    /**
     * uDao: User Data Accessible variable for the test
     */
    private UserDao uDao;
    /**
     * loginRequest: request variable for the test
     */
    private LoginRequest loginRequest;
    /**
     * badRequest: request variable for the test
     */
    private RegisterRequest badRequest;
    /**
     * registerRequest: request variable for the test
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
        //create request and user variables
        registerRequest = new RegisterRequest("Seany","asdf","sean@babo", "Sean", "Galacher", "m" );
        loginRequest = new LoginRequest("Seany", "asdf");
        badRequest = new RegisterRequest("Seany","wedsc","sean@babo", "Sean", "Galacher", "m" );
        bestUser = new User("Seany", "asdf", "sean@babo", "Sean", "Galacher", "m", "wefsdcx");
        //open the connection and remove previous info
        Connection conn = db.getConnection();
        uDao = new UserDao(conn);
        uDao.clear();
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
     * pass case for the registerService test
     */
    @Test
    public void registerServicePass() throws DataAccessException {
        //insert the User
        uDao.insert(bestUser);
        // check if the user is in the database
        User UcompareTest = uDao.find(bestUser.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser, UcompareTest);
        db.closeConnection(true);
        //then use the service
        RegisterService service= new RegisterService();
        RegisterResult result = service.register(registerRequest);
        //then user the service
        LoginService Service = new LoginService();
        LoginResult Result = Service.login(loginRequest);
        Connection conn = db.getConnection();
        //check if it's the same thing or not
        assertEquals(registerRequest.getUsername(), Result.getUsername());
        //check if the message is printed correctly
        assertTrue(Result.isSuccess());
    }
    /**
     * fail case for the registerService test
     */
    @Test
    public void registerServiceFail() throws DataAccessException {
        //insert the User
        uDao.insert(bestUser);
        // check if the user is in the database
        User UcompareTest = uDao.find(bestUser.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser, UcompareTest);
        // close the connection and use the service
        db.closeConnection(true);
        RegisterService service= new RegisterService();
        RegisterResult result = service.register(badRequest);
        //open the connection again
        Connection conn = db.getConnection();
        //check if it's the same thing or not
        assertNull(result.getUsername());
        assertNull(result.getAuthtoken());
        assertNull(result.getPersonID());
        //check if the message is printed correctly
        assertTrue(!result.isSuccess());
    }
}
