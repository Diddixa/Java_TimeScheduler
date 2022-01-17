package models;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Master {
    private SimpleStringProperty userID;
    private SimpleStringProperty username;
    private SimpleStringProperty userFirstName;
    private SimpleStringProperty userLastName;
    private SimpleStringProperty userEmail;
    private SimpleStringProperty userPassword;

    public Master(String userID, String username, String userFirstName, String userLastName, String userEmail, String userPassword) {
        this.userID = new SimpleStringProperty(userID);
        this.username = new SimpleStringProperty(username);
        this.userFirstName = new SimpleStringProperty(userFirstName);
        this.userLastName = new SimpleStringProperty(userLastName);
        this.userEmail = new SimpleStringProperty(userEmail);
        this.userPassword = new SimpleStringProperty(userPassword);
    }

    public String getUserID() {
        return userID.get();
    }

    public void setUserID(String userID) {
        this.userID = new SimpleStringProperty(userID);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getUserFirstName() {
        return userFirstName.get();
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = new SimpleStringProperty(userFirstName);
    }

    public String getUserLastName() {
        return userLastName.get();
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = new SimpleStringProperty(userLastName);
    }

    public String getUserEmail() {
        return userEmail.get();
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = new SimpleStringProperty(userEmail);
    }

    public String getUserPassword() {
        return userPassword.get();
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = new SimpleStringProperty(userPassword);
    }
}
