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


@RestController
@RequestMapping(path="/verify")
public class VerifyController {
  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;

  @PostMapping(path="/login")
  public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
    String[] tokens;
    User user = userService.findUserByEmail(email);
    if(permissionService.validPassword(email, password)) {
      permissionService.cullTokens();
      tokens = permissionService.findTokenByUser(user.getUserId());
      if(tokens.length == 0) {
        return permissionService.setToken(user);
      }
      return permissionService.refreshToken(user.getUserId(), tokens);
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return ResponseEntity.ok(null);
  }
}
