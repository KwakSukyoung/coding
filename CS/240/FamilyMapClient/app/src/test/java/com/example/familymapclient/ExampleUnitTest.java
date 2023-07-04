package com.example.familymapclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import Model.Event;
import Model.Person;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;


public class ExampleUnitTest {
    private ServerProxy sp;
    private String ServerHost = "localhost";
    private String ServerPost = "8080";
    private LoginRequest loginJane = new LoginRequest("JS", "slay");
    private RegisterRequest RegisterJane = new RegisterRequest("JS", "slay", "jane@gmail.com", "Jane", "Slagle", "f");
    private LoginRequest loginJake = new LoginRequest("JM", "world");

    private RegisterRequest RegisterJake = new RegisterRequest("JM", "world", "jake@gmail.com", "Jake", "Murphy", "m");

    @Before
    public void setUP(){
        //connect with server
        sp = new ServerProxy();
    }

    @Test
    public void loginPass() {
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //login Jane now
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());

        //check if it had the same info
        assertEquals(loginResult.getUsername(), "JS");
    }

    @Test
    public void loginFail() {
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //login Jane now
        LoginResult result = sp.loginResult(loginJake, ServerHost, ServerPost);
        assertTrue(!result.isSuccess());

        //check if it had the same info
        assertNotEquals(result.getUsername(), "JS");
    }

    @Test
    public void registerPass(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );
        //check if you can login with it
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");
    }

    @Test
    public void registerFail(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //register the same person which shouldn't be true
        registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(!registerResult.isSuccess());
    }
    
    @Test
    public void getPeoplePass(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //retrieve people related to a logged in/registered user
        PersonResult personResult = sp.personResult(ServerHost, ServerPost, registerResult.getAuthtoken());

        //check it was successful
        assertTrue(personResult.isSuccess());
        assertNotNull(personResult.getData());

    }

    @Test
    public void getPeopleFail(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //retrieve people related to a logged in/registered user
        PersonResult personResult = sp.personResult(ServerHost, ServerPost, "bad auth");

        //check it was successful
        assertTrue(!personResult.isSuccess());
        assertNull(personResult.getData());

    }

    @Test
    public void getEventsPass(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //retrieve people related to a logged in/registered user
        EventResult eventResult = sp.eventResult(ServerHost, ServerPost, registerResult.getAuthtoken());

        //check it was successful
        assertTrue(eventResult.isSuccess());
        assertNotNull(eventResult.getData());

    }

    @Test
    public void getEventsFail(){
        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //retrieve people related to a logged in/registered user
        EventResult eventResult = sp.eventResult(ServerHost, ServerPost, "bad auth");

        //check it was successful
        assertTrue(!eventResult.isSuccess());
        assertNull(eventResult.getData());

    }

    @Test
    public void calculateFamilyPass(){
        DataCache data = new DataCache();

        //register Jane first
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //add people to the datacache
        data.setPeople(sp.personResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Person> people = data.getPersons();
        //find me
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);
        assertEquals(data.getPersons().size(), 31);
        //calculate myFamily
        ArrayList<Person> myFamily = data.forPersonActivity(me);
        assertNotEquals(data.getIdentify().get(me), "");

    }

    @Test
    public void calculateFamilyFail(){
        DataCache data = new DataCache();

        //register Jane and Jake
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        RegisterResult registerResultJake = sp.registerResult(RegisterJake, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResultJake.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //add people to the datacache
        data.setPeople(sp.personResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Person> people = data.getPersons();
        //find me
        Person me = data.findPerson("jake's personID");
        assertNull(me);
        assertEquals(data.getPersons().size(), 0);
        //calculate myFamily
        ArrayList<Person> myFamily = data.forPersonActivity(me);
        assertEquals(data.getIdentify().get(me), "");

    }

    @Test
    public void filterEventsPass(){
        DataCache data = new DataCache();

        //register Jane
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //add people to the datacache
        data.setPeople(sp.personResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Person> people = data.getPersons();
        //find me
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);
        data.setUserPersonID(me.getPersonID());

        //get fatherside
        data.fatherSide();
        assertEquals(data.getFatherSide().size(), 15);
    }

    @Test
    public void filterEventsFail(){
        DataCache data = new DataCache();

        //register Jane
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //add people to the datacache
        data.setPeople(sp.personResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Person> people = data.getPersons();
        //find me
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);
        data.setUserPersonID(me.getPersonID());

        //get mother
        data.motherSide();
        assertEquals(data.getMotherSide().size(), 15);
    }

    @Test
    public void sortEvents(){
        DataCache data = new DataCache();

        //register Jane
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //find me
        data.setPeople(sp.personResult(ServerHost, ServerPost, loginResult.getAuthtoken()).getData());
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);

        //get events
        data.setEvents(sp.eventResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Event> events = data.getEvents();
        assertEquals(events.size(), 92);

        //add more events to mine
        ArrayList<Event> myEvents = data.findEvents(loginResult.getPersonID());
        assertEquals(myEvents.size(), 2);
        myEvents.add(new Event("siahdkjn", "JS", "asfd1234", 30.f, 24.3f,"U.S.", "Utah","Ate Cheese", 1234));
        myEvents.add(new Event("werfs", "JS", "asfd1234", 234.f, 2.3f,"China.", "Candy","Got Robbed", 12345));
        assertEquals(myEvents.size(), 4);

        //check if the sorting worked
        ArrayList<Event> sortedEvents = data.sortEvents(me);

        assertEquals(sortedEvents.get(0).getYear(), 2000);
        assertEquals(sortedEvents.get(1).getYear(), 2010);

    }

    @Test
    public void sortEvents2(){
        DataCache data = new DataCache();

        //register Jane
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //find me
        data.setPeople(sp.personResult(ServerHost, ServerPost, loginResult.getAuthtoken()).getData());
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);
        Person father = data.findPerson(loginResult.getPersonID());
        assertNotNull(father);

        //get events
        data.setEvents(sp.eventResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Event> events = data.getEvents();
        assertEquals(events.size(), 92);

        //add more events to mine
        ArrayList<Event> hisEvents = data.findEvents(father.getPersonID());
        assertEquals(hisEvents.size(), 2);

        //check if the sorting worked
        ArrayList<Event> sortedEvents = data.sortEvents(father);

        assertTrue(sortedEvents.get(0).getYear()<= sortedEvents.get(1).getYear());


    }

    @Test
    public void searchPeople(){
        DataCache data = new DataCache();

        //register Jane
        RegisterResult registerResult = sp.registerResult(RegisterJane, ServerHost, ServerPost);
        assertTrue(registerResult.isSuccess());
        assertTrue(registerResult.isSuccess());

        //check if it has the same info
        assertEquals(registerResult.getUsername(),"JS" );

        //make sure you can log the person in
        LoginResult loginResult = sp.loginResult(loginJane, ServerHost, ServerPost);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(), "JS");

        //find me
        data.setPeople(sp.personResult(ServerHost, ServerPost, loginResult.getAuthtoken()).getData());
        Person me = data.findPerson(loginResult.getPersonID());
        assertNotNull(me);
        Person father = data.findPerson(loginResult.getPersonID());
        assertNotNull(father);

        //get events
        data.setEvents(sp.eventResult(ServerHost,ServerPost, loginResult.getAuthtoken()).getData());
        ArrayList<Event> events = data.getEvents();
        assertEquals(events.size(), 92);

        //add more events to mine
        ArrayList<Event> hisEvents = data.findEvents(father.getPersonID());
        assertEquals(hisEvents.size(), 2);

        //check if the sorting worked
        ArrayList<Event> sortedEvents = data.sortEvents(father);

        assertTrue(sortedEvents.get(0).getYear()<= sortedEvents.get(1).getYear());


    }


}