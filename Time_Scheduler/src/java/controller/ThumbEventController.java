package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
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


/*
    public void setData() {
        eventName.setText("Party");
        // Muss da nochmal schauen
        //priority.setBackground(new Background(new BackgroundFill(Color.web("#ff6363"))));
        eventDate.setText(LocalDate.of(2023, Month.JULY, 8).toString());

        eventStartTime.setText(LocalTime.of(12, 00).toString());
        eventEndTime.setText(LocalTime.of(13, 00).toString());

    }
*/
    public void setData(Event event) {

        eventName.setText(event.getName());
        // Muss da nochmal schauen
        //priority.setBackground(new Background(new BackgroundFill(Color.web("#ff6363"))));
        DateTimeFormatter df = DateTimeFormatter.ofPattern(("dd LLLL yyyy"));
        String dateFormat = event.getDate().format(df);
        eventDate.setText(dateFormat);

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeFormat = event.getStartTime().format(tf);
        eventStartTime.setText(startTimeFormat);
        String endTimeFormat = event.getEndTime().format(tf);
        eventEndTime.setText(endTimeFormat);

    }



}
