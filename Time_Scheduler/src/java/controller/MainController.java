package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Event;
import models.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main dashobard scene also deals with switching scenes between side navigation panel
 */
public class MainController implements Initializable  {

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private Button logout;

    @FXML
    private Label username;
    @FXML
    private Label firstLastName;

    @FXML
    private Label time;
    private final boolean stop = false;

    /** currently registered user */
    private User user;

    /**
     * function to retrieve the user from LoginController
     * @param user
     */
    public void retrieveUser(User user){

        this.user  = user;
        username.setText(this.user.getUsername());
        //firstLastName.setText(this.user.getFirstname() + " " + this.user.getLastname());
    }

    @FXML
    private Label eventName;

    @FXML
    void home(MouseEvent event) {
        bp.setCenter(ap);
    }

    @FXML
    void CreateEvent(MouseEvent event) throws IOException {
        loadingPage("CreateEvent");
    }

    @FXML
    void EventSchedule(MouseEvent event) throws IOException {
        loadingPage("EventSchedule");
    }

    @FXML
    void editProfile(MouseEvent event) throws IOException {
        loadingPage("editProfile");
    }

    public void logoutToLogin(ActionEvent e) throws IOException {

        JavaFxUtil.sceneSwitcher("Login.fxml", logout, 520, 580 );

    }


    /**
     * method to load between the scenes using the fxml name as parameter
     * send the logged-in user information between the scenes
     * @param page
     * @throws IOException
     */
    private void loadingPage(String page) throws IOException {
        Parent root = null;

        try {
            if(Objects.equals(page, "CreateEvent"))
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource(page + ".fxml"));
                root = loader.load();
               CreateEventController controller = loader.getController();
               controller.retrieveUser(this.user); // currently logged in user
                MailSender.reminderMail(this.user);
            }
            else if(Objects.equals(page, "EventSchedule")){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource(page + ".fxml"));
                root = loader.load();
                EventScheduleController EventSchedule = loader.getController();
                EventSchedule.retrieveUser(this.user); // currently logged in user
                EventSchedule.loadEvents();
                MailSender.reminderMail(this.user);
            }
            else if(Objects.equals(page, "editProfile")){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource(page + ".fxml"));
                root = loader.load();
                EditProfileController editController = loader.getController();
                editController.retrieveUser(this.user); // currently logged in user
                editController.loadUserData();
            }

        } catch (IOException | MessagingException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }

        bp.setCenter(root);

    }

    /**
     * method to show realtime
     */
    private void timeNow(){

        Thread thread = new Thread( () ->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while(!stop) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow);
                });
            }
        });
        thread.start();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeNow();
    }

}
