package controller;

import models.User;

import java.sql.*;

public class Database {
    public static Connection databaseLink;

    public static Connection getConnection() {

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

    /**
     * Fetch user data from database. This is only called by other user related DB
     * functions.
     *
     * @param connection - SQL jdbc connection object, connection to DB
     * @param key        - used to find a certain user ()
     * @return SQL result of data entry or <code>null</code> if user doesn't exist
     */
    private static <S> ResultSet fetchUserData(Connection connection, S key) throws SQLException {
        String sqlColumn = "";

        if(key instanceof String)
        {
            sqlColumn = "username";
        }else{ sqlColumn = "user_id";}

        String getValues = "SELECT * FROM user WHERE " + sqlColumn + " = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(getValues);
            if (key instanceof String) {
                String username = key.toString();
                statement.setString(1, username);

            } else if (key instanceof Integer) {
                int userId = ((Integer) key).intValue();
                statement.setInt(1, userId);
            }
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                System.out.println("Successful");
                return result;
            }
            return null;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }

    /**
     * Function to create a new user in the database, used for registration and for the admin
     * @param user
     */
    public static void registerUser(User user) {
        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();

        String InsertField = "INSERT INTO user(username, password, firstname, lastname, email) VALUES ('";
        String InsertValues = user.getUsername() + "','" + user.getPassword() + "','" + user.getFirstname() + "','" + user.getLastname() + "','" + user.getEmail() + "')";
        String InsertRegister = InsertField + InsertValues;

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(InsertRegister);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


    /**
     *  Verifies whether password and username are in the database and decrypts the password
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */

    public static int confirmLogin(String username, String password) throws SQLException {

        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();
        ResultSet userData = fetchUserData(connectDB, username);

        if(userData == null)
        {
            System.out.println("No user found");
            return 0;
        }

        String hash;
        hash = userData.getString("password");
        String pwd_encrypted = PasswordEncryption.verify(password, hash);

        String verifyLogin = "SELECT count(1) FROM user WHERE username = '" + username + "' AND password = '" + pwd_encrypted + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){ //if field 1 is equal to 1 the actual label is used
                    return 1;
                } else
                {
                    return 0;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return 10;
    }

}

