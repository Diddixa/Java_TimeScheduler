package com.javaproject.time_scheduler;

import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.calendarfx.view.CalendarView;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CalendarView calendarView = new CalendarView();

        Scene scene = new Scene(calendarView);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}