package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import models.User;

/**
 * Controller class to edit user information
 */
public class EditProfileController {

    @FXML
    private TextField emailText;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Label userLabel;

    @FXML
    private TextField usernameText;
    private User loggedUser;

    /**
     * load logged in user data into textfields
     */
    public void loadUserData(){
        emailText.setText(loggedUser.getEmail());
        firstnameText.setText(loggedUser.getFirstname());
        lastnameText.setText(loggedUser.getLastname());
        usernameText.setText(loggedUser.getUsername()); }

    /**
     * Check if user input is correct and update user information in DB
     * @param event
     */
    @FXML
    void saveChangesButtonOnAction(ActionEvent event) {

        if(usernameText.getText().isBlank() || firstnameText.getText().isBlank() || lastnameText.getText().isBlank() || emailText.getText().isBlank())
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Missing entry");
            errorAlert.setContentText("Please fill in all fields");
            errorAlert.showAndWait();
        }
        else{

        loggedUser.setEmail(emailText.getText());
        loggedUser.setFirstname(firstnameText.getText());
        loggedUser.setLastname(lastnameText.getText());
        loggedUser.setUsername(usernameText.getText());

            if(!Database.isTaken(loggedUser)) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Please check email or username!");
                errorAlert.setContentText("Either email or username already registered");
                errorAlert.showAndWait();
            }

        if(!passwordTxt.getText().isBlank()) {
            loggedUser.setPassword(PasswordEncryption.createHash(passwordTxt.getText()));
        }

        Database.editUser(loggedUser, loggedUser.getId());

        if(Database.editProfile(loggedUser)) {

            Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errorAlert.setTitle("User information updated");
            errorAlert.setContentText("Successfully updated user");
            errorAlert.showAndWait();
        }

        }}

    /**
     * function to retrieve the currently logged in user
     * @param user
     */
    public void retrieveUser(User user) {
        this.loggedUser = user;
    }

}



