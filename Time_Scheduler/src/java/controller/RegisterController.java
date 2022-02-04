package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController{

    @FXML
    public TextField confirmPasswordTxt;
    @FXML
    public Button registerButton;
    @FXML
    public TextField setPassword;
    @FXML
    public TextField usernameTxt;
    @FXML
    public Button closeButton;
    @FXML
    public TextField lastnameTxt;
    @FXML
    public TextField emailTxt;
    @FXML
     public TextField firstnameTxt;

    @FXML
    private PasswordField setPWD;

    @FXML
    private PasswordField confirmPWD;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label registerLabel;


    /**
     * Function created to switch from Register Scene to Login (will be global later)
     * @param e
     * @throws Exception
     */
    public void switchToLogin(ActionEvent e) throws Exception {

        JavaFxUtil.sceneSwitcher("Login.fxml", closeButton, 520, 560);

    }

    public void registerButton(ActionEvent e) throws IOException {

        if(usernameTxt.getText().isBlank() || firstnameTxt.getText().isBlank() || lastnameTxt.getText().isBlank() || setPWD.getText().isBlank() || emailTxt.getText().isBlank())
        {
            registerLabel.setText("One of the required fields is missing!");
        }
        else{
        if(setPWD.getText().equals(confirmPWD.getText())){

            String encryptPass = PasswordEncryption.createHash(setPWD.getText());
            User user = new User(usernameTxt.getText(), firstnameTxt.getText(), lastnameTxt.getText(), encryptPass, emailTxt.getText());

            if(!Database.isAvailable(user))
            {
                registerLabel.setText("*username or email already taken!");
                return;
            }
            Database.registerUser(user);

            JavaFxUtil.sceneSwitcher("Login.fxml", registerButton, 950, 600);

        }else{
            passwordLabel.setText("*Password does not match");
        }}

    }



}
