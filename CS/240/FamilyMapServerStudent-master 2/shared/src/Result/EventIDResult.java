package Result;

/**
 * class for eventID result
 */
public class EventIDResult {

    /**
     * associatedUsername: Username of user to which this event belongs
     */
    private String associatedUsername;
    /**
     * eventID: ID of person to which this event belongs
     */
    private String eventID;
    /**
     * personID: ID of person to which this event belongs
     */
    private String personID;
    /**
     * latitude: Latitude of event’s location
     */
    private float latitude;
    /**
     * longitude: Longitude of event’s location
     */
    private float longitude;
    /**
     * country: Country in which event occurred
     */
    private String country;
    /**
     * city: /City in which event occurred
     */
    private String city;
    /**
     * eventType: Type of event
     */
    private String eventType;
    /**
     * year: Year in which event occured
     */
    private int year;
    /**
     * message: message that you get
     */
    private String message;
    /**
     * success: notify if the request went through
     */
    private boolean success;

    /**
     * constructor for successful cases
     */
    public EventIDResult(String associatedUsername, String eventID,
                         String personID, float latitude,
                         float longitude, String country,
                         String city, String evenType,
                         int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = evenType;
        this.year = year;
        this.success = success;
    }

    /**
     * constructor for failed case
     */
    public EventIDResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    /**
     * getters
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}


