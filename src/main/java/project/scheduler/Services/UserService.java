package project.scheduler.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.User;
import project.scheduler.Util.UserToken;

/**
 * The service responsible for users
 */
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    @Inject 
    private PermissionService permissionService;

    /**
     *  Creates a new user entry
     * 
     * @param user  The User object to be created
     * 
     * @return  A UserToken object with a newly issued token or nothing if the email address already exists
     */
    public ResponseEntity<UserToken> create(User user) {
        try {
        userRepository.save(user);
        return permissionService.setToken(user);
        } catch (DataIntegrityViolationException e) {
        return new ResponseEntity<>(new UserToken(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     *  Finds a user by their id
     * 
     * @param user_id   The id of the user in question
     * 
     * @return  A User object representing the entry or null if it wasn't found
     */
    public Optional<User> findUserById(UUID user_id) {
        return userRepository.findById(user_id);
    }

    /**
     *  Finds a user by their email
     * 
     * @param email   The email of the user in question
     * 
     * @return  A User object representing the entry or null if it wasn't found
     */
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     *  Checks if a user associated with a given email address already exists
     * 
     * @param email The email to be checked for
     * 
     * @return  True if the user already exists, false if not
     */
    public Boolean checkByEmail(String email) {
        return (userRepository.checkByEmail(email) == 1);
    }
}
