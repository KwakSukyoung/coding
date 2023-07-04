package passoff;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDAOTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestUser: user variable for the test
     */
    private User bestUser;
    /**
     * bestUser1: user variable for the test
     */
    private User bestUser1;
    /**
     * uDao: User Data Accessible variable for the test
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
        // and user variables with random data
        bestUser = new User("Biking_123A", "Gale", "Gale123A",
                "35.9f", "140.1f", "f", "Ushiku");
        bestUser1 = new User("sadfa", "cdfe", "sdf",
                "3awefsdcx", "asdf", "f", "caefae");
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the userDAO, so it can access the database.
        uDao = new UserDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
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
     * pass case for the insert
     */
    @Test
    public void insertPass() throws DataAccessException {
        // Start by inserting an user into the database.
        uDao.insert(bestUser);
        // Let's use a find method to get the user that we just put in back out.
        User compareTest = uDao.find(bestUser.getUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestUser, compareTest);
    }
    /**
     * fail case for the insert
     */
    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the user will be inserted successfully.
        uDao.insert(bestUser);

        // However, our sql table is set up so that the column "userID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> uDao.insert(bestUser));
    }
    /**
     * fail case for the find
     */
    @Test
    public void findPass() throws DataAccessException {
        // Start by inserting the user into the database.
        uDao.insert(bestUser);
        // Let's use a find method to get the user that we just put in back out.
        User compareTest = uDao.find(bestUser.getUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the User class.
        assertEquals(bestUser, compareTest);
    }
    /**
     * fail case for the fail
     */
    @Test
    public void findFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the user will be inserted successfully.

        // However, our sql table is set up so that the column "userID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertNull(uDao.find(bestUser.getUsername()));
    }
    /**
     * class test case
     */
    @Test
    public void clearTest() throws DataAccessException{
        //insert the user
        uDao.insert(bestUser);
        //find the user and make sure it's g null
        assertNotNull(uDao.find(bestUser.getUsername()));
        //then clear it
        uDao.clear();
        //then find the user again and it should be null
        assertNull(uDao.find(bestUser.getUsername()));
    }
    @Test
    public void clearTest1() throws DataAccessException{
        //insert the user
        uDao.insert(bestUser1);
        //find the user and make sure it's g null
        assertNotNull(uDao.find(bestUser1.getUsername()));
        //then clear it
        uDao.clear();
        //then find the user again and it should be null
        assertNull(uDao.find(bestUser1.getUsername()));
    }


}
