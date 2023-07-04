package Request;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * class for Load information
 */
public class LoadRequest {
    /**
     * users: the arrays of users
     */
    private User[] users;
    /**
     * persons: the arrays of persons
     */
    private Person[] persons;
    /**
     * events: the arrays of events
     */
    private Event[] events;

    /**
     * constructor
     */
    public LoadRequest(User[] users, Person[] person,
                       Event[] events) {
        this.users = users;
        this.persons = person;
        this.events = events;
    }
    /**
     * Getter and Setter
     */
    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
