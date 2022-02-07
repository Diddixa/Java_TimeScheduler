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
    private DatePicker eventDate;
    @FXML
    private TextField eventName;
    @FXML
    private TextField locationEvent;
    @FXML
    private TextField participant;
    @FXML
    private ChoiceBox<Reminder> remindChoice = new ChoiceBox<>();;
    @FXML
    private TimeField startTime;
    @FXML
    private TimeField endTime;
    @FXML
    private Label addedUsers;

    /** currently registered user */
    private User user;
    /** chosen reminder by user */
    private Reminder chosenReminder;
    /** chosen priority by user */
    private Priority chosenPriority;
    /** chosen event date by user */
    private LocalDate chosenDate = null;
    /** chosen start time by user */
    private LocalTime chosenStartTime;
    /** chosen end time by user */
    private LocalTime chosenEndTime;
    /** create event */
    private Event event;
    /** arraylist of event participants */
    private ArrayList<User> participants = new ArrayList<>();
    boolean boolReminder;


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

    }

    /**
     * MouseEvents for user to choose priority;
     * @param event
     */
    @FXML
        void low(MouseEvent event) {
            chosenPriority = Priority.LOW;
        }

        @FXML
        void medium(MouseEvent event) {
            chosenPriority = Priority.MEDIUM;
            System.out.println(chosenPriority.name());

        }

        @FXML
        void high(MouseEvent event) {
            chosenPriority = Priority.HIGH;

        }

    /**
     * ActionEvent to save the priority from user
     * @param e
     */
    public void getPriority(ActionEvent e){

        chosenReminder = remindChoice.getValue();
        boolReminder = remindChoice.getSelectionModel().isEmpty();
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

    }

    String formattedString = "";
    /**
     * MouseEvent to get end time from user
     * @param e
     */
    public void getEndTime(MouseEvent e)
    {
        chosenEndTime = endTime.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        formattedString = chosenEndTime.format(dtf);

    }

    /**
     * function to add a participant to the participant arraylist afterwards into the according event in the event user bridge
     * @param e
     * @throws SQLException
     */
    @FXML
    public void addParticipant(ActionEvent e) throws SQLException {

        String p = participant.getText();
        User newUser = Database.getUser(p);

        if (newUser == null)
        {
            addedUsers.setText("*user couldn't be found");
        }
        else{
        participants.add(newUser);
        addedUsers.setText("*" + newUser.getUsername() + " has been added to the event"); }

    }


    /**
     * initialize event object and create event 
     * @param e
     */
    @FXML
    public void createEvent(ActionEvent e) {


        if(eventName.getText().isBlank() || formattedString.isBlank() || locationEvent.getText().isBlank() || chosenPriority == null || chosenDate == null){ //|| !boolReminder || chosenPriority == null){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Missing entry");
            errorAlert.setContentText("Please fill in all entries");
            errorAlert.showAndWait();
        }
       else{
       event = new Event(eventName.getText(), chosenDate, chosenStartTime, chosenEndTime, locationEvent.getText(), participants, chosenPriority, chosenReminder);
       this.user.createEvent(event);

            Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errorAlert.setTitle("Event scheduled");
            errorAlert.setContentText("Successfully created event");
            errorAlert.showAndWait();}

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        remindChoice.getItems().addAll(Reminder.values());
        remindChoice.setOnAction(this::getPriority);
        startTime.setOnMouseExited(this::getStartTime);
        endTime.setOnMouseExited(this::getEndTime);

    }
}