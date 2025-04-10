package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserCourseRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Tables.UserCourse;
import project.scheduler.Util.Password;


@Controller
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
  public @ResponseBody String addNewUser (@RequestParam Integer role, @RequestParam String name
      , @RequestParam String password, @RequestParam String email) {
    Password pw = new Password(password);
    User new_user = userRepository.save(new User(role, name, pw.getPassword(), email));
    return String.format("Added user: %s with user id %d%n", name, new_user.getId());
  }

  @PostMapping(path="/token")
  public @ResponseBody String addNewWeeklyToken (@RequestParam Integer userId, @RequestParam String token) {
    User u = userRepository.findById(userId).orElse(null);
    if(u == null) {
        return String.format("User ID not found%n");
    }
    Token t = new Token(u , token);
    tokenRepository.save(t);
    return String.format("Added token: %s for user %d%n", token, userId);
  }

  @PostMapping(path="/course")
  public @ResponseBody String addCourse (@RequestParam String name) {
    Course c = new Course(name);
    c = courseRepository.save(c);
    return String.format("Added course '%s' with id: %d", name, c.getId());
  }

  @PostMapping(path="/student")
  public @ResponseBody String inscribeStudent(@RequestParam Integer user_id, @RequestParam Integer course_id) {
    UserCourse uc = new UserCourse(userRepository.findById(user_id).orElse(null), 
                                    courseRepository.findById(course_id).orElse(null));
      userCourseRepository.save(uc);
      return String.format("Inscribed user: %d into course %d", user_id, course_id);
  }
}
