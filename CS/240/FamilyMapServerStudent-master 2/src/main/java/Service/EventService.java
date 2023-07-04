package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Result.EventResult;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * class for EventService
 */
public class EventService {
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;

    /**
     * constructor
     */
    public EventService() throws DataAccessException {

        conn = db.getConnection();
    }

    /**
     * THE MAIN FUNCTION
     */

    public EventResult event(String authtoken) {
        try{
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);
            EventDao myEvent = new EventDao(conn);

            //1.check if the authtoken exists
            //get the authtoken
            AuthToken mylittleAuthToken = myAuthToken.find(authtoken);
            //check if it's registered
            if (mylittleAuthToken!=null){
                //get the events
                ArrayList<Event> events = myEvent.findForEvents(mylittleAuthToken.getUsername());
                //2.returns event
                db.closeConnection(true);
                return new EventResult(events, true);
            }

            //not registered or have no matching password
            db.closeConnection(false);
            return new EventResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new EventResult("Error: [Description of the error]", false);
        }
    }
}
