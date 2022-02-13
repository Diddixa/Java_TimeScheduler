package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * JavaFX utility class to make the code more readable and reusable
 */
public class JavaFxUtil {

    /**
     * method used to switch scenes
     * @param fxml fxml file
     * @param button button that's used
     * @param v size parameter x
     * @param v1 size parameter y
     * @throws IOException
     */
    public static void sceneSwitcher(String fxml, Button button, int v, int v1) throws IOException {

        Parent root = FXMLLoader.load(JavaFxUtil.class.getClassLoader().getResource(fxml));
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root, v, v1);
        scene.getStylesheets().add(JavaFxUtil.class.getResource("/main.css").toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
    }


}
