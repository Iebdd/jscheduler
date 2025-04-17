package project.scheduler.Repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.Token;

/**
 * Repository representing the Bookings database table
 */
@Transactional
public interface TokenRepository extends CrudRepository<Token, UUID> {

    /**
     * Selects all tokens associated with a specific user
     * 
     * @param user_id   The id of the user in question as a UUID object
     * 
     * @return  A String Array containing all valid tokens within the database
     */
    @NativeQuery(value = "SELECT token FROM tokens WHERE t_user_id= ?1")
    String[] findTokenByUser(UUID user_id);

    /**
     *  Checks if the token associated with the given user exists
     * 
     * @param user_id   The id of the user in question as a UUID object
     * @param token  The token in question as a 25 letter String containing upper and lower case letters as well as numbers
     * 
     * @return  The id of the token or null if it was not found
     */
    @NativeQuery(value = "SELECT EXISTS (SELECT 1 FROM token t WHERE t.t_user_id = ?1 AND t.token = ?2)")
    UUID ifExistsTokenByUser(UUID user_id, String token);

    /**
     * Removes all tokens whose expiration date has passed
     * 
     * @param now   The current time to be compared against as an Instant object
     */
    @Modifying
    @NativeQuery(value = "DELETE FROM tokens t WHERE t.expiry < ?1")
    void deleteExpired(Instant now);

    /**
     * Updates the expiry of the given token
     * 
     * @param newExpiry The new expiry of the token
     * @param token_id  The id of the token in question
     * 
     * @return  An integer representation of the amount of tokens changed. 1 if the token was refreshed, 0 if not
     */
    @Modifying
    @NativeQuery(value = "UPDATE tokens SET expiry = ?1 WHERE token_id = ?2")
    Integer refreshToken(Instant newExpiry, UUID token_id);
}
