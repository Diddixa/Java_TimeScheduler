package models;

public class User {
    /**
     * id of user
     */
    private int id;
    /**
     * Username of user
     */
    private String username;
    /**
     * Firstname of user
     */
    private String firstname;
    /**
     * Lastname of user
     */
    private String lastname;
    /**
     * Password of user
     */
    private String password;
    /**
     * Email of user
     */
    private String email;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String firstname, String lastname, String password, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }
}

