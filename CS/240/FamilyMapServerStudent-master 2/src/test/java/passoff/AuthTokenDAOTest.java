package passoff;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthTokenDAOTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * AuthToken: authtoken for the test
     */
    private AuthToken bestAuthToken;
    /**
     * AuthToken1: authtoken for the test
     */
    private AuthToken bestAuthToken1;
    /**
     * aDo: AuthToken Data acess for the test
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
        // and a new event with random data
        bestAuthToken = new AuthToken("Biking_123A", "Gale");
        bestAuthToken1 = new AuthToken("sadfv", "Gale");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the AuthTokenDAO, so it can access the database.
        aDao = new AuthTokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
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
     * pass case for the insert
     */
    @Test
    public void insertPass() throws DataAccessException {
        // Start by inserting an event into the database.
        aDao.insert(bestAuthToken);
        // Let's use a find method to get the event that we just put in back out.
        AuthToken compareTest = aDao.find(bestAuthToken.getAuthtoken());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the AuthToken class.
        assertEquals(bestAuthToken, compareTest);
    }
    /**
     * fail case for the insert
     */
    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        aDao.insert(bestAuthToken);

        // However, our sql table is set up so that the column "authToken" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> aDao.insert(bestAuthToken));
    }
    /**
     * fail case for the find
     */
    @Test
    public void findPass() throws DataAccessException {
        // Start by inserting an authToken into the database.
        aDao.insert(bestAuthToken);
        // Let's use a find method to get the authToken that we just put in back out.
        AuthToken compareTest = aDao.find(bestAuthToken.getAuthtoken());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the AuthToken class.
        assertEquals(bestAuthToken, compareTest);
    }
    /**
     * fail case for the fail
     */
    @Test
    public void findFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.

        // However, our sql table is set up so that the column "authToken" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertNull(aDao.find(bestAuthToken.getAuthtoken()));
    }
    /**
     * class test case1
     */
    @Test
    public void clearTest() throws DataAccessException{
        //insert the authToken
        aDao.insert(bestAuthToken);
        //find the AuthToken and make sure it's not null
        assertNotNull(aDao.find(bestAuthToken.getAuthtoken()));
        //then clear it
        aDao.clear();
        //then find the event again and it should be null
        assertNull(aDao.find(bestAuthToken.getAuthtoken()));
    }
    /**
     * class test case2
     */
    @Test
    public void clearTest1() throws DataAccessException{
        //insert the authToken
        aDao.insert(bestAuthToken1);
        //find the AuthToken and make sure it's not null
        assertNotNull(aDao.find(bestAuthToken1.getAuthtoken()));
        //then clear it
        aDao.clear();
        //then find the event again and it should be null
        assertNull(aDao.find(bestAuthToken1.getAuthtoken()));
    }
}