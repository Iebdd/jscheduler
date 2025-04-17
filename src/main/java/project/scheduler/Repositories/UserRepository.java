package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.User;

/**
 * Repository representing the Bookings database table
 */
@Transactional
public interface UserRepository extends CrudRepository<User, UUID> {

    /**
     * Selects a user based on their first and last name
     * 
     * @param firstName The first name of the user in question
     * @param lastName  The last name of the user in question
     * 
     * @return  A User object of the user in question, if found
     */
    @NativeQuery(value = "SELECT * FROM users u WHERE u.firstName= ?1 AND u.lastName = ?2")
    User findUserByName(String firstName, String lastName);

    /**
     * Selects a user based on their email address
     * 
     * @param email The email of the user in question
     * 
     * @return  A User object of the user in question
     */
    @NativeQuery(value = "SELECT * FROM users u WHERE u.email = lower(?1)")
    User findUserByEmail(String email);

    /**
     * Selects a user's password based on their email address
     * 
     * @param email The email of the user in question
     * 
     * @return  A String representation of the password
     */
    @NativeQuery(value = "SELECT password FROM users u WHERE u.email = ?1")
    String findPasswordByEmail(String email);

    /**
     * Updates a user's password
     * 
     * @param password  The new password
     * @param user_id   The id of the user in question as a UUID object
     */
    @Modifying
    @NativeQuery(value = "UPDATE users u SET password = ?1 WHERE u.user_id = ?2")
    void updatePassword(String password, UUID user_id);

    /**
     * Finds the user associated with a specific token
     * 
     * @param token The tokens to be searched by
     * 
     * @return  A User object of the user in question
     */
    @NativeQuery(value = "SELECT * FROM tokens t, users u WHERE t.token = ?1 AND t.t_user_id = u.user_id")
    User findUserByToken(String token);

    /**
     * Checks if the given email address is already present
     * 
     * @param email The email address to be checked
     * 
     * @return  An integer representation of the result. 1 if it exists, 0 if not
     */
    @NativeQuery(value = "SELECT EXISTS (SELECT 1 FROM users u WHERE u.email = ?1)")
    Integer checkByEmail(String email);
}
