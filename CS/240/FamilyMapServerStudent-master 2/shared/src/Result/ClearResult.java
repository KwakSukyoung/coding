package Result;

/**
 * class for clearing result
 */
public class ClearResult {
    /**
     * message: message that you get
     */
    private String message;

    /**
     * success: notify if the request went through
     */
    private boolean success;

    /**
     * Constructor
     */
    public ClearResult(String message, boolean success) {this.message = message; this.success = success;}

    /**
     * getters
     */
    public String getMessage() {
        return message;
    }
    public boolean isSuccess() {
        return success;
    }
}
