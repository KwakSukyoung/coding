package passoff;

import DataAccess.*;
import Model.Person;
import Model.User;
import Result.LoadResult;
import Service.LoadService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Request.LoadRequest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;

//We will use this to test that our insert method is working and failing in the right ways
public class LoadServiceTest {
    /**
     * db: database to connect
     */
    private Database db;
    /**
     * uDao: User Data Accessible for the test
     */
    private UserDao uDao;
    /**
     * pDao: Person Data Accessible for the test
     */
    private PersonDao pDao;
    /**
     * eDao: Event Data Accessible for the test
     */
    private EventDao eDao;
    /**
     * loadRequest: Request Accessible for the test
     */
    private LoadRequest loadRequest;
    /**
     * loadRequest1: Request Accessible for the test
     */
    private LoadRequest loadRequest1;
    /**
     * setting up for the tests
     */
    @BeforeEach
    public void setUp() throws DataAccessException, FileNotFoundException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        //We are creating a JsonReader from the LoadData.json file
        JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
        //We are creating a LoadRequest from the JsonReader we made
        Gson gson = new Gson();
        loadRequest = gson.fromJson(jsonReader, LoadRequest.class);
        //We are creating a second LoadRequest from the JsonReader we made
        jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
        loadRequest1 = gson.fromJson(jsonReader, LoadRequest.class);
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
        db.closeConnection(true);
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
     * pass case1 for the loadService test
     */
    @Test
    public void loadServicePass() throws DataAccessException {
        //then use the service
        LoadService service = new LoadService();
        LoadResult result = service.load(loadRequest);
        Connection conn = db.getConnection();
        //Then we pass that connection to the userDAO,PersonDAO, and EventDAO, so it can access the database.
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //now check if it passed or not
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("persons"));
    }

    /**
     * pass case2 for the loadService test
     */
    @Test
    public void loadServicePass1() throws DataAccessException {
        //then user the service
        LoadService service = new LoadService();
        LoadResult result = service.load(loadRequest1);
        Connection conn = db.getConnection();
        //Then we pass that connection to the userDAO,PersonDAO, and EventDAO, so it can access the database.
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //now check if it passed or not
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("persons"));
    }
}
