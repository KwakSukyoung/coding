package Model;
import java.util.Objects;

/**
 * class for Person
 */
public class Person {
    /**
     * personID: Unique identifier for this person//BYUID
     */
    private String personID;
    /**
     * associatedUsername: Username of user to which this person belongs
     */
    private String associatedUsername;
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
     * Constructor
     * for null info, we want to keep it as ""
     */
    public Person(String personID, String associatedUsername,
                  String firstName, String lastName,
                  String gender, String fatherID,
                  String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public Person() {}

    /**
     * getters and setters
     */
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
    /**
     * equal functions to compare
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) &&
                Objects.equals(associatedUsername, person.associatedUsername) &&
                Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }
}
