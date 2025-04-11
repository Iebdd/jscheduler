package project.scheduler.Services;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;
import project.scheduler.Util.UserToken;

@Service
public class PermissionService {
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

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

    public boolean validRole(String token, Integer user_id, Permissions role) {
        User active_user = userRepository.findUserByToken(token);
        return !(!active_user.getUserId().equals(user_id) && active_user.getUserId() < role.getNum());
    }

    public boolean validRole(String token, Permissions role) {
        User active_user = userRepository.findUserByToken(token);
        return !(active_user.getUserId() < role.getNum());
    }

    public boolean validPassword(String new_password, String old_password) {
        return Password.compare(new_password, old_password);
    }

    public void setTokenDebug(User user_id, String token) {
        Token t = new Token(user_id , token);
        tokenRepository.save(t);
    }

    public ResponseEntity<Object> setToken(User user) {
        UserToken new_token = new UserToken();
        tokenRepository.save(new Token(user, new_token.getFirstToken(), Instant.now().plusSeconds(604800)));
        return ResponseEntity.ok(new_token);
    }

    public ResponseEntity<Object> refreshToken(int user_id, String[] tokens) {
        tokenRepository.refreshToken(Instant.now().plusSeconds(604800), user_id);
        return ResponseEntity.ok(new UserToken(tokens, true));
    }

    public void cullTokens() {
        tokenRepository.deleteExpired(Instant.now()); 
    }

    public String[] findTokenByUser(int user_id) {
        return tokenRepository.findTokenByUser(user_id);
    }

    public void setPassword(String new_password, int user_id) {
        userRepository.updatePassword(new Password(new_password).getPassword(), user_id);
    }
}
