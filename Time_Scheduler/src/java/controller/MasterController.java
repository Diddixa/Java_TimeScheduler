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
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterController implements Initializable {

    public TableView<User> tableView;

    public TableColumn<User, Integer> colID;
    public TableColumn<User, String> colUsername;
    public TableColumn<User, String> colFirstName;
    public TableColumn<User, String> colLastName;
    public TableColumn<User, String> colPassword;
    public TableColumn<User, String> colEmail;

    public Button closeButton;
    public TextField textFieldUsername;
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public TextField textFieldPassword;
    public TextField textFieldEmail;

    ObservableList<User> observableList = FXCollections.observableArrayList();

    /*
    Initializes the MasterController Class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadData();

        /* Tableview Settings */
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the cells in the table to editable
        tableView.setEditable(true);
        colUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());

    }
    
    /*
    This method will return an ObservableList of User objects.
     */
    public void loadData() {
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("Username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));

        try {
            Connection connection = Database.getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM user");

            while (resultSet.next()) {
                observableList.add(new User(
                        resultSet.getInt("USER_ID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("FIRSTNAME"),
                        resultSet.getString("LASTNAME"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("EMAIL")
                ));
                tableView.setItems(observableList);
            }
        } catch (SQLException e) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void buttonClose(ActionEvent event) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void buttonDeleteAll(ActionEvent event) {
        tableView.getItems().clear();
    }

    @FXML
    public void buttonRefreshDb(ActionEvent event) {
        ObservableList<User> allUser;

        // selects all Users and stores them in this variable
        allUser = tableView.getItems();

        // Clears user table
        allUser.clear();

        // Load data from database
        loadData();
    }

    /*
    This method will add a new User.
     */
    @FXML
    public void buttonCreateUser(ActionEvent event) {
        String encryptPass = PasswordEncryption.createHash(textFieldPassword.getText());
        User user = new User(
                textFieldUsername.getText(),
                textFieldFirstName.getText(),
                textFieldLastName.getText(),
                encryptPass,
                textFieldEmail.getText()
        );
        tableView.getItems().add(user);
        Database.registerUser(user);
    }

    /*
    This method will remove a selected User.
     */
    @FXML
    public void buttonRemoveUser(ActionEvent event) {
        ObservableList<User> allUser, singleUser;

        // selects all Users and stores them in this variable
        allUser = tableView.getItems();

        // get the selected column or row
        singleUser = tableView.getSelectionModel().getSelectedItems();

        // the selected row(s) will be deleted from allUser
        singleUser.forEach(allUser::remove);
        User user = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User: " + singleUser);
        Database.deleteUser(user.getId());
    }

    /*
    This method will edit the selected field of the table
     */
    @FXML
    public void onEditChange(TableColumn.CellEditEvent<User, String> userStringCellEditEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        user.setUsername(userStringCellEditEvent.getNewValue());
        user.setFirstname(userStringCellEditEvent.getNewValue());
        user.setLastname(userStringCellEditEvent.getNewValue());
        user.setPassword(userStringCellEditEvent.getNewValue());
        user.setEmail(userStringCellEditEvent.getNewValue());
        Database.editUser(user);
    }


}
