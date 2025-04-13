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

@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    @Inject 
    private PermissionService permissionService;

    public ResponseEntity<UserToken> create(User user) {
        try {
        userRepository.save(user);
        return permissionService.setToken(user);
        } catch (DataIntegrityViolationException e) {
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public Optional<User> findUserById(UUID user_id) {
        return userRepository.findById(user_id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
