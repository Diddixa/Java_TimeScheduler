package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Event;
import models.Priority;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    void loadEvents(ActionEvent event) {
       // this.user.updateEventList();
        List<Event> events = new ArrayList<>(user.getEvents());

/*
        for(int i = 0; i < events.size(); i++)
        {
            Priority prio = events.get(i).getPriority();

            switch(prio){
                case Priority.HIGH:  break;
                case Priority.MEDIUM:


            }

            eventName.setText(events.get(i).getName());
            eventName.setText(events.get(i).getLocation());
        }
        eventName.setText(events.get(4).getName()); */

        // if(hostid == userID)

    }


    @FXML
    void home(MouseEvent event) {
        bp.setCenter(ap);
    }

    @FXML
    void page1(MouseEvent event) throws IOException {
        loadingPage("page1");
    }

    @FXML
    void page2(MouseEvent event) throws IOException {
        loadingPage("page2");
    }

    @FXML
    void page3(MouseEvent event) throws IOException {
        loadingPage("page3");
    }

    public void logoutToLogin(ActionEvent e) throws IOException {

        JavaFxUtil.sceneSwitcher("Login.fxml", logout, 520, 580 );

    }


    /**
     * method to load between the scenes using the fxml name as parameter
     * send the logged in user information between the scenes
     * @param page
     * @throws IOException
     */
    private void loadingPage(String page) throws IOException {
        Parent root = null;

        try {
            if(Objects.equals(page, "page1"))
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource(page + ".fxml"));
                root = loader.load();
               ScheduleEventController controller = loader.getController();
               controller.retrieveUser(this.user); // currently logged in user
            }
            else if(Objects.equals(page, "page2")){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource(page + ".fxml"));
                root = loader.load();
                CalendarController controller = loader.getController();
                controller.retrieveUser(this.user); // currently logged in user
            }

        } catch (IOException e) {
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
