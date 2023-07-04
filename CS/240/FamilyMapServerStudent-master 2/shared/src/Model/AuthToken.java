package Model;
import java.util.Objects;

/**
 * class for authToken
 */
public class AuthToken {
    /**
     * authtoken: Unique authtoken
     */
    private String authtoken;
    /**
     * username: Username that is associated with the authtoken
     */
    private String username;
    /**
     * constructor
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }
    /**
     * getters
     */
    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    /**
     * equal functions to compare
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken auth = (AuthToken) o;
        return Objects.equals(authtoken, auth.authtoken) &&
                Objects.equals(username, auth.username);
    }
}
