package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Result.EventIDResult;
import Service.EventIDService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class EventIDServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestEvent: event variable for the test
     */
    private Event bestEvent;
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
        // create a event and authToken variable
        bestEvent = new Event("Biking_123A", "Gale","asdf", 12.3f, 123.5f, "country", "city", "death", 1234);
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
     * pass case for the eventIDService test
     */
    @Test
    public void eventIDServicePass() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent);
        aDao.insert(bestAuth);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent, EcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        EventIDService service = new EventIDService();
        EventIDResult result = service.eventID(AcompareTest.getAuthtoken(), EcompareTest.getEventID());
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertEquals(result.getAssociatedUsername(), bestEvent.getAssociatedUsername());
        assertEquals(result.getEventID(), bestEvent.getEventID());
        assertEquals(result.getPersonID(), bestEvent.getPersonID());
        assertEquals(result.getLatitude(), bestEvent.getLatitude());
        assertEquals(result.getLongitude(), bestEvent.getLongitude());
        assertEquals(result.getCountry(), bestEvent.getCountry());
        assertEquals(result.getCity(), bestEvent.getCity());
        assertEquals(result.getEventType(), bestEvent.getEventType());
        assertEquals(result.getYear(), bestEvent.getYear());
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    /**
     * fail case for the eventIDService test
     */
    @Test
    public void eventIDServiceFail() throws DataAccessException {
        // Start by inserting data into the database.
        eDao.insert(bestEvent);
        aDao.insert(bestAuth);
        // check if the event is in the database
        Event EcompareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(EcompareTest);
        assertEquals(bestEvent, EcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        EventIDService service = new EventIDService();
        EventIDResult result = service.eventID("bad auth", EcompareTest.getEventID());
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        //check if the result is null or not
        assertNotNull(result);
        //now check if the data has expected value
        assertNull(result.getAssociatedUsername());
        assertNull(result.getEventID());
        assertNull(result.getPersonID());
        assertEquals(0.0f,result.getLatitude());
        assertEquals(0.0f,result.getLongitude());
        assertNull(result.getCountry());
        assertNull(result.getCity());
        assertNull(result.getEventType());
        assertEquals(0,result.getYear());
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
    }
}

