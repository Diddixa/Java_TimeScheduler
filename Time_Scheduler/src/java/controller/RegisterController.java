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

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 560));
    }

    public void registerButton(ActionEvent e){
        if(setPWD.getText().equals(confirmPWD.getText())){
            registerUser();

        }else{
            passwordLabel.setText("Password does not match");
        }
        registerLabel.setText("Thank you for your Registration :)");
    }

    /**
     * Function to add registered User to DB (will be global later in Database class)
     */
    public void registerUser() {
        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();

        String email = emailTxt.getText();
        String firstname = firstnameTxt.getText();
        String lastname = lastnameTxt.getText();
        String username = usernameTxt.getText();
        String password = setPWD.getText();

        String InsertField = "INSERT INTO user(email, firstname, lastname, username, password) VALUES ('";
        String InsertValues = email + "','" + firstname + "','" + lastname + "','" + username + "','" + password + "')";
        String InsertRegister = InsertField + InsertValues;

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(InsertRegister);
            registerLabel.setText("Thank you for your Registration :)");

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }


    }




}
