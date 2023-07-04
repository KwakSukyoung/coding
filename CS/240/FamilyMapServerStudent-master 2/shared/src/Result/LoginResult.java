package Result;
/**
 * class for LoginResult
 */
public class LoginResult {
    /**
     * authtoken: authtoken to return
     */
    private String authtoken;
    /**
     * username: username to return
     */
    private String username;
    /**
     * personID: personID to return
     */
    private String personID;
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
    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }
    /**
     * constructor for failed result
     */
    public LoginResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    /**
     * getter for success variable
     */

    public boolean isSuccess() {
        return success;
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

