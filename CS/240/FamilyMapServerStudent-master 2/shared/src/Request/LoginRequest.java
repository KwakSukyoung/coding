package Request;

/**
 * class for Login information
 */
public class LoginRequest {

    /**
     * String for myUsername
     */
    private String username;
    /**
     * String for mypassword
     */
    private String password;

    /**
     * constructor
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * Getter and Setter
     */
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
