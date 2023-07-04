package passoff;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class EventDAOTest {
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
     * eDao: Event Data Access variable for the test
     */
    private EventDao eDao;
    /**
     * events: event ArrayList for the test
     */
    private ArrayList<Event> events = new ArrayList<>();
    /**
     * empty: empty ArrayList for the test
     */
    private ArrayList<Event> empty = new ArrayList<>();
    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale","asdf", 12.3f, 123.5f, "country", "city", "death", 1234);
        bestEvent1 = new Event("asdf", "Gale","qwer", 45.6f, 78.9f, "lamb", "pork", "chicken", 3456);
        events.add(bestEvent);
        events.add(bestEvent1);
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        eDao = new EventDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
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
     * pass case for the insert
     */
    @Test
    public void insertPass() throws DataAccessException {
        // Start by inserting an event into the database.
        eDao.insert(bestEvent);
        // Let's use a find method to get the event that we just put in back out.
        Event compareTest = eDao.find(bestEvent.getEventID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestEvent, compareTest);
    }
    /**
     * fail case for the insert
     */
    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        eDao.insert(bestEvent);

        // However, our sql table is set up so that the column "event" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }
    /**
     * fail case for the find
     */
    @Test
    public void findPass() throws DataAccessException {
        // Start by inserting an event into the database.
        eDao.insert(bestEvent);
        // Let's use a find method to get the event that we just put in back out.
        Event compareTest = eDao.find(bestEvent.getEventID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the AuthToken class.
        assertEquals(bestEvent, compareTest);
    }
    /**
     * fail case for the fail
     */
    @Test
    public void findFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.

        // However, our sql table is set up so that the column "event" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertNull(eDao.find(bestEvent.getEventID()));
    }
    /**
     * class test case
     */
    @Test
    public void clearTest() throws DataAccessException{
        //insert the event
        eDao.insert(bestEvent);
        //find the event and make sure it's not null
        assertNotNull(eDao.find(bestEvent.getEventID()));
        //then clear it
        eDao.clear();
        //then find the event again and it should be null
        assertNull(eDao.find(bestEvent.getEventID()));
    }
    @Test
    public void clearTest1() throws DataAccessException{
        //insert the event
        eDao.insert(bestEvent1);
        //find the event and make sure it's not null
        assertNotNull(eDao.find(bestEvent1.getEventID()));
        //then clear it
        eDao.clear();
        //then find the event again and it should be null
        assertNull(eDao.find(bestEvent1.getEventID()));
    }

    /**
     * pass case for the findForEvents
     */
    @Test
    public void findForEventsPass() throws DataAccessException {
        // Start by inserting events into the database.
        eDao.insert(bestEvent);
        eDao.insert(bestEvent1);
        // Let's use a findForEvents method to get the event that we just put in back out.
        ArrayList<Event> compareTest = eDao.findForEvents(bestEvent.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the events class.
        assertEquals(events, compareTest);
    }

    /**
     * fail case for the findForEvents
     */
    @Test
    public void findForEventsFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time events will be inserted successfully.

        // However, our sql table is set up so that the column "event" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertEquals(empty, eDao.findForEvents("df"));
    }
    /**
     * pass case for the clearWithName
     */
    @Test
    public void clearWithNamePass() throws DataAccessException {

        // Start by inserting events into the database.
        eDao.insert(bestEvent);
        eDao.insert(bestEvent1);
        // Let's use a findForEvents method to get the event that we just put in back out.
        ArrayList<Event> compareTest = eDao.findForEvents(bestEvent.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the events class.
        assertEquals(events, compareTest);
        //we clear them
        eDao.clearWithName(bestEvent.getAssociatedUsername());
        //then find the event again and it should be null
        assertEquals(empty,eDao.findForEvents(bestEvent.getAssociatedUsername()));
    }
    /**
     * fail case for the clearWithName
     */
    @Test
    public void clearWithNameFail() throws DataAccessException {
        // Start by inserting events into the database.
        eDao.insert(bestEvent);
        eDao.insert(bestEvent1);
        // Let's use a findForEvents method to get the event that we just put in back out.
        ArrayList<Event> compareTest = eDao.findForEvents(bestEvent.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the events class.
        assertEquals(events, compareTest);
        //we clear them
        eDao.clearWithName("ss");
        //then find the event again ,and it should not be null
        assertNotNull(eDao.findForEvents(bestEvent.getAssociatedUsername()));
    }

}

