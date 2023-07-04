package passoff;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Result.EventResult;
import Service.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class EventServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestEvent: event variable for the test
     */
    private Event bestEvent;
    /**
     * bestEvent1: event variable for the test
     */
    private Event bestEvent1;
    /**
     * bestAuth: authToken variable for the test
     */
    private AuthToken bestAuth;
    /**
     * eDao: Event Data Access variable for the test
     */
    private EventDao eDao;
    /**
     * aDao: AuthToken Data Access variable for the test
     */
    private AuthTokenDao aDao;
    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // create 2 event variables and 1 authToken object
        bestEvent = new Event("Biking_123A", "Gale","asdf", 12.3f, 123.5f, "country", "city", "death", 1234);
        bestEvent1 = new Event("asdf", "Gale","qwer", 45.6f, 78.9f, "lamb", "pork", "chicken", 3456);
        bestAuth = new AuthToken("goodAuth", "Gale");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO and AuthTokenDAO, so it can access the database.
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        eDao.clear();
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
     * pass case for the eventService test
     */
    @Test
    public void eventServicePass() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent);
        eDao.insert(bestEvent1);
        aDao.insert(bestAuth);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent, EcompareTest);
        Event EcompareTest1 = eDao.find(bestEvent1.getEventID());
        assertNotNull(EcompareTest1);
        assertEquals(bestEvent1, EcompareTest1);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        EventService service = new EventService();
        EventResult result = service.event(AcompareTest.getAuthtoken());
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertEquals(2,result.getData().size());
        assertEquals(bestEvent, result.getData().get(0));
        assertEquals(bestEvent1, result.getData().get(1));
    }
    /**
     * fail case for the eventService test
     */
    @Test
    public void eventServiceFail() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent);
        eDao.insert(bestEvent1);
        aDao.insert(bestAuth);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent, EcompareTest);
        Event EcompareTest1 = eDao.find(bestEvent1.getEventID());
        assertNotNull(EcompareTest1);
        assertEquals(bestEvent1, EcompareTest1);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        EventService service = new EventService();
        EventResult result = service.event("bad auth");
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertNull(result.getData());
    }
}

