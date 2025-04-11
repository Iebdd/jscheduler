package project.scheduler.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.InscriptionService;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;


@RestController
@RequestMapping(path="/remove")
public class DeleteController {

  @Inject
  private InscriptionService inscriptionService;
  @Inject
  private PermissionService permissionService;


  /*
  private RoomRepository roomRepository; */

  @PostMapping(path="/inscription")
  public ResponseEntity<String> removeStudent(@RequestParam Integer user_id, @RequestParam Integer course_id, @RequestParam String token) {
    if(!permissionService.validRole(token, user_id, Permissions.Assistant)) {
      return new ResponseEntity<>("Insufficient permissions", HttpStatus.UNAUTHORIZED);
    }
    if(!inscriptionService.ifExists(user_id, course_id)) {
      return new ResponseEntity<>(String.format("User: %d is not inscribed in Course: %d", user_id, course_id), HttpStatus.BAD_REQUEST);
    }
    return inscriptionService.remove(user_id, course_id);
  }
}
