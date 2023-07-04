package Model;
import java.util.Objects;

/**
 * class for User
 */
public class User {
    /**
     * username: Unique username for user
     */
    private String username;
    /**
     * password: User’s password
     */
    private String password;
    /**
     * email: User’s email address
     */
    private String email;
    /**
     * firstName: User’s first name
     */
    private String firstName;
    /**
     * lastName: User’s last name
     */
    private String lastName;
    /**
     * gender: User’s gender//string "f" or "m"
     */
    private String gender;
    /**
     * personID: Unique Person ID assigned to this user’s generated Person e.g. BYU ID number
     */
    private String personID;

    /**
     * Constructor for User class
     */
    public User(String username, String password,
                String email, String firstName,
                String lastName, String gender,
                String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }
    /**
     * Getters and Setters
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

    public String getPersonID() {
        return personID;
    }

    /**
     * equal functions to compare
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(personID, user.personID) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(gender, user.gender);
    }
}
