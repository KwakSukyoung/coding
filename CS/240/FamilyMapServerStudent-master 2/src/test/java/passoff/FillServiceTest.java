package passoff;

import DataAccess.*;
import Result.FillResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Service.FillService;
import Model.User;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways

public class FillServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestUser: User variable for the test
     */
    private User bestUser;
    /**
     * uDao: User Data Access for the test
     */
    private UserDao uDao;
    /**
     * pDao: Person Data Access for the test
     */
    private PersonDao pDao;
    /**
     * eDao: Event Data Access for the test
     */
    private EventDao eDao;
    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestUser = new User("Seany", "asdfjl;", "sean@babo", "Sean", "Galacher", "m", "sfdsdf");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the UserDao,PersonDao, and EventDao, so it can access the database.
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        uDao.clear();
        pDao.clear();
        eDao.clear();
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
     * pass case for the fillService test
     */
    @Test
    public void fillServicePass() throws DataAccessException {
        // Start by inserting data into the database.
        uDao.insert(bestUser);
        // check if the User is in the database
        User UcompareTest = uDao.find(bestUser.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser, UcompareTest);
        db.closeConnection(true);
        //then use the service
        FillService service = new FillService();
        FillResult result = service.fill(bestUser.getUsername(),4);
        Connection conn = db.getConnection();
        //Then we pass that connection to the userDAO, PersonDAO, EventDAO, so it can access the database.
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //now check to see if there are 31 people
        assertEquals(31, pDao.findForPersons(bestUser.getUsername()).size());
        //now check to see if there are 92 events
        assertEquals(92, eDao.findForEvents(bestUser.getUsername()).size());
    }

    /**
     * fail case for the fillService test
     */
    @Test
    public void fillServiceFail() throws DataAccessException {
        // Start by inserting data into the database.
        uDao.insert(bestUser);
        // check if the User is in the database
        User UcompareTest = uDao.find(bestUser.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser, UcompareTest);
        db.closeConnection(true);
        //then user the service
        FillService service = new FillService();
        FillResult result = service.fill("bad username",4);
        Connection conn = db.getConnection();
        //Then we pass that connection to the userDAO, so it can access the database.
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //now check to see if it returns null
        assertEquals(0,pDao.findForPersons(bestUser.getUsername()).size());
        assertEquals(0,eDao.findForEvents(bestUser.getUsername()).size());
    }
}
