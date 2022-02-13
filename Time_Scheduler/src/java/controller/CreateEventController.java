package controller;
import com.calendarfx.view.TimeField;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.time.LocalDate;

/**
 * Controller used to create an event references FXML file page1
 */
public class CreateEventController implements Initializable {

    @FXML
    private TextField eventName;
    @FXML
    private DatePicker eventDate;
    @FXML
    private TimeField startTime;
    @FXML
    private TimeField endTime;
    @FXML
    private Label falseTime;
    @FXML
    private TextField locationEvent;
    @FXML
    private TextField participant;
    @FXML
    private Label addedUsers;
    @FXML
    private ChoiceBox<Reminder> remindChoice = new ChoiceBox<>();
    @FXML
    public Button lowButton;
    @FXML
    public Button mediumButton;
    @FXML
    public Button highButton;
    @FXML
    private TextField attachmentText;
    @FXML
    private TextField description;
    @FXML
    public Button buttonCreateEvent;
    @FXML
    public Button buttonCancel;
    @FXML
    public Button buttonDeleteEvent = new Button("Delete event");
    @FXML
    public Button buttonExportEvent = new Button("Export event");
    @FXML
    public VBox boxLeft;
    @FXML
    public AnchorPane ap;
    @FXML
    public Pane header;


    /** currently registered user */
    private User user;
    /** chosen reminder by user */
    private Reminder chosenReminder;
    /** chosen priority by user */
    private Priority chosenPriority;
    /** chosen event date by user */
    private LocalDate chosenDate = null;
    /** chosen start time by user */
    private LocalTime chosenStartTime;
    /** chosen end time by user */
    private LocalTime chosenEndTime;
    /** create event */
    private Event event;
    /** arraylist of event participants */
    private ArrayList<User> participants = new ArrayList<>();
    /** arraylist of added files (attachments) */
    private ArrayList<File> attachments = new ArrayList<File>();
    FileChooser fileChooser = new FileChooser();
    boolean boolReminder;

    /**
     * function to retrieve the logged in user from LoginController
     * @param user
     */
    public void retrieveUser(User user){
        this.user = user;
        System.out.println("Retrieve User: "+this.user.getUsername());
    }

    public void retrieveEvent(Event event){
        this.event = event;
        System.out.println("Retrieve Event: "+this.event.getName());
    }

    /**
     * ActionEvent for user to choose event date
     * @param event
     */
    @FXML
    void scheduleDate(ActionEvent event) {
        chosenDate = eventDate.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = chosenDate.format(formatter);

    }

    /**
     * MouseEvents for user to choose priority;
     * @param event
     */
    @FXML
    void low(MouseEvent event) {
        chosenPriority = Priority.LOW;
        lowButton.setStyle("-fx-background-color: #65F093");
        mediumButton.setStyle("-fx-background-color: transparent");
        highButton.setStyle("-fx-background-color: transparent");
    }

    @FXML
    void medium(MouseEvent event) {
        chosenPriority = Priority.MEDIUM;
        lowButton.setStyle("-fx-background-color: transparent");
        mediumButton.setStyle("-fx-background-color: ffe08c");
        highButton.setStyle("-fx-background-color: transparent");
    }

    @FXML
    void high(MouseEvent event) {
        chosenPriority = Priority.HIGH;
        lowButton.setStyle("-fx-background-color: transparent");
        mediumButton.setStyle("-fx-background-color: transparent");
        highButton.setStyle("-fx-background-color: #ff7979");
    }

    /**
     * ActionEvent to save the priority from user
     * @param e
     */
    public void getPriority(ActionEvent e){
        chosenReminder = remindChoice.getValue();
        boolReminder = remindChoice.getSelectionModel().isEmpty();
    }


    /**
     * MouseEvent to get start time from user
     * @param e
     */
    public void getStartTime(MouseEvent e)
    {
        chosenStartTime = startTime.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String formattedString = chosenStartTime.format(dtf);

    }

    String formattedString = "";
    /**
     * MouseEvent to get end time from user
     * @param e
     */
    public void getEndTime(MouseEvent e)
    {
        chosenEndTime = endTime.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        formattedString = chosenEndTime.format(dtf);

    }

    /**
     * add a participant to the participant arraylist afterwards into the according event in the event user bridge
     * @param e
     * @throws SQLException
     */
    @FXML
    public void addParticipant(ActionEvent e) throws SQLException {

        String p = participant.getText();
        User newUser = Database.getUser(p);

        if (newUser == null)
        {
            addedUsers.setTextFill(Color.RED);
            addedUsers.setText("*user couldn't be found");
        }
        else{
            participants.add(newUser);
            addedUsers.setTextFill(Color.GREEN);

            addedUsers.setText("*" + newUser.getUsername() + " has been added to the event"); }

    }


    /**
     * add a file to the Attachments array list
     * @param e
     * @throws SQLException
     */
    @FXML
    public void addFile(ActionEvent e) throws SQLException {
            Stage stage = new Stage();

           /* FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.PDF", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);*/

             File file =  fileChooser.showSaveDialog(stage);

            attachments.add(file);

            attachmentText.setText(attachments.size() + " " + "File(s) added");

    }


    /**
     * initialize event object and create event while checking for the correct entries
     * @param e
     */
    @FXML
    public void createEvent(ActionEvent e) throws MessagingException, IOException {

        if(update == false) {
            if(eventName.getText().isBlank() || formattedString.isBlank() || locationEvent.getText().isBlank() || chosenPriority == null || chosenDate == null){ //|| !boolReminder || chosenPriority == null){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Missing entry");
                errorAlert.setContentText("Please fill in all entries");
                errorAlert.showAndWait();
            }
           else if (chosenStartTime.isAfter(chosenEndTime)){
                falseTime.setText("*start time can't be before end time");
            }
           else{
                    event = new Event(eventName.getText(), chosenDate, chosenStartTime, chosenEndTime, locationEvent.getText(), participants, chosenPriority, chosenReminder, attachments, description.getText());
                    this.user.createEvent(event);

                    Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    errorAlert.setTitle("Event scheduled");
                    errorAlert.setContentText("Successfully created event");
                    errorAlert.showAndWait();

                    // Clear the textfields
                    eventName.clear();
                    locationEvent.clear();
                    description.clear();
                    participants.clear();
                    remindChoice.setValue(null);
                    participants.clear();
                    addedUsers.setTextFill(Color.TRANSPARENT);
                    attachmentText.clear();
           }
        }
        else {
            if(this.user.getId() == this.event.getEventHostId()){
            if(eventName.getText().isBlank() || formattedString.isBlank() || locationEvent.getText().isBlank() || chosenPriority == null || chosenDate == null){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Missing entry");
                errorAlert.setContentText("Please fill in all entries");
                errorAlert.showAndWait();
            }
            else if (chosenStartTime.isAfter(chosenEndTime)){
                falseTime.setText("*end time can't be before start time");
            }
            else {
                this.event.setName(eventName.getText());
                this.event.setDate(eventDate.getValue());
                this.event.setStartTime(chosenStartTime);
                this.event.setEndTime(chosenEndTime);
                this.event.setLocation(locationEvent.getText());
                this.event.setParticipants(participants);
                this.event.setPriority(chosenPriority);
                this.event.setAttachments(attachments);
                this.event.setDescription(description.getText());

                this.event.editEvent(this.event);

            }}
            else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("No Permission");
            errorAlert.setContentText("You are not allowed to edit this event!");
            errorAlert.showAndWait();
        }
    }}

    int eventID;
    void setTextFields(int eId, String eName, LocalDate eDate, LocalTime eStart, LocalTime eEnd, String eLocation, String eParticipants, Reminder eReminder, String eDescription) {
        eventID = eId;
        eventName.setText(eName);
        eventDate.setValue(eDate);
        startTime.setValue(eStart);
        endTime.setValue(eEnd);
        locationEvent.setText(eLocation);
        participant.setText(eParticipants);
        remindChoice.setValue(eReminder);
        description.setText(eDescription);

        if(update == true){
            ap.getChildren().remove(header);
            buttonCancel.setStyle("-fx-background-color: transparent");
            buttonCreateEvent.setText("Update event");
            buttonDeleteEvent.setPrefWidth(200);
            buttonDeleteEvent.setTranslateX(55);
            boxLeft.getChildren().add(buttonDeleteEvent);

            buttonExportEvent.setPrefWidth(200);
            buttonExportEvent.setTranslateX(20);
            buttonExportEvent.setTranslateY(20);
            ap.getChildren().add(buttonExportEvent);
        }
       else if (chosenStartTime.isAfter(chosenEndTime)) {
            falseTime.setText("*start time can't be after end time");
        }

    }
    private void saveFile(Button btn, String content) throws IOException, DocumentException {

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
        alert.showAndWait();

        //File selectedFile = fileChooser.showOpenDialog(stage);

        stage.centerOnScreen();
    }

    /**
     * Export selected event to pdf
     * @param e
     */
    @FXML
    public void btnExportToPDFOnAction(ActionEvent e) throws IOException, DocumentException {
        // Get selected row
        Event event = this.event;

        // Ask to select an event if none is selected
        if (event == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Event");
            alert.setContentText("Please select an event to export!");
            alert.showAndWait();
            return;
        }

        saveFile(buttonExportEvent, event.toString());

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("myEvents.pdf"));
            document.open();
            document.add(new Paragraph(event.getName()));
            document.add(new Paragraph(event.getDate().toString()));
            document.add(new Paragraph(event.getStartTime().toString()));
            document.add(new Paragraph(event.getEndTime().toString()));
            document.add(new Paragraph(event.getLocation()));
            for (int i = 0; i < event.getParticipants().size(); i++) {
                document.add(new Paragraph(event.getParticipants().get(i).getUsername()));
            }
            document.add(new Paragraph(event.getPriority().toString()));
            document.add(new Paragraph(event.getDescription()));
            document.close();
            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Event successful");
            alert.setContentText("Successfully exported events");
            alert.showAndWait();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    private boolean update;
    void setUpdate(boolean b) {
        this.update = b;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        remindChoice.getItems().addAll(Reminder.values());
        remindChoice.setOnAction(this::getPriority);
        startTime.setOnMouseExited(this::getStartTime);
        endTime.setOnMouseExited(this::getEndTime);

        buttonDeleteEvent.setOnAction(e -> {
            try {
                if(this.user.getId() == event.getEventHostId()){
                    this.user.deleteEvent(event);
                }
                else{
                    this.user.removeEvent(event);
                }
            } catch (MessagingException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            close(e);
        });

        buttonExportEvent.setOnAction(e -> {
            try {
                btnExportToPDFOnAction(e);
                close(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void close(ActionEvent event) {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }
}