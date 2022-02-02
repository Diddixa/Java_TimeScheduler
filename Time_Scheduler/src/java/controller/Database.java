package controller;

import models.Event;
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
     * Query a username and return the corresponding User object from its table
     * entry. Used to search the user table.
     *
     * @param key - String of username or Int of userid
     * @return User object on successful query, else <code>null</code>
     */

    public static <S> User getUser(S key) throws SQLException {
        Connection connection = getConnection();

        ResultSet result = fetchUserData(connection, key);
        if (result == null) {
            return null;
        }

        try {
            int id = result.getInt("user_id");
            String username = result.getString("username");
            String email = result.getString("email");
            String firstname = result.getString("firstname");
            String lastname = result.getString("lastname");

           /* ArrayList<Event> events = getEventsFromUser(id); */
            User user = new User(id, username, firstname, lastname, email);

            System.out.println("Fetched user.");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
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
     * Edits a user in the database with the parameter user.
     *
     * @param user This user's attribute values are taken to edit the user in the DB
     *             with the same id
     *
     * @return <code>true</code>, if successful
     */
    public static boolean editUser(User user) {
        String sql = "UPDATE User SET firstname = ?, lastname = ?, username = ?, email = ?, password = ? WHERE user_id = ?";

        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();
        try {
            PreparedStatement edit = connectDB.prepareStatement(sql);
            edit.setString(1, user.getFirstname());
            edit.setString(2, user.getLastname());
            edit.setString(3, user.getUsername());
            edit.setString(4, user.getEmail());
            edit.setString(5, user.getPassword());

            edit.executeUpdate();
            edit.close();

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

        System.out.println("Updated user.");
        return true;
    }

    /** Delete user in the user table and user's corresponding entries
     in table Location and table User_Event.
     @param id - id of user to delete
     @return true if deletion was successful
     */

    public static boolean deleteUser(int id) {
        String sql =" DELETE FROM User WHERE user_id = ?";
        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();

        System.out.println("User Id:" + id);

        try {
            PreparedStatement delete = connectDB.prepareStatement(sql);
            delete.setInt(1,id);
            delete.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
    /**
     * Check if username or email is already taken.
     *
     * @param user - User data
     * @return true if user data is available
     */
    public static boolean isAvailable(User user) {
        String sql = "SELECT * FROM user WHERE username = ? OR email=?";

        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (result.getInt("user_id") != user.getId()) {
                    return false;
                }
            }
            statement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }
    /**
     * Create table entry of new event in database.
     *
     * @param event Object of new entry.
     * @return event ID on successful creation, return -1 on failed creation
     */
    public static int storeEvent(Event event) {
        String sql = "INSERT INTO events (eventhost_id, name, date, startTime, endTime, location, reminder, priority)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        int eventId;

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, event.getEventHostId());
            statement.setString(2, event.getName());
            statement.setDate(3, Date.valueOf(event.getDate()));
            statement.setTime(4, Time.valueOf(event.getStartTime()));
            statement.setTime(5, Time.valueOf(event.getEndTime()));
            statement.setString(6, event.getLocation());
            statement.setString(7, event.getReminder().name());
            statement.setString(8, event.getPriority().name());

            statement.executeUpdate();

            ResultSet generatedKey = statement.getGeneratedKeys();

            if (generatedKey.next()) {
                eventId = generatedKey.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            statement.close();

            return eventId;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Creates an entry in the User_Event table in the Database.
     *
     * @param userId the user id of the according user
     * @param eventId the event id of the according event
     * @return true when insertion was successful, false when insertion had an
     *         exception.
     */
    public static boolean createUserEvents(int userId, int eventId) {
        String sql = "INSERT INTO user_Events (user_id , event_id) " + "VALUES(?, ?)";
        Connection connection = getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, eventId);

            ps.executeUpdate();

            ps.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
}
