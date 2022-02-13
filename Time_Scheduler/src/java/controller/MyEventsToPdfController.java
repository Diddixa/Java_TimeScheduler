/*
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MyEventsToPdfController implements Initializable {

    public TableView<Event> myEventsTableView;

    public TableColumn<Event, Integer> colId;
    public TableColumn<Event, String> colName;
    public TableColumn<Event, LocalDate> colDate;
    public TableColumn<Event, LocalTime> colStartTime;
    public TableColumn<Event, LocalTime> colLEndTime;
    public TableColumn<Event, String> colLocation;
    public TableColumn<Event, ArrayList<User>> colParticipants;
    public TableColumn<Event, Priority> colPriority;
    public TableColumn<Event, Reminder> colReminder;

    */
/** currently registered user *//*

    private User user;

    @FXML
    private BorderPane bp;

    @FXML
    private Button btnExportWeeklyEvents;

    @FXML
    private Button btnExportPDF;


    ObservableList<Event> observableList = FXCollections.observableArrayList();

    */
/*
    Initializes the  Class.
     *//*

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        */
/* Tableview Settings *//*

        myEventsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void saveFile(Button btn, String content) throws IOException, DocumentException {

        if (content.trim().length() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Event");
            alert.setContentText("Please select an event to export or export the current week. You might possibly not have any event lined up for the current week!");
            alert.showAndWait();
            return;
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
        document.open();
        document.add(new Paragraph(content));
        document.close();
        writer.close();

        //File selectedFile = fileChooser.showOpenDialog(stage);

        stage.centerOnScreen();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Event successful");
        alert.setContentText("Successfully exported events to " + file.getAbsolutePath());
        alert.showAndWait();


    }

    */
/**
     * function to retrieve the logged in user from LoginController
     * @param user
     *//*

    public void retrieveUser(User user){
        this.user = user;
    }

    */
/*
    This method will return an ObservableList of Event objects.
     *//*

    public void loadData() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        colLEndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        colParticipants.setCellValueFactory(new PropertyValueFactory<>("Participants"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        colReminder.setCellValueFactory(new PropertyValueFactory<>("Reminder"));

        try {
            Connection connection = Database.getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM events e join  where eventhost_id = " + this.user.getId() );

            while (resultSet.next()) {
                observableList.add(new Event(
                        resultSet.getInt("events_id"),
                        resultSet.getString("name"),
                        resultSet.getObject("date", LocalDate.class),
                        resultSet.getObject("startTime", LocalTime.class),
                        resultSet.getObject("endTime", LocalTime.class),
                        resultSet.getString("location"),
                        (ArrayList<User>) resultSet.getArray("participants"),
                        Priority.valueOf(resultSet.getString("priority")),
                        Reminder.valueOf(resultSet.getString("reminder"))
                ));
                myEventsTableView.setItems(observableList);
            }
        } catch (SQLException e) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    */
/**
     * Export selected event to pdf
     * @param e
     *//*

    @FXML
    public void btnExportToPDFOnAction(ActionEvent e) throws IOException, DocumentException {
        // Get selected row
        Event event = myEventsTableView.getSelectionModel().getSelectedItem();
*/
/*
        // Ask to select an event if none is selected
        if (event == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Event");
            alert.setContentText("Please select an event to export!");
            alert.showAndWait();
            return;
        }
*//*

        saveFile(btnExportPDF, event.toString());
    }


    */
/**
     * Export current week events to pdf
     * @param e
     *//*

    @FXML
    public void btnExportWeeklyToPDFOnAction(ActionEvent e) throws IOException, DocumentException {
        // Get selected row
        ArrayList<Event> currentWeekEvents = new ArrayList<>();
        Event event = myEventsTableView.getSelectionModel().getSelectedItem();

        try {
            Connection connection = Database.getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM events where YEARWEEK(date , 1) = YEARWEEK(CURDATE(), 1) and eventhost_id = " + this.user.getId() );

            while (resultSet.next()) {
                currentWeekEvents.add(new Event(
                        resultSet.getInt("events_id"),
                        resultSet.getString("name"),
                        resultSet.getObject("date", LocalDate.class),
                        resultSet.getObject("startTime", LocalTime.class),
                        resultSet.getObject("endTime", LocalTime.class),
                        resultSet.getString("location"),
                        (ArrayList<User>) resultSet.getArray("participants"),
                        Priority.valueOf(resultSet.getString("priority")),
                        Reminder.valueOf(resultSet.getString("reminder"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Iterate through all events in the current week
        // and put them in a string
        String events = "";
        for(int i = 0; i < currentWeekEvents.size(); i++) {
            events += currentWeekEvents.get(i).toString() + "\n\n";
        }

        saveFile(btnExportWeeklyEvents, events);
    }


}
*/
