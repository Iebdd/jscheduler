package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;



@Controller
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
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
  

  @GetMapping(path="/tokens")
  public @ResponseBody Iterable<Token> getAllTokens() {
    return tokenRepository.findAll();
  }

  @GetMapping(path="/courses")
  public @ResponseBody Iterable<Course> getAllCourses() {
    return courseRepository.findAll();
  }
}
