package project.scheduler.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;


@RestController
@RequestMapping(path="/update")
public class UpdateController {

  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;

  @PostMapping(path="/password")
  public ResponseEntity<String> setPasswordById(@RequestParam String old_password, @RequestParam String new_password, @RequestParam Integer user_id) {
      User new_user = userService.findUserById(user_id).orElse(null);
      if (new_user == null || !Password.compare(old_password, new_user.getPassword())) {
        return new ResponseEntity<>(String.format("User %d not found or password incorrect%n", user_id), HttpStatus.UNPROCESSABLE_ENTITY);
      }
      permissionService.setPassword(new_password, user_id);
      return ResponseEntity.ok(String.format("Saved new password for user: %d%n", user_id));
  }

  @PatchMapping(path="/password")
  public ResponseEntity<String> setPasswordByToken(@RequestParam String token, @RequestParam String new_password, @RequestParam Integer user_id) {
    if (!permissionService.validRole(token, user_id, Permissions.Admin)) {
      return new ResponseEntity<>("Insufficient permissions", HttpStatus.UNAUTHORIZED);
    }
    permissionService.setPassword(new_password, user_id);
    return ResponseEntity.ok(String.format("Saved new password for user: %d%n", user_id));
  }
  
}
