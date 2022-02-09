package controller;

import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The database class deals with all queries on our Mysql DB, for better overview and reusability
 */
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
     * Close an existing connection to the database. Functions is used to avoid max_user in sql
     */
    public static void closeDatabase() {
        try {
            if (databaseLink != null) {
                databaseLink.close();
                databaseLink = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * fetch user data from DB (only user data no events or participants)
     * functions.
     *
     * @param connection - SQL jdbc connection object, connection to DB
     * @param key        - used to find a certain user
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
                System.out.println("User data fetched");
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
            closeDatabase();
            return null;
        }

        try {
            int id = result.getInt("user_id");
            String username = result.getString("username");
            String email = result.getString("email");
            String firstname = result.getString("firstname");
            String lastname = result.getString("lastname");

            ArrayList<Event> events = getEventsFromUser(id);
            User user = new User(id, username, firstname, lastname, email, events);

            System.out.println("Fetched user.");
            closeDatabase();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
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
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            closeDatabase();
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
    public static boolean editUser(User user, int user_id) {
        String sql = "UPDATE user SET firstname = ?, lastname = ?, username = ?, email = ?, password = ? WHERE user_id = ?";

        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();
        try {
            PreparedStatement edit = connectDB.prepareStatement(sql);
            edit.setString(1, user.getFirstname());
            edit.setString(2, user.getLastname());
            edit.setString(3, user.getUsername());
            edit.setString(4, user.getEmail());
            edit.setString(5, user.getPassword());
            edit.setInt(6, user_id);

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
     in table Location and table user_Events.
     @param id - id of user to delete
     @return true if deletion was successful
     */

    public static boolean deleteUser(int id) {
        String sql =" DELETE FROM user WHERE user_id = ?";
        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();

        System.out.println("User Id: " + id);

        try {
            PreparedStatement delete = connectDB.prepareStatement(sql);
            delete.setInt(1,id);
            delete.executeUpdate();
            closeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
            return false;
        }
    }


    /**
     *  Verifies whether password and username are in the database and decrypts the password
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */

    public static boolean confirmLogin(String username, String password) throws SQLException {

        // Database connectNow = new Database();
        Connection connectDB = getConnection();
        ResultSet userData = fetchUserData(connectDB, username);

        if(userData == null)
        {
            System.out.println("No user found");
            return false;
        }

        String hash;
        hash = userData.getString("password");
        String pwd_encrypted = PasswordEncryption.verify(password, hash);

        String verifyLogin = "SELECT * FROM user WHERE username = ? AND password = ? ";

        PreparedStatement statement = connectDB.prepareStatement(verifyLogin);

        statement.setString(1, username);
        statement.setString(2, pwd_encrypted);

        ResultSet result = statement.executeQuery();

        Boolean res = result.next(); //returns the boolean value
        statement.close(); //close preparedstatement for good practice


        System.out.println("user successfully found");
        return res;
    }

    /**
     * Check if username or email is already taken.
     *
     * @param user - User data
     * @return true if user data is available
     */
    public static boolean isTaken(User user) {
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
            closeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
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
            closeDatabase();

            return eventId;

        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
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
            closeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
            return false;
        }
    }

    /**
     * Gets all events from User with help of the userId.
     *
     * @param userId is used to find the relative data
     * @return a list of the events the user is in
     */
    public static ArrayList<Event> getEventsFromUser(int userId) {
        String sql = "SELECT * FROM events " + "LEFT JOIN user_Events " + "ON user_Events.event_id = events.events_id "
                + "WHERE user_Events.user_id = ?";

        Connection connection = getConnection();
        ArrayList<Event> events = new ArrayList<Event>();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                String name = rs.getString("name");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                String location = rs.getString("location");
                Reminder reminder = Enum.valueOf(Reminder.class, rs.getString("reminder"));
                Priority priority = Enum.valueOf(Priority.class, rs.getString("priority"));
                ArrayList<File> attachments = getAttachmentsFromEvent(eventId);
                int host_id = rs.getInt("eventhost_id");

                Event event = new Event(eventId, name, date, startTime, endTime, location, getParticipants(eventId), priority, reminder);

                event.setId(eventId);
                event.setEventHostId(host_id);
                event.setAttachments(attachments);
                events.add(event);
            }

            rs.close();
            ps.close();
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
            return null;
        }

    }

    /**
     * Edits the event.
     *
     * @param event new event object which the Database should be adjusted for
     * @return true, if successful
     */
    public static boolean editEvent(Event event) {
        String sql = "UPDATE Event SET name = ? , reminder = ? , priority = ? , date = ? , startTime = ? , endTime = ? , location = ?,  host_id = ? "
                + "WHERE event_id = ? ";

        Database connectNow = new Database();
        Connection connection = connectNow.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);


            ps.setString(1,event.getName());
            ps.setString(2, event.getReminder().name());
            ps.setString(3, event.getPriority().name());
            ps.setDate(4,Date.valueOf(event.getDate()));
            ps.setTime(5,Time.valueOf(event.getStartTime()));
            ps.setTime(6, Time.valueOf(event.getEndTime()));
            ps.setString(7,event.getLocation());
            ps.setInt(8,event.getEventHostId());
            ps.setInt(9, event.getId());

            ps.executeUpdate();
            ps.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            e.getErrorCode();

            return false;
        }
    }

    /**
     * Delete table entry in Event.
     *
     * @param eventId the event id of the event to be deleted
     * @return true when deletion is successful, false when deletion is unsuccessful
     */
    public static boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM events WHERE events_id = ?";
        Connection connection = getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);

            ps.executeUpdate();

            ps.close();

            closeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
            return false;
        }
    }

    /**
     * Delete table entry in the User_Event & therefore remove the connection
     * between the User & Event.
     *
     * @param userId  User that should be removed from the event
     * @param eventId Event that the User should be removed from
     * @return true on success, false on failure.
     */
    public static boolean deleteUserEventBridge(int userId, int eventId) {
        String sql;
        Connection connection = getConnection();
        sql = "DELETE FROM user_Events WHERE user_id = ? AND event_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, eventId);

            ps.executeUpdate();
            closeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
            return false;
        }
    }


    /**
     * Gets a list of participants for an event.
     *
     * @param eventId eventid of the event you want the participants from
     * @return the participants
     */
    private static ArrayList<User> getParticipants(int eventId) {
        String queryParticipants = "SELECT * FROM user " + "LEFT JOIN user_Events "
                + "ON user_Events.user_id = user.user_id WHERE user_Events.event_id = ? ";

        Connection connection = getConnection();
        ArrayList<User> participants = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(queryParticipants);
            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("firstname"),
                        rs.getString("lastName"), rs.getString("email"));

                participants.add(user);
            }
            rs.close();
            ps.close();
            return participants;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds attachment entry into the Database.
     *
     * @param file File to be uploaded into the database
     * @param event Event that the file belongs to
     * @return -1 on failed creation, ID on successful creation
     */
    public static int storeAttachment(File file, Event event) {
        String sql = "INSERT INTO attachments (attachment, eventID, attachmentName) VALUES ( ? , ? , ? )";
        Connection connection = getConnection();
        int attachmentId = -1;
        try{
            PreparedStatement ps = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);
            FileInputStream input = new FileInputStream(file);
            ps.setBinaryStream(1 , input);
            ps.setInt(2 , event.getId());
            ps.setString(3, file.getName());

            ps.executeUpdate();
            ResultSet generatedKey = ps.getGeneratedKeys();

            if (generatedKey.next()) {
                attachmentId = generatedKey.getInt(1);
            } else {
                throw new SQLException("Couldn't store attachment.");
            }

            input.close();
            ps.close();
            closeDatabase();
            System.out.println("Attachment stored.");
            return attachmentId;

        } catch (SQLException e){
            e.printStackTrace();
            closeDatabase();
            return attachmentId;
        } catch (IOException e) {
            e.printStackTrace();
            return attachmentId;
        }
    }

    /**
     * Gets the Attachments out of the Database.
     *
     * @param eventId Id of an event from which the attachments should be returned
     * @return List of files
     */
    public static ArrayList<File> getAttachmentsFromEvent(int eventId){
        String sql = "SELECT * FROM Attachment WHERE event_id = ?";
        Connection connection = getConnection();
        ArrayList<File> files = new ArrayList<>();
        InputStream input = null;
        FileOutputStream output = null;

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1 ,  eventId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                File tempFile = new File(rs.getString("name"));
                output = new FileOutputStream(tempFile);
                input = rs.getBinaryStream("file");

                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0){
                    output.write(buffer);
                }

                files.add(tempFile);

                input.close();
                output.close();
            }


            return files;
        } catch (SQLException | IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Delete all Attachment entries in the Database.
     *
     * @param eventId Event which the entries should be deleted from.
     */
    public static void deleteAllAttachments(int eventId){
        String sql = "DELETE FROM Attachment WHERE event_id = ?";

        Connection connection = getConnection();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt( 1, eventId);

            ps.executeUpdate();

            ps.close();
            closeDatabase();
        } catch(SQLException e){
            e.printStackTrace();
            closeDatabase();
        }
    }


}
