package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class ThumbEventController {
    @FXML
    public Label eventName;
    @FXML
    public Pane priority;
    @FXML
    public LocalDate eventDate;
    @FXML
    public LocalTime eventStartTime;
    @FXML
    public LocalTime eventEndTime;

    public void loadEvent(Event event) {
        eventName.setText(event.getName());
        // Muss da nochmal schauen
        //priority.setBackground(new Background(new BackgroundFill(Color.web("#ff6363"))));
        //eventDate.

    }


}
