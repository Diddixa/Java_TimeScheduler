package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for the Admin works with Master.fxml
 */
public class MasterController implements Initializable {

    public TableView<User> tableView;

    public TableColumn<User, Integer> colID;
    public TableColumn<User, String> colUsername;
    public TableColumn<User, String> colFirstName;
    public TableColumn<User, String> colLastName;
    public TableColumn<User, String> colPassword;
    public TableColumn<User, String> colEmail;

    public Button logoutButton;

    ObservableList<User> observableList = FXCollections.observableArrayList();

    /**
     * Initializes the MasterController Class.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();

        /**
         * Tableview settings, to resize the columns automatically.
         */
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    /**
     * This method will return an ObservableList of User objects.
     */
    public void loadData() {

        try {
            Connection connection = Database.getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM user");

            while (resultSet.next()) {
                observableList.add(new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                ));
                tableView.setItems(observableList);
            }
        } catch (SQLException e) {
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, e);
        }

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    /**
     * Method to clear all information of the table
     */
    @FXML
    public void buttonClearTable(ActionEvent event) {
        tableView.getItems().clear();
    }

    /**
     * Method to populate the table with all users from the database.
     */
    @FXML
    public void buttonPopulateTable(ActionEvent event) {
        buttonClearTable(event);
        loadData();
    }

    /**
     * Method to display the add-a-user-dialog
     * @param event
     * @throws IOException
     */
    @FXML
    public void buttonAddUser(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("DialogAddUser.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * Method to reuse the add-a-user-dialog to edit user's information
     * @param event
     * @throws IOException
     */
    public void buttonUpdateUser(ActionEvent event) throws IOException {
        User user = tableView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DialogAddUser.fxml"));
        Parent root = loader.load();

        DialogAddUserController addUserController = loader.getController();
        addUserController.setUpdate(true);
        addUserController.setTextField(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getPassword(),
                user.getEmail()
                );

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Method to display the delete-a-user-dialog
     * @param event
     * @throws IOException
     */
    @FXML
    public void buttonDeleteUser(ActionEvent event) throws IOException {
        User user = tableView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DialogDeleteUser.fxml"));
        Parent root = loader.load();

        DialogDeleteUserController deleteUserController = loader.getController();
        deleteUserController.setTextField(user.getId());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Button to logout. Will return to the login screen.
     * @param event
     * @throws IOException
     */
    @FXML
    public void buttonLogout(ActionEvent event) throws IOException {
        JavaFxUtil.sceneSwitcher("Login.fxml", logoutButton, 520, 580 );
    }
}
