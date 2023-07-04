package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Result.PersonResult;
import Service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class PersonServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestPerson: person variable for the test
     */
    private Person bestPerson;
    /**
     * bestPerson1: person variable for the test
     */
    private Person bestPerson1;
    /**
     * bestAuth: AuthToken variable for the test
     */
    private AuthToken bestAuth;
    /**
     * pDao: Person Data Accessible for the test
     */
    private PersonDao pDao;
    /**
     * aDao: AuthToken Data Accessible for the test
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
        // and a new person and authtoken with random data
        bestPerson = new Person("Biking_123A", "Gale","Sean","Galacher","m", "", "", "");
        bestPerson1 = new Person("rfghb", "Gale","Jake","Murphy","m", "", "", "");
        bestAuth = new AuthToken("goodAuth", "Gale");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the personDAO and AuthTokenDAO, so it can access the database.
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        pDao.clear();
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
        pDao.insert(bestPerson);
        pDao.insert(bestPerson1);
        aDao.insert(bestAuth);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson, PcompareTest);
        Person PcompareTest1 = pDao.find(bestPerson1.getPersonID());
        assertNotNull(PcompareTest1);
        assertEquals(bestPerson1, PcompareTest1);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        PersonService service = new PersonService();
        PersonResult result = service.person(AcompareTest.getAuthtoken());
        Connection conn = db.getConnection();
        //Then we pass that connection to the personDao,AuthTokenDao, so it can access the database.
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertEquals(2,result.getData().size());
        assertEquals(bestPerson, result.getData().get(0));
        assertEquals(bestPerson1, result.getData().get(1));
    }
    /**
     * fail case for the eventService test
     */
    @Test
    public void personServiceFail() throws DataAccessException {
        // Start by inserting data into the database.
        pDao.insert(bestPerson);
        pDao.insert(bestPerson1);
        aDao.insert(bestAuth);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson, PcompareTest);
        Person PcompareTest1 = pDao.find(bestPerson1.getPersonID());
        assertNotNull(PcompareTest1);
        assertEquals(bestPerson1, PcompareTest1);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        PersonService service = new PersonService();
        PersonResult result = service.person("bad auth");
        Connection conn = db.getConnection();
        //Then we pass that connection to the personDao,AuthTokenDao, so it can access the database.
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertNull(result.getData());
    }
}

