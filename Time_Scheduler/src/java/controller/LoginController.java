package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button signUpButton;

    @FXML
    private Label loginMsgLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private PasswordField enterPassword;

    @FXML
    private Button loginButton;


    @FXML
    private TextField usernameTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Function created to switch from Login Scene to Register (will be global later)
     * @param e
     * @throws IOException
     */
    public void switchToRegister(ActionEvent e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 580));
    }

    public void loginButtonOnAction(ActionEvent e) throws IOException {

        if (Objects.equals(usernameTxt.getText(), "Admin") && Objects.equals(enterPassword.getText(), "12345678")) {

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Master.fxml"));
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root, 950, 600));
            }

        if (!usernameTxt.getText().isBlank() && !enterPassword.getText().isBlank()) {
            try {
                if (Database.confirmLogin(usernameTxt.getText(), enterPassword.getText()) == 1) {

                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Dashboard1.fxml"));
                        Stage stage = (Stage) signUpButton.getScene().getWindow();
                        stage.setScene(new Scene(root, 950, 600));
                        stage.centerOnScreen();
                    }
                    else{
                    loginMsgLabel.setText("Sadly invalid, maybe try to register?"); }}
                catch(SQLException sqlException){
                sqlException.printStackTrace();}
            }
            }


    /**
     * Closes the register stage
     * @param e
     */
    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }




}
