package models;

public enum Reminder {
    WEEKS("One week before", 10080),
    DAYS("Three days before",4320 ),
    HOURS("One hour before", 60),
    MINUTES("Ten minutes before", 10),
    NONE("No need to remind me", 0 );

    /** Name of reminder */
    private String name;
    /** minutes before start of event */
    private int minutes;

    /**
     * Constructor for reminder enum
     * @param name - name of the enum
     * @param minutes - minutes before start of en event when the user should be reminded
     */
    private Reminder(String name, int minutes) {
        this.name = name;
        this.minutes = minutes;
    }

    /**
     * Get the name of the reminder as a string
     * @return name of the reminder as a string
     */
    public String toString() {
        return name;
    }
}
