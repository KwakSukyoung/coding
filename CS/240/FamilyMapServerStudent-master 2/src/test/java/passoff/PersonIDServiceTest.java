package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Result.PersonIDResult;
import Service.PersonIDService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways

public class PersonIDServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestPerson: person variable for the test
     */
    private Person bestPerson;
    /**
     * bestAuth: authToken variable for the test
     */
    private AuthToken bestAuth;
    /**
     * pDao: person Data Accessible for the test
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
        bestPerson = new Person("Biking_123A", "Gale","Sean","Galacher", "m", "", "", "");
        bestAuth = new AuthToken("goodAuth", "Gale");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO and AuthTokenDAO, so it can access the database.
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
     * pass case for the personIDService test
     */
    @Test
    public void personIDServicePass() throws DataAccessException {
        // Start by inserting data into the database.
        pDao.insert(bestPerson);
        aDao.insert(bestAuth);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson, PcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        PersonIDService service = new PersonIDService();
        PersonIDResult result = service.personID(AcompareTest.getAuthtoken(), PcompareTest.getPersonID());
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        //now check if the data is equal to what we have
        assertEquals(result.getPersonID(), bestPerson.getPersonID());
        assertEquals(result.getGender(), bestPerson.getGender());
        assertEquals(result.getLastName(), bestPerson.getLastName());
        assertEquals(result.getFirstName(), bestPerson.getFirstName());
        assertEquals(result.getFatherID(), bestPerson.getFatherID());
        assertEquals(result.getAssociatedUsername(), bestPerson.getAssociatedUsername());
        assertEquals(result.getMotherID(), bestPerson.getMotherID());
        assertEquals(result.getSpouseID(), bestPerson.getSpouseID());

    }

    /**
     * fail case for the eventIDService test
     */
    @Test
    public void eventIDServiceFail() throws DataAccessException {
        // Start by inserting data into the database.
        pDao.insert(bestPerson);
        aDao.insert(bestAuth);
        // check if the person is in the database
        Person PcompareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(PcompareTest);
        assertEquals(bestPerson, PcompareTest);
        // check if the authtoken is in the database
        AuthToken AcompareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(AcompareTest);
        assertEquals(bestAuth, AcompareTest);
        db.closeConnection(true);
        //then user the service
        PersonIDService service = new PersonIDService();
        PersonIDResult result = service.personID("bad auth", PcompareTest.getPersonID());
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO,AuthTokenDao, so it can access the database.
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        //check if the result is null or not
        assertNotNull(result);
        //now check if the data has expected value
        assertNull(result.getAssociatedUsername());
        assertNull(result.getPersonID());
        assertNull(result.getFatherID());
        assertNull(result.getMotherID());
        assertNull(result.getGender());
        assertNull(result.getSpouseID());
        assertNull(result.getLastName());
        assertNull(result.getFirstName());
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
    }


}

