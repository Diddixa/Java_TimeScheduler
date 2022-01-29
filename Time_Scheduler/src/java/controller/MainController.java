package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private Button logout;

    @FXML
    void home(MouseEvent event) {
        bp.setCenter(ap);
    }

    @FXML
    void page1(MouseEvent event) {
        loadingPage("page1");
    }

    @FXML
    void page2(MouseEvent event) {
        loadingPage("page2");
    }

    @FXML
    void page3(MouseEvent event) {
        loadingPage("page3");
    }

    public void logoutToLogin(ActionEvent e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("LoginHash.fxml"));
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 580));
        stage.centerOnScreen();

    }

    private void loadingPage(String page) {
        Parent root = null;

        try {
             root = FXMLLoader.load(getClass().getClassLoader().getResource(page + ".fxml"));
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }

        bp.setCenter(root);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
