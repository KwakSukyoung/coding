package Result;

/**
 * class for PersonIDResult
 */
public class PersonIDResult {
    /**
     * associatedUsername: Username of user to which this person belongs
     */
    private String associatedUsername;
    /**
     * personID: Unique identifier for this person//BYUID
     */
    private String personID;
    /**
     * firstName: Person’s first name
     */
    private String firstName;
    /**
     * lastName: Person’s last name
     */
    private String lastName;
    /**
     * gender: Person’s gender//string "f" or "m"
     */
    private String gender;
    /**
     * fatherID: Person ID of person’s father//may be null
     */
    private String fatherID = null;
    /**
     * motherID: Person ID of person’s mother//may be null
     */
    private String motherID = null;
    /**
     * spouseID: /Person ID of person’s spouse//may be null
     */
    private String spouseID = null;
    /**
     * message: message to notify
     */
    private String message;
    /**
     * success: notify if it went through
     */
    private boolean success;

    /**
     * constructor for succuessful result
     */
    public PersonIDResult(String associatedUsername, String personID,
                          String firstName, String lastName,
                          String gender, String fatherID,
                          String motherID, String spouseID,
                          boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }
    /**
     * constructor for failed result
     */

    public PersonIDResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * getter for success variable
     */

    public boolean isSuccess() {
        return success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public String getMessage() {
        return message;
    }
}



