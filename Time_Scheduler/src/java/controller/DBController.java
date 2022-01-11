package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {

    public Connection databaseLink;

    /** Database Username
     */
    String databaseUser = "ummyxpjqfaflxgpt";
    /** Database Password */
    String databasePassword = "6cMSMYdNZ5nK4urkpbs9";
    /** Database URL */
    String url = "jdbc:mysql://bpxq2ruzggpg92vaehks-mysql.services.clever-cloud.com";

    /*
    establish DB Connection
     */
    Connection con = null;

    /**
     * Function to create the connection to the database while checking for errors.
     * @return con (connection object) or error code on failed connection
     */
    Connection getConnection(){


       try{
           if (con != null)
           {
               return con;
           }
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, databaseUser, databasePassword);

        return con;

       }catch(SQLException e)
       {
           System.out.println(e.getMessage());
       }catch(Exception e)
       {
           e.printStackTrace();
           e.getCause();
       }

       return null;

    }

    /**
     * Closes an existing connection to the database to avoid multiple unused connections. That could result in too
     * much cloud traffic.
     */
    private  void closeDatabase() {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
