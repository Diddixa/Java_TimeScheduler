//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import models.Event;
import models.Priority;
import models.Reminder;
import models.User;

    /**
     * The database class deals with all queries on our Mysql DB, for better overview and readability
    */
    public class Database {
    public static Connection databaseLink;

    public Database() {
    }

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
     * Close an existing connection to the database. Method is used to avoid max_user_connections
     */
    public static void closeDatabase() {
        try {
            if (databaseLink != null) {
                databaseLink.close();
                databaseLink = null;
            }
        } catch (SQLException var1) {
            var1.printStackTrace();
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
        if (key instanceof String) {
            sqlColumn = "username";
        } else {
            sqlColumn = "user_id";
        }

        String getValues = "SELECT * FROM user WHERE " + sqlColumn + " = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(getValues);
            if (key instanceof String) {
                String username = key.toString();
                statement.setString(1, username);
            } else if (key instanceof Integer) {
                int userId = (Integer)key;
                statement.setInt(1, userId);
            }

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("User data fetched");
                return result;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }

    /**
     * Query a user and return the corresponding User object from its table
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
        } else {
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
    }

    /**
     * Create a new user and insert the data into our database.
     * @param user necessary to determine new user
     */
    public static void registerUser(User user) {
        Connection connectDB = getConnection();
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
     * @param user_id
     *
     * @return <code>true</code>, if successful
     */
    public static boolean editUser(User user, int user_id) {
        String sql = "UPDATE user SET firstname = ?, lastname = ?, username = ?, email = ?, password = ? WHERE user_id = ?";
        Database connectNow = new Database();
        Connection connectDB = getConnection();

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
        } catch (SQLException var6) {
            var6.printStackTrace();
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
        String sql = " DELETE FROM user WHERE user_id = ?";
        Database connectNow = new Database();
        Connection connectDB = getConnection();
        System.out.println("User Id: " + id);

        try {
            PreparedStatement delete = connectDB.prepareStatement(sql);
            delete.setInt(1, id);
            delete.executeUpdate();
            closeDatabase();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            closeDatabase();
            return false;
        }
    }

    /**
     * Edits a user in the database with the parameter user.
     * @param user This user's attribute values are taken to edit the user in the DB
     * @return <code>true</code>, if successful
     */
    public static boolean editProfile(User user) {
        String sql = "UPDATE user SET firstname = ?, lastname = ?, username = ?, email = ? WHERE user_id = ?";

        Database connectNow = new Database();
        Connection connectDB = connectNow.getConnection();
        try {
            PreparedStatement edit = connectDB.prepareStatement(sql);
            edit.setString(1, user.getFirstname());
            edit.setString(2, user.getLastname());
            edit.setString(3, user.getUsername());
            edit.setString(4, user.getEmail());
            edit.setInt(5, user.getId());

            edit.executeUpdate();
            edit.close();

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

        System.out.println("Updated user.");
        return true;
    }

    /**
     *  Verifies whether password and username are in the database and decrypts the password
     * @param username used to fetch the right user from DB
     * @param password  to compare user password with internally saved password
     * @return
     * @throws SQLException
     */

        public static boolean confirmLogin(String username, String password) throws SQLException {
        Connection connectDB = getConnection();
        ResultSet userData = fetchUserData(connectDB, username);
        if (userData == null) {
            System.out.println("No user found");
            return false;
        } else {
            String hash = userData.getString("password");
            String pwd_encrypted = PasswordEncryption.verify(password, hash);
            String verifyLogin = "SELECT * FROM user WHERE username = ? AND password = ? ";
            PreparedStatement statement = connectDB.prepareStatement(verifyLogin);
            statement.setString(1, username);
            statement.setString(2, pwd_encrypted);
            ResultSet result = statement.executeQuery();
            Boolean res = result.next();
            statement.close();
            System.out.println("user successfully found");
            return res;
        }
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

            do {
                if (!result.next()) {
                    statement.close();
                    closeDatabase();
                    return true;
                }
            } while(result.getInt("user_id") == user.getId());

            return false;
        } catch (SQLException var5) {
            var5.printStackTrace();
            closeDatabase();
            return false;
        }
    }

    /**
     * Create table entry of new event in database.
     *
     * @param event Object of new entry.
     * @return event ID on successful creation, return -1 on failed creation (since no eventID can be -1)
     */
     public static int storeEvent(Event event) {
        String sql = "INSERT INTO events (eventhost_id, name, date, startTime, endTime, location, reminder, priority, description) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql, 1);
            statement.setInt(1, event.getEventHostId());
            statement.setString(2, event.getName());
            statement.setDate(3, Date.valueOf(event.getDate()));
            statement.setTime(4, Time.valueOf(event.getStartTime()));
            statement.setTime(5, Time.valueOf(event.getEndTime()));
            statement.setString(6, event.getLocation());
            statement.setString(7, event.getReminder().name());
            statement.setString(8, event.getPriority().name());
            statement.setString(9, event.getDescription());
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                int eventId = generatedKey.getInt(1);
                statement.close();
                closeDatabase();
                return eventId;
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
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
        String sql = "INSERT INTO user_Events (user_id , event_id) VALUES(?, ?)";
        Connection connection = getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            ps.close();
            closeDatabase();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            closeDatabase();
            return false;
        }
    }

    /**
     * Gets all events from User with help of the userId.
     * @param userId is used to find the relative data
     * @return a list of the events the user is in
     */
        public static ArrayList<Event> getEventsFromUser(int userId) {
        String sql = "SELECT * FROM events LEFT JOIN user_Events ON user_Events.event_id = events.events_id WHERE user_Events.user_id = ?";
        Connection connection = getConnection();
        ArrayList events = new ArrayList();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int eventId = rs.getInt("events_id");
                String name = rs.getString("name");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                String location = rs.getString("location");
                Reminder reminder = (Reminder)Enum.valueOf(Reminder.class, rs.getString("reminder"));
                Priority priority = (Priority)Enum.valueOf(Priority.class, rs.getString("priority"));
                String description = rs.getString("description");
                ArrayList<File> attachments = getAttachmentsFromEvent(eventId);
                int host_id = rs.getInt("eventhost_id");

                Event event = new Event(eventId, name, date, startTime, endTime, location, getParticipants(eventId), priority, reminder, description);
                event.setId(eventId);
                event.setEventHostId(host_id);
                event.setAttachments(attachments);
                events.add(event);
            }

            rs.close();
            ps.close();
            return events;
        } catch (SQLException var17) {
            var17.printStackTrace();
            closeDatabase();
            return null;
        }
    }

        /**
         * method used to edit a user event
         * @param event is necessary to load all event data
         * @return true on success and false on failure
         */
    public static boolean editEvent(Event event) {
        String sql = "UPDATE events SET name = ? , reminder = ? , priority = ? , date = ? , startTime = ? , endTime = ? , location = ?,  host_id = ?, description = ? WHERE events_id = ? ";
        Connection connection = getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, event.getName());
            ps.setString(2, event.getReminder().name());
            ps.setString(3, event.getPriority().name());
            ps.setDate(4, Date.valueOf(event.getDate()));
            ps.setTime(5, Time.valueOf(event.getStartTime()));
            ps.setTime(6, Time.valueOf(event.getEndTime()));
            ps.setString(7, event.getLocation());
            ps.setInt(8, event.getEventHostId());
            ps.setInt(9, event.getId());
            ps.setString(10, event.getDescription());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            var5.getErrorCode();
            return false;
        }
    }

        /**
         * delete event from events table
         * @param eventId know which event to be deleted
         * @return
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
        } catch (SQLException var4) {
            var4.printStackTrace();
            closeDatabase();
            return false;
        }
    }

        /**
         *  delete event in user_Events table
         * @param userId user to be removed from table
         * @param eventId corresponding event to user to be removed
         * @return
         */
    public static boolean deleteUserEvents(int userId, int eventId) {
        Connection connection = getConnection();
        String sql = "DELETE FROM user_Events WHERE user_id = ? AND event_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            closeDatabase();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
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
        String queryParticipants = "SELECT * FROM user LEFT JOIN user_Events ON user_Events.user_id = user.user_id WHERE user_Events.event_id = ? ";
        Connection connection = getConnection();
        ArrayList participants = new ArrayList();

        try {
            PreparedStatement ps = connection.prepareStatement(queryParticipants);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("firstname"), rs.getString("lastName"), rs.getString("email"));
                participants.add(user);
            }

            rs.close();
            ps.close();
            return participants;
        } catch (SQLException var7) {
            var7.printStackTrace();
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

        try {
            PreparedStatement ps = connection.prepareStatement(sql, 1);
            FileInputStream input = new FileInputStream(file);
            ps.setBinaryStream(1, input);
            ps.setInt(2, event.getId());
            ps.setString(3, file.getName());
            ps.executeUpdate();
            ResultSet generatedKey = ps.getGeneratedKeys();
            if (generatedKey.next()) {
                attachmentId = generatedKey.getInt(1);
                input.close();
                ps.close();
                closeDatabase();
                System.out.println("Attachment stored.");
                return attachmentId;
            } else {
                throw new SQLException("Couldn't store attachment.");
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
            closeDatabase();
            return attachmentId;
        } catch (IOException var9) {
            var9.printStackTrace();
            return attachmentId;
        }
    }

    /**
     * Gets the Attachments out of the Database.
     *
     * @param eventId Id of an event from which the attachments should be returned
     * @return List of files
     */
    public static ArrayList<File> getAttachmentsFromEvent(int eventId) {
        String sql = "SELECT * FROM attachments WHERE eventID = ?";
        Connection connection = getConnection();
        ArrayList<File> files = new ArrayList();
        InputStream input = null;
        FileOutputStream output = null;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                File tempFile = new File(rs.getString("attachmentName"));
                output = new FileOutputStream(tempFile);
                input = rs.getBinaryStream("attachment");
                byte[] buffer = new byte[1024];

                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                files.add(tempFile);
                input.close();
                output.close();
            }

            return files;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete all Attachment entries in the Database.
     *
     * @param eventId Event which the entries should be deleted from.
     */
         public static void deleteAllAttachments(int eventId) {
        String sql = "DELETE FROM Attachment WHERE eventID = ?";
        Connection connection = getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ps.executeUpdate();
            ps.close();
            closeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            closeDatabase();
        }

    }
}
