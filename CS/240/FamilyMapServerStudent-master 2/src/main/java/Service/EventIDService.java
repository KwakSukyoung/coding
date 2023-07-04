package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Result.EventIDResult;

import java.sql.Connection;

/**
 * class for EventIDService
 */
public class EventIDService{
    /**
     * myDatabase: database to go through
     */
    private final Connection conn;
    private Database db = new Database();;

    /**
     * constructor
     */
    public EventIDService() throws DataAccessException {
        conn = db.getConnection();
    }
    /**
     * THE MAIN FUNCTION
     */
    public EventIDResult eventID(String authtoken, String event_id) {
        try{
            AuthTokenDao myAuthToken = new AuthTokenDao(conn);
            EventDao myEvent = new EventDao(conn);

            //1.check if the authtoken exists
            //get the authtoken
            AuthToken mylittleAuthToken = myAuthToken.find(authtoken);
            //check if it's registered
            if (mylittleAuthToken!=null){
                //2.returns Event
                Event event = myEvent.find(event_id);
                if(event!=null){
                    if(event.getAssociatedUsername().equals(mylittleAuthToken.getUsername())){
                        db.closeConnection(true);
                        return new EventIDResult(event.getAssociatedUsername(), event.getEventID(),
                                event.getPersonID(), event.getLatitude(),
                                event.getLongitude(), event.getCountry(),
                                event.getCity(), event.getEventType(),
                                event.getYear(), true);
                    }
                }
            }
            //not registered or have no matching password
            db.closeConnection(false);
            return new EventIDResult("Error: [Description of the error]", false);

        }catch (Exception e){
            e.printStackTrace();
            db.closeConnection(false);
            return new EventIDResult("Error: [Description of the error]", false);
        }
    }
}
