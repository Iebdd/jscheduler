package project.scheduler.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserCourseRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Inscription;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;


@RestController
@RequestMapping(path="/add")
public class CreateController {
  @Autowired
  private UserRepository userRepository;
  @Inject
  private CourseRepository courseRepository;
  /*
  private RoomRepository roomRepository;
  */
  @Inject
  private UserCourseRepository userCourseRepository;
  /*
  private RoomCourseRepository roomCourseRepository; */
  @Inject
  private TokenRepository tokenRepository;

  @PostMapping(path="/user")
  public ResponseEntity<String> addNewUser (@RequestParam Integer role, @RequestParam String name
      , @RequestParam String password, @RequestParam String email) {
    Password pw = new Password(password);
    try {
      User new_user = userRepository.save(new User(role, name, pw.getPassword(), email));
      return ResponseEntity.ok(String.format("Added user: %s with user id %d", name, new_user.getId()));
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity<>(String.format("User with email: %s already exists.", email), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
  }

  @PostMapping(path="/token")
  public ResponseEntity<String> addNewWeeklyToken (@RequestParam Integer userId, @RequestParam String token) {
    User u = userRepository.findById(userId).orElse(null);
    if(u == null) {
      return new ResponseEntity<>("User ID not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    Token t = new Token(u , token);
    tokenRepository.save(t);
    return ResponseEntity.ok(String.format("Added token: %s for user %d", token, userId));
  }

  @PostMapping(path="/course")
  public ResponseEntity<String> addCourse (@RequestParam String name) {
    Course c = new Course(name);
    c = courseRepository.save(c);
    return ResponseEntity.ok(String.format("Added course '%s' with id: %d", name, c.getId()));
  }

  @PostMapping(path="/student")
  public ResponseEntity<String> inscribeStudent(@RequestParam Integer user_id, @RequestParam Integer course_id) {
    StringBuilder response = new StringBuilder();
    User user = userRepository.findById(user_id).orElse(null);
    Course course = courseRepository.findById(course_id).orElse(null);
    try {
      Inscription uc = new Inscription(user, course);
      userCourseRepository.save(uc);
      return ResponseEntity.ok(String.format("Inscribed user: %d into course %d", user_id, course_id));
    } catch (NullPointerException e) {
      if (user == null) {
        response.append(String.format("User: %s ", user_id));
      }
      if (course == null) {
        if(!response.isEmpty()) {
          response.append("and ");
        }
        response.append(String.format("Course: %s", course_id));
      }
      response.append("could not be found.");
      return new ResponseEntity<>(response.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

      
      
  }
}
