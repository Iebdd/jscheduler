package project.scheduler.Services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.User;

@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    @Inject 
    private PermissionService permissionService;

    public ResponseEntity<Object> create(User user) {
        try {
        userRepository.save(user);
        return permissionService.setToken(user);
        } catch (DataIntegrityViolationException e) {
        return new ResponseEntity<>(String.format("User with email: %s already exists.", user.getEmail()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public Optional<User> findUserById(int user_id) {
        return userRepository.findById(user_id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
