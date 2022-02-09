package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Event;

import java.time.format.DateTimeFormatter;

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

    public void loadEvent(Event event) {
        eventName.setText(event.getName());
        // Muss da nochmal schauen
        //priority.setBackground(new Background(new BackgroundFill(Color.web("#ff6363"))));
        DateTimeFormatter df = DateTimeFormatter.ofPattern(("dd LLLL yyyy"));
        String dateFormat = event.getDate().format(df);
        eventDate.setText(dateFormat);

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeFormat = event.getStartTime().format(tf);
        eventStartTime.setText(startTimeFormat);
        String endTimeFormat = event.getStartTime().format(tf);
        eventStartTime.setText(endTimeFormat);

    }


}
