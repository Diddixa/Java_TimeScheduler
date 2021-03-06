package models;

import controller.Database;
import controller.MailSender;
import javafx.scene.chart.PieChart;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User model
 */
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

    /**
     * Create new user
     * @param username
     * @param firstname
     * @param lastname
     * @param password
     * @param email
     */
    public User(String username, String firstname, String lastname, String password, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }

    /**
     * Add participant to an event
     * @param id
     * @param username
     * @param firstname
     * @param lastname
     * @param email
     */
    public User(int id, String username, String firstname, String lastname, String email) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    /**
     * Fetch user data from database by using getUser
     * @param id
     * @param username
     * @param firstname
     * @param lastname
     * @param email
     * @param events
     */
    public User(int id, String username, String firstname, String lastname, String email, ArrayList<Event> events) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.events = events;
    }

    /**
     * called upon when loading user data into Adminview
     * @param id
     * @param username
     * @param firstname
     * @param lastname
     * @param password
     * @param email
     */
    public User(int id, String username, String firstname, String lastname, String password, String email) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }



    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * Copy constructor used to edit the user
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
    public void createEvent(Event event) throws MessagingException, IOException {

        event.setEventHostId(this.getId());
        int eventId = Database.storeEvent(event);
        event.setId(eventId);
        event.getAttachments().forEach(e -> Database.storeAttachment(e, event));
        this.addEvent(event);

        for (User participant : event.getParticipants()) {
            if (participant == this) continue;
            participant.addEvent(event);
        }

          updateEventList();

        if (!event.getParticipants().isEmpty()) {
            MailSender.sendEventMail(event, MailStatus.CREATED);
        }
    }

    /**
     * Remove event from participant and user_Events table
     *
     * @param event - Event to be removed
     */
    public void removeEvent(Event event) {

        Database.deleteUserEvents(this.getId(), event.getId());
    }


    /**
     * method removes event from user and all participants
     * @param event - Event to be deleted
     */
    public void deleteEvent(Event event) throws MessagingException, IOException {

        removeEvent(event);
        if (event.getEventHostId() == this.getId()) {

            for (User participant : event.getParticipants()) {
                participant.removeEvent(event);
            }
            Database.deleteEvent(event.getId());
            Database.deleteAllAttachments(event.getId());
        }


        if (!event.getParticipants().isEmpty()) {
            MailSender.sendEventMail(event, MailStatus.DELETED);
        }
    }


    /**
     * Updates the event list
     */
    public void updateEventList(){
        events.clear();
        events.addAll(Database.getEventsFromUser(this.getId()));
    }




}

