package Result;

/**
 * class for registerResult
 */
public class RegisterResult {
    /**
     * authotoken: authotoken for the user
     */
    private String authtoken;
    /**
     * username: username for the user
     */
    private String username;
    /**
     * personID: ID for the user
     */
    private String personID;
    /**
     * message: String for message
     */
    private String message;
    /**
     * success: notify it it went through
     */
    private boolean success;

    /**
     * constructor for successful result
     */
    public RegisterResult(String authtoken, String username,
                          String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }
    /**
     * constructor for failed result
     */
    public RegisterResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * getter for success variable
     */
    public boolean isSuccess() {
        return success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }
}
