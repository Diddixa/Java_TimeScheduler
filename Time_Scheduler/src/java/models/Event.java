package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
    /**
     * List of participants of an event
     */
    private ArrayList<User> participants;
    /**
     * Priority of event
     */
    private Priority priority;
    private Reminder reminder;


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
     * Constructor for creating an event
     *
     * @param name - name of the event
     * @param date - date of the event
     * @param startTime - starting time of the event
     * @param endTime - duration of the event in minutes
     * @param participants - list of participants
     * @param attachments - list of attachments
     * @param location - location of the event
     * @param reminder - selected reminder for the event
     */
    public Event(String name, LocalDate date, LocalTime startTime,LocalTime endTime, String location,
                 ArrayList<User> participants, ArrayList<File> attachments, Reminder reminder) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.attachments = attachments;
        this.location = location;
        this.reminder = reminder;
    }

}
