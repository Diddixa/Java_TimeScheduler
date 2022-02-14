package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class EventScheduleController implements Initializable {

    public Button btnExportWeeklyToPDFOnAction;
    User user;

    @FXML
    private GridPane eventGrid;

    /**
     * function to retrieve the user from LoginController
     * @param user
     */
    public void retrieveUser(User user){
        this.user  = user;
        System.out.println("retrieveUser Event: "+this.user.getUsername());
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
                AnchorPane box = fxmlLoader.load();
                ThumbEventController thumbEventController = fxmlLoader.getController();
                thumbEventController.setData(events.get(i));
                thumbEventController.retrieveUser(this.user);

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


    /**
     * Export current week events to pdf
     * @param e
     */
    @FXML
    public void btnExportWeeklyToPDFOnAction(ActionEvent e) throws IOException, DocumentException {
        // Get selected row
        ArrayList<Event> currentWeekEvents = new ArrayList<>();

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
                        Priority.valueOf(resultSet.getString("priority")),
                        Reminder.valueOf(resultSet.getString("reminder"))
                ));



            }
        } catch (SQLException ex) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String userP = "";
        String weeklyEventToString = "";
        for(Event event: currentWeekEvents){
            for(int i = 0; i < currentWeekEvents.size(); i++) {
                userP = this.user.getEvents().get(i).getParticipantsFullNames().toString();
            }
            weeklyEventToString += event.toString() + userP + "\n\n";

        }

        saveFile(btnExportWeeklyToPDFOnAction, weeklyEventToString);

    }

    private void saveFile(Button btn, String content) throws IOException, DocumentException {
        // If content is empty
        if (content.trim().length() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Event");
            alert.setContentText("Please select an event to export!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files (.pdf)", ".pdf"));
        File file = fileChooser.showSaveDialog(stage);

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
        document.open();
        document.add(new Paragraph(content));
        document.close();
        writer.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Event successful");
        alert.setContentText("Successfully exported events");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.showAndWait();

        //File selectedFile = fileChooser.showOpenDialog(stage);

        stage.centerOnScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
