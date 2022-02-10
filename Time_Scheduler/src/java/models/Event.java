package models;

import controller.Database;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Event model
 */
public class Event {


    /**
     * id of event
     */
    private int id;
    /**
     * Name of event
     */
    private String name;
    /**
     * Date of event
     */
    private LocalDate date;
    /**
     * Start time of event
     */
    private LocalTime startTime;
    /**
     * Duration in minutes of event
     */
    private LocalTime endTime;
    /**
     * Location object of event
     */
    private String location;
    /**
     * Event host id of event
     */
    private int eventHostId;
    /** List of participants of an event*/
    private ArrayList<User> participants;
    /** List of attachments */
    private ArrayList<File> attachments;
    /** Priority of event*/
    private Priority priority;
    private Reminder reminder;

    public ArrayList<File> getAttachments() {
        return attachments;
        }

    public void setAttachments(ArrayList<File> attachments) {
        this.attachments = attachments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEventHostId() {
        return eventHostId;
    }

    public void setEventHostId(int eventHostId) {
        this.eventHostId = eventHostId;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get all events from user
     * @param id
     * @param name
     * @param date
     * @param startTime
     * @param endTime
     * @param location
     * @param participants
     * @param priority
     * @param reminder
     */
    public Event(int id, String name, LocalDate date, LocalTime startTime, LocalTime endTime, String location, ArrayList<User> participants, Priority priority, Reminder reminder) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.participants = participants;
        this.priority = priority;
        this.reminder = reminder;
    }


    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime, String location, Reminder reminder, Priority priority) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.priority = priority;
        this.reminder = reminder;
    }

    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime, String location, ArrayList<User> participants, Priority priority, Reminder reminder) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.participants = participants;
        this.priority = priority;
        this.reminder = reminder;
    }

    /**
     * Create a new event
     * @param name
     * @param date
     * @param startTime
     * @param endTime
     * @param location
     * @param participants
     * @param priority
     * @param reminder
     * @param attachments
     */
    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime, String location, ArrayList<User> participants, Priority priority, Reminder reminder, ArrayList<File> attachments) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.participants = participants;
        this.priority = priority;
        this.reminder = reminder;
        this.attachments = attachments;
    }

    /**
     * method to edit the event information
     * @param other - Event to be copied from
     */
    public void editEvent(Event other) {
        name = other.name;
        date = other.date;
        startTime = other.startTime;
        endTime = other.endTime;
        location = other.location;
        participants = other.participants;
        priority = other.priority;
        attachments = other.attachments;
        reminder = other.reminder;

        Database.editEvent(this);
    }
}
