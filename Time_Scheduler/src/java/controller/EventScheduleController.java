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
    public void btnExportWeeklyToPDFOnAction(ActionEvent e) throws IOException {
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
                        //(ArrayList<User>) resultSet.getArray("participants"),
                        Priority.valueOf(resultSet.getString("priority")),
                        Reminder.valueOf(resultSet.getString("reminder"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }




        if (currentWeekEvents.size() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Current Week Event");
            alert.setContentText("No event for this week to export!");
            alert.showAndWait();
        }
        //CreateEventController.saveFile(btnExportWeeklyToPDFOnAction, currentWeekEvents.toString());

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("myWeeklyEvents.pdf"));
            document.open();
            for(int i = 0; i < currentWeekEvents.size(); i++) {
                document.add(new Paragraph("Event name: "+currentWeekEvents.get(i).getName() + "\n"));
                document.add(new Paragraph("Date: "+currentWeekEvents.get(i).getDate().toString() + "\n"));
                document.add(new Paragraph("Start time: "+currentWeekEvents.get(i).getStartTime().toString() + "\t\t End time: "+currentWeekEvents.get(i).getEndTime().toString() + "\n"));
                document.add(new Paragraph("Location: "+currentWeekEvents.get(i).getLocation() + "\n"));
                ArrayList<User> pList = currentWeekEvents.get(i).getParticipants();
                if(pList != null) {
                    document.add(new Paragraph("Participants: "));
                    for (int j = 0; j < pList.size(); j++) {
                        document.add(new Paragraph(pList.get(j).getUsername() + "\n"));
                    }
                }
                document.add(new Paragraph("Priority: "+currentWeekEvents.get(i).getPriority().toString() + "\n"));
                if(pList != null) {
                    document.add(new Paragraph("Description: "+currentWeekEvents.get(i).getDescription() + "\n"));
                }
            }
            document.close();
            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export weekly events");
            alert.setContentText("Successfully exported current week events");
            alert.showAndWait();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
