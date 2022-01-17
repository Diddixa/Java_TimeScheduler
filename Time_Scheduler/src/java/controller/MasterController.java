package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import models.Master;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class MasterController implements Initializable {
    public TableView<Master> tableView;

    public TableColumn<Master, String> colID;
    public TableColumn<Master, String> colUsername;
    public TableColumn<Master, String> colFirstName;
    public TableColumn<Master, String> colLastName;
    public TableColumn<Master, String> colEmail;
    public TableColumn<Master, LocalDate> colBirthday;

    public Button closeButton;
    public TextField textFieldID;
    public TextField textFieldUsername;
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public TextField textFieldEmail;
    public DatePicker datePickerBirthday;

    @FXML
    public void handleCloseButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /*
    Initializes the MasterController Class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("Username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("UserFirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("UserLastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("UserEmail"));
        colBirthday.setCellValueFactory(new PropertyValueFactory<>("UserBirthday"));

        // Load dummy data
        tableView.setItems(observableList);

        // Set the cells in the table to editable
        tableView.setEditable(true);
        colUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /*
    This method will return an ObservableList of Master objects.
     */
    ObservableList<Master> observableList = FXCollections.observableArrayList(
            new Master("123", "Diddi", "Djidde", "Saengsawad", "diddi@mail.de", LocalDate.of(1995, Month.JULY, 8)),
            new Master("456", "Lami", "Lam", "Dao Ngoc", "lami@mail.de", LocalDate.of(1999, Month.JUNE, 2))
    );

    /*
    This method will add a new Master.
     */
    public void buttonAdd(ActionEvent event) {
        Master master = new Master(textFieldID.getText(), textFieldUsername.getText(), textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(), datePickerBirthday.getValue());
        tableView.getItems().add(master);
    }

    /*
    This method will remove a selected Master.
     */
    public void buttonRemove(ActionEvent event) {
        ObservableList<Master> allMaster, singleMaster;

        // selects all Users and stores them in this variable
        allMaster = tableView.getItems();

        // get the selected column or row
        singleMaster = tableView.getSelectionModel().getSelectedItems();

        // the selected row(s) will be deleted from allMaster
        singleMaster.forEach(allMaster::remove);
    }

    /*
    This method will edit the selected field of the table
     */
    public void onEditChange(TableColumn.CellEditEvent<Master, String> userStringCellEditEvent) {
        Master master = tableView.getSelectionModel().getSelectedItem();
        master.setUsername(userStringCellEditEvent.getNewValue());
        master.setUserFirstName(userStringCellEditEvent.getNewValue());
        master.setUserLastName(userStringCellEditEvent.getNewValue());
        master.setUserEmail(userStringCellEditEvent.getNewValue());
    }

}
