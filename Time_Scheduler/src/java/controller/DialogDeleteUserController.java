package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * DialogDeleteUser is a controller utility class that deals with adding users to the admin view
 */
public class DialogDeleteUserController {
    @FXML
    public TextField textFieldID;
    @FXML
    public Button buttonDelete;
    @FXML
    public Button buttonCancel;

    int userID;

    /**
     * Deletes a certain user when clicked on the confirm button.
     * Needs to enter the user ID to delete the user.
     * @param event
     */
    public void delete(MouseEvent event) {
        buttonDelete.setOnAction(e -> {
            String deleteUser = textFieldID.getText();
            int deleteUserId = Integer.valueOf(deleteUser);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonYes = new ButtonType("yes");
            ButtonType buttonNo = new ButtonType("no");
            alert.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
            alert.getButtonTypes().addAll(buttonYes, buttonNo);
            alert.setTitle("Warning!");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the user?");
            alert.showAndWait();
            if(alert.getResult() == buttonYes) {
                Database.deleteUser(deleteUserId);

                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Deleted a user!");
                alertInfo.setGraphic(null);
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("User has been successfully deleted!");
                alertInfo.showAndWait();

                textFieldID.clear();
            }
        });
    }

    /**
     * Method to set the user ID into the textfield
     * @param id
     */
    void setTextField(int id) {
        userID = id;
        String stringID = String.valueOf(userID);
        textFieldID.setText(stringID);
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
