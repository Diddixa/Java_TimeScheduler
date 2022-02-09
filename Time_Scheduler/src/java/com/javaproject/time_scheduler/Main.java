package com.javaproject.time_scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = (new Scene(root, 520, 520));
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();



    }



    public static void main(String[] args) {
        launch(args);
    }
}
