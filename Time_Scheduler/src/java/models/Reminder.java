package models;

/**
 * Enum to define Reminder priority
 */
public enum Reminder {
    WEEKS("One week before", 10080),
    DAYS("Three days before",4320 ),
    HOURS("One hour before", 60),
    MINUTES("Ten minutes before", 10),
    NONE("No need to remind me", 0 );

    /** Name of reminder */
    private String name;
    /** reminder time */
    private int minutes;

    /**
     * Constructor for reminder enum
     * @param name
     * @param minutes
     */

    private Reminder(String name, int minutes) {
        this.name = name;
        this.minutes = minutes;
    }

}
