package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Event;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventScheduleController implements Initializable {

    User user;

    /**
     * function to retrieve the user from LoginController
     * @param user
     */
    public void retrieveUser(User user){
        this.user  = user;
    }

    void loadEvents() {
        this.user.updateEventList();
        ArrayList<Event> events = new ArrayList<>(user.getEvents());

        int columns = 0;
        int row = 1;

        try {
            for (int i = 0; i < events.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("ThumbEvent.fxml"));
                VBox box = fxmlLoader.load();
                ThumbEventController thumbEventController = fxmlLoader.getController();
                thumbEventController.setData(events.get(i));

                if(columns == 2) {
                    columns = 0;
                    ++row;
                }
                eventGrid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private GridPane eventGrid;
    private List<Event> events;
    ObservableList<User> eventList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
