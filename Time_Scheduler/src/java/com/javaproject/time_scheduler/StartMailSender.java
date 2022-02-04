package com.javaproject.time_scheduler;


import controller.MailSender;

public class StartMailSender {
    public static void main(String[] args) {

        // Get username and password from the class Zugangsdaten
        String username = "diddi.saeng@gmail.com";
        String password = "Passwort!123";

        MailSender sender = new MailSender();	// Instanzieren

        // SMTP Port for TLS/STARTTLS: 587
        sender.login("smtp.gmail.com", "587", username, password);

        // Message of the email HTML formatted
        // message = "Test";


        // while Schleife einfügen, wenn mehrere Absender benutzt werden sollen
        // Am besten nicht unendlich lang durchlaufen lassen, damit man Massenmails nicht unterstützt
        try {
            sender.send("diddi.saeng@gmail.com", "Diddi", "djidde.sa@gmx.de", "test Subject", "Test Content");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
