package project.scheduler.Util;

import java.util.UUID;

import project.scheduler.Tables.User;


/**
 * Record responsible for transmitting public facing user data
 */
public class PublicUser {

    private UUID user_id;
    private Integer role;
    private String firstName; 
    private String lastName;
    private String email;

    public PublicUser() {};

    public PublicUser(User user) {
        this.user_id = user.getUserId();
        this.role = user.getRole();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }

    public Integer getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public UUID getUser_id() {
        return user_id;
    }
}

