package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.User;

/**
 * DialogAddUser is a controller utility class that deals with adding users to the admin view
 */
public class DialogAddUserController {
    @FXML
    public TextField textFieldUsername;
    @FXML
    public TextField textFieldFirstname;
    @FXML
    public TextField textFieldLastname;
    @FXML
    public TextField textFieldPassword;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public Button buttonCancel;

    int userID;
    private boolean update;
    String oldPassword;

    /**
     * Creates a new user when all needed information are entered and clicked on the confirm button.
     * @param event
     */
    public void confirm(MouseEvent event) {

        String username = textFieldUsername.getText();
        String firstName = textFieldFirstname.getText();
        String lastName = textFieldLastname.getText();
        String password = textFieldPassword.getText();
        String email = textFieldEmail.getText();
        User user = new User(
                username,
                firstName,
                lastName,
                password,
                email
        );

        if(username.isBlank() || firstName.isBlank() || lastName.isBlank() || password.isBlank() || email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all data!");
            alert.showAndWait();
        }
        else {
            getQuery(user);

            // Clear the textfields
            textFieldUsername.clear();
            textFieldFirstname.clear();
            textFieldLastname.clear();
            textFieldPassword.clear();
            textFieldEmail.clear();
        }
    }

    /**
     * Method to determine whether a user will be added or updated.
     * @param user
     */
    public void getQuery(User user) {
        if(update == false) {
            String password = PasswordEncryption.createHash(textFieldPassword.getText());
            user.setPassword(password);
            Database.registerUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added a new user!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("New user has been successfully added!");
            alert.showAndWait();

            System.out.println("Create User: " + user.getId());
        }
        else {
            if(!oldPassword.equals(textFieldPassword.getText())) {
                String password = PasswordEncryption.createHash(textFieldPassword.getText());
                user.setPassword(password);
                Database.editUser(user, userID);
                System.out.println("New Password");
            }
            else {
                Database.editUser(user, userID);
                System.out.println("Old Password");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User information updated!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("Changed user information");
            alert.showAndWait();
        }
    }

    /**
     * Method to set the user information into the textfield
     * @param id
     */
    void setTextField(int id, String username, String firstName, String lastName, String password, String email) {
        userID = id;
        textFieldUsername.setText(username);
        textFieldFirstname.setText(firstName);
        textFieldLastname.setText(lastName);
        textFieldPassword.setText(password);
        textFieldEmail.setText(email);
    }

    public String getOldPassword(String password) {
        oldPassword = password;
        return oldPassword;
    }

    /**
     * Boolean to switch to update mode
     * @param b
     */
    void setUpdate(boolean b) {
        this.update = b;
    }

    /**
     * Closes the dialog when clicking on the cancel button.
     * @param event
     */
    public void cancel(MouseEvent event) {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

}
