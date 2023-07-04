package com.example.familymapclient;

import Model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import Model.Event;

public class DataCache {
    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Person> fatherSide = new ArrayList<>();
    private ArrayList<Person> motherSide = new ArrayList<>();
    private Set<String> personIDset = new HashSet<>();
    private Map<String, Float> eventColors = new HashMap<>();
    private Map<Person, String> identify = new HashMap<>();
    private boolean fromMain;
    private boolean fromSetting;
    private boolean showLifeStory;
    private boolean showFamilyTree;
    private boolean showSpouse;
    private boolean showFather;
    private boolean showMother;
    private boolean showMale;
    private boolean showFemale;
    private String userPersonID;

    private static DataCache instance;
    public static synchronized DataCache getInstance(){
        if(instance==null){instance = new DataCache();}
        return instance;
    }
    DataCache(){
        fromMain=true;
        showLifeStory=true;
        fromSetting=false;
        showFamilyTree=true;
        showSpouse=true;
        showFather=true;
        showMother=true;
        showMale = true;
        showFemale= true;
    }
    public boolean isShowFemale() {
        return showFemale;
    }
    public void setShowFemale(boolean showFemale) {
        this.showFemale = showFemale;
    }
    public boolean isShowMale() {
        return showMale;
    }
    public void setShowMale(boolean showMale) {
        this.showMale = showMale;
    }
    public boolean isShowMother() {
        return showMother;
    }
    public void setShowMother(boolean showMother) {
        this.showMother = showMother;
    }
    public ArrayList<Person> getFatherSide() {
        return fatherSide;
    }
    public ArrayList<Person> getMotherSide() {
        return motherSide;
    }
    public String getUserPersonID() {
        return userPersonID;
    }
    public void setUserPersonID(String userPersonID) {
        this.userPersonID = userPersonID;
    }
    public boolean isShowFather() {
        return showFather;
    }
    public void setShowFather(boolean showFather) {
        this.showFather = showFather;
    }
    public boolean isShowSpouse() {
        return showSpouse;
    }
    public void setShowSpouse(boolean showSpouse) {
        this.showSpouse = showSpouse;
    }
    public boolean isShowFamilyTree() {
        return showFamilyTree;
    }
    public void setShowFamilyTree(boolean showFamilyTree) {
        this.showFamilyTree = showFamilyTree;
    }
    public boolean isFromSetting() {
        return fromSetting;
    }
    public void setFromSetting(boolean fromSetting) {
        this.fromSetting = fromSetting;
    }
    public void setShowLifeStory(boolean showLifeStory) {
        this.showLifeStory = showLifeStory;
    }
    public boolean isShowLifeStory() {
        return showLifeStory;
    }
    public boolean isFromMain() {
        return fromMain;
    }
    public void setFromMain(boolean fromMain) {
        this.fromMain = fromMain;
    }
    //store List<Person> people data;
    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }
    //store List<Event> event data;
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public ArrayList<Person> getPersons() {
        return people;
    }
    public void setEventsColors(Map<String, Float> colorMap) {
        this.eventColors = colorMap;
    }
    public Map<String, Float> getEventColors() {
        return eventColors;
    }
    public Map<Person, String> getIdentify() {
        return identify;
    }

    //find person with personID
    public Person findPerson(String personID){
        for(Person person: people){
            if (Objects.equals(person.getPersonID(), personID)){
                return person;
            }
        }
        return null;
    }
    //set of events type
    public Set<String> eventTypes(){
        Set<String> type = new HashSet<>();
        for(Event event: events){
            type.add(event.getEventType().toLowerCase());
            personIDset.add(event.getPersonID());
        }
        return type;
    }
    //find event with personID
    public ArrayList<Event> findEvents(String personID){
        ArrayList<Event> result = new ArrayList<>();
        for(Event event: events){
            if (Objects.equals(event.getPersonID(), personID)){
                result.add(event);
            }
        }
        return result;
    }
    public ArrayList<Person> forPersonActivity(Person me){
        ArrayList<Person> myPeople = new ArrayList<>();
        for(Person person: people) {
            if (person.getFatherID() != null) {
                if (Objects.equals(person.getFatherID(), me.getPersonID())) {
                    myPeople.add(person);
                    identify.put(person, "Child");
                }
            } if (person.getMotherID() != null) {
                if (Objects.equals(person.getMotherID(), me.getPersonID())) {
                    myPeople.add(person);
                    identify.put(person, "Child");
                }
            } if (me.getMotherID() != null) {
                if (Objects.equals(me.getMotherID(), person.getPersonID())) {
                    myPeople.add(person);
                    identify.put(person, "Mother");
                }
            } if (me.getFatherID() != null) {
                if (Objects.equals(me.getFatherID(), person.getPersonID())) {
                    myPeople.add(person);
                    identify.put(person, "Father");
                }
            } if (me.getSpouseID() != null) {
                if (Objects.equals(me.getSpouseID(), person.getPersonID())) {
                    myPeople.add(person);
                    identify.put(person, "Spouse");
                }
            }
        }
        return myPeople;
    }
    public void fatherSide (){
        Person me = findPerson(userPersonID);
        //I exist
        if(me!=null){
            if(me.getFatherID()!=null){
                //Father exist
                Person father = findPerson(me.getFatherID());
                if(father!=null){
                    fatherSide.add(father);
                    helperFatherSide(father);
                }
            }
        }
    }
    public void motherSide (){
        Person me = findPerson(userPersonID);
        //I exist
        if(me!=null){
            if(me.getMotherID()!=null){
                //Father exist
                Person mother = findPerson(me.getMotherID());
                if(mother!=null){
                    motherSide.add(mother);
                    helperMotherSide(mother);
                }
            }
        }
    }
    private void helperFatherSide(Person me){
        if(me.getFatherID()!=null){
            //Father exist
            Person father = findPerson(me.getFatherID());
            if(father!=null){
                fatherSide.add(father);
                helperFatherSide(father);
            }
        }
        if(me.getMotherID()!=null){
            //mother exist
            Person mother = findPerson(me.getMotherID());
            if(mother!=null){
                fatherSide.add(mother);
                helperFatherSide(mother);
            }
        }
    }
    private void helperMotherSide(Person me){
        if(me.getFatherID()!=null){
            //Father exist
            Person father = findPerson(me.getFatherID());
            if(father!=null){
                motherSide.add(father);
                helperMotherSide(father);
            }
        }
        if(me.getMotherID()!=null){
            //mother exist
            Person mother = findPerson(me.getMotherID());
            if(mother!=null){
                motherSide.add(mother);
                helperMotherSide(mother);
            }
        }
    }
    public Event findMyEvent(String eventID){
        for(Event event: events){
            if(event.getEventID().equals(eventID)){
                return event;
            }
        }
        return null;
    }
    public ArrayList<Event> sortEvents(Person person){
        //get the events related to the person
        ArrayList<Event> personEvent = findEvents(person.getPersonID());
        //sort the events chronologically
        Collections.sort(personEvent, Comparator.comparingInt(Event::getYear));
        return personEvent;
    }

}
