package project.scheduler.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.CourseService;
import project.scheduler.Services.InscriptionService;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.RoomService;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;


@RestController
@RequestMapping(path="/add")
public class CreateController {

  @Inject
  private InscriptionService inscriptionService;
  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;
  @Inject
  private CourseService courseService;
  @Inject
  private RoomService roomService;

  @PostMapping(path="/user")
  public ResponseEntity<Object> addNewUser (@RequestParam Integer role, @RequestParam String name, 
                                            @RequestParam String password, @RequestParam String email) {
    Password pw = new Password(password);
    User user = new User(role, name, pw.getPassword(), email);
    return userService.create(user);
  }

  @PostMapping(path="/token")
  public ResponseEntity<String> addNewWeeklyToken (@RequestParam Integer user_id, @RequestParam String token) {
    User u = userService.findUserById(user_id).orElse(null);
    if(u == null) {
      return new ResponseEntity<>("User ID not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    permissionService.setTokenDebug(u, token);
    return ResponseEntity.ok(String.format("Added token: %s for User: %d", token, user_id));
  }

  @PostMapping(path="/course")
  public ResponseEntity<String> addCourse (@RequestParam String name) {
    return courseService.create(name);
  }

  @PostMapping(path="/room")
  public ResponseEntity<String> addRoom(@RequestParam String name) {
    return roomService.create(name);
  }

  @PostMapping(path="/inscription")
  public ResponseEntity<String> inscribeStudent(@RequestParam Integer user_id, @RequestParam Integer course_id, @RequestParam String token) {
    if(!permissionService.validRole(token, user_id, Permissions.Assistant)) {
      return new ResponseEntity<>("Insufficient permissions", HttpStatus.UNAUTHORIZED);
    }
    if(inscriptionService.ifExists(user_id, course_id)) {
      return new ResponseEntity<>(String.format("User: %s is already inscribed in Course: %s", user_id, course_id), HttpStatus.CONFLICT);
    }
    User user = userService.findUserById(user_id).orElse(null);
    Course course = courseService.findCourseById(course_id).orElse(null);
    try {
      inscriptionService.inscribe(user, course);
      return ResponseEntity.ok(String.format("Inscribed User: %d into Course: %d", user_id, course_id));
    } catch (NullPointerException e) {
      StringBuilder response = new StringBuilder();
      if (user == null) {
        response.append(String.format("User: %s ", user_id));
      }
      if (course == null) {
        if(!response.isEmpty()) {
          response.append("and ");
        }
        response.append(String.format("Course: %s", course_id));
      }
      response.append(" could not be found.");
      return new ResponseEntity<>(response.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
