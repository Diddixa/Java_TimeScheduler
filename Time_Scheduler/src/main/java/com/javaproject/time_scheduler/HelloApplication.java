package com.javaproject.time_scheduler;

import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import com.calendarfx.view.CalendarView;



// create login form using javafx
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


        // creating label email
        Text lbl1=new Text("Email");
        // creating label password
        Text lbl2=new Text("Password");
        // creating textfield for email
        TextField text1=new TextField();
        // creating textfield for password
        PasswordField text2=new PasswordField();
        // create buttons
        Button button1=new Button("Signin");
        Button button2=new Button("Cancel");

        // creating a Grid Pane
        GridPane gridPane=new GridPane();
        // setting the size for Pane
        gridPane.setMinSize(600,300);
        // setting the padding
        gridPane.setPadding(new Insets(10,10,10,10));
        // setting the vertical and horizontal gaps between the colums
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        // setting grid alignment
        gridPane.setAlignment(Pos.CENTER);
        // arranging all teh nodes in the grid
        gridPane.add(lbl1,0,0);
        gridPane.add(text1,1,0);
        gridPane.add(lbl2,0,1);
        gridPane.add(text2,1,1);
        gridPane.add(button1,0,2);
        gridPane.add(button2,1,2);
        // Styling nodes
        /* to do*/




        //setting the title to the stage
        stage.setTitle("Login");

        // create a scen object
        Scene scene1=new Scene(gridPane);

        // adding scene to the stage
        stage.setScene(scene1);

        // display the contents of the stage
        stage.show();




    }

    public static void main(String[] args) {
        launch(args);
    }
}