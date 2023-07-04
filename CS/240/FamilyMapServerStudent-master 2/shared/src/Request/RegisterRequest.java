package Request;


/**
 * class for Register information
 */
public class RegisterRequest {
    /**
     * username: string for username
     */
    private String username;
    /**
     * password: string for password
     */
    private String password;
    /**
     * email: string for email
     */
    private String email;
    /**
     * firstName: string for firstName
     */
    private String firstName;
    /**
     * lastName: string for lastName
     */
    private String lastName;
    /**
     * gender: string for gender
     */
    private String gender;

    /**
     * constructor
     */

    public RegisterRequest(String username, String password,
                           String email, String firstName,
                           String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

}
