package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;



@RestController
@RequestMapping(path="/read")
public class ReadController {
  @Autowired
  private UserRepository userRepository;
  @Inject
  private CourseRepository courseRepository;
  /*
  private RoomRepository roomRepository;
  private UserCourseRepository userCourseRepository;
  private RoomCourseRepository roomCourseRepository; */
  @Inject
  private TokenRepository tokenRepository;

  @GetMapping(path="/users")
  public ResponseEntity<Iterable<User>> getAllUsers() {
    return ResponseEntity.ok(userRepository.findAll());
  }
  

  @GetMapping(path="/tokens")
  public ResponseEntity<Iterable<Token>> getAllTokens() {
    return ResponseEntity.ok(tokenRepository.findAll());
  }

  @GetMapping(path="/courses")
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseRepository.findAll());
  }
}
