package project.scheduler.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.User;
import project.scheduler.Util.UserToken;

/**
 * Controller class responsible for removing entities from the database
 */
@RestController
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
    if(permissionService.validPassword(email, password)) {
      permissionService.cullTokens();
      tokens = permissionService.findTokenByUser(user.getUserId());
      if(tokens.length == 0) {
        return permissionService.setToken(user);
      }
      return permissionService.refreshToken(user.getUserId(), tokens);
    }
    try {
      Thread.sleep(1000);         // Server waits one second before returning a negative result
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return ResponseEntity.ok(null);
  }
}
