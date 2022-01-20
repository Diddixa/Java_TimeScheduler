package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
    /**
     * Function created to show the button "home" on anchorPane
     * @param event
     */
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Function created to switch between the pages on anchorPane
     * @param page
     * @throws IOException
     */
    private void loadingPage(String page) {
        Parent root = null;

        try {
             root = FXMLLoader.load(getClass().getClassLoader().getResource(page + ".fxml"));
        } catch (IOException e) {
           System.out.println("No page found" + page + "please check FXMLLoader");
        }

        bp.setCenter(root); //makes the switches possible on borderPane

    }


}
