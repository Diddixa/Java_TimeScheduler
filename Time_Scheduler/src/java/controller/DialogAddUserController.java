package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.User;

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

    /**
     * Creates a new user when all needed information are entered and clicked on the confirm button.
     * @param event
     */
    public void confirm(MouseEvent event) {

        String username = textFieldUsername.getText();
        String firstName = textFieldFirstname.getText();
        String lastName = textFieldLastname.getText();
        String encryptPass = PasswordEncryption.createHash(textFieldPassword.getText());
        String email = textFieldEmail.getText();
        User user = new User(
                username,
                firstName,
                lastName,
                encryptPass,
                email
        );

        if(username.isBlank() || firstName.isBlank() || lastName.isBlank() || encryptPass.isBlank() || email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all data!");
            alert.showAndWait();
        }
        else {
            Database.registerUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added a new user!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("New user has been successfully added!");
            alert.showAndWait();

            // Clear the textfields
            textFieldUsername.clear();
            textFieldFirstname.clear();
            textFieldLastname.clear();
            textFieldPassword.clear();
            textFieldEmail.clear();

            System.out.println("Create User: " + user.getId());
        }
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
