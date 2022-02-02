package controller;
import com.calendarfx.view.TimeField;
import javafx.scene.input.MouseEvent;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.time.LocalDate;


public class ScheduleEventController implements Initializable {

    @FXML
    private Button addEventButton;
    @FXML
    private DatePicker eventDate;
    @FXML
    private TextField eventName;
    @FXML
    private Button highButton;
    @FXML
    private TextField locationEvent;
    @FXML
    private Button lowButton;
    @FXML
    private Button mediumButton;
    @FXML
    private TextField participant;
    @FXML
    private ChoiceBox<Reminder> remindChoice = new ChoiceBox<>();;
    @FXML
    private TimeField startTime;
    @FXML
    private TimeField endTime;
    @FXML
    private Label name;

    /** currently registered user */
    private User user;
    /** currently registered user */
    private Reminder chosenReminder;
    private Priority chosenPriority;
    private LocalDate chosenDate;
    private LocalTime chosenStartTime;
    private LocalTime chosenEndTime;
    Event event;
    private ArrayList<User> participants = new ArrayList<>();


    String username;
    /**
     * function to retrieve the logged in user from LoginController
     * @param user
     */
    public void retrieveUser(User user){
        this.user = user;

    }

    /**
     * ActionEvent for user to choose event date
     * @param event
     */
    @FXML
    void scheduleDate(ActionEvent event) {
        chosenDate = eventDate.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = chosenDate.format(formatter);
        System.out.println(formattedString);
    }

    /**
     * MouseEvents for user to choose priority;
     * @param event
     */
    @FXML
        void low(MouseEvent event) {
            chosenPriority = Priority.LOW;
            System.out.println(chosenPriority.name());
        }

        @FXML
        void medium(MouseEvent event) {
            chosenPriority = Priority.MEDIUM;
            System.out.println(chosenPriority.name());

        }

        @FXML
        void high(MouseEvent event) {
            chosenPriority = Priority.HIGH;
            System.out.println(chosenPriority.name());

        }

    /**
     * ActionEvent to save the priority from user
     * @param e
     */
    public void getPriority(ActionEvent e){

        chosenReminder = remindChoice.getValue();
        //System.out.print(chosenReminder.name());
    }


    /**
     * MouseEvent to get start time from user
     * @param e
     */
    public void getStartTime(MouseEvent e)
    {
        chosenStartTime = startTime.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String formattedString = chosenStartTime.format(dtf);
        System.out.println(formattedString);

    }

    /**
     * MouseEvent to get end time from user
     * @param e
     */
    public void getEndTime(MouseEvent e)
    {
        chosenEndTime = endTime.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String formattedString = chosenEndTime.format(dtf);
        System.out.println(formattedString);

    }

    @FXML
    public void addParticipant(ActionEvent e) throws SQLException {
       /* if(participant.getText().isBlank()){

        }*/
        User newUser = Database.getUser(participant.getText());
        participants.add(newUser);

    }

    /**
     * initialize event object and create event 
     * @param e
     */
    @FXML
    public void createEvent(ActionEvent e) {
        event = new Event(eventName.getText(), chosenDate, chosenStartTime, chosenEndTime, locationEvent.getText(), participants, chosenPriority, chosenReminder);
        this.user.createEvent(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        remindChoice.getItems().addAll(Reminder.values());
        remindChoice.setOnAction(this::getPriority);
        startTime.setOnMouseExited(this::getStartTime);
        endTime.setOnMouseExited(this::getEndTime);

    }
}