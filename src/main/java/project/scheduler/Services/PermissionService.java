package project.scheduler.Services;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;
import project.scheduler.Util.UserToken;

/**
 * The service responsible for handling permission and permission validation
 */
@Service
public class PermissionService {

    public static final int TOKEN_LENGTH = 25;
    public static final Pattern TOKEN_MATCH = Pattern.compile("^Bearer ([a-zA-Z0-9]{25})$");
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

    /**
     * An enum representing the possible levels of permissions
     * 
     * I could imagine that it would make more sense to use the database permission system 
     * but I didn't have time for that anymore
     */
    public enum Permissions {
        Student(0),
        Assistant(1),
        Admin(2);
    
        private final int num;
    
        Permissions(int num) {
            this.num = num;
        }
    
        public int getNum() {
            return this.num;
        }
    }

    /**
     * Checks required permissions and permits lower permission for changing its own entries
     * 
     * @param token     The token used for validation
     * @param user_id   The id of the user making the request
     * @param role      The required permission level to validate successfully
     * 
     * @return  True if the token or  the user have valid permissions, false if not
     */
    public boolean validRole(String token, UUID user_id, Permissions role) {
        User active_user = userRepository.findUserByToken(token);
        return (active_user != null) ? !(!active_user.getUserId().equals(user_id) && active_user.getRole() < role.getNum()) : false;
    }

    /**
     * Checks required permissions
     * 
     * @param token     The token used for validation
     * @param role      The required permission level to validate successfully
     * 
     * @return  True if the token or  the user have valid permissions, false if not
     */
    public boolean validRole(String token, Permissions role) {
        User active_user = userRepository.findUserByToken(token);
        return (active_user != null) ? !(active_user.getRole() < role.getNum()) : false;
    }

    /**
     * Compares the password present and a new one given for parity
     * 
     * @param new_password  The password to be checked in plain text
     * @param old_password  The password to be checked against - Formatted as BCrypt with 10 hashing iterations
     * 
     * @return  True if the passwords are the same, false if not
     */
    public boolean validPassword(String new_password, String old_password) {
        return Password.compare(new_password, old_password);
    }

    /**
     * Creates a new token for a user and returns it
     * 
     * @param user  The user the token is created for
     * 
     * @return  A UserToken object containing the new token
     */
    public ResponseEntity<UserToken> setToken(User user) {
        UserToken new_token = new UserToken(user.getUserId(), TOKEN_LENGTH);
        tokenRepository.save(new Token(user, new_token.getFirstToken(), Instant.now().plusSeconds(604800)));
        return ResponseEntity.ok(new_token);
    }

    /**
     *  Updates a token's expiry date
     * 
     * @param user_id   The id of the user to be refreshed
     * @param token     The token to be refreshed    
     * 
     * @return  True if the token was refreshed successfully, false if the token was not found
     */
    public ResponseEntity<Boolean> refreshToken(UUID user_id, String token) {
        UUID token_id = tokenRepository.ifExistsTokenByUser(user_id, token);
        if(token_id == null) {
            return new ResponseEntity<>(false, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        tokenRepository.refreshToken(Instant.now().plusSeconds(604800), token_id);
        return ResponseEntity.ok(true);
    }

    /**
     * Deletes all tokens which have their expiry in the past
     */
    public void cullTokens() {
        tokenRepository.deleteExpired(Instant.now()); 
    }

    /**
     * Finds all tokens associated with a user
     * 
     * @param user_id   The id of the user to be searched for
     * 
     * @return  An Array of Strings representing all tokens found
     */
    public String[] findTokenByUser(UUID user_id) {
        return tokenRepository.findTokenByUser(user_id);
    }

    /**
     *  Updates the password for a given user
     * 
     * @param new_password  The user's new password
     * @param user_id       The id of the user to be updated
     */
    public void setPassword(String new_password, UUID user_id) {
        userRepository.updatePassword(new Password(new_password).getPassword(), user_id);
    }

    /**
     *  Validates a passed header to have the correct format
     * 
     * @param header    The header to be checked
     * 
     * @return  The extracted token as a String or an empty String if the headerh as an incorrect format
     */
    public String validAuthHeader(String header) { 
        Matcher m = TOKEN_MATCH.matcher(header);
        m.matches();
        try {
            return m.group(1);
        } catch (IllegalStateException e) {
            return "";
        }
    }
}
