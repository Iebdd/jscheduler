package project.scheduler.Services;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

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

    public static int TOKEN_LENGTH = 25;
    
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

    public boolean validRole(String token, UUID user_id, Permissions role) {         //Checks required permission and allows the role threshold
        User active_user = userRepository.findUserByToken(token);                       //or if the user requesting the change is using the same id
        return (active_user != null) ? !(!active_user.getUserId().equals(user_id) && active_user.getRole() < role.getNum()) : false;
    }

    public boolean validRole(String token, Permissions role) {                          //Checks required permissions but only allows the change
        User active_user = userRepository.findUserByToken(token);                       //with the necessary permissions
        return (active_user != null) ? !(active_user.getRole() < role.getNum()) : false;
    }

    public boolean validPassword(String new_password, String old_password) {
        return Password.compare(new_password, old_password);
    }

    public void setTokenDebug(User user_id, String token) {                             //Allows to directly set a token for debug and demonstration
        Token t = new Token(user_id , token);                                           //purposes DELETE FOR PROD!!
        tokenRepository.save(t);
    }

    public ResponseEntity<UserToken> setToken(User user) {
        UserToken new_token = new UserToken(user.getUserId(), TOKEN_LENGTH);
        tokenRepository.save(new Token(user, new_token.getFirstToken(), Instant.now().plusSeconds(604800)));
        return ResponseEntity.ok(new_token);
    }

    public ResponseEntity<UserToken> refreshToken(UUID user_id, String[] tokens) {
        tokenRepository.refreshToken(Instant.now().plusSeconds(604800), user_id);
        return ResponseEntity.ok(new UserToken(tokens, user_id, true));
    }

    public void cullTokens() {
        tokenRepository.deleteExpired(Instant.now()); 
    }

    public String[] findTokenByUser(UUID user_id) {
        return tokenRepository.findTokenByUser(user_id);
    }

    public Iterable<Token> findAllTokens() {
        return tokenRepository.findAll();
    }

    public void setPassword(String new_password, UUID user_id) {
        userRepository.updatePassword(new Password(new_password).getPassword(), user_id);
    }

    public String validAuthHeader(String header) { 
        System.out.println(header);
        System.out.println();
        System.out.println(Pattern.matches(header, "^Bearer ([A-Za-z0-9]{25})$"));
        return Pattern.compile("^Bearer ([A-Za-z0-9]{25})$").matcher(header).group();
    }
}
