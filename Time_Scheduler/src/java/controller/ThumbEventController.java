package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Event;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ThumbEventController {
    @FXML
    public Label eventName;
    @FXML
    public Pane priority;
    @FXML
    public Label eventDate;
    @FXML
    public Label eventStartTime;
    @FXML
    public Label eventEndTime;

    Event event;
    /** currently registered user */
    private User user;
    /**
     * function to retrieve the user from LoginController
     * @param user
     */
    public void retrieveUser(User user){
        this.user  = user;
    }

    public Event setData(Event e) {

        event = e;

        String prioBG = "";

        switch(event.getPriority()) {
            case LOW:
                prioBG = "-fx-background-color: #65F093";
                break;
            case MEDIUM:
                prioBG = "-fx-background-color: #ffe08c";
                break;
            case HIGH:
                prioBG = "-fx-background-color: #ff7979";
                break;
        }

        priority.setStyle(prioBG);
        eventName.setText(event.getName());
        DateTimeFormatter df = DateTimeFormatter.ofPattern(("dd LLLL yyyy"));
        String dateFormat = event.getDate().format(df);
        eventDate.setText(dateFormat);

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeFormat = event.getStartTime().format(tf);
        eventStartTime.setText(startTimeFormat);
        String endTimeFormat = event.getEndTime().format(tf);
        eventEndTime.setText(endTimeFormat);

        return event;
    }


    public void buttonShowEvent(ActionEvent e) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateEvent.fxml"));
        Parent root = loader.load();

        ArrayList<User> p = event.getParticipants();
        String userP = "";

        for (int i = 0; i < p.size(); i++) {
            if(i == 0) {
                userP += p.get(i).getUsername() + "(Host)";
            }
            else {
                userP += ", "+ p.get(i).getUsername();
            }
        }

        CreateEventController createEventController = loader.getController();
        createEventController.setUpdate(true);
        createEventController.retrieveUser(this.user);
        createEventController.retrieveEvent(this.event);
        createEventController.setTextFields(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation(),
                userP,
                event.getReminder(),
                event.getDescription()
        );

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        root.getStylesheets().add(JavaFxUtil.class.getResource("/main.css").toExternalForm());
        stage.show();
        stage.centerOnScreen();
    }
}
