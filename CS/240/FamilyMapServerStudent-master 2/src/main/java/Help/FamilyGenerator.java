package Help;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class FamilyGenerator {
    /**
     * data for location
     */
    private readLocation locData;
    /**
     * data for female names
     */
    private readFemaleName femaleData;
    /**
     * data for male names
     */
    private readMaleName maleData;
    /**
     * data for last names
     */
    private readLastName lastData;
    /**
     * ArrayList of persons
     */
    private ArrayList<Person> people;
    /**
     * ArrayList of events
     */
    private ArrayList<Event> events;
    /**
     * random generator variable
     */
    private randomNumber random = new randomNumber();


    /**
     * constructor
     */
    public FamilyGenerator(){
        people = new ArrayList<>();
        events = new ArrayList<>();
    }

    /**
     * we make a fake family here
     */
    public void generatePerson(User user, int generations) throws DataAccessException, FileNotFoundException {
        //read and save the locations, firstnames, and lastnames
        gettingData();
        int birthYear = 2000;
        int eventYear = birthYear;

        // PERSON///////////////////////////////////////////////////////////////////////////////////////////////////////
        //1.insert person property
        Person person = new Person(user.getPersonID(), user.getUsername(),
                user.getFirstName(), user.getLastName(),
                user.getGender(), "","","");

        //2.define the event property
        String[] types = {"Birth", "Baptism"};
        for(String type: types){
            if(type.equals("Baptism")){
                eventYear += 10;
            }
            //get a random location for the event
            int randomNum = ThreadLocalRandom.current().nextInt(locData.getData().length);
            Event event = new Event(random.randomNumber(), user.getUsername(), user.getPersonID(),
                    locData.getData()[randomNum].getLatitude(), locData.getData()[randomNum].getLongitude(),
                    locData.getData()[randomNum].getCountry(), locData.getData()[randomNum].getCity(),
                    type, eventYear);
            events.add(event);
        }

        Person mother = null;
        Person father = null;

        // PARENTS////////////////////////////////////////////////////////////////////////////////////////////////////
        if(generations> 1){
            mother = helperGeneratePerson("f", generations,birthYear-30,user);
            father = helperGeneratePerson("m", generations,birthYear-30,user);

            //Set mother's and father's spouse IDs
            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());

            people.add(mother);
            people.add(father);

            // Add marriage events to mother and father
            // (their marriage events must be in sync with each other)
            int num = ThreadLocalRandom.current().nextInt(locData.getData().length);
            events.add(marriageEvent(user, mother, num,birthYear-2));
            events.add(marriageEvent(user, father, num,birthYear-2));

            //Set mother's and father's spouse IDs
            person.setFatherID(father.getPersonID());
            person.setMotherID(mother.getPersonID());
        }

        people.add(person);
    }
    /**
     *helper function for the generatePersons
     */
    private Person helperGeneratePerson(String gender, int generations, int defYear, User user) throws DataAccessException {
        //read and save the locations, firstnames, and lastnames
        int eventYear = defYear;

        // PERSON///////////////////////////////////////////////////////////////////////////////////////////////////////
        //1.insert person property
        String first = "";
        if(Objects.equals(gender, "f")){
            first = female();
        }else first = male();
        Person person = new Person(random.randomNumber(), user.getUsername(),
                first, sur(), gender, "","","");

        //2.define the event property
        String[] types = {"Birth", "Death"};
        for(String type: types){
            if(type.equals("Death")){
                eventYear += 80;
            }
            //get a random location for the event
            int randomNum = ThreadLocalRandom.current().nextInt(locData.getData().length);
            Event event = new Event(random.randomNumber(), user.getUsername(), person.getPersonID(),
                    locData.getData()[randomNum].getLatitude(), locData.getData()[randomNum].getLongitude(),
                    locData.getData()[randomNum].getCountry(), locData.getData()[randomNum].getCity(),
                    type, eventYear);
            events.add(event);
        }

        Person mother = null;
        Person father = null;

        // PARENTS////////////////////////////////////////////////////////////////////////////////////////////////////
        if(generations> 1){
            mother = helperGeneratePerson("f", generations -1,defYear-30,user);
            father = helperGeneratePerson("m", generations -1,defYear-30,user);

            //Set mother's and father's spouse IDs
            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());

            // Add marriage events to mother and father
            // (their marriage events must be in sync with each other)
            int num = ThreadLocalRandom.current().nextInt(locData.getData().length);
            events.add(marriageEvent(user, mother, num,defYear-2));
            events.add(marriageEvent(user, father, num,defYear-2));

            people.add(mother);
            people.add(father);

            //Set mother's and father's spouse IDs
            person.setFatherID(father.getPersonID());
            person.setMotherID(mother.getPersonID());
        }

        return person;
    }

    /**
     * reads json files and save them in the private varaibles
     */
    private void gettingData() throws FileNotFoundException {
        //getting the json info
        Gson gson = new Gson();
        Reader reader = new FileReader("json/locations.json");
        locData = gson.fromJson(reader, readLocation.class);
        gson = new Gson();
        reader = new FileReader("json/fnames.json");
        femaleData =  gson.fromJson(reader, readFemaleName.class);
        gson = new Gson();
        reader = new FileReader("json/mnames.json");
        maleData =  gson.fromJson(reader, readMaleName.class);
        gson = new Gson();
        reader = new FileReader("json/snames.json");
        lastData =  gson.fromJson(reader, readLastName.class);
    }

    /**
     * specifically sets a marriage event
     */
    private Event marriageEvent(User user, Person person,int randomNum, int BirthYear){
        Event event = new Event(random.randomNumber(),user.getUsername(), person.getPersonID(),
                locData.getData()[randomNum].getLatitude(), locData.getData()[randomNum].getLongitude(),
                locData.getData()[randomNum].getCountry(), locData.getData()[randomNum].getCity(),
                "Marriage", BirthYear);
        return event;
    }

    /**
     * get a random female name
     */
    private String female(){
        int randomNum = ThreadLocalRandom.current().nextInt(femaleData.getData().length);
        return femaleData.getData()[randomNum];
    }
    /**
     * get a random male name
     */
    private String male(){
        int randomNum = ThreadLocalRandom.current().nextInt(maleData.getData().length);
        return maleData.getData()[randomNum];
    }
    /**
     * get a random surname
     */
    private String sur(){
        int randomNum = ThreadLocalRandom.current().nextInt(lastData.getData().length);
        return lastData.getData()[randomNum];
    }
    /**
     * getter and setter
     */
    public ArrayList<Person> getPeople() {
        return people;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
}
