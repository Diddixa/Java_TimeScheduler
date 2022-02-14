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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this controller deals with the Registration of the user data into our database
 */
public class RegisterController{

    @FXML
    public Button registerButton;
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
     * method to switch to the login screen
     * @param e
     * @throws Exception
     */
    public void switchToLogin(ActionEvent e) throws Exception {

        JavaFxUtil.sceneSwitcher("Login.fxml", closeButton, 700, 400);

    }

    /**
     *  check mail format
     * @param mail entered mail address
     * @return true when succesful
     */
    public static boolean valMail(String mail){
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(mail);
        return matcher.find();
    }

    /**
     *  Validate user input and register him into database
     * @param e
     * @throws IOException
     */
    public void registerButton(ActionEvent e) throws IOException {

        if(usernameTxt.getText().isBlank() || firstnameTxt.getText().isBlank() || lastnameTxt.getText().isBlank() || setPWD.getText().isBlank() || emailTxt.getText().isBlank())
        {
            registerLabel.setText("*one of the required fields is missing");
        }
        else{
            registerLabel.setText("");
        if(setPWD.getText().equals(confirmPWD.getText())){
            passwordLabel.setText("");
            if(valMail(emailTxt.getText())){
                registerLabel.setText("");

            String encryptPass = PasswordEncryption.createHash(setPWD.getText());
            User user = new User(usernameTxt.getText(), firstnameTxt.getText(), lastnameTxt.getText(), encryptPass, emailTxt.getText());

            if(!Database.isTaken(user))
            {
                registerLabel.setText("");
                registerLabel.setText("*username or email already taken");
                return;
            }
            Database.registerUser(user);

            JavaFxUtil.sceneSwitcher("Login.fxml", registerButton, 700, 400); }
            else{
                registerLabel.setText("");
                registerLabel.setText("*mail is not in valid email format");
            }

        }else{
            passwordLabel.setText("*Password does not match");
        }}

    }



}
