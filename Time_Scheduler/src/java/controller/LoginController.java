package controller;

import controller.DBController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.net.URL;

public class LoginController {

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

   /* @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("")
    } */

    public void loginButtonOnAction(ActionEvent e) {

        if (!usernameTxt.getText().isBlank() && !enterPassword.getText().isBlank()){

            loginMsgLabel.setText("You succesfully logged in");
        } else {
            loginMsgLabel.setText("Please enter username and Password");

        }

        }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void confirmLogin() {
        DBController connect = new DBController();
        Connection connectDB = connect.getConnection();

        String verifyLogin = "SELECT count(3) FROM user WHERE username = '" + usernameTxt.getText() + "' AND password ='" + enterPassword.getText() + "'";

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
