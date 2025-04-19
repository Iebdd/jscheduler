package project.scheduler.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.User;
import project.scheduler.Util.PublicUser;
import project.scheduler.Util.UserToken;

/**
 * Controller class responsible for removing entities from the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/verify")
public class VerifyController {
  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;

  /**
   * Verifies a user based on the email address and the associated password and issues a token if none are present <br>
   * or transmits valid tokens to be verified
   * 
   * @param email     The email address associated with the user
   * @param password  The password associated with the user
   * 
   * @return  A UserToken object either containing a new token to be saved or existing ones to be verified
   */
  @PostMapping(path="/login")
  public ResponseEntity<UserToken> login(@RequestParam String email, @RequestParam String password) {
    String[] tokens;
    User user = userService.findUserByEmail(email);
    if(user == null) {
      return ResponseEntity.ok(null);
    }
    if(permissionService.validPassword(password, user.getPassword())) {
      permissionService.cullTokens();
      tokens = permissionService.findTokenByUser(user.getUserId());
      if(tokens.length == 0) {
        return permissionService.setToken(user);
      }
      return ResponseEntity.ok(new UserToken(tokens, user.getUserId(), true));
    }
    try {
      Thread.sleep(1000);         // Server waits one second before returning a negative result
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping(path="/token")
  public ResponseEntity<PublicUser> verifyToken(@RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new PublicUser(), HttpStatus.UNAUTHORIZED);
    }
    User user = userService.findUserByToken(token);
    if(user == null) {
      return new ResponseEntity<>(new PublicUser(), HttpStatus.UNAUTHORIZED);
    } else {
      System.out.println(user.getUserId());
      return ResponseEntity.ok(new PublicUser(user));
    }
  }
}
