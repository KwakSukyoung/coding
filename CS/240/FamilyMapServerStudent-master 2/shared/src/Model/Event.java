package Model;
import java.util.Objects;

/**
 * class for Event
 */
public class Event {
    /**
     * eventID: Unique identifier for this event
     */
    private String eventID;
    /**
     * associatedUsername: Username of user to which this event belongs
     */
    private String associatedUsername;
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
     * Getting constructor
     */
    public Event(String eventID, String associatedUsername,
                 String personID, float latitude,
                 float longitude, String country,
                 String city, String eventType,
                 int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * just a dummy constructor
     */
    public Event() {}

    /**
     * Getters and Setters
     */
    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    /**
     * equal functions to compare
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) &&
                Objects.equals(associatedUsername, event.associatedUsername) &&
                Objects.equals(personID, event.personID) &&
                Objects.equals(latitude, event.latitude) &&
                Objects.equals(longitude, event.longitude) &&
                Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) &&
                Objects.equals(eventType, event.eventType) &&
                Objects.equals(year, event.year);
    }
}
