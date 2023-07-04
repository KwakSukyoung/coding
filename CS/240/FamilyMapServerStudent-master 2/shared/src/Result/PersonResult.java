package Result;

import Model.Person;

import java.util.ArrayList;

/**
 * class for PersonResult
 */

public class PersonResult {
    /**
     * persons: arrays of person
     */
    private ArrayList<Person> data;
    /**
     * message: message to notify
     */
    private String message;
    /**
     * success: notify if it went through
     */
    private boolean success;

    /**
     * constructor for successful result
     */
    public PersonResult(ArrayList<Person> persons, boolean success) {
        this.data = persons;
        this.success = success;
    }
    /**
     * constructor for failed result
     */
    public PersonResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * getter for success variable
     */

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}



