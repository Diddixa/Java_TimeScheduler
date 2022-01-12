package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public Connection databaseLink;

    public Connection getConnection() {

        /** Database Username*/
        String databaseUser = "ummyxpjqfaflxgpt";
        /** Database Password */
        String databasePassword = "6cMSMYdNZ5nK4urkpbs9";
        /** Database URL */
        String url = "jdbc:mysql://bpxq2ruzggpg92vaehks-mysql.services.clever-cloud.com/bpxq2ruzggpg92vaehks";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }


}
