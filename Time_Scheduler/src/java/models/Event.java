package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    /** id of event*/
    private int id;
    /** name of event*/
    private int name;
    /** date of event*/
    private LocalDate date;
    /** start of event*/
    private LocalTime startTime;
    /** end of event*/
    private LocalTime endTime;
    /** location of event*/
    private String location;
    /** host-id of event*/
    private int host_id;
    /**event priority*/
    private Priority priority;
    /** reminder for email*/
    private Reminder reminder;
}
