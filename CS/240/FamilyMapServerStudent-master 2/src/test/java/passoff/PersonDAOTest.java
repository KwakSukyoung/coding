package passoff;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * bestPerson: Person variable for the test
     */
    private Person bestPerson;
    /**
     * bestPerson1: Person variable for the test
     */
    private Person bestPerson1;
    /**
     * pDao: Person Data Access for the test
     */
    private PersonDao pDao;
    /**
     * persons: Person ArrayList for the test
     */
    private ArrayList<Person> persons = new ArrayList<>();
    /**
     * persons: Person ArrayList for the test
     */
    private ArrayList<Person> empty = new ArrayList<>();
    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestPerson = new Person("Biking_123A", "Gale", "Gale123A",
                "35.9f", "f", "Japan", "Ushiku",
                "Biking_Around");
        bestPerson1 = new Person("asdf", "Gale", "df",
                "234", "f", "evb", "kitkiat",
                "nara");
        persons.add(bestPerson);
        persons.add(bestPerson1);
        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        pDao = new PersonDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        pDao.clear();
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
        // Start by inserting an person into the database.
        pDao.insert(bestPerson);
        // Let's use a find method to get the person that we just put in back out.
        Person compareTest = pDao.find(bestPerson.getPersonID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Person class.
        assertEquals(bestPerson, compareTest);
    }
    /**
     * fail case for the insert
     */
    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the person will be inserted successfully.
        pDao.insert(bestPerson);

        // However, our sql table is set up so that the column "personID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));
    }
    /**
     * fail case for the find
     */
    @Test
    public void findPass() throws DataAccessException {
        // Start by inserting an person into the database.
        pDao.insert(bestPerson);
        // Let's use a find method to get the person that we just put in back out.
        Person compareTest = pDao.find(bestPerson.getPersonID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestPerson, compareTest);
    }
    /**
     * fail case for the fail
     */
    @Test
    public void findFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the person will be inserted successfully.

        // However, our sql table is set up so that the column "personID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertNull(pDao.find(bestPerson.getPersonID()));
    }
    /**
     * class test case
     */
    @Test
    public void clearTest() throws DataAccessException{
        //insert the event
        pDao.insert(bestPerson);
        //find the event and make sure it's not null
        assertNotNull(pDao.find(bestPerson.getPersonID()));
        //then clear it
        pDao.clear();
        //then find the event again and it should be null
        assertNull(pDao.find(bestPerson.getPersonID()));
    }
    @Test
    public void clearTest1() throws DataAccessException{
        //insert the event
        pDao.insert(bestPerson1);
        //find the event and make sure it's not null
        assertNotNull(pDao.find(bestPerson1.getPersonID()));
        //then clear it
        pDao.clear();
        //then find the event again and it should be null
        assertNull(pDao.find(bestPerson1.getPersonID()));
    }
    /**
     * pass case for the findForPersons
     */
    @Test
    public void findForPersonsPass() throws DataAccessException {
        // Start by inserting persons into the database.
        pDao.insert(bestPerson);
        pDao.insert(bestPerson1);
        // Let's use a findForPerson method to get the person that we just put in back out.
        ArrayList<Person> compareTest = pDao.findForPersons(bestPerson.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the persons class.
        assertEquals(persons, compareTest);
    }
    /**
     * fail case for the findForPersons
     */
    @Test
    public void findForPersonsFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time persons will be inserted successfully.

        // However, our sql table is set up so that the column "person" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertEquals(empty, pDao.findForPersons("df"));
    }
    /**
     * pass case for the clearWithName
     */
    @Test
    public void clearWithNamePass() throws DataAccessException {
        // Start by inserting persons into the database.
        pDao.insert(bestPerson);
        pDao.insert(bestPerson1);
        // Let's use a findForPersons method to get the event that we just put in back out.
        ArrayList<Person> compareTest = pDao.findForPersons(bestPerson.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the events class.
        assertEquals(persons, compareTest);
        //we clear them
        pDao.clearWithName(bestPerson.getAssociatedUsername());
        //then find the person again and it should be null
        assertEquals(empty,pDao.findForPersons(bestPerson.getAssociatedUsername()));
    }
    /**
     * fail case for the clearWithName
     */
    @Test
    public void clearWithNameFail() throws DataAccessException {
        // Start by inserting persons into the database.
        pDao.insert(bestPerson);
        pDao.insert(bestPerson1);
        // Let's use a findForPersons method to get the event that we just put in back out.
        ArrayList<Person> compareTest = pDao.findForPersons(bestPerson.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the persons class.
        assertEquals(persons, compareTest);
        //we clear them
        pDao.clearWithName("ss");
        //then find the person again ,and it should not be null
        assertNotNull(pDao.findForPersons(bestPerson.getAssociatedUsername()));
    }

}
