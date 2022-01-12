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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

    public void loginButtonOnAction(ActionEvent e) {

        if (!usernameTxt.getText().isBlank() && !enterPassword.getText().isBlank()){

            confirmLogin();
        } else {
            loginMsgLabel.setText("Please enter username and Password");

        }

        }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void confirmLogin() {
        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user WHERE username = '" + usernameTxt.getText() + "' AND password = '" + enterPassword.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){ //if field 1 is equal to 1 the actual label is used
                loginMsgLabel.setText("You did it!");
                } else
                    {
                        loginMsgLabel.setText("Sadly invalid, try again");
                    }
                }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}
