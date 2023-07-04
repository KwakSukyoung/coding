package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.ClearResult;
import Service.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class clearServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestEvent: event variable for the test
     */
    private Event bestEvent;
    /**
     * bestEvent1: Event variable for the test
     */
    private Event bestEvent1;
    /**
     * bestPerson: person variable for the test
     */
    private Person bestPerson;
    /**
     * betsPerson1: Person variable for the test
     */
    private Person bestPerson1;
    /**
     * bestAuthToken: AuthToken variable for the test
     */
    private AuthToken bestAuthToken;
    /**
     * bestAuthToken1: AuthToken variable for the test
     */
    private AuthToken bestAuthToken1;
    /**
     * bestUser: User variable for the test
     */
    private User bestUser;
    /**
     * bestUser1: User variable for the test
     */
    private User bestUser1;
    /**
     * eDao: Data access variable for the test
     */
    private EventDao eDao;
    /**
     * pDao:  Data access variable for the test
     */
    private PersonDao pDao;
    /**
     * aDao: Data access variable for the test
     */
    private AuthTokenDao aDao;
    /**
     * uDao: Data access variable for the test
     */
    private UserDao uDao;

    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // create event objects
        bestEvent = new Event("Biking_123A", "Gale","asdf", 12.3f, 123.5f, "country", "city", "death", 1234);
        bestEvent1 = new Event("efg", "sdf","bdfb", 1f, 3.5f, "hg", "iolj", "hgjh", 78);
        // create person objects
        bestPerson = new Person("per", "ass", "fir", "las", "m", "","","");
        bestPerson1 = new Person("dfav", "w ef", "dg", " sf", "dtsh", "","","");
        // create user objects
        bestUser = new User("us", "pas", "em", "firl","lasl", "m","perl");
        bestUser1 = new User("sdfag", "aes", "erttt", "jgjf","iu", "m","sdvx");
        // create authtoken objects
        bestAuthToken = new AuthToken("aut", "userr");
        bestAuthToken1 = new AuthToken("srgth", "esfd");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,PersonDao,UserDao,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        eDao.clear();
        pDao.clear();
        uDao.clear();
        aDao.clear();
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
     * pass case for the clearService
     */
    @Test
    public void clearPass() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent);
        pDao.insert(bestPerson);
        aDao.insert(bestAuthToken);
        uDao.insert(bestUser);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent, EcompareTest);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson, PcompareTest);
        // check if the user is in the database
        User UcompareTest = uDao.find(bestUser.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser, UcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuthToken.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuthToken, AcompareTest);
        db.closeConnection(true);
        //then user the service
        ClearService clear = new ClearService();
        ClearResult result = clear.clear();
        //check if it's empty
        assertNotNull(result.getMessage(), "Clear message was null OR its variable name did not match that of the expected JSon (see API)");
        //Checks to see if you filled clearResult with a message String
        assertNotEquals("", result.getMessage(), "Clear message was empty string");
        //Checks to be sure the clearResult message contains the words "clear succeeded"
        assertTrue(result.getMessage().toLowerCase().contains("clear succeeded"), "Clear message did not contain the APIs success message");
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,PersonDao,UserDao,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        //now we see if the data exists or not
        assertNull(aDao.find(bestAuthToken.getAuthtoken()));
        assertNull(pDao.find(bestPerson.getPersonID()));
        assertNull(uDao.find(bestUser.getUsername()));
        assertNull(eDao.find(bestEvent.getEventID()));
    }

    /**
     * pass case for the clearService
     */
    @Test
    public void clearPass1() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent1);
        pDao.insert(bestPerson1);
        aDao.insert(bestAuthToken1);
        uDao.insert(bestUser1);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent1.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent1, EcompareTest);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson1.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson1, PcompareTest);
        // check if the user is in the database
        User UcompareTest = uDao.find(bestUser1.getUsername());
        assertNotNull(UcompareTest);
        assertEquals(bestUser1, UcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuthToken1.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuthToken1, AcompareTest);
        db.closeConnection(true);
        //then user the service
        ClearService clear = new ClearService();
        ClearResult result = clear.clear();
        //check if it's empty
        assertNotNull(result.getMessage(), "Clear message was null OR its variable name did not match that of the expected JSon (see API)");
        //Checks to see if you filled clearResult with a message String
        assertNotEquals("", result.getMessage(), "Clear message was empty string");
        //Checks to be sure the clearResult message contains the words "clear succeeded"
        assertTrue(result.getMessage().toLowerCase().contains("clear succeeded"), "Clear message did not contain the APIs success message");
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,PersonDao,UserDao,AuthTokenDao so it can access the database.
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        //now we see if the data exists or not
        assertNull(aDao.find(bestAuthToken1.getAuthtoken()));
        assertNull(pDao.find(bestPerson1.getPersonID()));
        assertNull(uDao.find(bestUser1.getUsername()));
        assertNull(eDao.find(bestEvent1.getEventID()));
    }
}

