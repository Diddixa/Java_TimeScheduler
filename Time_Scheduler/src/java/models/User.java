package models;

import controller.Database;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;

public class User {
    /** id of user*/
    private int id;
    /**
     * Username of user*/
    private String username;
    /**
     * Firstname of user*/
    private String firstname;
    /**
     * Lastname of user*/
    private String lastname;
    /**
     * Password of user*/
    private String password;
    /**
     * Email of user*/
    private String email;
    /** List of events */
    private ArrayList<Event> events = new ArrayList<Event>();



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String username, String firstname, String lastname, String password, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }

    public User(int id, String username, String firstname, String lastname, String email) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public User(int id, String username, String firstname, String lastname, String email, ArrayList<Event> events) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.events = events;
    }

    public User(int user_id, String username, String firstname, String lastname, String password, String email) {
    }



    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * Copy constructor
     *
     * @param other - Other user to be copied from
     */
    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.firstname = other.firstname;
        this.lastname = other.lastname;
        this.password = other.password;
        this.email = other.email;
        this.events = other.events;
    }

    /**
     * Add new event to event list
     *
     * @param event - Newly created event
     */
    private void addEvent(Event event) {

        Database.createUserEvents(this.getId() , event.getId());
    }

    /**
     * Set self to event host and add event
     *
     * @param event - Newly created event
     */
    public void createEvent(Event event) {

        event.setEventHostId(this.getId());
        int eventId = Database.storeEvent(event);
        event.setId(eventId);
        this.addEvent(event);

        for (User participant : event.getParticipants()) {
            if (participant == this) continue;
            participant.addEvent(event);
        }

    }

    /**
     * Updates the local list of events from the database
     */
    public void updateEventList(){
        events.clear();
        events.addAll(Database.getEventsFromUser(this.getId()));
    }




}

