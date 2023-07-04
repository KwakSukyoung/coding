package Result;

import Model.Event;

import java.util.ArrayList;

/**
 * class for eventResult
 */
public class EventResult {
    /**
     * persons: arrays of event
     */
    private ArrayList<Event> data;
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
    public EventResult(ArrayList<Event> events, boolean success) {
        this.data = events;
        this.success = success;
    }
    /**
     * constructor for failed result
     */
    public EventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    /**
     * getter
     */
    public boolean isSuccess() {
        return success;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}